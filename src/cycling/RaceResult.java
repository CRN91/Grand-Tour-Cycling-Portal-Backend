package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;

public class RaceResult implements Comparable<RaceResult>, Serializable {
  private int id;
  private int riderId;
  private int raceId;
  private static int latestId = 0;

  private LocalTime[] times; // Stage final finish times
  private LocalTime finishTime; // The sum of all stage finish times (the GC time)

  public int compareTo(RaceResult raceResult) {
    return this.getFinishTime().compareTo(raceResult.getFinishTime());
  }

  public int getId() {
    return id;
  }

  public int getRiderId() {
    return riderId;
  }

  public int getRaceId() {
    return raceId;
  }

  public LocalTime getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(LocalTime time) {
    this.finishTime = time;
  }

  public static void resetIdCounter() {
    latestId = 0;
  }

  public RaceResult(int riderId, int raceId, LocalTime... stageFinalTimes) {
    this.id = latestId++;
    this.riderId = riderId;
    this.raceId = raceId;
    this.times = stageFinalTimes;
    // Sum final times
    this.finishTime = sumLocalTimes.addLocalTimes(stageFinalTimes);
  }
}
