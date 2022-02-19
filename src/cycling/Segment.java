package src.cycling;

import java.util.HashMap;

/**
 * Represents a segment of a race.
 *
 * @author Sam Barker, Adam Kaizrea
 * @version 1.0
 */
public class Segment {
  private SegmentType segmentType;

  public SegmentType getSegmentType() {
    return segmentType;
  }

  public void setSegmentType(SegmentType segmentType) {
    this.segmentType = segmentType;
  }
}
