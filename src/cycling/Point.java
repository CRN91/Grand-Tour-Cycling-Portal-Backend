package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;

public class Point implements Comparable<Point>, Serializable {
  private int riderId;
  private Integer riderPoints;

  public int compareTo(Point point) {
    return point.getRiderPoints().compareTo(this.getRiderPoints());
  }

  public int getRiderId() {
    return riderId;
  }

  public Integer getRiderPoints() {
    return riderPoints;
  }

  public Point(int riderId, int riderPoints) {
    this.riderId = riderId;
    this.riderPoints = riderPoints;
  }
}
