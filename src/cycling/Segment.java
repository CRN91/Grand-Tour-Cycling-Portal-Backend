package src.cycling;

import java.util.HashMap;

/**
 * Represents a segment of a race.
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
