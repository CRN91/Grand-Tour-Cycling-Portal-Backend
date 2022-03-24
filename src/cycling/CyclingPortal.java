package src.cycling;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
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
    // Trim leading and trailing whitespace and check if the name is blank, is more than 30
    // characters in length, or is null.
    name = name.trim();
    if (name.equals("") || name.length() > 30 || name == null) {
      throw new InvalidNameException("Race name '" + name + "' is invalid!");
    }
    // Search hashmap of races for one with the given name and throw exception if found.
    for (StagedRace stagedRace : raceIdsToRaces.values()) {
      String stagedRaceName = stagedRace.getName();
      if (stagedRaceName.equals(name)) {
        throw new IllegalNameException("There is already a race with the name '" + name + "'!");
      }
    }

    // For assertion.
    int noOfRaces = raceIdsToRaces.size();

    // Race doesn't already exist.
    StagedRace raceToAdd = new StagedRace(name, description);
    int raceToAddId = raceToAdd.getId();
    raceIdsToRaces.put(raceToAddId, raceToAdd);
    assert raceIdsToRaces.size() == noOfRaces + 1 : "Race not added to hashmap!";

    return raceToAdd.getId();
  }

  @Override
  public String viewRaceDetails(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }

    // Calculate total race length.
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
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }

    // Remove race result objects.
    for (RiderRaceResult result : race.getResults()) {
      race.getResults().remove(result);
      assert !race.getResults().contains(result) : "Race result not removed!";
    }

    // Remove stages of the race
    for (Stage stage : race.getStages()) {
      race.getStages().remove(stage);
      assert !race.getStages().contains(stage) : "Stage not removed!";
      try {
        removeStageById(stage.getId());
      } catch (IDNotRecognisedException ex) {
        System.out.println("Stage waiting for results and cannot be deleted!");
      }
    }

    // Remove race results

    raceIdsToRaces.remove(raceId);
    assert raceIdsToRaces.get(raceId) == null : "Race not successfully removed!";
  }

  @Override
  public int getNumberOfStages(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    // Does the race exist?
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }

    return race.getStages().size();
  }

  @Override
  public int addStageToRace(int raceId, String name, String description, double length,
      LocalDateTime startTime, StageType type)
      throws IDNotRecognisedException, IllegalNameException, InvalidNameException,
      InvalidLengthException {
    // Check the race exists.
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    // Trim leading and trailing whitespace and check if the name is blank, is more than 30
    // characters in length, or is null.
    name = name.trim();
    if (name.equals("") || name.length() > 30 || name == null) {
      throw new InvalidNameException("Name '" + name + "' is greater than 30 characters!");
    }

    // Check the length is >=5km.
    if (length < 5.0) {
      throw new InvalidLengthException("Length of stage must be greater than or equal to 5km!");
    }

    // For assertion.
    int amountOfStages = race.getStages().size();

    // Create stage and add to hashmap for easier access.
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
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }

    // Sorts stages by their starting time to get the order.
    ArrayList<Stage> stages = race.getStages();
    Collections.sort(stages);

    // Iterates though stages in race and gets their IDs.
    int[] stageIds = new int[stages.size()];
    int i = 0;
    for (Stage stage : stages) {
      stageIds[i] = stage.getId();
      i++;
    }

    assert race.getStages().size() == stageIds.length : "Incorrect amount of stage IDs!";

    return stageIds;
  }

  @Override
  public double getStageLength(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    return stage.getLength();
  }

  @Override
  public void removeStageById(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    // Remove stage results
    for (RiderStageResult result : stage.getResults()) {
      stage.getResults().remove(result);
      assert !stage.getResults().contains(result) : "Stage result not removed!";
    }
    // Remove segments
    for (Segment segment : stage.getSegmentsInStage()) {
      try {
        removeSegment(segment.getId());
      } catch (InvalidStageStateException ex) {
        System.out.println("Segment waiting for results!");
      }
    }

    // Get the race object that contains it.
    StagedRace race = raceIdsToRaces.get(stage.getRaceId());
    race.getStages().remove(stage);
    assert !race.getStages().contains(stage) : "Stage not removed from race!";

    Boolean foundId = false;
    for (Integer stgId : stageIdsToStages.keySet()) {
      if (stgId == stageId) {
        stageIdsToStages.remove(stgId);
        assert stageIdsToStages.get(stgId) == null : "Stage not removed!";
        foundId = true;
        break;
      }
    }
    if (!foundId) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
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
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    // Is the location valid?
    if ((location >= stage.getLength()) || (location <= 0)) {
      throw new InvalidLocationException("Invalid location " + location + "!");
    }
    // Is the stage state "under development"?
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

    // Creates new categorised climb and adds it to the stage.
    Segment categorisedClimb = new CategorisedClimb(stageId, type, averageGradient,
        length, location);
    int ccId = categorisedClimb.getId();
    segmentIdsToSegments.put(ccId, categorisedClimb);
    assert segmentIdsToSegments.get(ccId) != null : "Categorised climb not added to hashmap!";
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
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    // Is the location valid?
    if ((location >= stage.getLength()) || (location <= 0)) {
      throw new InvalidLocationException("Invalid location " + location + " !");
    }
    // Is the stage type valid?
    if (stage.getStageType() == StageType.TT) {
      throw new InvalidStageTypeException("Cannot add segments to a time trial stage.");
    }
    // Is the stage state "under development"?
    if (!stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("Stage is waiting for results!");
    }

    // Creates a nwe intermediate sprint and adds it to the stage.
    Segment intermediateSprint = new Segment(stageId, SegmentType.SPRINT, location);
    int intSprintId = intermediateSprint.getId();
    segmentIdsToSegments.put(intSprintId, intermediateSprint);
    assert segmentIdsToSegments.get(intSprintId) != null : "Intermediate sprint not added to"
        + "hashmap!";
    stage.addSegment(intermediateSprint);

    return intermediateSprint.getId();
  }

  @Override
  public void removeSegment(int segmentId) throws IDNotRecognisedException,
      InvalidStageStateException {
    // Does the segment exist?
    if (segmentIdsToSegments.get(segmentId) == null) {
      throw new IDNotRecognisedException("Segment ID " + segmentId + " is not recognised!");
    }
    Segment segment = segmentIdsToSegments.get(segmentId);
    int segmentStageId = segment.getStageId();
    // Can I do this (the stage has to be under development).
    // Find the stage.
    for (Map.Entry<Integer, Stage> stageEntry : stageIdsToStages.entrySet()) {
      Stage stage = stageEntry.getValue();
      if (stage.getId() == segmentStageId) {
        // Remove segment results
        for (RiderSegmentResult result : segment.getResults()) {
          segment.getResults().remove(result);
          assert !segment.getResults().contains(result) : "Result not removed from segment!";
        }
        // This is the right stage, now check we can delete segments from it.
        if (stage.getUnderDevelopment()) {
          segmentIdsToSegments.remove(segmentId);
          assert segmentIdsToSegments.get(segmentId) == null : "Segment not removed from hashmap!";
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
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    // Is it already waiting for results?
    Stage stage = stageIdsToStages.get(stageId);
    if (!stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("Stage " + stageId
          + " is already waiting for results!");
    } else {
      stage.setUnderDevelopment(false);
    }
    assert stage.getUnderDevelopment() == false : "Stage preparations not concluded!";
  }

  @Override
  public int[] getStageSegments(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    // Iterates through the segments in the stage and gets their IDs.
    ArrayList<Segment> segmentsInStage = stage.getSegmentsInStage();
    int[] segmentIdsInStage = new int[segmentsInStage.size()];
    Collections.sort(segmentsInStage);
    int i = 0;
    for (Segment segment : segmentsInStage) {
      segmentIdsInStage[i] = segment.getId();
      i++;
    }
    assert segmentsInStage.size() == segmentIdsInStage.length : "Segment IDs not gathered"
        + "correctly!";
    return segmentIdsInStage;
  }

  @Override
  public int createTeam(String name, String description) throws IllegalNameException,
      InvalidNameException {
    // Trim leading and trailing whitespace and check if the name is blank, is more than 30
    // characters in length, or is null.
    name = name.trim();
    if (name == null || name == "" || name.length() > 30) {
      throw new InvalidNameException("Invalid name of a team!");
    }
    for (Team team : teamIdsToTeams.values()) {
      if (team.getName() == name) {
        throw new IllegalNameException("Team name already in use!");
      }
    }
    // Creates a new team and then stores it in a hashmap for easier access.
    Team newTeam = new Team(name, description);
    int newTeamId = newTeam.getId();
    teamIdsToTeams.put(newTeamId, newTeam);
    assert teamIdsToTeams.get(newTeamId) != null : "Team not added to hashmap!";
    return newTeam.getId();
  }

  @Override
  public void removeTeam(int teamId) throws IDNotRecognisedException {
    Team team = teamIdsToTeams.get(teamId);
    if (team == null) {
      throw new IDNotRecognisedException("Team ID " + teamId + " is not recognised!");
    }
    teamIdsToTeams.remove(teamId);
    assert teamIdsToTeams.get(teamId) == null : "Team not removed from hashmap!";
  }

  @Override
  public int[] getTeams() {
    // Iterates through the IDs of teams from the hashmap and returns it as an integer array.
    Set<Integer> teamIdsSet = teamIdsToTeams.keySet();
    int[] teamIds = new int[teamIdsSet.size()];
    int index = 0;
    for (Integer i : teamIdsSet) {
      teamIds[index++] = i;
    }
    assert teamIdsToTeams.size() == teamIds.length : "Team IDs not gathered correctly!";

    return teamIds;
  }

  @Override
  public int[] getTeamRiders(int teamId) throws IDNotRecognisedException {
    ArrayList<Integer> riderIdArrayList = new ArrayList<>();

    // Check if the team exists.
    if (teamIdsToTeams.get(teamId) == null) {
      throw new IDNotRecognisedException("Team ID " + teamId + " is not recognised!");
    }

    // Look for all riders in this team.
    for (Map.Entry<Integer, Rider> idToRider : riderIdsToRiders.entrySet()) {
      Integer riderId = idToRider.getKey();
      Rider rider = idToRider.getValue();
      if (rider.getTeamId() == teamId) {
        // Found one; add to list.
        riderIdArrayList.add(riderId);
      }
    }

    // Convert the ArrayList to int[].
    return riderIdArrayList.stream().mapToInt(i -> i).toArray();
  }

  @Override
  public int createRider(int teamID, String name, int yearOfBirth)
      throws IDNotRecognisedException, IllegalArgumentException {
    name = name.trim();
    // Check if the team exists.
    Team team = teamIdsToTeams.get(teamID);
    if (team == null) {
      throw new IDNotRecognisedException("Team ID " + teamID + " is not recognised!");
    }
    // Trim leading and trailing whitespace and check if the name is blank, is more than 30
    // characters in length, or is null.
    if (name == null || name == "" || name.length() > 30) {
      throw new IllegalArgumentException("Invalid name of a team!");
    }

    // Check YOB > 1900.
    if (yearOfBirth < 1900) {
      throw new IllegalArgumentException("Invalid year of birth!");
    }

    // Creates a new rider and adds it to the hashmap for easier access.
    Rider newRider = new Rider(name, teamID, yearOfBirth);
    int newRiderId = newRider.getId();
    riderIdsToRiders.put(newRiderId, newRider);
    assert riderIdsToRiders.get(newRiderId) != null : "Rider not added to hashmap!";
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
        assert riders.get(riderId) == null : "Rider not successfully removed from their team!";
        break;
      } catch (NullPointerException ex) { } // Errors thrown for each team until ID is found.
    }
    if (!hasBeenFound) {
      throw new IDNotRecognisedException("Rider ID " + riderId + " is not recognised!");
    }

    riderIdsToRiders.remove(riderId);
    assert riderIdsToRiders.get(riderId) == null : "Rider not removed from hashmap!";

    // Results.
    boolean foundRiderInRace = false;
    for (StagedRace race : raceIdsToRaces.values()) {
      ArrayList<RiderRaceResult> raceResults = race.getResults();
      // Destroy references to this rider in races.
      for (RiderRaceResult raceResult : raceResults) {
        if (raceResult.getRiderId() == riderId) {
          foundRiderInRace = true;
          raceResults.remove(raceResult);
          assert !raceResults.contains(raceResult) : "Rider's race result not removed!";

          // Destroy references to this rider in stages.
          for (Stage stage : race.getStages()) {
            ArrayList<RiderStageResult> stageResults = stage.getResults();

            for (RiderStageResult stageResult : stageResults) {
              if (stageResult.getRiderId() == riderId) {
                int stageResultsLength = stageResults.size();
                stageResults.remove(stageResult);
                assert stageResults.size() == stageResultsLength - 1 : "Rider's stage result not"
                    + "successfully removed!";

                // Destroy references to this rider in segments
                for (Segment segment : stage.getSegmentsInStage()) {
                  ArrayList<RiderSegmentResult> segmentResults = segment.getResults();

                  for (RiderSegmentResult segmentResult : segmentResults) {
                    if (segmentResult.getRiderId() == riderId) {
                      int segmentResultsLength = segmentResults.size();
                      segmentResults.remove(segmentResult);
                      assert segmentResults.size() == segmentResultsLength - 1 : "Rider's segment"
                          + "result not successfully removed!";
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

    // Error checking.
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    Rider rider = riderIdsToRiders.get(riderId);
    if (rider == null) {
      throw new IDNotRecognisedException("Rider ID " + riderId + " is not recognised!");
    }

    if (stage.getUnderDevelopment()) {
      throw new InvalidStageStateException("The stage is under development so can't add rider"
          + "results!");
    }
    // Get the location data for each segment, order it, sync it to times and store it.
    // Check whether the result has already been registered for this rider in this stage.
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

    // Check whether checkpoints are in chronological order.
    LocalTime previousTime = LocalTime.of(0, 0, 0);
    for (LocalTime time : checkpoints) {
      if (time.compareTo(previousTime) < 0) {
        throw new InvalidCheckpointsException("Checkpoint times are not in chronological order!");
      }
      previousTime = time;
    }

    int stageResultsLength = stage.getResults().size();
    stage.addRiderResults(riderId, checkpoints);
    assert stage.getResults().size() == stageResultsLength + 1 : "Rider not added to stage!";
  }

  @Override
  public LocalTime[] getRiderResultsInStage(int stageId, int riderId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    // If the rider doesn't exist / doesn't have a result in this stage.
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
      throw new IDNotRecognisedException("Rider ID " + riderId + " is not recognised!");
    }
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    // Populate the RiderStageResults objects with their adjusted elapsed times.
    stage.generateAdjustedFinishTimes();

    for (RiderStageResult result : stage.getResults()) {
      if (result.getRiderId() == riderId) {
        return result.getAdjustedFinishTime();
      }
    }

    // Returns null if no result for the rider is found.
    return null;
  }

  @Override
  public void deleteRiderResultsInStage(int stageId, int riderId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    // Does stage exist?
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    // Does rider exist?
    if (riderIdsToRiders.get(riderId) == null) {
      throw new IDNotRecognisedException("Rider ID " + riderId + " is not recognised!");
    }

    int stageResultsLength = stage.getResults().size();
    stage.removeResultByRiderId(riderId);
    assert stage.getResults().size() == stageResultsLength - 1 : "Rider not removed from stage!";
  }

  @Override
  public int[] getRidersRankInStage(int stageId) throws IDNotRecognisedException {
    // Does stage exist?
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) {
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    ArrayList<Integer> riderIdsList = new ArrayList<>();
    ArrayList<RiderStageResult> results = stage.getResults();

    // Sort results by finish time.
    Collections.sort(results);

    // Fill the IDs list with the rider ids which are now in rank order.
    for (RiderStageResult result : results) {
      riderIdsList.add(result.getRiderId());
    }

    // Convert the ArrayList to int[].
    return riderIdsList.stream().mapToInt(i -> i).toArray();
  }

  @Override
  public LocalTime[] getRankedAdjustedElapsedTimesInStage(int stageId)
      throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) { // Checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    // Creates the adjusted results and adds the attribute to the result object.
    // Also orders results by finishing time (rank).
    stage.generateAdjustedFinishTimes();

    // Iterates through stage results and gets their adjusted time in rank order.
    ArrayList<RiderStageResult> results = stage.getResults();
    LocalTime[] rankAdjustedElapsedTimesInStage = new LocalTime[results.size()];
    int i = 0;
    for (RiderStageResult result : stage.getResults()) {
      rankAdjustedElapsedTimesInStage[i] = result.getAdjustedFinishTime();
      i++;
    }
    return rankAdjustedElapsedTimesInStage;
  }

  @Override
  public int[] getRidersPointsInStage(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) { // Checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }

    // Generates the points in the stage and returns rider IDs ordered by points.
    return stage.generatePointsInStage(false);
  }

  @Override
  public int[] getRidersMountainPointsInStage(int stageId) throws IDNotRecognisedException {
    Stage stage = stageIdsToStages.get(stageId);
    if (stage == null) { // Checks race is in cycling portal.
      throw new IDNotRecognisedException("Stage ID " + stageId + " is not recognised!");
    }
    if ((stage.getStageType() == StageType.TT) || (stage.getSegmentsInStage().size() == 0)) {
      // No mountain points (no segments).
      return new int[0];
    }

    // Check if there even are any climbs.
    boolean climbFound = false;
    for (Segment segment : stage.getSegmentsInStage()) {
      if (segment.getSegmentType() != SegmentType.SPRINT) {
        climbFound = true;
      }
    }
    // No climbs so there will be no results.
    if (!climbFound) {
      return new int[0];
    }
    // Generates the mountain points in the stage and returns rider IDs ordered by  mountain
    // points.
    return stage.generatePointsInStage(true);
  }

  @Override
  public void eraseCyclingPortal() {
    // Reset all internal counters.
    Rider.resetIdCounter();
    Team.resetIdCounter();
    Stage.resetIdCounter();
    StagedRace.resetIdCounter();
    Segment.resetIdCounter();
    CategorisedClimb.resetIdCounter();
    // Erase all references and get them garbage collected.
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
    // Find the named race's ID.
    for (Map.Entry<Integer, StagedRace> stagedRaceEntry : raceIdsToRaces.entrySet()) {
      int raceId = stagedRaceEntry.getKey();
      StagedRace stagedRace = stagedRaceEntry.getValue();

      // Remove race result objects.
      for (RiderRaceResult result : stagedRace.getResults()) {
        stagedRace.getResults().remove(result);
        assert !stagedRace.getResults().contains(result) : "Race result not removed!";
      }

      // Remove stages of the race.
      for (Stage stage : stagedRace.getStages()) {
        stagedRace.getStages().remove(stage);
        assert !stagedRace.getStages().contains(stage) : "Stage not removed!";
        try {
          removeStageById(stage.getId());
        } catch (IDNotRecognisedException ex) {
          System.out.println("Stage waiting for results and cannot be deleted!");
        }
      }

      // Remove the race object.
      if (stagedRace.getName().equals(name)) {
        // Found it.
        foundRace = true;
        raceIdsToRaces.remove(raceId);
        assert raceIdsToRaces.get(raceId) == null : "Race not removed from hashmap!";
      }
    }
    // Gone through the whole hashmap and still haven't found it.
    if (!foundRace) {
      throw new NameNotRecognisedException("No race found with name '" + name + "'!");
    }
  }

  @Override
  public LocalTime[] getGeneralClassificationTimesInRace(int raceId)
      throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    // Calculates the finish times for riders in the race.
    race.generateRidersRaceAdjustedElapsedTimes();
    ArrayList<RiderRaceResult> raceResults = race.getResults();

    // Iterates through race results and adds finish time to an array.
    LocalTime[] finishTimes = new LocalTime[raceResults.size()];
    int i = 0;
    for (RiderRaceResult result : raceResults) {
      finishTimes[i] = result.getElapsedTime();
      i++;
    }
    assert raceResults.size() == finishTimes.length : "Finish times gathered incorrectly!";
    return finishTimes;
  }

  @Override
  public int[] getRidersPointsInRace(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) { // Error check.
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    // Calculate points and returns an array of points sorted by descending order.
    return race.generateRidersPointsInRace(false);
  }

  @Override
  public int[] getRidersMountainPointsInRace(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) { // Error check.
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    // Calculate mountain points and returns an array of mountain points sorted by descending order.
    return race.generateRidersPointsInRace(true);
  }

  @Override
  public int[] getRidersGeneralClassificationRank(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId); // Gets the race.
    if (race == null) { // Error check.
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    race.generateRidersRaceAdjustedElapsedTimes(); // Returns race results also stores in race result list.
    ArrayList<RiderRaceResult> raceResults = race.getResults(); // Gets race result arraylist.

    // Iterates through race results to fill an array with rider IDs sorted by their rank.
    int raceResultsSize = raceResults.size();
    int[] riderIdsOrderedByRank = new int[raceResultsSize];
    assert raceResultsSize == riderIdsOrderedByRank.length : "Integer array created incorrectly!";
    for (int i = 0; i < raceResultsSize; i++) {
      riderIdsOrderedByRank[i] = raceResults.get(i).getRiderId();
    }

    return riderIdsOrderedByRank;
  }

  @Override
  public int[] getRidersPointClassificationRank(int raceId) throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    // Calculate points in race.
    race.generateRidersPointsInRace(false);

    return race.getRiderIdsOrderedByPoints(false);
  }

  @Override
  public int[] getRidersMountainPointClassificationRank(int raceId)
      throws IDNotRecognisedException {
    StagedRace race = raceIdsToRaces.get(raceId);
    if (race == null) {
      throw new IDNotRecognisedException("Race ID " + raceId + " is not recognised!");
    }
    // Calculate mountain points in race.
    race.generateRidersPointsInRace(true);


    return race.getRiderIdsOrderedByPoints(true);
  }
}