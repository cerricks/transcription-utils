/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.punctuation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * A utility for replacing punctuation in a file.
 *
 * @author Clifford Errickson
 */
public class TranscriptionPunctuationUtil {

  private final static Logger logger = Logger.getLogger(TranscriptionPunctuationUtil.class.getName());

  private final static Options options;

  static {
    options = new Options();

    Option input = new Option("i", "input", true, "input file to read data from");
    input.setArgName("FILE PATH");

    Option output = new Option("o", "output", true, "output file or directory to write the final result");
    output.setArgName("FILE PATH");

    Option help = new Option("h", "help", false, "print this message");

    options.addOption(input);
    options.addOption(output);
    options.addOption(help);
  }

  public static void main(String[] args) throws ParseException {
    CommandLineParser parser = new DefaultParser();
    CommandLine cmd = parser.parse(options, args);

    if (cmd.hasOption("help")) {
      HelpFormatter formatter = new HelpFormatter();
      formatter.printHelp("java -jar transcription-punctuation.jar", options, true);
      System.exit(1);
    }

    String input = null;
    String output = null;

    if (cmd.hasOption("input")) {
      input = cmd.getOptionValue("input");
    } else {
      logger.log(Level.WARNING, "Input is required. See -help for more options.");
      System.exit(1);
    }

    if (cmd.hasOption("output")) {
      output = cmd.getOptionValue("output");
    } else {
      logger.log(Level.WARNING, "Output is required. See -help for more options.");
      System.exit(1);
    }

    TranscriptionPunctuationFormatter formatter = new TranscriptionPunctuationFormatter();

    try {
      formatter.findAndReplace(input, output);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Formatting failed", ex);
      System.exit(1);
    }

    logger.log(Level.INFO, "Processing complete");
  }
}
