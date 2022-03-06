package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;

public class RaceResult implements Comparable<RaceResult>, Serializable {
  private int id;
  private int riderId;
  private int stageId;
  private LocalTime[] times;
  private LocalTime finishTime;
  private LocalTime adjustedFinishTime;

  private static int latestId;

  public int compareTo(RaceResult result) {
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

  public RaceResult(int riderId, int stageId, LocalTime[] times) {
    this.riderId = riderId;
    this.stageId = stageId;
    this.times = times;
    this.finishTime = times[times.length -1];
    this.adjustedFinishTime = this.finishTime;
    this.id = latestId++;
  }
}
