package src.cycling;

import java.util.ArrayList;

/**
 * Represents an intermediate sprint. CategorisedClimb is a subclass of this for categorised
 * climbs.
 * @author Sam Barker, Adam Kaizrea
 * @version 1.0
 */
public class Segment implements Comparable<Segment> {

  private static int latestId = 0;
  private final Integer stageId;
  private Double location;
  private SegmentType segmentType;
  private final Integer id;
  private ArrayList<RiderSegmentResult> results = new ArrayList<>();

  /**
   * @param stageId The ID of the stage this sprint is part of.
   * @param segmentType The type of segment (SPRINT)
   * @param location The location of the segment in the stage.
   */
  public Segment(int stageId, SegmentType segmentType, Double location) {
    this.stageId = stageId;
    this.segmentType = segmentType;
    this.id = latestId++;
    this.location = location;
  }

  /**
   * Reset the internal ID counter
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  /**
   * @param segment The segment to compare the calling object to.
   * @return 1 if location of calling object > the passed in one. 0 if equal. -1 if less than.
   */
  public int compareTo(Segment segment) {
    assert (segment instanceof Segment) : "Comparing incorrect types!";
    return this.getLocation().compareTo(segment.getLocation());
  }

  /**
   * @return The segment type.
   */
  public SegmentType getSegmentType() {
    return segmentType;
  }

  /**
   * Set the segment type.
   * @param segmentType The segment type to set this object's segmentType to.
   */
  public void setSegmentType(SegmentType segmentType) {
    this.segmentType = segmentType;
  }

  /**
   * @return The ID of the stage this segment is part of.
   */
  public int getStageId() {
    return this.stageId;
  }

  /**
   * @return The ArrayList of RiderSegmentResults objects representing the per-segment,
   * per-rider results in this segment.
   */
  public ArrayList<RiderSegmentResult> getResults() {
    return results;
  }

  /**
   * Set the ArrayList of RiderSegmentResults objects representing the per-segment, per-rider
   * results in this segment.
   * @param results
   */
  public void setResults(ArrayList<RiderSegmentResult> results) {
    this.results = results;
  }

  /**
   * @return The ID of this particular segment.
   */
  public Integer getId() {
    return id;
  }

  /**
   * @return The location of the segment in the stage
   */
  public Double getLocation() {
    return location;
  }

  /**
   * Set the location of the segment in the stage.
   * @param location
   */
  public void setLocation(Double location) {
    this.location = location;
  }
}
