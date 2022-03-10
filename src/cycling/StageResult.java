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
public class StageResult implements Comparable<StageResult>, Serializable {
  private int id;
  private int riderId;
  private int stageId;
  private LocalTime[] times;
  private LocalTime finishTime;
  private LocalTime adjustedFinishTime;

  private static int latestId;

  public int compareTo(StageResult result) {
    return this.getFinishTime().compareTo(result.getFinishTime());
  }

  public int getId() {
    return id;
  }

  public static void resetIdCounter() {
    latestId = 0;
  }

  public int getRiderId() {
    return riderId;
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

  public StageResult(int riderId, int stageId, LocalTime[] times) {
    this.riderId = riderId;
    this.stageId = stageId;
    this.times = times;
    this.finishTime = times[times.length -1];
    this.adjustedFinishTime = this.finishTime;
    this.id = latestId++;
  }
}
