package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents the result of a rider in a stage, holding their times in all segments of that stage,
 * their final time, and their adjusted elapsed time. Objects of this class are compared to each
 * other by their finish times, which functions the same as comparing them by their adjusted times.
 *
 * @author Adam Kaizra, Sam Barker
 */
public class RiderStageResult implements Comparable<RiderStageResult>, Serializable {

  private int riderId;
  private final int stageId;
  private final LocalTime[] times; // Times for start, segments, finish
  private final LocalTime finishTime;
  private LocalTime adjustedFinishTime;
  private int rank;
  private int points;
  private int mountainPoints;

  public RiderStageResult(int riderId, int stageId, LocalTime[] times) {
    this.riderId = riderId;
    this.stageId = stageId;
    this.times = times;
    this.finishTime = times[times.length - 1];
    this.adjustedFinishTime = this.finishTime;
  }

  public int compareTo(RiderStageResult result) {
    assert (result instanceof RiderStageResult) : "Comparing incorrect types!";
    return this.getFinishTime().compareTo(result.getFinishTime());
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void addPoints(int points) {
    this.points += points;
  }

  public int getMountainPoints() {
    return mountainPoints;
  }

  public void setMountainPoints(int mountainPoints) {
    this.mountainPoints = mountainPoints;
  }

  public void addMountainPoints(int mountainPoints) {
    this.mountainPoints += mountainPoints;
  }

  public int getRiderId() {
    return riderId;
  }

  public void setRiderId(int riderId) {
    this.riderId = riderId;
  }

  public int getStageId() {
    return stageId;
  }

  public LocalTime[] getTimes() {
    return times;
  }

  public LocalTime getFinishTime() {
    return finishTime;
  }

  public LocalTime getAdjustedFinishTime() {
    return adjustedFinishTime;
  }

  public void setAdjustedFinishTime(LocalTime adjustedFinishTime) {
    this.adjustedFinishTime = adjustedFinishTime;
  }
}
