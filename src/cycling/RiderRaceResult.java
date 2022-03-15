package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Collections;

public class RiderRaceResult implements Comparable<RiderRaceResult>, Serializable {
  private int riderId;
  private int raceId;
  private int points;
  private int mountainPoints;
  private int rank;

  private LocalTime[] times; // Stage final finish times
  private LocalTime finishTime; // The sum of all stage finish times (the GC time)

  public int compareTo(RiderRaceResult raceResult) {
    return this.getFinishTime().compareTo(raceResult.getFinishTime());
  }
  public int compareByPoints(RiderRaceResult otherRiderRaceResult) {
    if (this.getPoints() < otherRiderRaceResult.getPoints()) {
      return -1;
    } else if (this.getPoints() == otherRiderRaceResult.getPoints()) {
      return 0;
    } else {
      return 1;
    }
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

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public void setMountainPoints(int points) {
    this.mountainPoints = points;
  }

  public void setFinishTime(LocalTime time) {
    this.finishTime = time;
  }

  public RiderRaceResult(int riderId, int raceId, LocalTime... stageFinalTimes) {
    this.riderId = riderId;
    this.raceId = raceId;
    this.times = stageFinalTimes;
    // Sum final times
    this.finishTime = sumLocalTimes.addLocalTimes(stageFinalTimes);
    this.points = 0;
    this.mountainPoints = 0;
  }
}
