/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.soap;

import java.io.File;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Clifford Errickson
 */
public class TranscriptionSOAPParserTest {

  /**
   * Test of parse method, of class TranscriptionSOAPParser.
   */
  @Test
  public void testParse() throws Exception {
    String input = "src/test/resources/input/input.txt";
    String output = "src/test/resources/output/output.txt";

    TranscriptionSOAPParser instance = new TranscriptionSOAPParser();
    instance.parse(input, output);

    File expectedResult = Paths.get("src/test/resources/output/expected-result.txt").toFile();
    File outputFile = Paths.get(output).toFile();

    assertEquals("The files differ!",
            FileUtils.readFileToString(expectedResult, "utf-8"),
            FileUtils.readFileToString(outputFile, "utf-8"));

    outputFile.delete();
  }

}
