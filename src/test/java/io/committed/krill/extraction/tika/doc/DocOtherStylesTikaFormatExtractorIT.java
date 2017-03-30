package io.committed.krill.extraction.tika.doc;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocOtherStylesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "otherstyles.doc";

  public DocOtherStylesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    assertBody("" + "<main class=\"Document\"> \n" + " <h3>Other styles </h3> \n"
        + " <p>There are a number of other styles that can be used inline. These are <i>Subtle Emphasis</i>, <i>Emphasis, Intense Emphasis</i>, <b>Strong</b>, Subtle Reference, and <b>Intense Reference</b>. </p> \n"
        + " <h3>Custom styles </h3> \n"
        + " <p>It is possible to define completely new styles; in the following paragraph any computer code has been labelled using a character style called ‘code’ which is not based on any existing style and uses a monospaced font, ‘Menlo’: </p> \n"
        + " <p>The main entry point into a C/C++ program is a function called main, which can be defined as either int main(void) or int main(int argc, char* argv[]). This can be logically rewritten as int main(int argc, char** argv). Some platforms (POSIX, and hence Unix/Windows) add the environment int main(int argc, char **argv, char **envp) as a way to internalise extern char **environ. On Darwin/OSX add additional parameters are provided through the apple argument of the entry point int main(int argc, char **argv, char **envp, char **apple) - for example, apple[0] contains the path to the currently executing binary (not necessarily the same as the value of argv[0] since this can be set to any arbitrary value by execve(2)).</p> \n"
        + "</main>");
  }



}
