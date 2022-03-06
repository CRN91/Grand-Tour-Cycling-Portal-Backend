package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

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

  private GeneralClassification generalClassification;
  private MountainClassification mountainClassification;
  private PointsClassification pointsClassification;
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

  public GeneralClassification getGeneralClassification() {
    return generalClassification;
  }

  public MountainClassification getMountainClassification() {
    return mountainClassification;
  }

  public PointsClassification getPointsClassification() {
    return pointsClassification;
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
    this.generalClassification = new GeneralClassification();
    this.mountainClassification = new MountainClassification();
    this.pointsClassification = new PointsClassification();
  }
}
