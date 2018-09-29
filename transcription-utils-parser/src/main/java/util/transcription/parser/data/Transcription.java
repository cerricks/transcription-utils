/*
 * Copyright 2018 Clifford Errickson.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package util.transcription.parser.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a JSON object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transcription {

  private List<Result> results;

  @JsonProperty("speaker_labels")
  private List<SpeakerLabel> speakerLabels;

  @JsonProperty("result_index")
  private int resultIndex;

  public Transcription() {
  }

  public List<Result> getResults() {
    return results;
  }

  public void setResults(List<Result> results) {
    this.results = results;
  }

  public int getResultIndex() {
    return resultIndex;
  }

  public void setResultIndex(int resultIndex) {
    this.resultIndex = resultIndex;
  }

  public List<SpeakerLabel> getSpeakerLabels() {
    return speakerLabels;
  }

  public void setSpeakerLabels(List<SpeakerLabel> speakerLabels) {
    this.speakerLabels = speakerLabels;
  }

  public Alternative getAlternativeStartingAt(double startTime) {
    for (Result result : results) {
      for (Alternative alternative : result.getAlternatives()) {
        if (alternative.getStartTime() == startTime) {
          return alternative;
        }
      }
    }

    return null;
  }

  public Integer getSpeakerIndex(Timestamp timestamp) {
    for (SpeakerLabel speakerLabel : speakerLabels) {
      if (speakerLabel.getFrom() == timestamp.getFrom()) {
        return speakerLabel.getSpeaker();
      }
    }

    return null;
  }

  public String getTranscript(double from, double to) {
    StringBuilder sb = new StringBuilder();

    for (Result result : results) {
      for (Alternative alternative : result.getAlternatives()) {
        for (Timestamp timestamp : alternative.getTimestamps()) {
          if (timestamp.getFrom() >= from && timestamp.getFrom() < to) {
            sb.append(timestamp.getWord()).append(' ');
          }
        }
      }
    }

    return sb.toString().trim();
  }

}
