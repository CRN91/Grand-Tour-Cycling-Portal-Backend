package src.cycling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Set;

public class CyclingPortal implements CyclingPortalInterface {

  private HashMap<Integer, Team> teamIdsToTeams = new HashMap<Integer, Team>();

  @Override
  public int[] getRaceIds() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int createRace(String name, String description)
      throws IllegalNameException, InvalidNameException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub
    return null;
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
    // TODO Auto-generated method stub
    return 0;
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int createTeam(String name, String description) throws IllegalNameException,
      InvalidNameException {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void removeTeam(int teamId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub

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
      return newRider.getRiderId();
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Team ID not found!");
    }
  }

  @Override
  public void removeRider(int riderId) throws IDNotRecognisedException {
    // TODO Auto-generated method stub

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

  public static void main(String[] args) throws IDNotRecognisedException {
    CyclingPortal cyclingPortal = new CyclingPortal();
    int[] riderIds = cyclingPortal.getTeamRiders(1);
    for (int i : riderIds) {
      System.out.println(Integer.toString(i));
    }
  }
}
