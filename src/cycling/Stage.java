package src.cycling;

//import com.sun.source.tree.Tree;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
//import java.util.TreeMap;

/**
 * Represents a non-time trial stage.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class Stage implements Serializable {
  private Integer raceId;
  private String name;
  private String description;
  private Double length;
  private LocalDateTime startTime;
  private StageType stageType;
  private Integer id;
  private HashMap<Integer,Integer> riderIdsToMountainPoints = new HashMap<>();
  private HashMap<Integer,Integer> riderIdsToPoints = new HashMap<>();

  //protected HashMap<Integer, LocalTime[]> riderIdsToResults = new HashMap<>();
  private ArrayList<RiderStageResult> results = new ArrayList<>();

  private ArrayList<Segment> segmentsInStage = new ArrayList<>();
  private Boolean underDevelopment = true; // Either under development(T) or waiting results(F).

  private static int latestId = 0; // enumerates to get unique id, with 2^32 possible ids.

  /**
   *
   * @return stage's ID.
   */
  public Integer getId() { return id; }

  /**
   *
   * @return ID of the staged race this stage is in.
   */
  public Integer getRaceId() { return raceId; }

  /**
   *
   * @return name of the stage.
   */
  public String getName() { return name; }

  /**
   *
   * @param name
   */
  public void setName(String name) { this.name = name; }

  /**
   *
   * @return description of the stage.
   */
  public String getDescription() { return description; }

  /**
   *
   * @param description
   */
  public void setDescription(String description) { this.description = description; }

  /**
   *
   * @return length of the stage measured in kilometers.
   */
  public Double getLength() { return length; }

  /**
   *
   * @param length
   */
  public void setLength(Double length) { this.length = length; }

  /**
   *
   * @return Local date and time of the start of the race.
   */
  public LocalDateTime getStartTime() { return startTime; }

  /**
   *
   * @param startTime
   */
  public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

  /**
   *
   * @return terrain type of the stage.
   */
  public StageType getStageType() { return stageType; }

  /**
   *
   * @param stageType
   */
  public void setStageType(StageType stageType) { this.stageType = stageType; }

  /**
   *
   * @return array list of all the segments in this stage.
   */
  public ArrayList<Segment> getSegmentsInStage() {
    return segmentsInStage;
  }

  public HashMap<Integer, Integer> getRiderIdsToMountainPoints() {
    return riderIdsToMountainPoints;
  }

  public void setRiderIdsToMountainPoints(HashMap<Integer, Integer> riderIdsToMountainPoints) {
    this.riderIdsToMountainPoints = riderIdsToMountainPoints;
  }

  public HashMap<Integer, Integer> getRiderIdsToPoints() {
    return riderIdsToPoints;
  }

  public void setRiderIdsToPoints(HashMap<Integer, Integer> ridersToPoints) {
    this.riderIdsToPoints = ridersToPoints;
  }

  public void addSegment(Segment segment){
    segmentsInStage.add(segment);
    Collections.sort(segmentsInStage);
  }

  /**
   *
   * @return state of the stage. Either under development (true) or waiting results (false).
   */
  public Boolean getUnderDevelopment() { return underDevelopment; }

  /**
   *
   * @param state
   */
  public void setUnderDevelopment(Boolean state) { this.underDevelopment = state; }

  public ArrayList<RiderStageResult> getResults() {
    return this.results;
  }

  public void removeResultByRiderId(int riderId) throws IDNotRecognisedException {
    boolean riderIdFound = false;
    for (RiderStageResult result : results) {
      if (result.getRiderId() == riderId) {
        riderIdFound = true;
        results.remove(result);
      }
    }
    if (!riderIdFound) {
      throw new IDNotRecognisedException("Rider ID " + riderId + " not found in stage!");
    }
  }

  public void addRiderResults(Integer riderId, LocalTime[] times) {
    RiderStageResult result = new RiderStageResult(riderId, this.id, times);
    if (!(results.contains(result))) {
      results.add(result);
    }
    assert (results.contains(result)) : "Result object not created!";
    Collections.sort(results);
  }

  public void generateAdjustedResults() {
    LocalTime previousTime = LocalTime.of(0,0,0, 0);
    LocalTime pelotonLeader = LocalTime.of(0,0,0);

    for (RiderStageResult result : this.results) {
      LocalTime currentTime = result.getFinishTime();
      double currentTimeSeconds = (currentTime.getHour() * 3600) + (currentTime.getMinute() * 60) + currentTime.getSecond();
      double previousTimeSeconds = (previousTime.getHour() * 3600) + (previousTime.getMinute() * 60 + previousTime.getSecond());
      if ((currentTimeSeconds - previousTimeSeconds) <= 1.0) {
        result.setAdjustedFinishTime(pelotonLeader);
      } else {
        pelotonLeader = currentTime;
        result.setAdjustedFinishTime(currentTime);
      }
      previousTime = currentTime;
    }
    Collections.sort(this.results);
  }

  public void generateRiderSegmentResults() {
    Collections.sort(this.segmentsInStage); // Order segments by their location.
    int segmentCounter = 1; // starts from 1 as initial time is start time.
    for (Segment segment : this.segmentsInStage){ // iterates through every segment.
      ArrayList<RiderSegmentResult> riderSegmentResults = new ArrayList<>();
      // Stores segment results then sorts.
      int riderCounter = 0;
      for (RiderStageResult result : results){
        riderSegmentResults.add(new RiderSegmentResult(result.getTimes()[segmentCounter], result.getRiderId()));
      }
      // Sorts riders segment results in order of their time in ascending order.
      Collections.sort(riderSegmentResults);
      // Stores these results in segment for more intuitive access.
      segment.setResults(riderSegmentResults);

      // Sets rank of each rider in segment
      int rank = 0;
      for (RiderSegmentResult result : riderSegmentResults){
        result.setRank(rank);
        rank++;
      }
    }
    if ((!this.segmentsInStage.isEmpty()) && (!results.isEmpty())) {
      assert !this.segmentsInStage.get(0).getResults().isEmpty();
    }
  }

  public int getRidersRankInSegment(int segment, int riderId) throws IDNotRecognisedException {
    //System.out.println(segmentsInStage.toString()+" segments in stage"+segment);
    //System.out.println("inside segment for 2");
    this.generateRiderSegmentResults();
    for (RiderSegmentResult segmentResult : segmentsInStage.get(segment).getResults()){
      if (segmentResult.getRiderId() == riderId){
        return segmentResult.getRank();
      }
    }
    throw new IDNotRecognisedException("Rider ID not found in segment");
    // TODO this will break if rider did not finish each segment
  }

  /**
   * Reset the internal ID counter
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  public int[] generatePointsInStage(boolean isMountain)
      throws IDNotRecognisedException {

    Map<Enum, int[]> pointsConversion;

    if (isMountain){
      // Table of rank to points for each stage type.
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
      // Table of rank to points for each stage type.
      int[] flatPointsConversion = {50,30,20,18,16,14,12,10,8,7,6,5,4,3,2};
      int[] hillyPointsConversion = {30,25,22,19,17,15,13,11,9,7,6,5,4,3,2};
      int[] mountainPointsConversion = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
      int[] timeTrialPointsConversion = mountainPointsConversion;
      pointsConversion = Map.of(
          StageType.FLAT,flatPointsConversion,
          StageType.MEDIUM_MOUNTAIN,hillyPointsConversion,
          StageType.HIGH_MOUNTAIN,mountainPointsConversion,
          StageType.TT,timeTrialPointsConversion
      );
    }

    if (isMountain) {
      // Do mountain checks
      if ((this.getStageType() == StageType.TT) || (this.getSegmentsInStage().size() == 0)) {
        // No mountain points (no segments)
        return new int[0];
      }

      // Check if there even are any climbs
      boolean climbFound = false;
      for (Segment segment : this.getSegmentsInStage()) {
        if (segment.getSegmentType() != SegmentType.SPRINT) {
          climbFound = true;
        }
      }
      // No climbs so there will be no results
      if (!climbFound) {
        return new int[0];
      }
    }

    HashMap<Integer,Integer> riderIdsToPoints = new HashMap<Integer,Integer>();
    // Sum of points for each rider for the specified race.

    // For the points classification, add on the points from finish times which mountain doesn't include.
    if (!isMountain) {
      int pointsIndex = 0;
      this.generateAdjustedResults(); // Sort times in ascending order.

      for (RiderStageResult result : this.getResults()) { // iterate through rider.
        int riderId = result.getRiderId();
        int points;
        if (pointsIndex < 15) { // Only first 15 riders are awarded points.
          points = pointsConversion.get(this.getStageType())[pointsIndex]; // points for stage type
        }else {
          points = 0;
        }
        riderIdsToPoints.put(riderId, points);
        result.setPoints(points);
        pointsIndex++;
      }
    }

    // Add on points from segments.
    ArrayList<Segment> segmentsInStage = this.getSegmentsInStage();
    for (Segment segment : segmentsInStage){

      // Cast for the appropriate Enum for mountain or points as a key for their points conversion table.
      SegmentType currentSegmentType = segment.getSegmentType();
      Enum mapKey;
      if (isMountain) {
        mapKey = (SegmentType) currentSegmentType;
      } else {
        mapKey = (StageType) StageType.HIGH_MOUNTAIN; //
      }

      // Only allow intermediate sprint + points classification or categorised climb + mountain classification
      // combinations.

      if (((currentSegmentType == SegmentType.SPRINT) && !isMountain)
          || (!(currentSegmentType == SegmentType.SPRINT) && isMountain)) {
        int i = 0;
        this.generateRiderSegmentResults(); // Creates segment objects.
        for (RiderSegmentResult riderResult : segment.getResults()){ // Iterates through each rider's segment results.
          int riderId = riderResult.getRiderId();
          int riderRank = riderResult.getRank(); // Gets riders rank in segment.
          int points;
          int[] rowOfPointsConversion = pointsConversion.get(mapKey);
          int pointsLimit = rowOfPointsConversion.length;
          if (riderRank < pointsLimit) {
            points = rowOfPointsConversion[riderRank]; // Points limit prevent index out of bounds error.
          }else {
            points = 0;
          }

          if (riderIdsToPoints.get(riderId) == null) { // If the rider is not registered with points add them.
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

      if (isMountain) {
        this.setRiderIdsToMountainPoints(riderIdsToPoints);
      } else {
        this.setRiderIdsToPoints(riderIdsToPoints);
      }
      return pointsOrderedByRank;
    } else {
      return new int[0];
    }
  }

  /**
   * Constructor
   *
   * @param raceId
   * @param name
   * @param description
   * @param length
   * @param startTime
   * @param stageType
   */
  public Stage(Integer raceId, String name, String description, Double length,
               LocalDateTime startTime, StageType stageType) {
    this.raceId = raceId;
    this.name = name;
    this.description = description;
    this.length = length;
    this.startTime = startTime;
    this.stageType = stageType;
    this.id = latestId++;
  }

  public static void main(String[] args) {
    Stage stage = new Stage(0, "a", "a", 10.0, LocalDateTime.now(), StageType.FLAT);
    LocalTime[] times1 = {LocalTime.of(0,0,0), LocalTime.of(0,0,29)};
    LocalTime[] times2 = {LocalTime.of(0,0,0), LocalTime.of(0,0,30)};
    LocalTime[] times3 = {LocalTime.of(0,0,0), LocalTime.of(0,0,40)};
    stage.addRiderResults(0, times1);
    stage.addRiderResults(1, times2);
    stage.addRiderResults(2, times3);
    for (RiderStageResult result : stage.getResults()) {
      System.out.println(result.getFinishTime()+" b4 finish time");
      System.out.println(result.getAdjustedFinishTime()+" b4 adjusted time");
    }
    stage.generateAdjustedResults();
    for (RiderStageResult result : stage.getResults()) {
      System.out.println(result.getFinishTime()+" ft");
      System.out.println(result.getAdjustedFinishTime()+" at");
    }
  }
}
