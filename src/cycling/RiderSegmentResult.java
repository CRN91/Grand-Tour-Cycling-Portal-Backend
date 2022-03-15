package src.cycling;

import java.time.LocalTime;

public class RiderSegmentResult implements Comparable<RiderSegmentResult> {
  private LocalTime time;
  private int riderId;
  private int rank;

  public int compareTo(RiderSegmentResult segmentTime) {
    return this.getTime().compareTo(segmentTime.getTime());
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public LocalTime getTime(){
    return this.time;
  }

  public void setTime(LocalTime time) {
    this.time = time;
  }

  public int getRiderId() {
    return riderId;
  }

  public void setRiderId(int riderId) {
    this.riderId = riderId;
  }

  public RiderSegmentResult(LocalTime time, int riderId) {
    this.time = time;
    this.riderId = riderId;
  }
}
