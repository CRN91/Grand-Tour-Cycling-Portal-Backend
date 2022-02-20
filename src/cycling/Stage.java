package src.cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Represents a non-time trial stage.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class Stage {
  protected Integer raceId;
  protected String name;
  protected String description;
  protected Double length;
  protected LocalDateTime startTime;
  protected StageType stageType;
  protected Integer id;
  private ArrayList<Segment> segmentsInStage;
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
  public Stage(Integer raceId, String name, String description, Double length, LocalDateTime startTime, StageType stageType) {
    this.raceId = raceId;
    this.name = name;
    this.description = description;
    this.length = length;
    this.startTime = startTime;
    this.stageType = stageType;
    this.id = latestId++;
  }
}
