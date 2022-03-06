package src.cycling;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Represents an intermediate sprint. CategorisedClimb is a subclass of this for categorised climbs.
 *
 * @author Sam Barker, Adam Kaizrea
 * @version 1.0
 */

//(int stageId, Double location, SegmentType type,
//                                        Double averageGradient, Double length)
public class Segment implements Serializable {
  private Integer stageId;
  private Double location;
  private SegmentType segmentType;
  private Integer id;

  private static int latestId = 0;

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
  public Integer getId() {
    return id;
  }

  /**
   * Reset the internal ID counter
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  public Segment(int stageId, SegmentType segmentType) {
    this.stageId = stageId;
    this.segmentType = segmentType;
    this.id = latestId++;
  }
}
