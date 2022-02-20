package src.cycling;

import java.time.LocalDateTime;

/**
 * Represents a time trial stage.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class TimeTrial extends Stage{
   StageType stageType = StageType.TT;

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
  public TimeTrial(Integer raceId, String name, String description, Double length, LocalDateTime startTime, StageType stageType) {
    super(raceId, name, description, length, startTime, stageType);
  }
}
