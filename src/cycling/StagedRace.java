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

  private static int latestId = 0;
  private final String name;
  private final String description;
  private final int raceId;
  private LocalTime[] datesOfCompetitions;
  private final ArrayList<Stage> stages = new ArrayList<>();
  private final ArrayList<RiderRaceResult> raceResults = new ArrayList<>();
  private HashMap<Integer, Integer> riderIdsToPoints = new HashMap<>();
  private HashMap<Integer, Integer> riderIdsToMountainPoints = new HashMap<>();

  public StagedRace(String name, String description) {
    this.name = name;
    this.description = description;
    this.raceId = latestId++;
  }

  /**
   * Reset the internal ID counter
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

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

    int sizeOfArrayOfTotalTimes;
    if (this.getStages().size() == 0) {
      return;
    } else {
      sizeOfArrayOfTotalTimes = this.getStages().get(0).getResults().size();
    }

    LocalTime[] arrayOfTotalTimes = new LocalTime[sizeOfArrayOfTotalTimes];
    for (Stage stage : this.getStages()) { // iterates through all stages in a race
      int i = 0;
      for (RiderStageResult riderStageResult : stage.getResults()) { // iterates through all results in a stage
        int riderId = riderStageResult.getRiderId();
        boolean riderFound = false;
        for (RiderRaceResult raceResult : this.raceResults) { // iterates through race results
          if (raceResult.getRiderId() == riderId) {
            arrayOfTotalTimes[i] = SumLocalTimes.addLocalTimes(arrayOfTotalTimes[i],
                riderStageResult.getFinishTime());

            // sums race results finish time with new stages finish time
            riderFound = true;
            break;
          }
        }

        if (!riderFound) { // if no race result for rider one is made.
          RiderRaceResult raceResult = new RiderRaceResult(riderStageResult.getRiderId(),
              this.raceId);
          arrayOfTotalTimes[i] = riderStageResult.getFinishTime();

          this.raceResults.add(raceResult);

        }
        i++;
      }
    }
    int i = 0;
    for (RiderRaceResult result : this.raceResults) {
      result.setFinishTime(arrayOfTotalTimes[i]);
      i++;
    }
    Collections.sort(this.raceResults); // orders race results by total time.
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

  public ArrayList<RiderRaceResult> getResults() {
    return raceResults;
  }

  public int[] generateRidersPointsInRace(boolean isMountain) throws IDNotRecognisedException {
    HashMap<Integer, Integer> riderIdsToPointsInRace = new HashMap<Integer, Integer>();

    this.generateRidersRaceResults();

    int[] points = new int[this.raceResults.size()];
    for (Stage stage : this.getStages()) { // iterate through stages.
      stage.generatePointsInStage(isMountain); // points ordered by rank
      for (RiderStageResult stageResult : stage.getResults()) {
        int i = 0;
        if (isMountain) {
          for (RiderRaceResult raceResult : this.getResults()) {
            if (stageResult.getRiderId() == raceResult.getRiderId()) {
              if (points[i] == 0) {
                points[i] = stageResult.getMountainPoints();
              } else {
                points[i] += stageResult.getMountainPoints();
              }

            }
            i++;
          }
        } else {
          for (RiderRaceResult raceResult : this.getResults()) {
            if (stageResult.getRiderId() == raceResult.getRiderId()) {
              if (points[i] == 0) {
                points[i] = stageResult.getPoints();
              } else {
                points[i] += stageResult.getPoints();
              }
            }
            i++;
          }
        }
      }
      int i = 0;
      for (RiderRaceResult result : this.getResults()) {
        if (isMountain) {
          result.setMountainPoints(points[i]);
        } else {
          result.setPoints(points[i]);
        }
        i++;
      }
    }
    // Sum of points for each rider for the specified race.
    int resultsSize = this.raceResults.size();
    int[] racePoints = new int[resultsSize];
    int i = 0;

    if (isMountain) {
      Collections.sort(raceResults, RiderRaceResult::compareByMountainPoints);
    } else {
      Collections.sort(raceResults, RiderRaceResult::compareByPoints);
    }
    for (RiderRaceResult result : this.raceResults) {
      if (isMountain) {
        racePoints[i] = result.getMountainPoints();
      } else {
        racePoints[i] = result.getPoints();
      }
      i++;
    }
    return racePoints;
  }

  public int[] getRiderIdsOrderedByPoints(boolean isMountain) throws IDNotRecognisedException {
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
    return riderIdsByPoints;
  }
}
