/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.punctuation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;

/**
 * Replaces words in a file with punctuation.
 *
 * @author Clifford Errickson
 */
public class TranscriptionPunctuationFormatter {

  private static final Pattern CAPS_PATTERN = Pattern.compile("(?i)\\bcaps on (.*?) caps off\\b");

  /**
   * Reads content from an input file, replaces punctuation, and writes result
   * to output.
   *
   * @param input input file
   * @param output output file or directory
   * @throws IOException on error
   */
  public void findAndReplace(String input, String output) throws IOException {
    Path inputPath = Paths.get(input);
    Path outputPath = Paths.get(output);

    if (Files.isDirectory(outputPath)) {
      String baseName = FilenameUtils.getBaseName(inputPath.getFileName().toString());
      String extension = FilenameUtils.getExtension(inputPath.getFileName().toString());
      String outputFileName = baseName + "-modified." + extension;

      outputPath = Paths.get(outputPath.toString(), outputFileName);
    }

    try (Stream<String> lines = Files.lines(inputPath)) {
      List<String> replaced = lines.map(line -> formatLine(line)).collect(Collectors.toList());

      Files.write(outputPath, replaced);
    }
  }

  /**
   * Takes a line of text, replaces punctuation, and returns the result.
   *
   * @param line the original line of text.
   * @return the resulting line of text with punctuation replaced.
   */
  private static String formatLine(String line) {
    line = line.replaceAll("(?i)\\b[ ]?period\\b", ".");
    line = line.replaceAll("(?i)\\b[ ]?comma\\b", ",");
    line = line.replaceAll("(?i)\\b[ ]?question mark\\b", "?");
    line = line.replaceAll("(?i)\\b[ ]?exclamation point\\b", "!");
    line = line.replaceAll("(?i)\\b[ ]?exclamation mark\\b", "!");
    line = line.replaceAll("(?i)\\b[ ]?semi colon\\b", ";"); // this must come before "colon"
    line = line.replaceAll("(?i)\\b[ ]?semi-colon\\b", ";"); // this must come before "colon"
    line = line.replaceAll("(?i)\\b[ ]?colon\\b", ":");
    line = line.replaceAll("(?i)\\bopen quote[ ]?\\b", "\"");
    line = line.replaceAll("(?i)\\b[ ]?close quote\\b", "\"");
    line = line.replaceAll("(?i)\\bopen parenthesis[ ]?\\b", "(");
    line = line.replaceAll("(?i)\\b[ ]?close parenthesis\\b", ")");
    line = line.replaceAll("(?i)\\b[ ]?ellipsis\\b", "...");
    line = line.replaceAll("(?i)\\bampersand\\b", "&");
    line = line.replaceAll("(?i)\\b[ ]?dash[ ]?\\b", "-");
    line = line.replaceAll("(?i)\\b[ ]?hyphen[ ]?\\b", "-");
    line = line.replaceAll("(?i)\\b[ ]?new line[ ]?\\b", System.lineSeparator());
    line = line.replaceAll("(?i)\\b[ ]?new paragraph[ ]?\\b", System.lineSeparator());
    line = line.replaceAll("(?i)\\b[ ]?tab key[ ]?\\b", "\t");

    StringBuffer sb = new StringBuffer();

    Matcher capsMatcher = CAPS_PATTERN.matcher(line);

    while (capsMatcher.find()) {
      capsMatcher.appendReplacement(sb, capsMatcher.group(1).toUpperCase());
    }

    capsMatcher.appendTail(sb);

    return sb.toString();
  }
}
