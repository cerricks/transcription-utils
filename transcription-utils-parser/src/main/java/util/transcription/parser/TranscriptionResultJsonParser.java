/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.transcription.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import util.transcription.parser.data.Alternative;
import util.transcription.parser.data.Result;
import util.transcription.parser.data.SpeakerLabel;
import util.transcription.parser.data.Timestamp;
import util.transcription.parser.data.Transcription;

/**
 * Parses a JSON file containing transcription data into various files.
 *
 * @author Clifford Errickson
 */
public class TranscriptionResultJsonParser {

  private final static String OUTPUT1_FILENAME = "continuous_transcript.txt";
  private final static String OUTPUT2_FILENAME = "speaker_duration.txt";
  private final static String OUTPUT3_FILENAME = "keyword_count.txt";

  public void parse(String input, String output) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

    Transcription transcription = objectMapper.readValue(new File(input), Transcription.class);

    // Continuous transcript
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(output != null ? Paths.get(output, OUTPUT1_FILENAME).toFile() : new File(OUTPUT1_FILENAME)))) {
      for (Result result : transcription.getResults()) {
        writer.write(result.getAlternatives().get(0).getTranscript());
      }
    }

    // speaker duration
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(output != null ? Paths.get(output, OUTPUT2_FILENAME).toFile() : new File(OUTPUT2_FILENAME)))) {
      SpeakerLabel startSpeakerLabel = null;
      SpeakerLabel previousSpeakerLabel = null;

      for (SpeakerLabel speakerLabel : transcription.getSpeakerLabels()) {
        if (startSpeakerLabel == null) {
          startSpeakerLabel = speakerLabel;
          previousSpeakerLabel = speakerLabel;
          continue;
        }

        if (speakerLabel.getSpeaker() != previousSpeakerLabel.getSpeaker()) {
          writer.write("Speaker " + startSpeakerLabel.getSpeaker() + " (" + startSpeakerLabel.getFrom() + "-" + previousSpeakerLabel.getTo() + "):");
          writer.newLine();
          writer.write(transcription.getTranscript(startSpeakerLabel.getFrom(), previousSpeakerLabel.getTo()));
          writer.newLine();

          startSpeakerLabel = speakerLabel;
        }

        previousSpeakerLabel = speakerLabel;
      }

      writer.write("Speaker " + startSpeakerLabel.getSpeaker() + " (" + startSpeakerLabel.getFrom() + "-" + previousSpeakerLabel.getTo() + "):");
      writer.newLine();
      writer.write(transcription.getTranscript(startSpeakerLabel.getFrom(), previousSpeakerLabel.getTo()));
      writer.newLine();
    }

    // keywords
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(output != null ? Paths.get(output, OUTPUT3_FILENAME).toFile() : new File(OUTPUT3_FILENAME)))) {
      Map<String, Map<Integer, Integer>> keywordSpeakerCounts = new HashMap<>();

      for (Result result : transcription.getResults()) {
        for (Alternative alternative : result.getAlternatives()) {
          for (Timestamp timestamp : alternative.getTimestamps()) {
            if (timestamp.getWord().matches("[%].*")) {
              String keyword = timestamp.getWord().substring(1);

              int speakerIndex = transcription.getSpeakerIndex(timestamp);

              if (keywordSpeakerCounts.containsKey(keyword)) {
                if (keywordSpeakerCounts.get(keyword).containsKey(speakerIndex)) {
                  keywordSpeakerCounts.get(keyword).put(speakerIndex, keywordSpeakerCounts.get(keyword).get(speakerIndex) + 1);
                } else {
                  keywordSpeakerCounts.get(keyword).put(speakerIndex, 1);
                }
              } else {
                Map<Integer, Integer> speakerCounts = new HashMap();
                speakerCounts.put(speakerIndex, 1);

                keywordSpeakerCounts.put(keyword, speakerCounts);
              }
            }
          }
        }
      }

      for (String keyword : keywordSpeakerCounts.keySet()) {
        for (Integer speakerIndex : keywordSpeakerCounts.get(keyword).keySet()) {
          writer.write(keyword + ": Speaker " + speakerIndex + ": " + keywordSpeakerCounts.get(keyword).get(speakerIndex) + " time(s)");
          writer.newLine();
        }
      }
    }
  }
}
