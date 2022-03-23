package src.cycling;

import java.time.LocalDateTime;

/**
 * Represents a time trial stage.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class TimeTrial extends Stage {

  StageType stageType = StageType.TT;

  /**
   * Constructor.
   *
   * @param raceId The ID of the race in which this time trial happens.
   * @param name The name of the time trial.
   * @param description The description of the time trial.
   * @param length The length of the time trial.
   * @param startTime The start time of the time trial.
   * @param stageType The StageType of the time trial (always StageType.TT)
   */
  public TimeTrial(Integer raceId, String name, String description, Double length,
      LocalDateTime startTime, StageType stageType) {
    super(raceId, name, description, length, startTime, stageType);
  }
}
