package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

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

  public void generateRidersRaceResults() {
    // sort race result object then return the list of integers
    //StagedRace race = raceIdsToRaces.get(raceId);
    for (Stage stage : this.getStages()){ // iterates through all stages in a race
      for (RiderStageResult riderStageResult : stage.getResults()) { // iterates through all results in a stage
        int riderId = riderStageResult.getRiderId();
        boolean riderFound = false;
        for (RiderRaceResult raceResult : this.raceResults) { // iterates through race results
          if (raceResult.getRiderId() == riderId) {
            raceResult.setFinishTime(sumLocalTimes.addLocalTimes(raceResult.getFinishTime(),
                riderStageResult.getFinishTime())); // sums race results finish time with new stages finish time
            riderFound = true;
            break;
          }
        }
        if (!riderFound) { // if no race result for rider one is made.
          RiderRaceResult raceResult = new RiderRaceResult(riderStageResult.getRiderId(), this.raceId,
              riderStageResult.getFinishTime());
          this.raceResults.add(raceResult);
        }
      }
    }
    Collections.sort(this.raceResults); // orders race results by total time.
  }

  public ArrayList<RiderRaceResult> getResults() {
    return raceResults;
  }

  public int[] generateRidersPointsInRace(boolean isMountain) throws IDNotRecognisedException {
    HashMap<Integer,Integer> riderIdsToPointsInRace = new HashMap<Integer,Integer>();

    this.generateRidersRaceResults();

    System.out.println("race results generated with "+this.raceResults.size()+" items");
    // Sum of points for each rider for the specified race.

    System.out.println("amount of stages = "+this.getStages().size());
    int resultsSize = this.raceResults.size();
    int[] racePoints = new int[resultsSize];
    for (int i = 0; i < resultsSize; i++) {
      racePoints[i] = 0;
    }
    for (Stage stage : this.getStages()) { // iterate through stages.
      int i = 0;
      int[] stagePoints = stage.generatePointsInStage(isMountain);
      for (int point : stagePoints) {
        racePoints[i] += point;
        i++;
      }
    }
    return racePoints;
  }
    //  for (RiderStageResult result : stage.getResults()) { // iterate through rider.
//
    //    int riderId = result.getRiderId();
    //    int points;
//
    //    if (isMountain) {
    //      points = result.getMountainPoints();
    //    } else {
    //      points = result.getPoints();
    //    }
//
    //    riderIdsToPointsInRace.merge(riderId, points, Integer::sum);
    //    if (!isMountain) {
    //      this.raceResults.get(i).addPoints(points);
    //    } else {
    //      this.raceResults.get(i).addMountainPoints(points);
    //    }
    //    i++;
    //  }
    //}
//
    //// Adds riders points to their race results.
    ///*
//
    //for (RiderRaceResult result : this.getResults()) { // Set riders points classification points in race.
    //  if (isMountain) {
    //    result.setMountainPoints(riderIdsToPointsInRace.get(result.getRiderId()));
    //  } else {
    //    result.setPoints(riderIdsToPointsInRace.get(result.getRiderId()));
    //  }
    //}*/
    //int[] pointsOrderedByRank = new int[this.raceResults.size()];
    //if (!(riderIdsToPointsInRace.isEmpty())) {
    //  // creates an int array of points ordered by rank
    //  int i = 0;
    //  for (RiderRaceResult result : this.raceResults) {
    //    if (isMountain) {
    //      pointsOrderedByRank[i] = result.getMountainPoints();
    //    } else {
    //      pointsOrderedByRank[i] = result.getPoints();
    //    }
    //    i++;
    //  }
    //  /*if (isMountain) {
    //    this.setRiderIdsToMountainPoints(riderIdsToPointsInRace);
    //  } else {
    //    this.setRiderIdsToPoints(riderIdsToPointsInRace);
    //  }*/
    //}
 // re//turn pointsOrderedByRank;



  public int[] getRiderIdsOrderedByPoints(boolean isMountain) throws IDNotRecognisedException {
    this.generateRidersPointsInRace(isMountain);
    ArrayList<RiderRaceResult> riderRaceResults = this.getResults();
    if (isMountain) {
      Collections.sort(riderRaceResults, RiderRaceResult::compareByMountainPoints);
    } else {
      Collections.sort(riderRaceResults, RiderRaceResult::compareByPoints);
    }

    int[] riderIdsByPoints = new int[riderRaceResults.size()];
    int i = 0;
    for (RiderRaceResult result : riderRaceResults) {
      riderIdsByPoints[i] = result.getRiderId();
      i++;
    }

    Collections.sort(riderRaceResults);
    return riderIdsByPoints;
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
