package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents the result of a rider in a stage, holding their times in all segments of that stage,
 * their final time, their adjusted elapsed time, their points classification points and their
 * mountain classification points. Objects of this class are compared to each other by their finish
 * times, which functions the same as comparing them by their adjusted times as adjusted times
 * retain order.
 *
 * @author Adam Kaizra, Sam Barker
 */
public class RiderStageResult implements Comparable<RiderStageResult>, Serializable {

  private final int riderId;
  private final int stageId;
  private final LocalTime[] times; // Times for the rider's start, segments and finish times.
  private final LocalTime finishTime;
  private LocalTime adjustedFinishTime; // Adjusted to take the peloton leader's time if applicable.
  private int points; // Points classification points for this stage.
  private int mountainPoints; // Mountain classification points for this stage.

  /**
   * Constructor.
   *
   * @param riderId
   * @param stageId
   * @param times First input is starting time, final input is finishing time, all other inputs (if
   *              applicable) are segment times in chronological order.
   */
  public RiderStageResult(int riderId, int stageId, LocalTime[] times) {
    this.riderId = riderId;
    this.stageId = stageId;
    this.times = times; // Times the rider crossed each checkpoint.
    this.finishTime = times[times.length - 1]; // Final time in the array is the finish time.
    this.adjustedFinishTime = this.finishTime; // Adjusted time initialised to finish time in case
    // the rider is not in a peloton so their time is not changed.
  }

  /**
   * Compares the finish times of two RiderStageResultObjects.
   *
   * @param riderStageResult The stage result to be compared against.
   * @return The comparator value, negative if less, positive if greater, zero if equal.
   */
  public int compareTo(RiderStageResult riderStageResult) {
    assert (riderStageResult instanceof RiderStageResult) : "Comparing incorrect types!";
    return this.getFinishTime().compareTo(riderStageResult.getFinishTime());
  }

  /**
   * @return The rider's points classification points for this stage.
   */
  public int getPoints() {
    return points;
  }

  /**
   * @param points The rider's points classification points for this stage.
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * Adds the given input to the rider's total points classification points.
   *
   * @param points The rider's points classification points for this stage.
   */
  public void addPoints(int points) {
    this.points += points;
  }

  /**
   * @return The rider's mountain points classification points for this stage.
   */
  public int getMountainPoints() {
    return mountainPoints;
  }

  /**
   * @param mountainPoints The rider's mountain points classification points for this stage.
   */
  public void setMountainPoints(int mountainPoints) {
    this.mountainPoints = mountainPoints;
  }

  /**
   * Adds the given input to the rider's total mountain points classification points.
   *
   * @param mountainPoints The rider's mountain points classification points for this stage.
   */
  public void addMountainPoints(int mountainPoints) {
    this.mountainPoints += mountainPoints;
  }

  /**
   * @return The rider's ID.
   */
  public int getRiderId() {
    return riderId;
  }

  /**
   * @return The stage ID from where the results are taken from.
   */
  public int getStageId() {
    return stageId;
  }

  /**
   * @return The times the rider passed each checkpoint.
   */
  public LocalTime[] getTimes() {
    return times;
  }

  /**
   * @return The rider's finish time.
   */
  public LocalTime getFinishTime() {
    return finishTime;
  }

  /**
   * @return The rider's finish time adjusted to the peloton leader's time if applicable.
   */
  public LocalTime getAdjustedFinishTime() {
    return adjustedFinishTime;
  }

  /**
   * @param newAdjustedFinishTime
   */
  public void setAdjustedFinishTime(LocalTime newAdjustedFinishTime) {
    this.adjustedFinishTime = newAdjustedFinishTime;
  }
}
