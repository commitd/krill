package io.committed.krill.extraction.pdfbox.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.google.common.base.CharMatcher;

import io.committed.krill.extraction.pdfbox.physical.Line;
import io.committed.krill.extraction.pdfbox.physical.PositionedContainer;
import io.committed.krill.extraction.pdfbox.physical.Text;
import io.committed.krill.extraction.pdfbox.physical.Word;

/**
 * A simple implementation of of WordSegmenter that only considers whitespace characters to break
 * words, and does not attempt to segment the line into potential columns (it assumes this has been
 * done previously).
 */
public class WhitespaceWordSegmenter implements WordSegmenter {

  @Override
  public Optional<Line> segmentWords(PositionedContainer<Text> orderedLine) {
    final List<Word> words = new ArrayList<>();
    List<Text> content = new ArrayList<>();

    Iterator<Text> it = orderedLine.getContents().iterator();

    while (it.hasNext()) {
      Text positionedStyledText = it.next();
      String text = positionedStyledText.toString();
      if (CharMatcher.whitespace().matchesAllOf(text)) {
        if (!content.isEmpty()) {
          words.add(new Word(content));
          content = new ArrayList<>();
        }
      } else {
        content.add(positionedStyledText);
      }
    }

    if (!content.isEmpty()) {
      words.add(new Word(content));
    }

    if (words.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(new Line(words));
  }

}
