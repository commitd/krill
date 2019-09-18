package io.committed.krill.extraction.pdfbox.interpretation;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/** Finds grids in a collection of lines. */
public class GridExtractor {

  /** The Constant QUANTISATION. */
  private static final int QUANTISATION = 4;

  /**
   * Find grids.
   *
   * @param lines the lines
   * @return the collection
   */
  public Collection<Grid> findGrids(Collection<Line2D> lines) {
    // find all x and y lines
    Collection<Line2D> xlines = new LinkedHashSet<>();
    Collection<Line2D> ylines = new LinkedHashSet<>();
    for (Line2D line : lines) {
      Line2D roundedLine =
          new EqualsAndHashcodeLine2D(
              QUANTISATION * Math.round(line.getX1() / QUANTISATION),
              QUANTISATION * Math.round(line.getY1() / QUANTISATION),
              QUANTISATION * Math.round(line.getX2() / QUANTISATION),
              QUANTISATION * Math.round(line.getY2() / QUANTISATION));

      if (equalTo(roundedLine.getX1(), roundedLine.getX2())
          && equalTo(roundedLine.getY1(), roundedLine.getY2())) {
        continue;
      }

      if (isXline(roundedLine)) {
        xlines.add(roundedLine);
      } else if (isYline(roundedLine)) {
        ylines.add(roundedLine);
      }
    }

    Collection<Collection<Point2D>> intersectionPoints = findIntersectionPoints(xlines, ylines);
    return intersectionPoints.stream().map(this::makeGrid).collect(Collectors.toList());
  }

  /**
   * Make grid.
   *
   * @param points the points
   * @return the grid
   */
  private Grid makeGrid(Collection<Point2D> points) {
    double[] xpositions = points.stream().mapToDouble(Point2D::getX).sorted().distinct().toArray();
    double[] ypositions = points.stream().mapToDouble(Point2D::getY).sorted().distinct().toArray();

    ArrayList<Point2D> sortedPoints = new ArrayList<>(points);
    sortedPoints.sort(Comparator.comparing(Point2D::getY).thenComparing(Point2D::getX));

    Grid grid = new Grid();
    Row row = new Row();
    grid.addRow(row);

    Point2D origin = sortedPoints.get(0);

    // 1. top-left to bottom-right (left to right, top to bottom) finding grid intersection points.
    // 2. when an aligned rectangle is found, add it as a cell and calculate its cell/row span based
    // on the number of x / y line positions it spans
    while (sortedPoints.size() > 3) {
      Point2D topLeft = sortedPoints.get(0);
      Point2D topRight = null;
      Point2D bottomLeft = null;
      Point2D bottomRight = null;
      for (Point2D point2d : sortedPoints) {
        if (topRight == null
            && equalTo(point2d.getY(), topLeft.getY())
            && point2d.getX() > topLeft.getX()) {
          topRight = point2d;
        } else if (bottomLeft == null
            && point2d.getY() > topLeft.getY()
            && equalTo(point2d.getX(), topLeft.getX())) {
          bottomLeft = point2d;
        } else if (topRight != null
            && equalTo(point2d.getX(), topRight.getX())
            && bottomLeft != null
            && equalTo(point2d.getY(), bottomLeft.getY())) {
          bottomRight = point2d;
          break;
        }
      }

      if (topRight != null && bottomLeft != null && bottomRight != null) {
        if (equalTo(topLeft.getX(), origin.getX())) {
          row = new Row();
          grid.addRow(row);
        }

        int colSpan =
            Arrays.binarySearch(xpositions, bottomRight.getX())
                - Arrays.binarySearch(xpositions, topLeft.getX());
        int rowSpan =
            Arrays.binarySearch(ypositions, bottomRight.getY())
                - Arrays.binarySearch(ypositions, topLeft.getY());
        Cell cell = new Cell(topLeft, bottomRight, colSpan, rowSpan);
        row.addCell(cell);
      }
      sortedPoints.remove(0);
    }
    return grid;
  }

  /**
   * Checks if is xline.
   *
   * @param line the line
   * @return true, if is xline
   */
  private static boolean isXline(Line2D line) {
    return equalTo(line.getY1(), line.getY2());
  }

  /**
   * Checks if is yline.
   *
   * @param line the line
   * @return true, if is yline
   */
  private static boolean isYline(Line2D line) {
    return equalTo(line.getX1(), line.getX2());
  }

  /**
   * Find intersection points.
   *
   * @param xlines the xlines
   * @param ylines the ylines
   * @return the collection
   */
  private Collection<Collection<Point2D>> findIntersectionPoints(
      Collection<Line2D> xlines, Collection<Line2D> ylines) {
    Collection<Line2D> lines = new ArrayList<>();
    lines.addAll(xlines);
    lines.addAll(ylines);
    // now gather all lines that intersect. This gives us grid candidates in groups
    Collection<Collection<Line2D>> groups = collectIntersectingLines(lines);
    Collection<Collection<Point2D>> points = new HashSet<>();
    for (Collection<Line2D> group : groups) {
      Collection<Point2D> findIntersections = findIntersections(group);
      if (!findIntersections.isEmpty()) {
        points.add(findIntersections);
      }
    }
    return points;
  }

