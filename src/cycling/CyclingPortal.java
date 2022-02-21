package src.cycling;

import javax.naming.Name;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementor of the CyclingPortalInterface interface.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class CyclingPortal implements CyclingPortalInterface {


  private HashMap<Integer, StagedRace> raceIdsToRaces = new HashMap<>();
  private HashMap<Integer, Competition> competitionIdsToCompetitions = new HashMap<>();
  private HashMap<Integer, Stage> stageIdsToStages = new HashMap<>();
  private HashMap<Integer, Segment> segmentIdsToRaces = new HashMap<>();
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
    try {
      raceIdsToRaces.remove(raceId);
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Race not found!");
    }
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

    return stage.getId();
  }

  @Override
  public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
    Set<Integer> stageIdsSet = stageIdsToStages.keySet();
    int[] stageIds = new int[stageIdsSet.size()];
    int index = 0;
    for (Integer i : stageIdsSet) {
      stageIds[index++] = i;
    }

    return stageIds;
  }

  @Override
  public double getStageLength(int stageId) throws IDNotRecognisedException {
    for (Map.Entry<Integer, Stage> idToStg : stageIdsToStages.entrySet()) {
      if (idToStg.getKey() == stageId) {
        return idToStg.getValue().getLength();
      }
    }
    throw new IDNotRecognisedException("Stage ID not recognised!");
  }

  @Override
  public void removeStageById(int stageId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub

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
    ArrayList<Integer> riderIdArrayList = new ArrayList<>();

    // Check if the team exists
    if (teamIdsToTeams.get(teamId) == null) {
      throw new IDNotRecognisedException("Team not found!");
    }

    // Look for all riders in this team
    for (Map.Entry<Integer, Rider> idToRider : riderIdsToRiders.entrySet()) {
      Integer riderId = idToRider.getKey();
      Rider rider = idToRider.getValue();
      if (rider.getTeamId() == teamId) {
        // Found one; add to list
        riderIdArrayList.add(riderId);
      }
    }

    // Convert the ArrayList to int[]
    return riderIdArrayList.stream().mapToInt(i -> i).toArray();
  }

  @Override
  public int createRider(int teamID, String name, int yearOfBirth)
      throws IDNotRecognisedException, IllegalArgumentException {
    // Check if the team exists
    if (teamIdsToTeams.get(teamID) == null) {
      throw new IDNotRecognisedException("Team ID not found!");
    }
    // NullPointerException not thrown so it does
    Rider newRider = new Rider(name, teamID, yearOfBirth);
    riderIdsToRiders.put(newRider.getId(), newRider);
    return newRider.getId();
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
    for (Map.Entry<Integer, Race> idToRace : raceIdsToStage.entrySet()) {
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
    boolean foundRace = false;
    // Find the named race's ID
    for (Map.Entry<Integer, StagedRace> stagedRaceEntry : raceIdsToRaces.entrySet()) {
      int raceId = stagedRaceEntry.getKey();
      StagedRace stagedRace = stagedRaceEntry.getValue();
      if (stagedRace.getName().equals(name)) {
        // Found it
        foundRace = true;
        raceIdsToRaces.remove(raceId);
      }
    }
    // Gone through the whole hashmap and still haven't found it
    if (!foundRace) {
      throw new NameNotRecognisedException("No race found with name " + name);
    }
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

  public static void main(String[] args)
      throws IDNotRecognisedException, InvalidNameException, IllegalNameException, InvalidLengthException {
    CyclingPortal cycPort = new CyclingPortal();
  }
}
