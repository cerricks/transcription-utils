/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.parser;

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
 * A utility for parsing a Transcription result file into multiple outputs.
 *
 * @author Clifford Errickson
 */
public class TranscriptionResultParserUtil {

  private final static Logger logger = Logger.getLogger(TranscriptionResultParserUtil.class.getName());

  private final static Options options;

  static {
    options = new Options();

    Option input = new Option("i", "input", true, "input file to read data from");
    input.setArgName("FILE PATH");

    Option output = new Option("o", "output", true, "output directory to write the final results");
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
      formatter.printHelp("java -jar transcription-parser.jar", options, true);
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

    TranscriptionResultJsonParser transcriptionParser = new TranscriptionResultJsonParser();

    logger.log(Level.INFO, "Parsing file: {0}", input);

    try {
      transcriptionParser.parse(input, output);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Transctiption parsing failed", ex);
      System.exit(1);
    }

    logger.log(Level.INFO, "Processing complete");
  }

}
