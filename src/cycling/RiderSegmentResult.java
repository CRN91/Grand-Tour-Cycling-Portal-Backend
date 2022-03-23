package src.cycling;

import java.time.LocalTime;

/**
 * Represents the result of one rider in one segment of a stage, including finish time,
 * and rank. Points are not included for individual segments, but created for stages and races,
 * and stored in RiderStageResult and RiderRaceResult respectively.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class RiderSegmentResult implements Comparable<RiderSegmentResult> {

  private LocalTime time;
  private int riderId;
  private int rank;

  /**
   * @param time The finish time of the rider in the segment.
   * @param riderId The ID of the rider who achieved this result.
   */
  public RiderSegmentResult(LocalTime time, int riderId) {
    this.time = time;
    this.riderId = riderId;
  }

  /**
   * Compare two RiderSegmentResults by their finish time (for the purpose of sorting).
   *
   * @param result
   * @return 1 if calling object's finish time is greater than the parameter object's.
   * 0 if equal. -1 if less than.
   */
  public int compareTo(RiderSegmentResult result) {
    assert (result instanceof RiderSegmentResult) : "Comparing incorrect types!";
    return this.getTime().compareTo(result.getTime());
  }

  /**
   * @return The rank of the rider in the segment (by finish time).
   */
  public int getRank() {
    return rank;
  }

  /**
   * Set the rank of the rider in the segment (by finish time).
   *
   * @param rank
   */
  public void setRank(int rank) {
    this.rank = rank;
  }

  /**
   * @return The finish time of the rider in the segment.
   */
  public LocalTime getTime() {
    return this.time;
  }

  /**
   * Set the finish time of the rider in the segment.
   *
   * @param time
   */
  public void setTime(LocalTime time) {
    this.time = time;
  }

  /**
   * @return The ID of the rider who achieved this result.
   */
  public int getRiderId() {
    return riderId;
  }

  /**
   * Set the ID of the rider who achieved this result.
   *
   * @param riderId
   */
  public void setRiderId(int riderId) {
    this.riderId = riderId;
  }
}
