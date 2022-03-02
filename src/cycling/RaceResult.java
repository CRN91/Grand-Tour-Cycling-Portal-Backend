package src.cycling;

import java.time.LocalTime;

public class RaceResult implements Comparable<RaceResult> {
  private int id;
  private int riderId;
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

  public int getRiderId() {
    return riderId;
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

  public RaceResult(int riderId, LocalTime[] times) {
    this.riderId = riderId;
    this.times = times;
    this.finishTime = times[times.length -1];
    this.adjustedFinishTime = this.finishTime;
    this.id = latestId++;
  }
}
