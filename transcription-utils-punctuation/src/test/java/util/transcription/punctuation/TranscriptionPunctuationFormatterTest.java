/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.punctuation;

import java.io.File;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Clifford Errickson
 */
public class TranscriptionPunctuationFormatterTest {

  /**
   * Test of findAndReplace method, of class TranscriptionPunctuationFormatter.
   */
  @Test
  public void testFindAndReplace_outputToFile() throws Exception {
    String input = "src/test/resources/input/test.txt";
    String output = "src/test/resources/output/out.txt";

    TranscriptionPunctuationFormatter instance = new TranscriptionPunctuationFormatter();
    instance.findAndReplace(input, output);

    File expectedResult = Paths.get("src/test/resources/output/expected-result.txt").toFile();
    File outputFile = Paths.get(output).toFile();

    assertEquals("The files differ!",
            FileUtils.readFileToString(expectedResult, "utf-8"),
            FileUtils.readFileToString(outputFile, "utf-8"));

    outputFile.delete();
  }

  /**
   * Test of findAndReplace method, of class TranscriptionPunctuationFormatter.
   */
  @Test
  public void testFindAndReplace_outputToDirectory() throws Exception {
    String input = "src/test/resources/input/test.txt";
    String output = "src/test/resources/output";

    TranscriptionPunctuationFormatter instance = new TranscriptionPunctuationFormatter();
    instance.findAndReplace(input, output);

    File expectedResult = Paths.get("src/test/resources/output/expected-result.txt").toFile();
    File outputFile = Paths.get("src/test/resources/output/test-modified.txt").toFile();

    assertEquals("The files differ!",
            FileUtils.readFileToString(expectedResult, "utf-8"),
            FileUtils.readFileToString(outputFile, "utf-8"));

    outputFile.delete();
  }

}
