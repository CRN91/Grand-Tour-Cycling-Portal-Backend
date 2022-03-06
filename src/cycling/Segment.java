package src.cycling;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.HashMap;

/**
 * Represents an intermediate sprint. CategorisedClimb is a subclass of this for categorised climbs.
 *
 * @author Sam Barker, Adam Kaizrea
 * @version 1.0
 */

//(int stageId, Double location, SegmentType type,
//                                        Double averageGradient, Double length)
public class Segment implements Comparable<Segment> {
  private Integer stageId;
  private Double location;
  private SegmentType segmentType;
  private Integer id;
  private SegmentTimes[] orderedTimesToRiderId;

  private static int latestId = 0;

  public int compareTo(Segment segment) {
    return this.location.compareTo(segment.getLocation());
  }

  public SegmentType getSegmentType() {
    return segmentType;
  }

  public void setSegmentType(SegmentType segmentType) {
    this.segmentType = segmentType;
  }

  public int getStageId() {
    return this.stageId;
  }

  public SegmentTimes[] getOrderedTimesToRiderId() {
    return orderedTimesToRiderId;
  }

  public void setOrderedTimesToRiderId(SegmentTimes[] orderedTimesToRiderId) {
    this.orderedTimesToRiderId = orderedTimesToRiderId;
  }

  /**
   *
   * @return stage's ID.
   */
  public Integer getId() {
    return id;
  }

  public Double getLocation() {
    return location;
  }

  public void setLocation(Double location) {
    this.location = location;
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
