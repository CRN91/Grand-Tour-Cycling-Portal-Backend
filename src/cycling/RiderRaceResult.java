package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Represents the result of one rider in one race, including points, mountain points, finish time,
 * and rank by finish time.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class RiderRaceResult implements Comparable<RiderRaceResult>, Serializable {

  private int riderId;
  private final int raceId;
  private int points = 0;
  private int mountainPoints = 0;
  private int rank;

  private LocalTime finishTime; // The sum of all stage finish times (the GC time)

  /**
   * Constructor.
   *
   * @param riderId The ID of the rider that achieved this result.
   * @param raceId The ID of the race in which this result was achieved.
   */
  public RiderRaceResult(int riderId, int raceId) {
    this.riderId = riderId;
    this.raceId = raceId;
    this.points = 0;
    this.mountainPoints = 0;
  }

  /**
   * Compare two RiderRaceResults by their finish times (for the purpose of sorting).
   *
   * @param result The result to compare the calling object's finish time to.
   * @return 1 if the calling object's finish time > the parameter object's.
   * 0 if equal. -1 if less than.
   */
  public int compareTo(RiderRaceResult result) {
    assert (result instanceof RiderRaceResult) : "Comparing incorrect types!";
    return this.getFinishTime().compareTo(result.getFinishTime());
  }

  /**
   * Compare two RiderRaceResults by their points in the race (for the purpose of sorting).
   *
   * @param result The result to compare the calling object's points to.
   * @return 1 if the calling object's points are less than the parameter object's.
   * 0 if equal. -1 if greater than.
   */
  public int compareByPoints(RiderRaceResult result) {
    if (this.getPoints() < result.getPoints()) {
      return 1;
    } else if (this.getPoints() == result.getPoints()) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * Compare two RiderRaceResults by their mountain points in the race (for the purpose of sorting).
   *
   * @param result The result to compare the calling object's mountain points to.
   * @return 1 if the calling object's mountain points are less than the parameter object's.
   * 0 if equal. -1 if greater than.
   */
  public int compareByMountainPoints(RiderRaceResult result) {
    if (this.getMountainPoints() < result.getMountainPoints()) {
      return 1;
    } else if (this.getMountainPoints() == result.getMountainPoints()) {
      return 0;
    } else {
      return -1;
    }
  }

  /**
   * @return The rank (by finish time) of the rider within this race.
   */
  public int getRank() {
    return rank;
  }

  /**
   * Set the rank of the rider in the race (by finish time).
   *
   * @param rank
   */
  public void setRank(int rank) {
    this.rank = rank;
  }

  /**
   * @return The ID of the rider that achieved this result.
   */
  public int getRiderId() {
    return riderId;
  }

  /**
   * Set the ID of the rider that achieved this result.
   *
   * @param riderId
   */
  public void setRiderId(int riderId) {
    this.riderId = riderId;
  }

  /**
   * @return The ID of the race in which this result was achieved.
   */
  public int getRaceId() {
    return raceId;
  }

  /**
   * @return The total finish time (sum of the finish times of all stages)
   * of the rider in the race.
   */
  public LocalTime getFinishTime() {
    return finishTime;
  }

  /**
   * Set the total finish time (sum of the finish times of all stages)
   * of the rider in the race.
   *
   * @param time
   */
  public void setFinishTime(LocalTime time) {
    this.finishTime = time;
  }

  /**
   * @return The total points (points classification points) the rider achieved in the race.
   * Summed from across all stages.
   */
  public int getPoints() {
    return points;
  }

  /**
   * Set the total points (points classification points) the rider achieved in the race.
   *
   * @param points
   */
  public void setPoints(int points) {
    this.points = points;
  }

  /**
   * @return The total mountain classification points the rider achieved in the race.
   * Summed from across all stages.
   */
  public int getMountainPoints() {
    return mountainPoints;
  }

  /**
   * Set the total mountain classification points the rider achieved in the race.
   *
   * @param points
   */
  public void setMountainPoints(int points) {
    this.mountainPoints = points;
  }
}
