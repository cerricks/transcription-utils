/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.soap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;

/**
 * Parses SOAP notes from a medical transcription using Regex patterns to match
 * the Subjective, Objective, Assessment, and Plan keywords.
 *
 * @author Clifford Errickson
 */
public class TranscriptionSOAPParser {

  private static final Pattern DEFAULT_PATTERN = Pattern.compile("(?i)^(.*?)(\\bSUBJECTIVE\\b|\\bOBJECTIVE\\b|\\bASSESSMENT\\b|\\bPLAN\\b|$)");
  private static final Pattern SUBJECTIVE_PATTERN = Pattern.compile("(?i)(SUBJECTIVE )(.*?)(\\bOBJECTIVE\\b|\\bASSESSMENT\\b|\\bPLAN\\b|$)");
  private static final Pattern OBJECTIVE_PATTERN = Pattern.compile("(?i)(OBJECTIVE )(.*?)(\\bSUBJECTIVE\\b|\\bASSESSMENT\\b|\\bPLAN\\b|$)");
  private static final Pattern ASSESSMENT_PATTERN = Pattern.compile("(?i)(ASSESSMENT )(.*?)(\\bSUBJECTIVE\\b|\\bOBJECTIVE\\b|\\bPLAN\\b|$)");
  private static final Pattern PLAN_PATTERN = Pattern.compile("(?i)(PLAN )(.*?)(\\bSUBJECTIVE\\b|\\bOBJECTIVE\\b|\\bASSESSMENT\\b|$)");

  /**
   * Parse SOAP notes from a file and output to a new file.
   *
   * @param input the input file.
   * @param output the output file or directory.
   * @throws IOException on IO error.
   */
  public void parse(String input, String output) throws IOException {
    Path inputPath = Paths.get(input);
    Path outputPath = Paths.get(output);

    if (Files.isDirectory(outputPath)) {
      String baseName = FilenameUtils.getBaseName(inputPath.getFileName().toString());
      String extension = FilenameUtils.getExtension(inputPath.getFileName().toString());
      String outputFileName = baseName + "-SOAP." + extension;

      outputPath = Paths.get(outputPath.toString(), outputFileName);
    }

    byte[] encoded = Files.readAllBytes(inputPath);

    String fileContent = new String(encoded, "UTF-8");

    String defaultText = getSection(fileContent, DEFAULT_PATTERN, 1);
    String subjectiveText = getSection(fileContent, SUBJECTIVE_PATTERN, 2);
    String objectiveText = getSection(fileContent, OBJECTIVE_PATTERN, 2);
    String assessmentText = getSection(fileContent, ASSESSMENT_PATTERN, 2);
    String planText = getSection(fileContent, PLAN_PATTERN, 2);

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
      writer.write("SUBJECTIVE:");
      writer.newLine();
      if (defaultText != null && !defaultText.equals("")) {
        writer.write(defaultText);
      }
      if (subjectiveText != null && !subjectiveText.equals("")) {
        if (defaultText != null && !defaultText.equals("")) {
          writer.write(" ");
        }
        writer.write(subjectiveText);
        writer.newLine();
      } else {
        writer.newLine();
      }
      writer.newLine();

      writer.write("OBJECTIVE:");
      writer.newLine();
      if (objectiveText != null && !objectiveText.equals("")) {
        writer.write(objectiveText);
        writer.newLine();
      }
      writer.newLine();

      writer.write("ASSESSMENT:");
      writer.newLine();
      if (assessmentText != null && !assessmentText.equals("")) {
        writer.write(assessmentText);
        writer.newLine();
      }
      writer.newLine();

      writer.write("PLAN:");
      writer.newLine();
      if (planText != null && !planText.equals("")) {
        writer.write(planText);
      }
    }
  }

  /**
   * Extract text matching the given pattern from a string.
   *
   * @param str the text string.
   * @param pattern the pattern to match and extract.
   * @param group the group id to extract from the matching pattern.
   * @return text matching the given pattern.
   */
  private static String getSection(String str, Pattern pattern, int group) {
    StringBuilder sb = new StringBuilder();

    Matcher capsMatcher = pattern.matcher(str);

    while (capsMatcher.find()) {
      sb.append(capsMatcher.group(group));
    }

    return sb.toString().trim();
  }

}
