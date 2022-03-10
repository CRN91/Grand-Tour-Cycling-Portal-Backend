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
  protected Integer raceId;
  protected String name;
  protected String description;
  protected Double length;
  protected LocalDateTime startTime;
  protected StageType stageType;
  protected Integer id;

  //protected HashMap<Integer, LocalTime[]> riderIdsToResults = new HashMap<>();
  protected ArrayList<StageResult> results = new ArrayList<>();

  private ArrayList<Segment> segmentsInStage = new ArrayList<>();
  protected Boolean underDevelopment = true; // Either under development(T) or waiting results(F).

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

  public void addSegment(Segment segment){
    segmentsInStage.add(segment);
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

  public ArrayList<StageResult> getResults() {
    return this.results;
  }

  public void removeResultByRiderId(int riderId) {
    results.removeIf(result -> result.getRiderId() == riderId);
  }

  public void addRiderResults(Integer riderId, LocalTime[] times) {
    StageResult result = new StageResult(riderId, this.id, times);
    if (!(results.contains(result))) {
      results.add(result);
    }
  }

  public void generateAdjustedResults() {
    LocalTime previousTime = LocalTime.of(0,0,0, 0);
    LocalTime pelotonLeader = LocalTime.of(0,0,0);

    for (StageResult result : this.results) {
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

  public void generateStageRanks() {
    Collections.sort(this.segmentsInStage); // Order segments by their location.
    int segmentCounter = 1; // starts from 1 as initial time is start time.
    for (Segment segment : segmentsInStage){ // iterates through every segment.
      ArrayList<SegmentTimes> segmentTimes = new ArrayList<>();
      // store segmentTime and associated rider then sort
      int riderCounter = 0;
      for (StageResult result : results){
        segmentTimes.add(new SegmentTimes(result.getTimes()[segmentCounter], result.getRiderId()));
      }
      Collections.sort(segmentTimes);
      segment.setOrderedTimesToRiderId(segmentTimes);
      int rank = 0;
      for (SegmentTimes segmentTime : segmentTimes){
        segmentTime.setRank(rank);
        rank++;
      }
    }
  }

  public int getRidersRankInSegment(int segment, int riderId) throws IDNotRecognisedException {
    //System.out.println(segmentsInStage.toString()+" segments in stage"+segment);
    //System.out.println("inside segment for 2");
    this.generateStageRanks();
    for (SegmentTimes segmentTime : segmentsInStage.get(segment).getOrderedTimesToRiderId()){
      if (segmentTime.getRiderId() == riderId){
        return segmentTime.getRank();
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
    for (StageResult result : stage.getResults()) {
      System.out.println(result.getFinishTime()+" b4 finish time");
      System.out.println(result.getAdjustedFinishTime()+" b4 adjusted time");
    }
    stage.generateAdjustedResults();
    for (StageResult result : stage.getResults()) {
      System.out.println(result.getFinishTime()+" ft");
      System.out.println(result.getAdjustedFinishTime()+" at");
    }
  }
}
