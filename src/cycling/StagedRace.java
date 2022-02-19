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
  private String nameOfRace;
  private LocalTime[] datesOfCompetitions;
  private HashMap<Integer, Team> teamIdsToTeamsCompeting;
  private Stage[] stagedRaceStages;

  public String getNameOfRace() {
    return nameOfRace;
  }

  public LocalTime[] getDatesOfCompetitions() {
    return datesOfCompetitions;
  }

  public HashMap<Integer, Team> getTeamIdsToTeamsCompeting() {
    return teamIdsToTeamsCompeting;
  }

  public Stage[] getStagedRaceStages() {
    return stagedRaceStages;
  }

  public void setNameOfRace(String nameOfRace) {
    this.nameOfRace = nameOfRace;
  }
}
