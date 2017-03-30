package io.committed.krill.extraction.tika.docx;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class DocxOtherStylesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "otherstyles.docx";

  public DocxOtherStylesTikaFormatExtractorIT() {
    super(RESOURCE_NAME);
  }


  @Test
  public void testBody() {
    // NOTE Styles are not pulled through

    assertBody("" + "<main class=\"Document\"> \n"
        + " <h3><a name=\"_Toc465796961\"></a>Other styles</h3> \n"
        + " <p>There are a number of other styles that can be used inline. These are Subtle Emphasis, Emphasis, Intense Emphasis, Strong, Subtle Reference, and Intense Reference.</p> \n"
        + " <h3><a name=\"_Toc465796962\"></a>Custom styles</h3> \n"
        + " <p>It is possible to define completely new styles; in the following paragraph any computer code has been labelled using a character style called ‘code’ which is not based on any existing style and uses a monospaced font, ‘Menlo’:</p> \n"
        + " <p></p> \n"
        + " <p>The main entry point into a C/C++ program is a function called main, which can be defined as either int main(void) or int main(int argc, char* argv[]). This can be logically rewritten as int main(int argc, char** argv). Some platforms (POSIX, and hence Unix/Windows) add the environment int main(int argc, char **argv, char **envp) as a way to internalise extern char **environ. On Darwin/OSX add additional parameters are provided through the apple argument of the entry point int main(int argc, char **argv, char **envp, char **apple) - for example, apple[0] contains the path to the currently executing binary (not necessarily the same as the value of argv[0] since this can be set to any arbitrary value by execve(2)).</p> \n"
        + "</main>");
  }



}
