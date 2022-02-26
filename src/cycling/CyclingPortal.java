package src.cycling;

import javax.naming.Name;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    try {
      raceIdsToRaces.remove(raceId);
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Race not found!");
    }
  }

  @Override
  public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
    int numberOfStages = 0;
    // Does the race exist?
    if (raceIdsToRaces.get(raceId) == null) {
      throw new IDNotRecognisedException("Race " + raceId + " not found!");
    }
    // Yes so count its stages
    for (Map.Entry<Integer, Stage> idToStage : stageIdsToStages.entrySet()) {
      Stage stage = idToStage.getValue();
      if (stage.getRaceId() == raceId) {
        numberOfStages++;
      }
    }
    return numberOfStages;
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
    // Check the length is >=5km
    Stage stage = new Stage(raceId, stageName, description, length, startTime, type);
    stageIdsToStages.put(stage.getId(), stage);

    return stage.getId();
  }

  @Override
  public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
    if ( raceIdsToRaces.get(raceId) == null) {
      throw new IDNotRecognisedException("Race ID not recognised!");
    }

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
    if ( stageIdsToStages.get(stageId) == null) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

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
    // Does the stage exist?
    if (stageIdsToStages.get(stageId) == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }
    // Is the location valid?
    Stage stage = stageIdsToStages.get(stageId);
    if ((location >= stage.getLength()) || (location <= 0)) {
      throw new InvalidLocationException("Invalid location!");
    }
    // Is the stage state "under development?"
    if (!stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("Stage is waiting for results!");
    }
    // Are you trying to add a sprint using the wrong method?
    if (type == SegmentType.SPRINT) {
      throw new InvalidStageTypeException("Cannot add sprint to stage using this method!");
    }
    // Is the length valid?
    if (length <= 0) {
      throw new InvalidLocationException("Invalid length entered! Enter one >= 0.");
    }
    if ((location + length) > stage.getLength()) {
      throw new InvalidLocationException("Invalid length exceeds length of stage!");
    }

    Segment categorisedClimb = new CategorisedClimb(stageId, type, averageGradient,
        length);
    segmentIdsToSegments.put(categorisedClimb.getId(), categorisedClimb);

    return categorisedClimb.getId();
  }

  @Override
  public int addIntermediateSprintToStage(int stageId, double location)
      throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
          InvalidStageTypeException {
    // Does the stage exist?
    if (stageIdsToStages.get(stageId) == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }
    // Is the location valid?
    Stage stage = stageIdsToStages.get(stageId);
    if ((location >= stage.getLength()) || (location <= 0)) {
      throw new InvalidLocationException("Invalid location!");
    }
    // Is the stage state "under development"?
    if (!stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("Stage is waiting for results!");
    }

    Segment intermediateSprint = new Segment(stageId, SegmentType.SPRINT);
    segmentIdsToSegments.put(intermediateSprint.getId(), intermediateSprint);

    return intermediateSprint.getId();
  }

  @Override
  public void removeSegment(int segmentId) throws IDNotRecognisedException,
      InvalidStageStateException {
    // Does the segment exist?
    if (segmentIdsToSegments.get(segmentId) == null) {
      throw new IDNotRecognisedException("Segment " + segmentId + " not found!");
    }
    Segment segment = segmentIdsToSegments.get(segmentId);
    int segmentStageId = segment.getStageId();
    // Can I do this (the stage has to be under development)
    // Find the stage
    for (Map.Entry<Integer, Stage> stageEntry : stageIdsToStages.entrySet()) {
      Stage stage = stageEntry.getValue();
      if (stage.getId() == segmentStageId) {
        // This is the right stage now check we can delete segments from it
        if (stage.getUnderDevelopment()) {
          segmentIdsToSegments.remove(segmentId);
        } else {
          throw new InvalidStageStateException("Stage " + stage.getId()
              + " is not under development!");
        }
      }
    }
  }

  @Override
  public void concludeStagePreparation(int stageId) throws IDNotRecognisedException,
      InvalidStageStateException {
    // Does the stage exist?
    if (stageIdsToStages.get(stageId) == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }
    // Is it already waiting for results?
    Stage stage = stageIdsToStages.get(stageId);
    if (!stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("Stage " + stageId
          + " is already waiting for results!");
    } else {
      stage.setUnderDevelopment(false);
    }
  }

  @Override
  public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
    if ( stageIdsToStages.get(stageId) == null ) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

    for (Map.Entry<Integer, Stage> idToStg : stageIdsToStages.entrySet()) {
      if (idToStg.getKey() == stageId) {
        ArrayList<Segment> segments = idToStg.getValue().getSegmentsInStage();
        if (segments != null) {
          int segmentsLength = segments.size();
          int i = 0;
          int[] arrayOfSegmentIds = new int[segmentsLength];
          for (Segment segment : segments) {
            arrayOfSegmentIds[i] = segment.getId();
            i++;
          }
          return arrayOfSegmentIds;
        }
        else {
          return null;
        }
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
    // TODO REMOVE ALL THIS RIDER'S RESULTS
  }

  @Override
  public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
      throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
      InvalidStageStateException {
    Stage stage = stageIdsToStages.get(stageId);

    if (stage == null){
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    Rider rider = riderIdsToRiders.get(riderId);
    if (rider == null){
      throw new IDNotRecognisedException("Rider ID not recognised!");
    }
    HashMap<Integer,LocalTime[]> riderResults = stage.getRiderIdsToResults();
    if (!(riderResults == null)){
      LocalTime[] checkDuplicate = riderResults.get(riderId);
      if (!(checkDuplicate == null)){
        throw new DuplicatedResultException("Rider's results are already registered!");
      }
    }
    ArrayList<Segment> segments = stage.getSegmentsInStage();
    int checkpointLength = checkpoints.length;
    if (!(segments == null)){
      if (!(checkpointLength == segments.size() + 2)){
        throw new InvalidCheckpointsException("Incorrect number of checkpoints in stage!");
      }
    }else if (checkpointLength != 2){
      throw new InvalidCheckpointsException("Incorrect number of checkpoints in stage!");
    }

    LocalTime previousTime = LocalTime.of(0,0,0);
    for (LocalTime time : checkpoints){
      if (time.compareTo(previousTime) < 0){
        throw new InvalidCheckpointsException("Checkpoint times are not in chronological order!");
      }
      previousTime = time;
    }
    if (stage.getUnderDevelopment() == true){
      throw new InvalidStageStateException("The stage is under development so can't add rider results!");
    }
    stage.addRiderIdsToResults(riderId, checkpoints);
  }

  @Override
  public LocalTime[] getRiderResultsInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null){
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    LocalTime[] results = stage.getRiderIdsToResults().get(riderId);
    if (results == null){
      throw new IDNotRecognisedException("Rider ID not recognised!");
    }
    return results;
  }

  @Override
  public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    // TODO from stageId get raceId from that get race then get competition.getAdjustedTimes
    // then search for our rider
    return null;
  }

  @Override
  public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
    // TODO goto stage, stage.removeRiderResults(riderId)

  }

  @Override
  public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
    // TODO from stageid get stage get race get competition.getStageResults
    Stage stage = stageIdsToStages.get(stageId);
    //stage.getRiderIdsToResults().keySet()
    //for (Map.Entry<Integer, LocalTime[]> riderToTimes : stage.getRiderIdsToResults().entrySet()){

    //}
    return null;
  }

  @Override
  public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId)
      throws IDNotRecognisedException {
    // TODO from stageId get stage, then get race, then competition.getAdjustedTimes
    return null;
  }

  @Override
  public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
    // TODO stageId to stage to race to competition then loop through all intermediate sprints
    //  in a stage and do competition.getSegmentResults and also competition.getStageResults
    return null;
  }

  @Override
  public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
    // TODO stageId to stage to race to competition then loop through all categorised climbs in
    //  a stage and do competition.getSegmentResults
    return null;
  }

  @Override
  public void eraseCyclingPortal() {
    // Reset all internal counters
    Rider.resetIdCounter();
    Team.resetIdCounter();
    Stage.resetIdCounter();
    StagedRace.resetIdCounter();
    Segment.resetIdCounter();
    // TODO competitions? and other stuff
    // Erase all references and get them garbage collected
    this.raceIdsToRaces = new HashMap<>();
    this.competitionIdsToCompetitions = new HashMap<>();
    this.stageIdsToStages = new HashMap<>();
    this.segmentIdsToSegments = new HashMap<>();
    this.teamIdsToTeams = new HashMap<>();
    this.riderIdsToRiders = new HashMap<>();
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
    // TODO from raceId get race then get competition obj then loop through each stage and
    // TODO competition.getStageResults and sum it to a variable then return ordered list
    return null;
  }

  @Override
  public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults
    return null;
  }

  @Override
  public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults
    return null;
  }

  @Override
  public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults then return just the riders
    return null;
  }

  @Override
  public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults then return just the riders
    return null;
  }

  @Override
  public int[] getRidersMountainPointClassificationRank(int raceId)
      throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults then return just the riders
    return null;
  }

  public static void main(String[] args) throws IDNotRecognisedException, InvalidNameException, IllegalNameException,
      InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, DuplicatedResultException, InvalidCheckpointsException {

    CyclingPortal cycPort = new CyclingPortal();
    cycPort.createRace("Big boy race", "food fight race");
    cycPort.addStageToRace(0,"stage uno", "first 1",
        10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.stageIdsToStages.get(0).setUnderDevelopment(false);
    cycPort.createTeam("america","wont invade ukraine");
    cycPort.createRider(0,"Ken",1608);
    cycPort.createRider(0,"HOG RIDER",2015);

    cycPort.registerRiderResultsInStage(0,0,LocalTime.of(0,0,2),
        LocalTime.of(0,0,2));

    for (LocalTime time : cycPort.getRiderResultsInStage(0,0) ){
      System.out.println(time);
    }
  }
}
