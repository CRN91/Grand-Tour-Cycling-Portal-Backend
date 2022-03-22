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

  /**
   * Constructor.
   *
   * @param name
   * @param description
   */
  public StagedRace(String name, String description) {
    this.name = name;
    this.description = description;
    this.raceId = latestId++;
  }

  /**
   * Reset the internal ID counter.
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  /**
   * @return The name of the staged race.
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return The description of the staged race.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * @return The ID of the staged race.
   */
  public int getId() {
    return this.raceId;
  }

  /**
   * @return An array list of stages in the staged race.
   */
  public ArrayList<Stage> getStages() {
    return stages;
  }

  /**
   * @param stage The new stage to be added to the staged race.
   */
  public void addStage(Stage stage) {
    this.stages.add(stage);
  }

  /**
   * Sums all the riders stage results finish times and stores it as a race finish time in each
   * rider's race result object.
   */
  public void generateRidersRaceFinishTimes() {
    int sizeOfArrayOfTotalTimes;
    if (this.getStages().size() == 0) {
      return;
    } else {
      sizeOfArrayOfTotalTimes = this.getStages().get(0).getResults().size();
    }

    LocalTime[] arrayOfTotalTimes = new LocalTime[sizeOfArrayOfTotalTimes];
    for (Stage stage : this.getStages()) { // Iterates through all stages in a race.
      int i = 0;
      for (RiderStageResult riderStageResult : stage.getResults()) { // Iterates through all results
        // in a stage.
        int riderId = riderStageResult.getRiderId();
        boolean riderFound = false;
        for (RiderRaceResult raceResult : this.raceResults) { // Iterates through race results.
          if (raceResult.getRiderId() == riderId) { // If the race result and stage result refer to
            // the same rider.
            arrayOfTotalTimes[i] = SumLocalTimes.addLocalTimes(arrayOfTotalTimes[i],
                riderStageResult.getFinishTime());
            // Sums race results finish time with new stages finish time.
            riderFound = true;
            break;
          }
        }
        if (!riderFound) { // If no race result for rider one is made.
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
    Collections.sort(this.raceResults); // Sorts race results by total time.
  }

  public ArrayList<RiderRaceResult> getResults() {
    return raceResults;
  }

  /**
   * Sums all the mountain or point classification points for each rider in the race.
   *
   * @param isMountain True if mountain point classification, false if point classification.
   * @return An array of rider's total points respective of given classification.
   */
  public int[] generateRidersPointsInRace(boolean isMountain) {
    this.generateRidersRaceFinishTimes();

    int[] points = new int[this.raceResults.size()]; // Points for the entire race.
    for (Stage stage : this.getStages()) { // Iterate through stages.
      stage.generatePointsInStage(isMountain); // Points ordered by rank are generated.
      for (RiderStageResult stageResult : stage.getResults()) {
        int i = 0;
        if (isMountain) { // Mountain classification.
          for (RiderRaceResult raceResult : this.getResults()) { // Iterates through race results.
            // If the stage result rider ID and the race result rider ID match.
            if (stageResult.getRiderId() == raceResult.getRiderId()) {
              // Points are set or added to the race total array.
              if (points[i] == 0) {
                points[i] = stageResult.getMountainPoints();
              } else {
                points[i] += stageResult.getMountainPoints();
              }

            }
            i++;
          }
        } else { // Point classification.
          for (RiderRaceResult raceResult : this.getResults()) {
            if (stageResult.getRiderId() == raceResult.getRiderId()) {
              // Points are sent to an array that stores each rider's total points in the race.
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
      // Iterates through race results and sets each rider's total points for the race.
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

    // Sorts race results by either their mountain or point classification points.
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
    Collections.sort(raceResults); // Sorts race results back to its order by finishing times.
    return racePoints;
  }

  /**
   * @param isMountain True if mountain point classification, false if point classification.
   * @return An array of rider ID's ordered by their points in the given classification.
   */
  public int[] getRiderIdsOrderedByPoints(boolean isMountain) {
    ArrayList<RiderRaceResult> riderRaceResults = this.getResults();
    // Sorts race results by their points in the given classification.
    if (isMountain) {
      Collections.sort(riderRaceResults, RiderRaceResult::compareByMountainPoints);
    } else {
      Collections.sort(riderRaceResults, RiderRaceResult::compareByPoints);
    }

    // Gets the rider ID's in order of points and adds them to an array.
    int[] riderIdsByPoints = new int[riderRaceResults.size()];
    int i = 0;
    for (RiderRaceResult result : riderRaceResults) {
      riderIdsByPoints[i] = result.getRiderId();
      i++;
    }
    Collections.sort(raceResults); // Sorts race results back to its order by finishing times.
    return riderIdsByPoints;
  }
}
