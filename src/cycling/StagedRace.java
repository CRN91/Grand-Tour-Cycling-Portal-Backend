package src.cycling;

import java.time.LocalTime;
import java.util.HashMap;

/**
 * Represents a grand tour staged race.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class StagedRace {
  private String name;
  private String description;
  private int raceId;
  private LocalTime[] datesOfCompetitions;

  private Competition[] competitions = new Competition[3];
  private Stage[] raceStages;

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
