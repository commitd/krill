package io.committed.krill.extraction.tika.helper;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import org.jsoup.nodes.Document;

public class HtmlTestCodeGenerator {

  private final static Splitter SPLITTER = Splitter.on("\n");
  private final static Joiner JOINER = Joiner.on("\\n\" +\n\"");

  private HtmlTestCodeGenerator() {
    // Singleton
  }

  public static void generate(final Document document) {

    final String method = "  @Test\n" + "  public void testBody() {\n" + "    assertBody(\"\"+\n"
        + "\"%s\");" + "\n}\n";

    final String html = document.body().html().replaceAll("\\\"", "\\\\\\\"");
    final Iterable<String> split = SPLITTER.split(html);
    final String newlined = JOINER.join(split);

    System.out.println();
    System.out.println(String.format(method, newlined));
    System.out.println();

  }
}
