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

/**
 * Represents a JSON object.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeakerLabel {

  private double from;
  private double to;
  private int speaker;
  private double confidence;

  public SpeakerLabel() {
  }

  public double getFrom() {
    return from;
  }

  public void setFrom(double from) {
    this.from = from;
  }

  public double getTo() {
    return to;
  }

  public void setTo(double to) {
    this.to = to;
  }

  public int getSpeaker() {
    return speaker;
  }

  public void setSpeaker(int speaker) {
    this.speaker = speaker;
  }

  public double getConfidence() {
    return confidence;
  }

  public void setConfidence(double confidence) {
    this.confidence = confidence;
  }

}
