package io.committed.krill.extraction.pdfbox.text;

import io.committed.krill.extraction.pdfbox.physical.Positioned;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * Implementation of the recursive x-y cut algorithm.
 *
 * @param <T> some implementation of {@link Positioned}
 */
public class RecursiveXyCut<T extends Positioned> {

  /** The by Y. */
  private final Comparator<T> byY = Comparator.comparing(s -> s.getPosition().getMinY());

  /** The by X. */
  private final Comparator<T> byX = Comparator.comparing(s -> s.getPosition().getMinX());

  /** The xspacing. */
  private final int xspacing;

  /** The yspacing. */
  private final int yspacing;

  /** The projection scale. */
  private final int projectionScale;

  /**
   * Instantiates a new recursive X-Y Cut.
   *
   * @param xspacing the x spacing
   * @param yspacing the y spacing
   * @param projectionScale the projection scale
   */
  public RecursiveXyCut(int xspacing, int yspacing, int projectionScale) {
    this.xspacing = xspacing;
    this.yspacing = yspacing;
    this.projectionScale = projectionScale;
  }

  /**
   * Perform the recursive x-y cut, mutating the given {@link TreeNode} and creating two child nodes
   * if a split is found.
   *
   * @param node the node to apply the algorithm to.
   */
  public void apply(TreeNode<PositionedContainer<T>> node) {
    PositionedContainer<T> container = node.getData();
    container.getContents().sort(byY.thenComparing(byX));

    // split vertically
    boolean[] xprojection = buildXprojection(container);
    Run longestx = findLongestFalseRun(xprojection);

    boolean[] yprojection = buildYprojection(container);
    Run longesty = findLongestFalseRun(yprojection);

    if (isYrunLongEnough(longesty)) {
      split(node, yprojection, longesty, s -> s.getPosition().getMinY());
    } else if (longesty.length <= longestx.length && isXrunLongEnough(longestx)) {
      split(node, xprojection, longestx, s -> s.getPosition().getMinX());
    }

    // recurse into any children that have been identified above
    for (TreeNode<PositionedContainer<T>> child : node.getChildren()) {
      apply(child);
    }
  }

  /**
   * Checks if is yrun long enough.
   *
   * @param longesty the longesty
   * @return true, if is yrun long enough
   */
  private boolean isYrunLongEnough(Run longesty) {
    return longesty.length > yspacing * projectionScale;
  }

  /**
   * Checks if is xrun long enough.
   *
   * @param longestx the longestx
   * @return true, if is xrun long enough
   */
  private boolean isXrunLongEnough(Run longestx) {
    return longestx.length > xspacing * projectionScale;
  }

  /**
   * Split.
   *
   * @param node the node
   * @param projection the projection
   * @param longest the longest
   * @param extractor the extractor
   */
  private void split(
      TreeNode<PositionedContainer<T>> node,
      boolean[] projection,
      Run longest,
      Function<Positioned, Double> extractor) {
    int splitPoint = (int) ((longest.index + longest.length / 2f) / projectionScale);

    List<T> before = new ArrayList<>();
    List<T> after = new ArrayList<>();
    for (T block : node.getData().getContents()) {
      if (extractor.apply(block) > splitPoint) {
        after.add(block);
      } else {
        before.add(block);
      }
    }

    if (!before.isEmpty() && !after.isEmpty()) {
      after.sort(Comparator.comparing(extractor));
      before.sort(Comparator.comparing(extractor));
      node.addChild(new PositionedContainer<T>(before));
      node.addChild(new PositionedContainer<T>(after));
      node.getData().getContents().clear();
    } else {
      node.getData().getContents().sort(byY.thenComparing(byX));
    }
  }

  /**
   * Find longest false run.
   *
   * @param projection the projection
   * @return the run
   */
  private Run findLongestFalseRun(boolean[] projection) {
    int firstTrueIndex = firstTrueIndex(projection);
    int lastTrueIndex = lastTrueIndex(projection);
    boolean previous = false;

    Run longest = new Run();
    Run current = new Run();

    for (int i = firstTrueIndex; i < lastTrueIndex; i++) {
      boolean currentValue = projection[i];
      if (!currentValue) {
        if (!previous) {
          current.length++;
          if (current.length > longest.length) {
            longest.length = current.length;
            longest.index = current.index;
          }
        } else {
          current.length = 1;
          current.index = i;
        }
      } else {
        current.length = 0;
        current.index = 0;
      }
      previous = currentValue;
    }
    return longest;
  }

  /**
   * Last true index.
   *
   * @param projection the projection
   * @return the int
   */
  private int lastTrueIndex(boolean[] projection) {
    int firstNonNullIndex = 0;
    for (int i = projection.length - 1; i >= 0; i--) {
      if (projection[i]) {
        firstNonNullIndex = i;
        break;
      }
    }
    return firstNonNullIndex;
  }

  /**
   * First true index.
   *
   * @param projection the projection
   * @return the int
   */
  private int firstTrueIndex(boolean[] projection) {
    int firstNonNullIndex = 0;
    for (int i = 0; i < projection.length; i++) {
      if (projection[i]) {
        firstNonNullIndex = i;
        break;
      }
    }
    return firstNonNullIndex;
  }

  /**
   * Builds the xprojection.
   *
   * @param data the data
   * @return the boolean[]
   */
  private boolean[] buildXprojection(PositionedContainer<T> data) {
    int width = (int) (data.getPosition().getMaxX() * projectionScale);
    if (width < 0) {
      return new boolean[0];
    }
    boolean[] projection = new boolean[width];
    for (Positioned p : data.getContents()) {
      for (int j = (int) (p.getPosition().getMinX() * projectionScale);
          j < (int) (p.getPosition().getMaxX() * projectionScale);
          j++) {
        projection[j < 0 ? 0 : j] = true;
      }
    }
    return projection;
  }

  /**
   * Builds the yprojection.
   *
   * @param data the data
   * @return the boolean[]
   */
  private boolean[] buildYprojection(PositionedContainer<T> data) {
    int height = (int) (data.getPosition().getMaxY() * projectionScale);
    if (height < 0) {
      return new boolean[0];
    }
    boolean[] projection = new boolean[height];
    for (Positioned p : data.getContents()) {
      for (int j = (int) (p.getPosition().getMinY() * projectionScale);
          j < (int) (p.getPosition().getMaxY() * projectionScale);
          j++) {
        projection[j < 0 ? 0 : j] = true;
      }
    }
    return projection;
  }

  /**
   * A simple tree implementation.
   *
   * @param <T> the generic type
   */
  public static class TreeNode<T> {

    /** The data. */
    private T data;

    /** The children. */
    private List<TreeNode<T>> children = new ArrayList<>();

    /**
     * Instantiates a new tree node.
     *
     * @param data the data
     */
    public TreeNode(T data) {
      this.data = data;
    }

    /**
     * Returns the data for this node.
     *
     * @return the data
     */
    public T getData() {
      return data;
    }

    /**
     * Add a child to this node.
     *
     * @param child the child.
     */
    public void addChild(T child) {
      children.add(new TreeNode<T>(child));
    }

    /**
     * Returns the children of this node.
     *
     * @return the children.
     */
    public List<TreeNode<T>> getChildren() {
      return children;
    }

    @Override
    public String toString() {
      return "TreeNode [data=" + data + ", children=" + children + "]";
    }
  }

  /** The Class Run. */
  private static class Run {

    /** The index. */
    int index = 0;

    /** The length. */
    int length = 0;

    @Override
    public String toString() {
      return "Run [index=" + index + ", length=" + length + "]";
    }
  }
}
