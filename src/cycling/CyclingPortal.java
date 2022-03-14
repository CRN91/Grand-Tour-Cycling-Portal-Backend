package src.cycling;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private HashMap<Integer, HashMap<Integer,Integer>> raceIdsToRidersToPoints = new HashMap<>();
  private HashMap<Integer, HashMap<Integer,Integer>> raceIdsToRidersToMountainPoints = new HashMap<>();
  private HashMap<Integer, HashMap<Integer,Integer>> stageIdsToRidersToPoints = new HashMap<>();
  private HashMap<Integer, HashMap<Integer,Integer>> stageIdsToRidersToMountainPoints = new HashMap<>();

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
    // TODO TESTING
    String raceDetails = "";

    StagedRace race;
    try {
      race = raceIdsToRaces.get(raceId);
    } catch (NullPointerException ex) {
      throw new IDNotRecognisedException("Race not found!");
    }

    int sumOfStagesLengths = 0;
    ArrayList<Stage> stages = race.getStages();
    for (Stage stage : stages) {
      sumOfStagesLengths += stage.length;
    }
    // Add details to status output string
    raceDetails = raceDetails
        + "Race ID: " + race.getId() + "\n"
        + "Race name: " + race.getName() + "\n"
        + "Description: " + race.getDescription() + "\n"
        + "Number of stages: " + race.getStages().size() + "\n"
        + "Total race length: " + sumOfStagesLengths;

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
    raceIdsToRaces.get(raceId).addStage(stage);

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
        length, location);
    segmentIdsToSegments.put(categorisedClimb.getId(), categorisedClimb);
    stage.addSegment(categorisedClimb);

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
      throw new InvalidLocationException("Invalid location " + location + "!");
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
    Stage stage = stageIdsToStages.get(stageId); // Gets the stage.

    // Error checking
    if (stage == null){
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    Rider rider = riderIdsToRiders.get(riderId);
    if (rider == null){
      throw new IDNotRecognisedException("Rider ID not recognised!");
    }

    if (stage.getUnderDevelopment()){
      throw new InvalidStageStateException("The stage is under development so can't add rider results!");
    }
// get the location data for each segment, order it, sync it to times, store it
    // Check whether the result has already been registered for this rider in this stage
    ArrayList<StageResult> results = stage.getResults();
    if (!results.isEmpty()) {
      for (StageResult result : results) {
        if (result.getRiderId() == riderId) {
          throw new DuplicatedResultException("Rider's result already registered in this stage!");
        }
      }
    }

    // Check the number of checkpoints in the stage
    ArrayList<Segment> segments = stage.getSegmentsInStage();
    int checkpointLength = checkpoints.length;
    if (!(segments == null)) {
      if (!(checkpointLength == segments.size() + 2)){
        throw new InvalidCheckpointsException("Incorrect number of checkpoints in stage!");
      }
    } else if (checkpointLength != 2){
      throw new InvalidCheckpointsException("Incorrect number of checkpoints in stage!");
    }

    // Check whether checkpoints are in chronological order
    LocalTime previousTime = LocalTime.of(0,0,0);
    for (LocalTime time : checkpoints){
      if (time.compareTo(previousTime) < 0){
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
    StageResult result = null;
    for (StageResult tmpResult : stage.getResults()) {
      if (tmpResult.getRiderId() == riderId) {
        result = tmpResult;
        break;
      }
    }
    if (result == null){
      throw new IDNotRecognisedException("Rider ID " + riderId + " not recognised!");
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
    if (stage == null){
      throw new IDNotRecognisedException("Stage ID is not recognised!");
    }

    stage.generateAdjustedResults();

    for (StageResult result : stage.getResults()) {
      if (result.getRiderId() == riderId) {
        return result.getAdjustedFinishTime();
      }
    }

    return null;
  }

  @Override
  public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
    // TODO goto stage->race->competition->results map
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
    if (stageIdsToStages.get(stageId) == null) {
      throw new IDNotRecognisedException("Stage " + stageId + " not found!");
    }

    Stage stage = stageIdsToStages.get(stageId);
    ArrayList<Integer> riderIdsList = new ArrayList<>();
    ArrayList<StageResult> results = stage.getResults();
    Collections.sort(results);

    // Fill the IDs list with the rider ids which are now in rank order
    for (StageResult result : results) {
      riderIdsList.add(result.getRiderId());
    }

    // Convert the ArrayList to int[]
    return riderIdsList.stream().mapToInt(i -> i).toArray();
  }

  @Override
  public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);

    stage.generateAdjustedResults();

    ArrayList<StageResult> results = stage.getResults();
    LocalTime[] RankAdjustedElapsedTimesInStage = new LocalTime[results.size()];
    int i = 0;
    for (StageResult result : stage.getResults()){
      RankAdjustedElapsedTimesInStage[i] = result.getAdjustedFinishTime();
      i++;
    }
    return RankAdjustedElapsedTimesInStage;
  }

  @Override
  public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null){ // checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }

    // Table of rank to points for each stage type.
    int[] flatPointsConversion = {50,30,20,18,16,14,12,10,8,7,6,5,4,3,2};
    int[] hillyPointsConversion = {30,25,22,19,17,15,13,11,9,7,6,5,4,3,2};
    int[] mountainPointsConversion = {20,17,15,13,11,10,9,8,7,6,5,4,3,2,1};
    int[] timeTrialPointsConversion = mountainPointsConversion;
    Map<Enum, int[]> pointsConversion = Map.of(
        StageType.FLAT,flatPointsConversion,
        StageType.MEDIUM_MOUNTAIN,hillyPointsConversion,
        StageType.HIGH_MOUNTAIN,mountainPointsConversion,
        StageType.TT,timeTrialPointsConversion
    );

    return calculatePointsInStage(pointsConversion, false, stageId);
  }

  public int[] calculatePointsInStage(Map<Enum, int[]> pointsConversion, boolean isMountain, int stageId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null){ // checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID not recognised!");
    }
    if (isMountain) {
      // Do mountain checks
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
    }

    HashMap<Integer,Integer> riderIdsToPoints = new HashMap<Integer,Integer>();
    // Sum of points for each rider for the specified race.
    // For the points classification, add on the points from finish times which mountain doesn't include.
    if (!isMountain) {
      int pointsIndex = 0;
      stage.generateAdjustedResults(); // Sort times in ascending order.

      for (StageResult result : stage.getResults()) { // iterate through rider.
        int riderId = result.getRiderId();
        int points;
        if (pointsIndex < 15) { // Only first 15 riders are awarded points.
          points = pointsConversion.get(stage.getStageType())[pointsIndex]; // points for stage type
        }else {
          points = 0;
        }
        riderIdsToPoints.put(riderId, points);
        pointsIndex++;
      }
    }
    ArrayList<Segment> segmentsInStage = stage.getSegmentsInStage();
    Collections.sort(segmentsInStage); // Sort segments by location.

    for (int segmentIndex = 0; segmentIndex < segmentsInStage.size(); segmentIndex++){
      SegmentType currentSegmentType = segmentsInStage.get(segmentIndex).getSegmentType();
      Enum mapKey;
      if (isMountain) {
        mapKey = (SegmentType) segmentsInStage.get(segmentIndex).getSegmentType();
      } else {
        mapKey = (StageType) StageType.HIGH_MOUNTAIN;
      }
      if (((currentSegmentType == SegmentType.SPRINT) && !isMountain)
          || !(currentSegmentType == SegmentType.SPRINT) && isMountain){
        // Only add points if we're looking at the right classification for this segment
        for (StageResult riderResult : stage.getResults()){
          int riderId = riderResult.getRiderId();
          int riderRank = stage.getRidersRankInSegment(segmentIndex,riderId); // rank starts from 0
          int points;
          int[] rowOfPointsTable = pointsConversion.get(mapKey);
          int pointsLimit = rowOfPointsTable.length;
          if (riderRank < pointsLimit) {
            points = rowOfPointsTable[riderRank];
          }else {
            points = 0;
          }

          if (riderIdsToPoints.get(riderId) == null) { // if the rider is not registered with points add them.
            riderIdsToPoints.put(riderId, points);
          } else {
            riderIdsToPoints.merge(riderId, points, Integer::sum);
          }
        }
      }
    }

    ArrayList<StageResult> stageResults = stage.getResults();
    Collections.sort(stageResults); // Sorts stage results by time.
    int stageResultsSize = stageResults.size();
    int[] riderIdsOrderedByRank = new int[stageResultsSize];
    for (int i = 0; i < stageResultsSize; i++){
      riderIdsOrderedByRank[i] = stageResults.get(i).getRiderId();
    }

    int[] pointsOrderedByStageTime = new int[riderIdsOrderedByRank.length];
    if (!(riderIdsToPoints.isEmpty())){
      int i = 0;
      for (int riderId : riderIdsOrderedByRank){
        pointsOrderedByStageTime[i] = riderIdsToPoints.get(riderId);
        i++;
      }
    }

    if (isMountain) {
      stageIdsToRidersToMountainPoints.put(stageId, riderIdsToPoints);
    } else {
      stageIdsToRidersToPoints.put(stageId, riderIdsToPoints);
    }

    return pointsOrderedByStageTime;
  }

  @Override
  public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null){ // checks race is in cycling portal.
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

    // Table of rank to points for each stage type.
    int[] pointsHC = {20, 15, 12, 10, 8, 6, 4, 2}; // TOP 8 ONLY
    int[] pointsC1 = {10, 8, 6, 4, 2, 1, 0, 0}; // TOP 6 ONLY
    int[] pointsC2 = {5, 3, 2, 1, 0, 0, 0, 0}; // TOP 4 ONLY
    int[] pointsC3 = {2, 1, 0, 0, 0, 0, 0, 0}; // TOP 2 ONLY
    int[] pointsC4 = {1, 0, 0, 0, 0, 0, 0, 0}; // TOP 1 ONLY
    Map<Enum, int[]> pointsConversion = Map.of(
        SegmentType.HC, pointsHC,
        SegmentType.C1, pointsC1,
        SegmentType.C2, pointsC2,
        SegmentType.C3, pointsC3,
        SegmentType.C4, pointsC4);

    return calculatePointsInStage(pointsConversion, true, stageId);
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

    FileOutputStream fileOutputStream = new FileOutputStream(filename);
    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
    objectOutputStream.writeObject(this);
    objectOutputStream.flush();
    objectOutputStream.close();
  }

  @Override
  public void loadCyclingPortal(String filename) throws IOException, ClassNotFoundException {
    // TODO Auto-generated method stub

    FileInputStream fileInputStream = new FileInputStream(filename);
    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
    CyclingPortal tmp = (CyclingPortal) objectInputStream.readObject();
    this.raceIdsToRaces = tmp.raceIdsToRaces;
    this.competitionIdsToCompetitions = tmp.competitionIdsToCompetitions;
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
    // TODO from raceId get race then get competition obj then loop through each stage and
    // TODO competition.getStageResults and sum it to a variable then return ordered list
    StagedRace race = raceIdsToRaces.get(raceId);
    ArrayList<RaceResult> raceResults = new ArrayList<>();
    for (Stage stage : race.getStages()){ // iterates through all stages in a race
      for (StageResult stageResult : stage.getResults()){ // iterates through all results in a stage
        int riderId = stageResult.getRiderId();
        boolean riderFound = false;
        for (RaceResult raceResult : raceResults){
          if (raceResult.getRiderId() == riderId){
            raceResult.setFinishTime(sumLocalTimes.addLocalTimes(raceResult.getFinishTime(),
                stageResult.getFinishTime())); // sums race results finish time with new stages finish time
            riderFound = true;
            break;
          }
        }
        if (!riderFound){ // if no race result for rider one is made.
          RaceResult raceResult = new RaceResult(stageResult.getRiderId(), raceId,
              stageResult.getFinishTime());
          raceResults.add(raceResult);
        }
      }
    }
    Collections.sort(raceResults); // orders race results by total time.

    LocalTime[] finishTimes = new LocalTime[raceResults.size()];
    int i = 0;
    for (RaceResult result : raceResults){
      finishTimes[i] = result.getFinishTime();
      i++;
    }
  return finishTimes;
  }

  @Override
  public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null){ // checks race is in cycling portal.
      throw new IDNotRecognisedException("Race ID not recognised!");
    }
    HashMap<Integer,Integer> riderIdsToPointsInRace = new HashMap<Integer,Integer>();
    // Sum of points for each rider for the specified race.
    for (Stage stage : race.getStages()){ // iterate through stages.
      int stageId = stage.getId();
      this.getRidersPointsInStage(stageId);
      HashMap<Integer,Integer> riderIdsToPointsInStage = stageIdsToRidersToPoints.get(stageId);

      for (StageResult result : stage.getResults()) { // iterate through rider.
        int riderId = result.getRiderId();
        int points = riderIdsToPointsInStage.get(riderId);
        if (riderIdsToPointsInRace.get(riderId) == null) { // if the rider is not registered with points add them.
          riderIdsToPointsInRace.put(riderId, points);
        } else {
          riderIdsToPointsInRace.merge(riderId, points, Integer::sum);
        }
      }
    }
    int[] genClassRanks = getRidersGeneralClassificationRank(raceId); // rider Ids sorted by time
    int[] pointsOrderedByGenClass = new int[genClassRanks.length];
    int i = 0;
    for (int riderId : genClassRanks){
      pointsOrderedByGenClass[i] = riderIdsToPointsInRace.get(riderId);
      i++;
    }

    raceIdsToRidersToPoints.put(raceId,riderIdsToPointsInRace);

    return pointsOrderedByGenClass;
  }

  public int[] calculateRidersPointsInRace(boolean isMountain, int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null){ // checks race is in cycling portal.
      throw new IDNotRecognisedException("Race ID not recognised!");
    }
    HashMap<Integer,Integer> riderIdsToPointsInRace = new HashMap<Integer,Integer>();

    // Sum of points for each rider for the specified race.
    for (Stage stage : race.getStages()){ // iterate through stages.
      int stageId = stage.getId();
      HashMap<Integer, Integer> riderIdsToPointsInStage = new HashMap<>();
      if (isMountain) {
        if (!(this.getRidersMountainPointsInStage(stageId).length == 0)) {
          riderIdsToPointsInStage = stageIdsToRidersToMountainPoints.get(stageId);
        }
      } else {
        if (!(this.getRidersPointsInStage(stageId).length == 0)) {
          riderIdsToPointsInStage = stageIdsToRidersToPoints.get(stageId);
        }
      }
      for (StageResult result : stage.getResults()) { // iterate through rider.
        int riderId = result.getRiderId();
        int points;
        if ((riderIdsToPointsInStage != null) && !(riderIdsToPointsInStage.get(riderId) == null)) {
          points = riderIdsToPointsInStage.get(riderId);
        } else {
          points = 0;
        }
        if (riderIdsToPointsInRace.get(riderId) == null) { // if the rider is not registered with points add them.
          riderIdsToPointsInRace.put(riderId, points);
        } else {
          riderIdsToPointsInRace.merge(riderId, points, Integer::sum);
        }
      }
    }

    if (!(riderIdsToPointsInRace.isEmpty())) {
      int[] genClassRanks = getRidersGeneralClassificationRank(raceId); // rider Ids sorted by time
      int[] pointsOrderedByGenClass = new int[genClassRanks.length];
      int i = 0;
      for (int riderId : genClassRanks) {
        pointsOrderedByGenClass[i] = riderIdsToPointsInRace.get(riderId);
        i++;
      }

      raceIdsToRidersToMountainPoints.put(raceId,riderIdsToPointsInRace);

      return pointsOrderedByGenClass;
    } else {
      return new int[0];
    }
  }

  @Override
  public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null){ // checks race is in cycling portal.
      throw new IDNotRecognisedException("Race ID not recognised!");
    }
    HashMap<Integer,Integer> riderIdsToPointsInRace = new HashMap<Integer,Integer>();
    // Sum of points for each rider for the specified race.
    for (Stage stage : race.getStages()){ // iterate through stages.
      int stageId = stage.getId();
      if (!(this.getRidersMountainPointsInStage(stageId).length == 0)){
        HashMap<Integer,Integer> riderIdsToPointsInStage = stageIdsToRidersToMountainPoints.get(stageId);

        for (StageResult result : stage.getResults()) { // iterate through rider.
          int riderId = result.getRiderId();
          int points = 0;
          if ((riderIdsToPointsInStage != null) && !(riderIdsToPointsInStage.get(riderId) == null)) {
            points = riderIdsToPointsInStage.get(riderId);
          } else {
            points = 0;
          }
          if (riderIdsToPointsInRace.get(riderId) == null) { // if the rider is not registered with points add them.
            riderIdsToPointsInRace.put(riderId, points);
          } else {
            riderIdsToPointsInRace.merge(riderId, points, Integer::sum);
          }
        }
      }
    }
    if (!(riderIdsToPointsInRace.isEmpty())) {
      int[] genClassRanks = getRidersGeneralClassificationRank(raceId); // rider Ids sorted by time
      int[] pointsOrderedByGenClass = new int[genClassRanks.length];
      int i = 0;
      for (int riderId : genClassRanks) {
        pointsOrderedByGenClass[i] = riderIdsToPointsInRace.get(riderId);
        i++;
      }

      raceIdsToRidersToMountainPoints.put(raceId,riderIdsToPointsInRace);

      return pointsOrderedByGenClass;
    } else {
      return new int[0];
    }
  }


  @Override
  public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    // sort race result object then return the list of integers
    StagedRace race = raceIdsToRaces.get(raceId);
    ArrayList<RaceResult> raceResults = new ArrayList<>();
    for (Stage stage : race.getStages()){ // iterates through all stages in a race
      for (StageResult stageResult : stage.getResults()){ // iterates through all results in a stage
        int riderId = stageResult.getRiderId();
        boolean riderFound = false;
        for (RaceResult raceResult : raceResults){
          if (raceResult.getRiderId() == riderId){
            raceResult.setFinishTime(sumLocalTimes.addLocalTimes(raceResult.getFinishTime(),
                stageResult.getFinishTime())); // sums race results finish time with new stages finish time
            riderFound = true;
            break;
          }
        }
        if (!riderFound){ // if no race result for rider one is made.
          RaceResult raceResult = new RaceResult(stageResult.getRiderId(), raceId,
              stageResult.getFinishTime());
          raceResults.add(raceResult);
        }
      }
    }
    Collections.sort(raceResults); // orders race results by total time.
    System.out.println(raceResults.toString()+"raceresults");
    for (RaceResult result : raceResults){
      System.out.println(result.getRiderId()+" "+result.getFinishTime().toString());
    }
    int raceResultsSize = raceResults.size();
    int[] riderIdsOrderedByRank = new int[raceResultsSize];
    for (int i = 0; i < raceResultsSize; i++){
      riderIdsOrderedByRank[i] = raceResults.get(i).getRiderId();
    }
    return riderIdsOrderedByRank;
  }

  @Override
  public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults then return just the riders

    this.getRidersPointsInRace(raceId);
    ArrayList<Point> points = new ArrayList<>();
    HashMap<Integer,Integer> riderIdsToPoints = raceIdsToRidersToPoints.get(raceId);
    for (Map.Entry<Integer, Integer> idToPoints : riderIdsToPoints.entrySet()) {
      points.add(new Point(idToPoints.getKey(),idToPoints.getValue()));
    }
    Collections.sort(points);
    int[] riderIdsByPoints = new int[points.size()];
    int i = 0;
    for (Point point : points){
      riderIdsByPoints[i] = point.getRiderId();
      i++;
    }

    return riderIdsByPoints;
  }

  @Override
  public int[] getRidersMountainPointClassificationRank(int raceId)
      throws IDNotRecognisedException {
    // TODO from raceId get race then get competition.getFinalResults then return just the riders
    this.getRidersMountainPointsInRace(raceId);
    ArrayList<Point> points = new ArrayList<>();
    HashMap<Integer,Integer> riderIdsToMountainPoints = raceIdsToRidersToPoints.get(raceId);
    for (Map.Entry<Integer, Integer> idToPoints : riderIdsToMountainPoints.entrySet()) {
      points.add(new Point(idToPoints.getKey(),idToPoints.getValue()));
    }
    Collections.sort(points);
    int[] riderIdsByPoints = new int[points.size()];
    int i = 0;
    for (Point point : points){
      riderIdsByPoints[i] = point.getRiderId();
      i++;
    }

    return riderIdsByPoints;
  }

  public static void main(String[] args) throws IDNotRecognisedException, InvalidNameException, IllegalNameException,
      InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, DuplicatedResultException, InvalidCheckpointsException {

    CyclingPortal cycPort = new CyclingPortal();
    cycPort.createRace("Big boy race", "food fight race");
    cycPort.addStageToRace(0,"stage uno", "first 1",
        20.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addStageToRace(0,"stage duo", "2",
        12.0, LocalDateTime.now(), StageType.HIGH_MOUNTAIN);
    cycPort.addIntermediateSprintToStage(0, 1.0);
    cycPort.addIntermediateSprintToStage(0, 2.0);
    //cycPort.addCategorizedClimbToStage(0,6.0,SegmentType.C1,7.0,10.0);
    //cycPort.addCategorizedClimbToStage(0,6.0,SegmentType.HC, 8.0, 10.0);
    cycPort.createTeam("america","wont invade ukraine");
    cycPort.createRider(0,"Ken",1608);
    cycPort.createRider(0,"HOG RIDER",2015);
    cycPort.createRider(0,"Kenith",1608);
    cycPort.createRider(0,"HOG RIDER 2",2015);
    cycPort.createRider(0,"Kenny",1608);
    cycPort.createRider(0,"HOG RIER",2015);
    cycPort.createRider(0,"Ken2",1608);
    cycPort.createRider(0,"HOG 2RIDER",2015);
    cycPort.createRider(0,"Keni2th",1608);
    cycPort.createRider(0,"HOG R3IDER 2",2015);
    cycPort.createRider(0,"Kenn3y",1608);
    cycPort.createRider(0,"HOG 3RIER",2015);
    cycPort.createRider(0,"Ke4n",1608);
    cycPort.createRider(0,"HO4G RIDER",2015);
    cycPort.createRider(0,"Ken4ith",1608);
    cycPort.createRider(0,"HOG 5RIDER 2",2015);
    cycPort.createRider(0,"Kenny5",1608);
    cycPort.createRider(0,"HOG RI5ER",2015);
    cycPort.concludeStagePreparation(0);
    cycPort.concludeStagePreparation(1);
    LocalTime t0 = LocalTime.of(0,0,0);
    LocalTime t1 = LocalTime.of(0,0,10);
    LocalTime t2 = LocalTime.of(0,0,20);
    LocalTime t3 = LocalTime.of(0,0,30);
    LocalTime t4 = LocalTime.of(0,0,40);
    LocalTime t5 = LocalTime.of(0,0,50);
    LocalTime t6 = LocalTime.of(0,0,59);
    LocalTime t7 = LocalTime.of(0,1,0);
    LocalTime t8 = LocalTime.of(0,1,10);
    LocalTime t9 = LocalTime.of(0,1,20);
    LocalTime t10 = LocalTime.of(0,1,30);
    LocalTime t11 = LocalTime.of(0,1,40);
    LocalTime t12 = LocalTime.of(0,1,50);
    LocalTime t13 = LocalTime.of(0,1,59);
    LocalTime t14 = LocalTime.of(0,2,0);
    LocalTime t15 = LocalTime.of(0,2,10);
    LocalTime t16 = LocalTime.of(0,2,20);
    LocalTime t17 = LocalTime.of(0,2,30);
    LocalTime t18 = LocalTime.of(0,2,40);
    LocalTime t19 = LocalTime.of(0,2,50);
    LocalTime t20 = LocalTime.of(0,3,0);
    LocalTime t21 = LocalTime.of(0,3,10);
    cycPort.registerRiderResultsInStage(0,0, t0, t1,t2, t19);
    cycPort.registerRiderResultsInStage(0,1, t0, t2,t3, t20);
    cycPort.registerRiderResultsInStage(0,2, t0, t3,t4, t20);
    cycPort.registerRiderResultsInStage(0,3, t0, t4,t5, t20);
    cycPort.registerRiderResultsInStage(0,4, t0, t5, t6,t21);
    cycPort.registerRiderResultsInStage(0,5, t0, t6,t7, t21);
    cycPort.registerRiderResultsInStage(0,6, t0, t7, t8,t21);
    cycPort.registerRiderResultsInStage(0,7, t0, t8,t9, t21);
    cycPort.registerRiderResultsInStage(0,8, t0, t9,t10, t21);


    // stage 1: rider 1 ,2 , 0 ,segments 0 , 1, 2
    cycPort.registerRiderResultsInStage(1,0, t0, t1);
    cycPort.registerRiderResultsInStage(1,1, t0, t2);
    cycPort.registerRiderResultsInStage(1,2, t0, t3);
    cycPort.registerRiderResultsInStage(1,3, t0, t4);
    cycPort.registerRiderResultsInStage(1,4, t0, t5);
    cycPort.registerRiderResultsInStage(1,5, t0, t6);
    cycPort.registerRiderResultsInStage(1,6, t0, t7);
    cycPort.registerRiderResultsInStage(1,7, t0, t8);
    cycPort.registerRiderResultsInStage(1,8, t0, t9);
    cycPort.registerRiderResultsInStage(1,9, t0, t10);
    cycPort.registerRiderResultsInStage(1,10, t0, t11);
    cycPort.registerRiderResultsInStage(1,11, t0, t12);
    cycPort.registerRiderResultsInStage(1,12, t0, t13);
    cycPort.registerRiderResultsInStage(1,13, t0, t14);
    cycPort.registerRiderResultsInStage(1,14, t0, t15);
    cycPort.registerRiderResultsInStage(1,15, t0, t16);
    cycPort.registerRiderResultsInStage(1,16, t0, t17);
    cycPort.registerRiderResultsInStage(1,17, t0, t1);
    // stage 2:  0, 1 ,2

    System.out.println("Mountain: " + Arrays.toString(cycPort.getRidersMountainPointsInStage(0)));
    System.out.println("Points: " + Arrays.toString(cycPort.getRidersPointsInStage(0)));
    //System.out.println(Arrays.toString(cycPort.getRidersGeneralClassificationRank(0))+" ranks gen class");
    //System.out.println(Arrays.toString(cycPort.getRidersPointsInRace(0))+" points by gen class");
    //System.out.println(Arrays.toString(cycPort.getRankedAdjustedElapsedTimesInStage(0)));
    //cycPort.getRidersPointsInRace(0);
  }
}