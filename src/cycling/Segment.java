package src.cycling;

import java.util.ArrayList;

/**
 * Represents an intermediate sprint. CategorisedClimb is a subclass of this for categorised
 * climbs.
 *
 * @author Sam Barker, Adam Kaizrea
 * @version 1.0
 */

//(int stageId, Double location, SegmentType type,
//                                        Double averageGradient, Double length)
public class Segment implements Comparable<Segment> {

  private static int latestId = 0;
  private final Integer stageId;
  private Double location;
  private SegmentType segmentType;
  private final Integer id;
  private ArrayList<RiderSegmentResult> results = new ArrayList<>();

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

  public int compareTo(Segment segment) {
    assert (segment instanceof Segment) : "Comparing incorrect types!";
    return this.getLocation().compareTo(segment.getLocation());
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

  public ArrayList<RiderSegmentResult> getResults() {
    return results;
  }

  public void setResults(ArrayList<RiderSegmentResult> results) {
    this.results = results;
  }

  /**
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
}