  /**
   * Collect intersecting lines.
   *
   * @param lines the lines
   * @return the collection
   */
  private static Collection<Collection<Line2D>> collectIntersectingLines(Collection<Line2D> lines) {
    Collection<Collection<Line2D>> groups = new ArrayList<>();

    for (Line2D newLine : lines) {
      boolean addedToGroup = false;
      for (Collection<Line2D> group : groups) {
        for (Line2D line : group) {
          if (newLine.intersectsLine(line)) {
            group.add(newLine);
            addedToGroup = true;
            break;
          }
        }
      }
      if (!addedToGroup) {
        ArrayList<Line2D> newGroup = new ArrayList<>();
        groups.add(newGroup);
        newGroup.add(newLine);
      }
    }

    mergeGroups(groups);

    return groups;
  }

  /**
   * Merge groups.
   *
   * @param groups the groups
   */
  private static void mergeGroups(Collection<Collection<Line2D>> groups) {
    int previousSize = 0;
    while (previousSize != groups.size()) {
      previousSize = groups.size();
      outer:
      for (Collection<Line2D> outer : groups) {
        Iterator<Collection<Line2D>> it = groups.iterator();
        while (it.hasNext()) {
          Collection<Line2D> inner = it.next();
          if (outer == inner) {
            continue;
          }
          for (Line2D line1 : outer) {
            for (Line2D line2 : inner) {
              if (line1.intersectsLine(line2)) {
                outer.addAll(inner);
                it.remove();
                break outer;
              }
            }
          }
        }
      }
    }
  }

  /**
   * Find intersections.
   *
   * @param lines the lines
   * @return the collection
   */
  private Collection<Point2D> findIntersections(Collection<Line2D> lines) {
    List<Line2D> xlines = new ArrayList<>();
    List<Line2D> ylines = new ArrayList<>();
    for (Line2D line : lines) {
      if (isXline(line)) {
        xlines.add(line);
      } else if (isYline(line)) {
        ylines.add(line);
      }
    }

    Collection<Point2D> points = new HashSet<>();
    for (Line2D xline : xlines) {
      for (Line2D yline : ylines) {
        if (xline.intersectsLine(yline)) {
          Point2D intersectionPoint = intersectionPoint(xline, yline);
          Point2D roundedIntersectionPoint =
              new Point2D.Double(
                  (int) Math.round(intersectionPoint.getX()),
                  (int) Math.round(intersectionPoint.getY()));
          // point length line intersections at ends result in NaN computations and spurious (0,0)
          // points
          if (!equalTo(roundedIntersectionPoint.getX(), 0)
              && !equalTo(roundedIntersectionPoint.getY(), 0)) {
            points.add(roundedIntersectionPoint);
          }
        }
      }
    }
    return points;
  }

  /**
   * Equal to.
   *
   * @param d1 the d 1
   * @param d2 the d 2
   * @return true, if successful
   */
  static boolean equalTo(double d1, double d2) {
    return Math.abs(d1 - d2) <= 2d;
  }

  /**
   * Intersection point.
   *
   * @param l1 the l 1
   * @param l2 the l 2
   * @return the point 2 D
   */
  static Point2D intersectionPoint(Line2D l1, Line2D l2) {
    double l1x1 = l1.getX1();
    double l1y1 = l1.getY1();
    double l1x2 = l1.getX2();
    double l1y2 = l1.getY2();
    double l2x1 = l2.getX1();
    double l2y1 = l2.getY1();
    double l2x2 = l2.getX2();
    double l2y2 = l2.getY2();

    double l1xdiff = l1x1 - l1x2;
    double l1ydiff = l1y1 - l1y2;
    double l2xdiff = l2x1 - l2x2;
    double l2ydiff = l2y1 - l2y2;

    double detLine1 = determinate(l1x1, l1y1, l1x2, l1y2);
    double detLine2 = determinate(l2x1, l2y1, l2x2, l2y2);
    double detDiff = determinate(l1xdiff, l1ydiff, l2xdiff, l2ydiff);

    double xintersection = determinate(detLine1, l1xdiff, detLine2, l2xdiff) / detDiff;
    double yintersection = determinate(detLine1, l1ydiff, detLine2, l2ydiff) / detDiff;
    return new Point2D.Double(xintersection, yintersection);
  }

  /**
   * Determinate calculation.
   *
   * @param avalue the a
   * @param bvalue the b
   * @param cvalue the c
   * @param dvalue the d
   * @return the determinate.
   */
  static double determinate(double avalue, double bvalue, double cvalue, double dvalue) {
    return (avalue * dvalue) - (bvalue * cvalue);
  }

  /** The Class MyLine2D. */
  private static class EqualsAndHashcodeLine2D extends Line2D.Double {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new my line 2 D.
     *
     * @param x1 the x 1
     * @param y1 the y 1
     * @param x2 the x 2
     * @param y2 the y 2
     */
    public EqualsAndHashcodeLine2D(double x1, double y1, double x2, double y2) {
      super(x1, y1, x2, y2);
    }

    @Override
    public int hashCode() {
      int hash = 31;
      hash += 31 * getX1();
      hash += 31 * getX2();
      hash += 31 * getY1();
      hash += 31 * getY2();
      return hash;
    }

    @Override
    public boolean equals(Object obj) {
      if (obj instanceof EqualsAndHashcodeLine2D) {
        EqualsAndHashcodeLine2D other = (EqualsAndHashcodeLine2D) obj;
        if ((other.getP1().equals(getP1()) && other.getP2().equals(getP2()))
            || (other.getP2().equals(getP1()) && other.getP1().equals(getP2()))) {
          return true;
        }
      }
      return false;
    }

    @Override
    public String toString() {
      return getX1() + " " + getX2() + " " + getY1() + " " + getY2();
    }
  }
}
