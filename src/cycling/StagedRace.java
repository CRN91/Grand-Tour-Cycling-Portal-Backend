package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Represents a grand tour staged race.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class StagedRace implements Serializable {
  private String name;
  private String description;
  private int raceId;
  private LocalTime[] datesOfCompetitions;

  private ArrayList<Stage> stages = new ArrayList<>();
  private ArrayList<RiderRaceResult> raceResults = new ArrayList<>();
  private ArrayList<Point> racePoints = new ArrayList<>();
  private HashMap<Integer,Integer> riderIdsToPoints = new HashMap<>();
  private HashMap<Integer,Integer> riderIdsToMountainPoints = new HashMap<>();

  private static int latestId = 0;

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public int getId() {
    return this.raceId;
  }

  public ArrayList<Stage> getStages() {
    return stages;
  }

  public void addStage(Stage stage) {
    this.stages.add(stage);
  }

  public HashMap<Integer, Integer> getRiderIdsToPoints() {
    return riderIdsToPoints;
  }

  public void setRiderIdsToPoints(HashMap<Integer, Integer> riderIdsToPoints) {
    this.riderIdsToPoints = riderIdsToPoints;
  }

  public HashMap<Integer, Integer> getRiderIdsToMountainPoints() {
    return riderIdsToMountainPoints;
  }

  public void setRiderIdsToMountainPoints(HashMap<Integer, Integer> riderIdsToMountainPoints) {
    this.riderIdsToMountainPoints = riderIdsToMountainPoints;
  }

  public ArrayList<RiderRaceResult> generateRidersRaceResults() {
    // sort race result object then return the list of integers
    //StagedRace race = raceIdsToRaces.get(raceId);
    for (Stage stage : this.getStages()){ // iterates through all stages in a race
      for (RiderStageResult riderStageResult : stage.getResults()) { // iterates through all results in a stage
        int riderId = riderStageResult.getRiderId();
        boolean riderFound = false;
        for (RiderRaceResult raceResult : raceResults) {
          if (raceResult.getRiderId() == riderId) {
            raceResult.setFinishTime(sumLocalTimes.addLocalTimes(raceResult.getFinishTime(),
                riderStageResult.getFinishTime())); // sums race results finish time with new stages finish time
            riderFound = true;
            break;
          }
        }
        if (!riderFound) { // if no race result for rider one is made.
          RiderRaceResult raceResult = new RiderRaceResult(riderStageResult.getRiderId(), raceId,
              riderStageResult.getFinishTime());
          raceResults.add(raceResult);
        }
      }
    }
    Collections.sort(raceResults); // orders race results by total time.
    return raceResults;
  }

  public ArrayList<RiderRaceResult> getResults() {
    return raceResults;
  }

  public int[] generateRidersPointsInRace(boolean isMountain) throws IDNotRecognisedException {
    HashMap<Integer,Integer> riderIdsToPointsInRace = new HashMap<Integer,Integer>();

    // Sum of points for each rider for the specified race.
    for (Stage stage : this.getStages()){ // iterate through stages.
      for (RiderStageResult result : stage.getResults()) { // iterate through rider.
        int riderId = result.getRiderId();
        int points;

        if (isMountain) {
          points = result.getMountainPoints();
        } else {
          points = result.getPoints();
        }

        if (riderIdsToPointsInRace.get(riderId) == null) { // if the rider is not registered with points add them.
          riderIdsToPointsInRace.put(riderId, points);
        } else { // else it sums their points to their total.
          riderIdsToPointsInRace.merge(riderId, points, Integer::sum);
        }
      }
    }

    // Adds riders points to their race results.
    this.generateRidersRaceResults();
    for (RiderRaceResult result : this.getResults()) { // Set riders points classification points.
      if (isMountain) {
        result.setMountainPoints(riderIdsToPointsInRace.get(result.getRiderId()));
      } else {
        result.setPoints(riderIdsToPointsInRace.get(result.getRiderId()));
      }
    }

    if (!(riderIdsToPointsInRace.isEmpty())) {
      // creates an int array of points ordered by rank
      int[] pointsOrderedByRank = new int[raceResults.size()];
      int i = 0;
      for (RiderRaceResult result : raceResults) {
        if (isMountain) {
          pointsOrderedByRank[i] = result.getMountainPoints();
        } else {
          pointsOrderedByRank[i] = result.getPoints();
        }
      }

      if (isMountain) {
        this.setRiderIdsToMountainPoints(riderIdsToPointsInRace);
      } else {
        this.setRiderIdsToPoints(riderIdsToPointsInRace);
      }


      return pointsOrderedByRank;
    } else {
      return new int[0];
    }
  }


  /**
   * Reset the internal ID counter
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  public StagedRace(String name, String description) {
    this.name = name;
    this.description = description;
    this.raceId = latestId++;
  }
}
