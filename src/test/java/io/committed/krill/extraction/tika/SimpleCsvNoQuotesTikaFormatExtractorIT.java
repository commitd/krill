package io.committed.krill.extraction.tika;

import org.junit.Test;

import io.committed.krill.extraction.tika.helper.AbstractTikaFormatExtractorIT;

public class SimpleCsvNoQuotesTikaFormatExtractorIT extends AbstractTikaFormatExtractorIT {

  private static final String RESOURCE_NAME = "simple.csv";

  public SimpleCsvNoQuotesTikaFormatExtractorIT() {
    super(RESOURCE_NAME, false);
  }

  @Test
  public void testBody() {
    assertBody("" + "<main class=\"SpreadSheet\"> \n" + " <article class=\"Sheet\"> \n"
        + "  <table> \n" + "   <tbody> \n" + "    <tr> \n" + "     <th>A</th> \n"
        + "     <th>B</th> \n" + "     <th>C</th> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>1a</td> \n" + "     <td>1b</td> \n" + "     <td>1</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>2a</td> \n" + "     <td>2b</td> \n" + "     <td>2</td> \n"
        + "    </tr> \n" + "    <tr> \n" + "     <td>3a</td> \n" + "     <td>3b</td> \n"
        + "     <td>3</td> \n" + "    </tr> \n" + "    <tr> \n" + "     <td>4</td> \n"
        + "     <td>4a</td> \n" + "     <td>4b</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>5</td> \n" + "     <td>5a</td> \n" + "     <td>5b</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>6</td> \n" + "     <td>6a</td> \n" + "     <td>6b</td> \n"
        + "    </tr> \n" + "    <tr> \n" + "     <td>7</td> \n" + "     <td>7a</td> \n"
        + "     <td>7b</td> \n" + "    </tr> \n" + "    <tr> \n" + "     <td>8</td> \n"
        + "     <td>8a</td> \n" + "     <td>8b</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>9</td> \n" + "     <td>9a</td> \n" + "     <td>9b</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>10</td> \n" + "     <td>10a</td> \n" + "     <td>10b</td> \n"
        + "    </tr> \n" + "    <tr> \n" + "     <td>11</td> \n" + "     <td>11a</td> \n"
        + "     <td>11b</td> \n" + "    </tr> \n" + "    <tr> \n" + "     <td>12</td> \n"
        + "     <td>12a</td> \n" + "     <td>12b</td> \n" + "    </tr> \n" + "    <tr> \n"
        + "     <td>13</td> \n" + "     <td>13a</td> \n" + "     <td>13b</td> \n" + "    </tr> \n"
        + "    <tr> \n" + "     <td>14</td> \n" + "     <td>14a</td> \n" + "     <td>14b</td> \n"
        + "    </tr> \n" + "    <tr> \n" + "     <td>15</td> \n" + "     <td>15a</td> \n"
        + "     <td>15b</td> \n" + "    </tr> \n" + "    <tr> \n" + "     <td>16</td> \n"
        + "     <td>16a</td> \n" + "     <td>16b</td> \n" + "    </tr> \n" + "   </tbody> \n"
        + "  </table> \n" + " </article> \n" + "</main>");
  }


}
