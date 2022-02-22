package src.cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.time.LocalDateTime;
/**
 * Implementor of the CyclingPortalInterface interface.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class CyclingPortal implements CyclingPortalInterface {


  private HashMap<Integer, StagedRace> raceIdsToRaces = new HashMap<>();
  private HashMap<Integer, Competition> competitionIdsToCompetitions = new HashMap<>();
  public HashMap<Integer, Stage> stageIdsToStages = new HashMap<>();
  private HashMap<Integer, Segment> segmentIdsToSegments = new HashMap<>();
  private HashMap<Integer, Team> teamIdsToTeams = new HashMap<>();
  private HashMap<Integer, Rider> riderIdsToRiders = new HashMap<>();

  @Override
  public int[] getRaceIds() {
    Set<Integer> raceIdsSet = raceIdsToRaces.keySet();
    int[] raceIds = new int[raceIdsSet.size()];
    int index = 0;
    for (Integer i : raceIdsSet) {
      raceIds[index++] = i;
    }

    return raceIds;
  }

  @Override
  public int createRace(String name, String description)
      throws IllegalNameException, InvalidNameException {
    if (name.trim().equals("")) {
      throw new InvalidNameException("Race name is invalid!");
    }
    // Search hashmap of races for one with the given name and throw exception if found
    for (StagedRace stagedRace : raceIdsToRaces.values()) {
      String stagedRaceName = stagedRace.getName();
      if (stagedRaceName.equals(name)) {
        throw new IllegalNameException("There is already a race with this name!");
      }
    }

    // Race doesn't already exist
    StagedRace raceToAdd = new StagedRace(name, description);
    raceIdsToRaces.put(raceToAdd.getId(), raceToAdd);

    return raceToAdd.getId();
  }

  @Override
  public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
    String raceDetails = "";

    StagedRace stagedRace;
    try {
      stagedRace = raceIdsToRaces.get(raceId);
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Race not found!");
    }

    // Add immediately accessible details to status output string
    raceDetails = raceDetails
        + "Race name: " + stagedRace.getName() + "\n"
        + "Description: " + stagedRace.getDescription() + "\n"
        + "Race ID: " + stagedRace.getId() + "\n"
        + "Dates: " + "\n";

    // TODO Add stage details to output
    // TODO Add results details to output

    return raceDetails;
  }

  @Override
  public void removeRaceById(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub

  }

  @Override
  public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int addStageToRace(int raceId, String stageName, String description, double length,
                            LocalDateTime startTime, StageType type)
          throws IDNotRecognisedException, IllegalNameException, InvalidNameException,
          InvalidLengthException {
    // Check the race exists
    if (raceIdsToRaces.get(raceId) == null) {
      throw new IDNotRecognisedException("Race" + raceId + "not found!");
    }
    Stage stage = new Stage(raceId, stageName, description, length, startTime, type);
    stageIdsToStages.put(stage.getId(), stage);
    for (Integer stg : stageIdsToStages.keySet()) {
      System.out.println(stg);
    }

    return stage.getId();

  }

  @Override
  public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double getStageLength(int stageId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void removeStageById(int stageId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub

    Boolean foundId = false;
    for ( Integer stgId : stageIdsToStages.keySet() ) {
      if (stgId == stageId) {
        stageIdsToStages.remove(stgId);
        foundId = true;
        break;
      }
    }
    if (!foundId) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
  }

  @Override
  public int addCategorizedClimbToStage(int stageId, Double location, SegmentType type,
                                        Double averageGradient, Double length)
      throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
      InvalidStageTypeException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public int addIntermediateSprintToStage(int stageId, double location)
      throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
      InvalidStageTypeException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void removeSegment(int segmentId) throws IDNotRecognisedException,
      InvalidStageStateException {
    // TODO Auto-generated method stub

  }

  @Override
  public void concludeStagePreparation(int stageId) throws IDNotRecognisedException,
      InvalidStageStateException {
    // TODO Auto-generated method stub

  }

  @Override
  public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
    if (stageIdsToStages.get(0) == null) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }


    for (Map.Entry<Integer, Stage> idToStg : stageIdsToStages.entrySet()) {
      if (idToStg.getKey() == stageId) {
        ArrayList<Segment> segments = idToStg.getValue().getSegmentsInStage();
        int segmentsLength = segments.size();
        int i = 0;
        int[] arrayOfSegmentIds = new int[segmentsLength];
        for (Segment segment : segments) {
          arrayOfSegmentIds[i] = segment.getId();
          i++;
        }
        return arrayOfSegmentIds;
      }
    }
    throw new IDNotRecognisedException("Stage ID not recognised!");
  }

  @Override
  public int createTeam(String name, String description) throws IllegalNameException,
      InvalidNameException {
    if (name == null || name == "" || name.trim() == "") {
      throw new InvalidNameException("Invalid name of a team!");
    }
    for (Team team : teamIdsToTeams.values()) {
      if (team.getName() == name) {
        throw new IllegalNameException("Team name already in use!");
      }
    }
    Team newTeam = new Team(name, description);
    teamIdsToTeams.put(newTeam.getId(), newTeam);
    return newTeam.getId();
  }

  @Override
  public void removeTeam(int teamId) throws IDNotRecognisedException {
    try {
      teamIdsToTeams.remove(teamId);
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Team does not exist!");
    }
  }

  @Override
  public int[] getTeams() {
    Set<Integer> teamIdsSet = teamIdsToTeams.keySet();
    int[] teamIds = new int[teamIdsSet.size()];
    int index = 0;
    for (Integer i : teamIdsSet) {
      teamIds[index++] = i;
    }

    return teamIds;
  }

  @Override
  public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
    try {
      Team team = teamIdsToTeams.get(teamId);
      HashMap<Integer, Rider> riderIdsToRiders = team.getRiderIdsToRiders();
      Set<Integer> riderIdsSet = riderIdsToRiders.keySet();
      int[] riderIds = new int[riderIdsSet.size()];
      int index = 0;
      for (Integer i : riderIdsSet) {
        riderIds[index++] = i;
      }

      return riderIds;
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Team ID not recognised!");
    }
  }

  @Override
  public int createRider(int teamID, String name, int yearOfBirth)
      throws IDNotRecognisedException, IllegalArgumentException {
    try {
      Team team = teamIdsToTeams.get(teamID);
      Rider newRider = new Rider(name, teamID, yearOfBirth);
      team.addRider(newRider);
      return newRider.getId();
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Team ID not found!");
    }
  }

  @Override
  public void removeRider(int riderId) throws IDNotRecognisedException {
    boolean hasBeenFound = false;
    for (Team team : teamIdsToTeams.values()) {
      HashMap<Integer, Rider> riders = team.getRiderIdsToRiders();
      try {
        if (riders.get(riderId) != null) {
          hasBeenFound = true;
        }
        riders.remove(riderId);
        break;
      } catch (NullPointerException ex) { } // Errors thrown for each team until ID is found
    }
    if (!hasBeenFound) {
      throw new IDNotRecognisedException("Rider ID not found!");
    }
  }

  @Override
  public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
      throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
      InvalidStageStateException {
    // TODO Auto-generated method stub

  }

  @Override
  public LocalTime[] getRiderResultsInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub

  }

  @Override
  public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId)
      throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void eraseCyclingPortal() {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveCyclingPortal(String filename) throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeRaceByName(String name) throws NameNotRecognisedException {
    // TODO Auto-generated method stub

  }

  @Override
  public LocalTime[] getGeneralClassificationTimesInRace(int raceId)
      throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int[] getRidersMountainPointClassificationRank(int raceId)
      throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
  }

  public static void main(String[] args) throws IDNotRecognisedException, InvalidNameException, IllegalNameException, InvalidLengthException {
    CyclingPortal cycPort = new CyclingPortal();
    LocalDateTime now = LocalDateTime.now();
    cycPort.createRace("cycle race", "race done on bicycles");
    cycPort.addStageToRace(0,"john","cena",0.0, now, StageType.FLAT);
    System.out.println(cycPort.getRaceStages(0));
    cycPort.removeStageById(0);
    System.out.println(cycPort.getRaceStages(0));
  }
}
