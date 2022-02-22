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
  private int stageId;
  private Integer id;

  private static int latestSegmentId = 0;

  public SegmentType getSegmentType() {
    return segmentType;
  }

  public void setSegmentType(SegmentType segmentType) {
    this.segmentType = segmentType;
  }

  public int getStageId() {
    return this.stageId;
  }

  /**
   *
   * @return stage's ID.
   */
  public Integer getId() { return id; }

  public Segment(int stageId, SegmentType segmentType) {
    this.stageId = stageId;
    this.segmentType = segmentType;
    this.id = latestSegmentId++;
  }
}
