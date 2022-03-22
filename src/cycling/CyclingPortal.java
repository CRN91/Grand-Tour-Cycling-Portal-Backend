package src.cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
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
  private HashMap<Integer, Stage> stageIdsToStages = new HashMap<>();
  private HashMap<Integer, Segment> segmentIdsToSegments = new HashMap<>();
  private HashMap<Integer, Team> teamIdsToTeams = new HashMap<>();
  private HashMap<Integer, Rider> riderIdsToRiders = new HashMap<>();

  /**
   * @return The hashmap of each race's ID to its corresponding StagedRace object.
   */
  public HashMap<Integer, StagedRace> getRaceIdsToRaces() {
    return raceIdsToRaces;
  }

  /**
   * @return The hashmap of each stage's ID to its corresponding Stage object.
   */
  public HashMap<Integer, Stage> getStageIdsToStages() {
    return stageIdsToStages;
  }

  /**
   * @return The hashmap of each segment's ID to its corresponding Stage object.
   */
  public HashMap<Integer, Segment> getSegmentIdsToSegments() {
    return segmentIdsToSegments;
  }

  /**
   * @return The hashmap of each team's ID to its corresponding Team object.
   */
  public HashMap<Integer, Team> getTeamIdsToTeams() {
    return teamIdsToTeams;
  }

  /**
   * @return The hashmap of each rider's ID to its corresponding Rider object.
   */
  public HashMap<Integer, Rider> getRiderIdsToRiders() {
    return riderIdsToRiders;
  }

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
    // Trim surrounding whitespace and check if the name is blank (meaning it was just whitespace,
    // it's more than 30 characters in length or it's null
    name = name.trim();
    if (name.equals("") || name.length() > 30 || name == null) {
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
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race not found!");
    }

    int sumOfStagesLengths = 0;
    ArrayList<Stage> stages = race.getStages();
    for (Stage stage : stages) {
      sumOfStagesLengths += stage.getLength();
    }
    // Add details to status output string

    return "Race ID: " + race.getId() + "\n"
        + "Race name: " + race.getName() + "\n"
        + "Description: " + race.getDescription() + "\n"
        + "Number of stages: " + race.getStages().size() + "\n"
        + "Total race length: " + sumOfStagesLengths;
  }

  @Override
  public void removeRaceById(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " not recognised!");
    }
    raceIdsToRaces.remove(raceId);
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
  public int addStageToRace(int raceId, String name, String description, double length,
      LocalDateTime startTime, StageType type)
      throws IDNotRecognisedException, IllegalNameException, InvalidNameException,
      InvalidLengthException {
    // Check the race exists
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race " + raceId + " not found!");
    }
    // Trim surrounding whitespace and check if the name is blank (meaning it was just whitespace,
    // it's more than 30 characters in length or it's null
    name = name.trim();
    if (name.equals("") || name.length() > 30 || name == null) {
      throw new InvalidNameException("Name is greater than 30 characters!");
    }

    // For assertion
    int amountOfStages = race.getStages().size();

    // Check the length is >=5km
    Stage stage = new Stage(raceId, name, description, length, startTime, type);
    stageIdsToStages.put(stage.getId(), stage);
    race.addStage(stage);

    assert race.getStages().size() == amountOfStages + 1;

    return stage.getId();
  }

  @Override
  public int[] getRaceStages(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID not recognised!");
    }

    ArrayList<Stage> stages = race.getStages();
    int[] stageIds = new int[stages.size()];
    int i = 0;
    for (Stage stage : stages) {
      stageIds[i] = stage.getId();
      i++;
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
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    // Get the race object that contains it
    StagedRace race = raceIdsToRaces.get(stage.getRaceId());
    race.getStages().remove(stage);

    Boolean foundId = false;
    for (Integer stgId : stageIdsToStages.keySet()) {
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
    Stage stage = stageIdsToStages.get(stageId);
    // Does the stage exist?
    if (stage == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }
    // Is the location valid?
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
    if (stage.getStageType() == StageType.TT) {
      throw new InvalidStageTypeException("Cannot add segments to a time trial stage.");
    }

    // Is the length valid?
    if (length <= 0) {
      throw new InvalidLocationException("Invalid length entered! Enter one >= 0.");
    }
    if ((location + length) > stage.getLength()) {
      throw new InvalidLocationException("Invalid length exceeds length of stage!");
    }

    Segment categorisedClimb = new CategorisedClimb(stageId, type, averageGradient,
        length, location);
    segmentIdsToSegments.put(categorisedClimb.getId(), categorisedClimb);
    stage.addSegment(categorisedClimb);

    return categorisedClimb.getId();
  }

  @Override
  public int addIntermediateSprintToStage(int stageId, double location)
      throws IDNotRecognisedException, InvalidLocationException, InvalidStageStateException,
      InvalidStageTypeException {
    Stage stage = stageIdsToStages.get(stageId);
    // Does the stage exist?
    if (stage == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }
    // Is the location valid?
    if ((location >= stage.getLength()) || (location <= 0)) {
      throw new InvalidLocationException("Invalid location " + location + "!");
    }
    if (stage.getStageType() == StageType.TT) {
      throw new InvalidStageTypeException("Cannot add segments to a time trial stage.");
    }
    // Is the stage state "under development"?
    if (!stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("Stage is waiting for results!");
    }

    Segment intermediateSprint = new Segment(stageId, SegmentType.SPRINT, location);
    segmentIdsToSegments.put(intermediateSprint.getId(), intermediateSprint);
    stage.addSegment(intermediateSprint);

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
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

    ArrayList<Segment> segmentsInStage = stage.getSegmentsInStage();
    int[] segmentIdsInStage = new int[segmentsInStage.size()];
    Collections.sort(segmentsInStage);
    int i = 0;
    for (Segment segment : segmentsInStage) {
      segmentIdsInStage[i] = segment.getId();
      i++;
    }
    return segmentIdsInStage;
  }

  @Override
  public int createTeam(String name, String description) throws IllegalNameException,
      InvalidNameException {
    // Trim surrounding whitespace and check if the name is blank (meaning it was just whitespace,
    // it's more than 30 characters in length or it's null
    name = name.trim();
    if (name == null || name == "" || name.length() > 30) {
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
    Team team = teamIdsToTeams.get(teamId);
    if (team == null) {
      throw new IDNotRecognisedException("Team does not exist!");
    }
    teamIdsToTeams.remove(teamId);
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
    name = name.trim();
    // Check if the team exists
    Team team = teamIdsToTeams.get(teamID);
    if (team == null) {
      throw new IDNotRecognisedException("Team ID not found!");
    }
    // Trim surrounding whitespace and check if the name is blank (meaning it was just whitespace,
    // it's more than 30 characters in length or it's null
    if (name == null || name == "" || name.length() > 30) {
      throw new IllegalArgumentException("Invalid name of a team!");
    }

    Rider newRider = new Rider(name, teamID, yearOfBirth);
    riderIdsToRiders.put(newRider.getId(), newRider);
    team.addRider(newRider);
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
      } catch (NullPointerException ex) {
      } // Errors thrown for each team until ID is found
    }
    if (!hasBeenFound) {
      throw new IDNotRecognisedException("Rider ID not found!");
    }

    riderIdsToRiders.remove(riderId);

    // Results
    boolean foundRiderInRace = false;
    for (StagedRace race : raceIdsToRaces.values()) {
      ArrayList<RiderRaceResult> raceResults = race.getResults();
      // Destroy references to this rider in races
      for (RiderRaceResult raceResult : raceResults) {
        if (raceResult.getRiderId() == riderId) {
          foundRiderInRace = true;
          raceResults.remove(raceResult);

          // Destroy references to this rider in stages
          for (Stage stage : race.getStages()) {
            ArrayList<RiderStageResult> stageResults = stage.getResults();

            for (RiderStageResult stageResult : stageResults) {
              if (stageResult.getRiderId() == riderId) {
                stageResults.remove(stageResult);

                // Destroy references to this rider in segments
                for (Segment segment : stage.getSegmentsInStage()) {
                  ArrayList<RiderSegmentResult> segmentResults = segment.getResults();

                  for (RiderSegmentResult segmentResult : segmentResults) {
                    if (segmentResult.getRiderId() == riderId) {
                      segmentResults.remove(segmentResult);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void registerRiderResultsInStage(int stageId, int riderId, LocalTime... checkpoints)
      throws IDNotRecognisedException, DuplicatedResultException, InvalidCheckpointsException,
      InvalidStageStateException {
    Stage stage = stageIdsToStages.get(stageId); // Gets the stage.

    // Error checking
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    Rider rider = riderIdsToRiders.get(riderId);
    if (rider == null) {
      throw new IDNotRecognisedException("Rider ID not recognised!");
    }

    if (stage.getUnderDevelopment()) {
      throw new InvalidStageStateException(
          "The stage is under development so can't add rider results!");
    }
    // get the location data for each segment, order it, sync it to times, store it
    // Check whether the result has already been registered for this rider in this stage
    ArrayList<RiderStageResult> results = stage.getResults();
    if (!results.isEmpty()) {
      for (RiderStageResult result : results) {
        if (result.getRiderId() == riderId) {
          throw new DuplicatedResultException("Rider's result already registered in this stage!");
        }
      }
    }

    // Check the number of checkpoints in the stage
    ArrayList<Segment> segments = stage.getSegmentsInStage();
    int checkpointLength = checkpoints.length;
    if (!(segments == null)) {
      if (!(checkpointLength == segments.size() + 2)) {
        throw new InvalidCheckpointsException("Incorrect number of checkpoints in stage!");
      }
    } else if (checkpointLength != 2) {
      throw new InvalidCheckpointsException("Incorrect number of checkpoints in stage!");
    }

    // Check whether checkpoints are in chronological order
    LocalTime previousTime = LocalTime.of(0, 0, 0);
    for (LocalTime time : checkpoints) {
      if (time.compareTo(previousTime) < 0) {
        throw new InvalidCheckpointsException("Checkpoint times are not in chronological order!");
      }
      previousTime = time;
    }

    stage.addRiderResults(riderId, checkpoints);
  }

  @Override
  public LocalTime[] getRiderResultsInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

    // If the rider doesn't exist/doesn't have a result in this stage
    RiderStageResult result = null;
    for (RiderStageResult tmpResult : stage.getResults()) {
      if (tmpResult.getRiderId() == riderId) {
        result = tmpResult;
        break;
      }
    }
    if (result == null) {
      return new LocalTime[0];
    }

    return result.getTimes();
  }

  @Override
  public LocalTime getRiderAdjustedElapsedTimeInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    if (riderIdsToRiders.get(riderId) == null) {
      throw new IDNotRecognisedException("Rider ID is not recognised!");
    }
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID is not recognised!");
    }

    // Populate the RiderStageResults objects with their adjusted elapsed times.
    stage.generateAdjustedResults();

    for (RiderStageResult result : stage.getResults()) {
      if (result.getRiderId() == riderId) {
        return result.getAdjustedFinishTime();
      }
    }

    return null;
  }

  @Override
  public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
    // Does stage exist?
    if (stageIdsToStages.get(stageId) == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }
    Stage stage = stageIdsToStages.get(stageId);
    // Does rider exist?
    if (riderIdsToRiders.get(riderId) == null) {
      throw new IDNotRecognisedException("Rider " + riderId + " not found!");
    }

    stage.removeResultByRiderId(riderId);
  }

  @Override
  public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
    // Does stage exist?
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }

    ArrayList<Integer> riderIdsList = new ArrayList<>();
    ArrayList<RiderStageResult> results = stage.getResults();

    // Fill the IDs list with the rider ids which are now in rank order
    for (RiderStageResult result : results) {
      riderIdsList.add(result.getRiderId());
    }

    // Convert the ArrayList to int[]
    return riderIdsList.stream().mapToInt(i -> i).toArray();
  }

  @Override
  public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) { // checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

    stage.generateAdjustedResults();

    ArrayList<RiderStageResult> results = stage.getResults();
    LocalTime[] RankAdjustedElapsedTimesInStage = new LocalTime[results.size()];
    int i = 0;
    for (RiderStageResult result : stage.getResults()) {
      RankAdjustedElapsedTimesInStage[i] = result.getAdjustedFinishTime();
      i++;
    }
    return RankAdjustedElapsedTimesInStage;
  }

  @Override
  public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) { // checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

    return stage.generatePointsInStage(false);
  }

  @Override
  public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) { // checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    if ((stage.getStageType() == StageType.TT) || (stage.getSegmentsInStage().size() == 0)) {
      // No mountain points (no segments)
      return new int[0];
    }

    // Check if there even are any climbs
    boolean climbFound = false;
    for (Segment segment : stage.getSegmentsInStage()) {
      if (segment.getSegmentType() != SegmentType.SPRINT) {
        climbFound = true;
      }
    }
    // No climbs so there will be no results
    if (!climbFound) {
      return new int[0];
    }

    return stage.generatePointsInStage(true);
  }

  @Override
  public void eraseCyclingPortal() {
    // Reset all internal counters
    Rider.resetIdCounter();
    Team.resetIdCounter();
    Stage.resetIdCounter();
    StagedRace.resetIdCounter();
    Segment.resetIdCounter();
    CategorisedClimb.resetIdCounter();
    // Erase all references and get them garbage collected
    this.raceIdsToRaces = new HashMap<>();
    this.stageIdsToStages = new HashMap<>();
    this.segmentIdsToSegments = new HashMap<>();
    this.teamIdsToTeams = new HashMap<>();
    this.riderIdsToRiders = new HashMap<>();
  }

  @Override
  public void saveCyclingPortal(String filename) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(filename);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    objectOutputStream.writeObject(this);
    objectOutputStream.flush();
    objectOutputStream.close();
  }

  @Override
  public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
    FileInputStream fileInputStream = new FileInputStream(filename);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    CyclingPortal tmp = (CyclingPortal) objectInputStream.readObject();
    this.raceIdsToRaces = tmp.raceIdsToRaces;
    this.stageIdsToStages = tmp.stageIdsToStages;
    this.segmentIdsToSegments = tmp.segmentIdsToSegments;
    this.teamIdsToTeams = tmp.teamIdsToTeams;
    this.riderIdsToRiders = tmp.riderIdsToRiders;
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
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " not found!");
    }
    race.generateRidersRaceResults();
    ArrayList<RiderRaceResult> raceResults = race.getResults();

    LocalTime[] finishTimes = new LocalTime[raceResults.size()];
    int i = 0;
    for (RiderRaceResult result : raceResults) {
      finishTimes[i] = result.getFinishTime();
      i++;
    }
    return finishTimes;
  }

  @Override
  public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) { //error check
      throw new IDNotRecognisedException("Race ID " + raceId + " not found!");
    }
    return race.generateRidersPointsInRace(false);
  }

  @Override
  public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) { //error check
      throw new IDNotRecognisedException("Race ID " + raceId + " not found!");
    }
    return race.generateRidersPointsInRace(true);
  }

  @Override
  public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId); // gets the race
    if (race == null) { //error check
      throw new IDNotRecognisedException("Race ID " + raceId + " not found!");
    }
    race.generateRidersRaceResults(); // returns race results also stores in race result list
    ArrayList<RiderRaceResult> raceResults = race.getResults(); //gets race result arraylist

    int raceResultsSize = raceResults.size();
    int[] riderIdsOrderedByRank = new int[raceResultsSize];
    for (int i = 0; i < raceResultsSize; i++) {
      riderIdsOrderedByRank[i] = raceResults.get(i).getRiderId();
    }

    return riderIdsOrderedByRank;
  }

  @Override
  public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " not recognised!");
    }
    race.generateRidersPointsInRace(false);
    return race.getRiderIdsOrderedByPoints(false);
  }

  @Override
  public int[] getRidersMountainPointClassificationRank(int raceId)
      throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " not recognised!");
    }
    race.generateRidersPointsInRace(true);
    return race.getRiderIdsOrderedByPoints(true);
  }
}