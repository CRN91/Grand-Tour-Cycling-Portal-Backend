package src.cycling;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a stage in the race and contains all the rider's results for the stage. If a stage is
 * under development results can not be registered but segments can be added.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class Stage implements Serializable {

  private static int latestId = 0; // enumerates to get unique id, with 2^32 possible ids.
  private final Integer raceId;
  private String name;
  private String description;
  private Double length;
  private LocalDateTime startTime;
  private StageType stageType;
  private final Integer id;
  private final ArrayList<RiderStageResult> results = new ArrayList<>();
  private final ArrayList<Segment> segmentsInStage = new ArrayList<>();
  private Boolean underDevelopment = true; // Either under development(T) or waiting results(F).

  /**
   * Constructor.
   *
   * @param raceId
   * @param stageName
   * @param description
   * @param length
   * @param startTime
   * @param stageType
   */
  public Stage(Integer raceId, String stageName, String description, Double length,
      LocalDateTime startTime, StageType stageType) {
    this.raceId = raceId;
    this.name = stageName;
    this.description = description;
    this.length = length;
    this.startTime = startTime;
    this.stageType = stageType;
    this.id = latestId++;
  }

  /**
   * Reset the internal ID counter.
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  /**
   * @return The stage's ID.
   */
  public Integer getId() {
    return id;
  }

  /**
   * @return The ID of the staged race this stage is in.
   */
  public Integer getRaceId() {
    return raceId;
  }

  /**
   * @return The name of the stage.
   */
  public String getName() {
    return name;
  }

  /**
   * @param newName
   */
  public void setName(String newName) {
    this.name = newName;
  }

  /**
   * @return The description of the stage.
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param newDescription
   */
  public void setDescription(String newDescription) {
    this.description = newDescription;
  }

  /**
   * @return The length of the stage measured in kilometers.
   */
  public Double getLength() {
    return length;
  }

  /**
   * @param newLength
   */
  public void setLength(Double newLength) {
    this.length = newLength;
  }

  /**
   * @return The local date and time of the start of the stage.
   */
  public LocalDateTime getStartTime() {
    return startTime;
  }

  /**
   * @param newStartTime
   */
  public void setStartTime(LocalDateTime newStartTime) {
    this.startTime = newStartTime;
  }

  /**
   * @return terrain type of the stage.
   */
  public StageType getStageType() {
    return stageType;
  }

  /**
   * @param newStageType
   */
  public void setStageType(StageType newStageType) {
    this.stageType = newStageType;
  }

  /**
   * @return An array list of all the segments in this stage.
   */
  public ArrayList<Segment> getSegmentsInStage() {
    return segmentsInStage;
  }

  /**
   * Adds a new segment to the stage.
   *
   * @param newSegment
   */
  public void addSegment(Segment newSegment) {
    segmentsInStage.add(newSegment);
    Collections.sort(segmentsInStage); // Sorts the segments by location.
  }

  /**
   * @return The state of the stage. Either under development (true) or waiting results (false).
   */
  public Boolean getUnderDevelopment() {
    return underDevelopment;
  }

  /**
   * If true the state of the stage is under development. If false the state of the stage is
   * awaiting results.
   *
   * @param newState
   */
  public void setUnderDevelopment(Boolean newState) {
    this.underDevelopment = newState;
  }

  /**
   * @return An array list containing all the RiderStageResult objects relating to this stage.
   */
  public ArrayList<RiderStageResult> getResults() {
    return this.results;
  }

  /**
   * Removes the results of a rider from a stage.
   *
   * @param riderId The rider ID of the rider to be removed.
   * @throws IDNotRecognisedException
   */
  public void removeResultByRiderId(int riderId) throws IDNotRecognisedException {
    boolean riderIdFound = false;
    RiderStageResult finalResult = null; // Empty value to be assigned if the rider is found.
    for (RiderStageResult result : results) { // Iterates through the stage's rider results to find
      // the correct rider.
      if (result.getRiderId() == riderId) {
        riderIdFound = true;
        finalResult = result;
        break;
      }
    }
    if (!riderIdFound) { // If the rider can't be found it can't be removed.
      throw new IDNotRecognisedException("Rider ID " + riderId + " not found in stage!");
    }
    results.remove(finalResult); // Removes the result from the stage results array list.
  }

  /**
   * Creates a new RiderStageResult object and adds it to an array list of rider results in this
   * stage.
   *
   * @param riderId
   * @param times The times the rider passed each checkpoint in chronological order.
   */
  public void addRiderResults(Integer riderId, LocalTime[] times) {
    RiderStageResult result = new RiderStageResult(riderId, this.id, times); // Creates new result.
    results.add(result); // The new result object is added to an array list of all result objects in
    // the stage.
    assert (results.contains(result)) : "Result object not created!";
    Collections.sort(results); // Sorts the results by their finishing times.
  }

  /**
   * Searches all the stages' riders' results to find pelotons (where riders are less than one
   * second behind the rider in front of them) and sets the adjusted time of each applicable result
   * to the peloton leader's finish time.
   */
  public void generateAdjustedResults() {
    // Initialise previous time and peloton leader time to zero.
    LocalTime previousTime = LocalTime.of(0, 0, 0, 0);
    LocalTime pelotonLeader = LocalTime.of(0, 0, 0);

    for (RiderStageResult result : this.results) { // Iterates through results of the stage.
      LocalTime currentTime = result.getFinishTime(); // The time of this rider to compare against.
      double currentTimeSeconds =
          (currentTime.getHour() * 3600) + (currentTime.getMinute() * 60) + currentTime.getSecond();
      double previousTimeSeconds = (previousTime.getHour() * 3600) + (previousTime.getMinute() * 60
          + previousTime.getSecond());
      // Converts LocalTime into double to calculate whether there is a 1 second or less gap between
      // riders.
      if (((currentTimeSeconds - previousTimeSeconds) <= 1.0) && (previousTimeSeconds != 0)) {
        result.setAdjustedFinishTime(pelotonLeader); // If there is a 1 second or less gap the
        // adjusted time is set to the peloton leader.
      } else {
        pelotonLeader = currentTime; // If the rider is not in a peloton then they could be the next
        // peloton's leader.
        result.setAdjustedFinishTime(currentTime); // Adjusted time is set to their finish time.
      }
      previousTime = currentTime; // A Peloton can consist of multiple riders so the one second gap
      // accounts for between riders.
    }
    Collections.sort(this.results); // The results are sorted by finishing times (same order as
    // adjusted).
  }

  /**
   * Iterates through all the segments in the stage to create RiderSegmentResult objects from the
   * results in RiderStageResults.
   */
  public void generateRiderSegmentResults() {
    Collections.sort(this.segmentsInStage); // Order segments by their location.
    int segmentCounter = 1; // Starts from 1 as initial time is start time.
    for (Segment segment : this.segmentsInStage) { // Iterates through every segment.
      ArrayList<RiderSegmentResult> riderSegmentResults = new ArrayList<>();
      // Stores segment results then sorts.
      for (RiderStageResult result : results) {
        riderSegmentResults.add(
            new RiderSegmentResult(result.getTimes()[segmentCounter], result.getRiderId()));
      }
      // Sorts riders segment results in order of their time in ascending order.
      Collections.sort(riderSegmentResults);
      // Stores these results in segment for more intuitive access.
      segment.setResults(riderSegmentResults);

      // Sets rank of each rider in segment
      int rank = 0;
      for (RiderSegmentResult result : riderSegmentResults) {
        result.setRank(rank);
        rank++;
      }
    }
    assert (this.segmentsInStage.isEmpty()) || (results.isEmpty()) || !this.segmentsInStage.get(0)
        .getResults().isEmpty();
  }

  /**
   *
   * @param isMountain True if mountain classification, false if point classification.
   * @return An array of rider ID's ordered by their points in the given classification.
   */
  public int[] generatePointsInStage(boolean isMountain) {

    Map<Enum, int[]> pointsConversion;

    if (isMountain) {
      // Table of rank to points for each segment type in mountain classification.
      int[] pointsHC = {20, 15, 12, 10, 8, 6, 4, 2}; // TOP 8 ONLY
      int[] pointsC1 = {10, 8, 6, 4, 2, 1, 0, 0}; // TOP 6 ONLY
      int[] pointsC2 = {5, 3, 2, 1, 0, 0, 0, 0}; // TOP 4 ONLY
      int[] pointsC3 = {2, 1, 0, 0, 0, 0, 0, 0}; // TOP 2 ONLY
      int[] pointsC4 = {1, 0, 0, 0, 0, 0, 0, 0}; // TOP 1 ONLY
      pointsConversion = Map.of(
          SegmentType.HC, pointsHC,
          SegmentType.C1, pointsC1,
          SegmentType.C2, pointsC2,
          SegmentType.C3, pointsC3,
          SegmentType.C4, pointsC4);
    } else {
      // Table of rank to points for each stage type in point classification.
      int[] flatPointsConversion = {50, 30, 20, 18, 16, 14, 12, 10, 8, 7, 6, 5, 4, 3, 2};
      int[] hillyPointsConversion = {30, 25, 22, 19, 17, 15, 13, 11, 9, 7, 6, 5, 4, 3, 2};
      int[] mountainPointsConversion = {20, 17, 15, 13, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
      pointsConversion = Map.of(
          StageType.FLAT, flatPointsConversion,
          StageType.MEDIUM_MOUNTAIN, hillyPointsConversion,
          StageType.HIGH_MOUNTAIN, mountainPointsConversion,
          StageType.TT, mountainPointsConversion // Time trials are awarded the same points as high
          // mountain stages.
      );
    }

    if (isMountain) {
      // Do mountain error checks.
      if ((this.getStageType() == StageType.TT) || (this.getSegmentsInStage().size() == 0)) {
        // No mountain points (no segments).
        return new int[0];
      }

      // Check if there even are any climbs.
      boolean climbFound = false;
      for (Segment segment : this.getSegmentsInStage()) {
        if (segment.getSegmentType() != SegmentType.SPRINT) {
          climbFound = true;
        }
      }
      // No climbs so there will be no results.
      if (!climbFound) {
        return new int[0];
      }
    }

    HashMap<Integer, Integer> riderIdsToPoints = new HashMap<Integer, Integer>();
    // Sum of points for each rider for the specified race.

    // For the points classification, add on the points from finish times which mountain doesn't
    // include.
    if (!isMountain) {
      int pointsIndex = 0;
      this.generateAdjustedResults(); // Sort times in ascending order.

      for (RiderStageResult result : this.getResults()) { // Iterate through rider.
        int riderId = result.getRiderId();
        int points;
        if (pointsIndex < 15) { // Only first 15 riders are awarded points.
          points = pointsConversion.get(this.getStageType())[pointsIndex]; // Points for stage type.
        } else {
          points = 0;
        }
        riderIdsToPoints.put(riderId, points);
        result.setPoints(points);
        pointsIndex++;
      }
    }

    // Add on points from segments.
    ArrayList<Segment> segmentsInStage = this.getSegmentsInStage();
    this.generateRiderSegmentResults(); // Creates segment objects.
    for (Segment segment : segmentsInStage) {
      // Cast for the appropriate Enum for mountain or points as a key for their points conversion
      // table.
      SegmentType currentSegmentType = segment.getSegmentType();
      Enum mapKey;
      if (isMountain) {
        mapKey = currentSegmentType;
      } else {
        mapKey = StageType.HIGH_MOUNTAIN; // Intermediate sprints are the only type of segment in
        // points classification and are awarded the same points as high mountain stages.
      }

      // Only allow intermediate sprint + points classification or categorised climb + mountain
      // classification combinations.

      if (((currentSegmentType == SegmentType.SPRINT) && !isMountain)
          || (!(currentSegmentType == SegmentType.SPRINT) && isMountain)) {
        int i = 0;
        for (RiderSegmentResult riderResult : segment.getResults()) { // Iterates through each
          // rider's segment results.
          int riderId = riderResult.getRiderId();
          int riderRank = riderResult.getRank(); // Gets riders rank in segment.
          int points;
          int[] rowOfPointsConversion = pointsConversion.get(mapKey);
          int pointsLimit = rowOfPointsConversion.length;
          if (riderRank < pointsLimit) {
            points = rowOfPointsConversion[riderRank]; // Points limit prevent index out of bounds
            // error.
          } else {
            points = 0;
          }

          if (riderIdsToPoints.get(riderId)
              == null) { // If the rider is not registered with points add them.
            riderIdsToPoints.put(riderId, points);
            if (!isMountain) {
              this.getResults().get(i).setPoints(points);
            } else {
              this.getResults().get(i).setMountainPoints(points);
            }
          } else { // Else sum their points to their total points.
            riderIdsToPoints.merge(riderId, points, Integer::sum);
            if (!isMountain) {
              this.getResults().get(i).addPoints(points);
            } else {
              this.getResults().get(i).addMountainPoints(points);
            }
          }
          i++;
        }
      }
    }
    Collections.sort(this.results);
    // Creates an int array of points ordered by rank
    if (!(riderIdsToPoints.isEmpty())) {
      int[] pointsOrderedByRank = new int[this.results.size()];
      int i = 0;
      for (RiderStageResult result : this.getResults()) {
        if (isMountain) {
          pointsOrderedByRank[i] = result.getMountainPoints();
        } else {
          pointsOrderedByRank[i] = result.getPoints();
        }
        i++;
      }

      return pointsOrderedByRank;
    } else {
      return new int[0];
    }
  }
}
