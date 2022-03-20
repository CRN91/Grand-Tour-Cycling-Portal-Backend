package src;

import java.io.*;
import src.cycling.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

/**
 * A short program to illustrate an app testing some minimal functionality of a
 * concrete implementation of the CyclingPortalInterface interface -- note you
 * will want to increase these checks, and run it on your CyclingPortal class
 * (not the BadCyclingPortal class).
 *
 * 
 * @author Diogo Pacheco
 * @version 1.0
 */
public class CyclingPortalInterfaceTestApp {

  public static void testCreateRace() throws InvalidNameException, IllegalNameException {
    CyclingPortal cycPort = new CyclingPortal();
    // Name not blank
    cycPort.createRace("Race 1", "aaaa");
    assert cycPort.getRaceIds().length == 1 : "Race not created!";

    // Name blank
    try {
      cycPort.createRace("", null);
    } catch (InvalidNameException ex) {
      System.out.println("Blank name successfully failed");
    }

    // Valid name with whitespace
    cycPort.createRace("    Whitespace    ", null);
    System.out.println(cycPort.getRaceIdsToRaces().get(1).getName());
    assert cycPort.getRaceIdsToRaces().get(1).getName().length() == 10 : "Whitespace not trimmed!";

    // Name just multiple whitespace
    try {
      cycPort.createRace("      ", null);
    } catch (InvalidNameException ex) {
      System.out.println("Whitespace name successfully failed");
    }

    // Name taken
    try {
      cycPort.createRace("Race 1", null);
    } catch (IllegalNameException ex) {
      System.out.println("Taken name successfully failed");
    }
  }

  public static void testRemoveRaceByName() throws InvalidNameException, IllegalNameException, NameNotRecognisedException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // No races exist
    try {
      cycPort.removeRaceByName("asdsa");
    } catch (NameNotRecognisedException ex) {
      System.out.println("No races successfully failed");
    }

    // Remove a race that exists
    cycPort.createRace("Race 1", "Race Description");
    cycPort.removeRaceByName("Race 1");
    assert cycPort.getRaceIds().length == 0 : "Race not removed!";

    // Race doesn't exist
    try {
      cycPort.removeRaceByName("ASDASDASDASD");
    } catch (NameNotRecognisedException ex) {
      System.out.println("Name not found successfully failed");
    }

    // Blank input

    try {
      cycPort.removeRaceByName("");
    } catch (NameNotRecognisedException ex) {
      System.out.println("Blank name successfully failed");
    }

    // Unicode test
    cycPort.createRace("女", null);
    cycPort.removeRaceByName("女");
    assert cycPort.getRaceIds().length == 0 : "Unicode race not removed!";
  }

  public static void testCreateTeam() throws InvalidNameException, IllegalNameException {
    CyclingPortal cycPort = new CyclingPortal();
    // Team name not taken
    cycPort.createTeam("Team 1", "asedasda");
    assert cycPort.getTeams().length == 1 : "Team not created";

    // Team name taken
    try {
      cycPort.createTeam("Team 1", null);
    } catch (IllegalNameException ex) {
      System.out.println("Taken name fail");
    }

    // Name blank
    try {
      cycPort.createTeam("", null);
    } catch (InvalidNameException ex) {
      System.out.println("Blank name fail");
    }

    // Valid name with whitespace
    cycPort.createTeam("    Whitespace    ", null);
    assert cycPort.getTeamIdsToTeams().get(1).getName().length() == 10 : "Whitespace not trimmed!";

    // Name multiple whitespace
    try {
      cycPort.createTeam("    ", null);
    } catch (InvalidNameException ex) {
      System.out.println("Whitespace name fail");
    }
  }

  public static void testCreateRider() throws IDNotRecognisedException, InvalidNameException, IllegalNameException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Team doesn't exist
    try {
      cycPort.createRider(0, "Alan", 1995);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Team doesnt exists failed successfully");
    }

    //Team Exists
    cycPort.createTeam("Alans team", null);
    cycPort.createRider(0, "Alan", 1995);
    assert cycPort.getRiderIdsToRiders().get(0).getName() == "Alan";

    // name blank
    try {
      cycPort.createRider(0, "", 1995);
    } catch (IllegalArgumentException ex) {
      System.out.println("Blank name failed successfully");
    }

    //name valid whitespace
    cycPort.createRider(0, "    Alan's uncle     ", 1995);
    assert cycPort.getRiderIdsToRiders().get(1).getName().length() == 12;

    // name invalid whitespace
    try {
      cycPort.createRider(0, "        ", 1995);
    } catch (IllegalArgumentException ex) {
      System.out.println("Invalid whitespace dfailed successfully");
    }

    // yOB = 1900
    cycPort.createRider(0, "OldAlan", 1900);
    assert cycPort.getRiderIdsToRiders().get(2).getName() == "OldAlan";

    //YOB < 1900
    try {
      cycPort.createRider(0, "ZombieAlan", 1600);
    } catch (IllegalArgumentException ex) {
      System.out.println("YOB < 1900 failed successfully");
    }
  }

  public static void testAddStageToRace() throws InvalidNameException, IllegalNameException, IDNotRecognisedException, InvalidLengthException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();


    // invalid race ID
    try {
      cycPort.addStageToRace(0,"name","jeff",10.0, LocalDateTime.now(), StageType.TT);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Invalid race Id FAILED SUCCESSFULLY");
    }

    // Valid race ID
    cycPort.createRace("alansrace",null);
    cycPort.addStageToRace(0,"alanismyname","jeff",10.0, LocalDateTime.now(), StageType.TT);
    assert  cycPort.getStageIdsToStages().get(0).getName() == "alanismyname";

    // length > 5km
    cycPort.addStageToRace(0,"2","jeff",10.0, LocalDateTime.now(), StageType.HIGH_MOUNTAIN);
    assert cycPort.getStageIdsToStages().get(1).getLength() > 5.0;

    // length = 5km
    cycPort.addStageToRace(0,"2","jeff",5.0, LocalDateTime.now(), StageType.MEDIUM_MOUNTAIN);
    assert cycPort.getStageIdsToStages().get(2).getLength() == 5.0;


    // length < 5km
    try {
      cycPort.addStageToRace(0,"2","jeff",1.0, LocalDateTime.now(), StageType.FLAT);
    } catch (InvalidLengthException ex) {
      System.out.println("Lenght invalid failed good");
    }

  }

  public static void testConcludeStagePreparations() throws InvalidNameException, IllegalNameException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();
    cycPort.createRace("Race 1", null);
    cycPort.addStageToRace(0, "Stage 1", "Desc", 10.0, LocalDateTime.now(), StageType.FLAT);

    // Stage exists
    cycPort.concludeStagePreparation(0);
    assert !cycPort.getStageIdsToStages().get(0).getUnderDevelopment();

    // Stage does not exist
    try {
      cycPort.concludeStagePreparation(69420);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Stage does not exist fail");
    }

    // Already waiting for results
    try {
      cycPort.concludeStagePreparation(0);
    } catch (InvalidStageStateException ex) {
      System.out.println("Already waiting for results fail");
    }
  }

  public static void testRemoveTeam() throws InvalidNameException, IllegalNameException, IDNotRecognisedException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Team does not exist
    try {
      cycPort.removeTeam(69420);
    } catch (IDNotRecognisedException ex) {
      System.out.println("remove team good");
    }

    // Team exists
    cycPort.createTeam("Team", null);
    cycPort.removeTeam(0);
    assert cycPort.getTeams().length == 0;
  }

  public static void testRemoveRaceById() throws InvalidNameException, IllegalNameException, NameNotRecognisedException, IDNotRecognisedException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // No races exist
    try {
      cycPort.removeRaceById(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("No races successfully failed");
    }

    // Remove a race that exists
    cycPort.createRace("Race 1", "Race Description");
    cycPort.removeRaceById(0);
    assert cycPort.getRaceIds().length == 0 : "Race not removed!";

    // Race doesn't exist
    try {
      cycPort.removeRaceById(8);
    } catch (IDNotRecognisedException ex) {
      System.out.println("RACE NO EXIST successfully failed");
    }
  }

  public static void testRemoveRider() throws IDNotRecognisedException, InvalidNameException, IllegalNameException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Rider no exist
    try {
      cycPort.removeRider(69420);
    }catch (IDNotRecognisedException ex) {
      System.out.println("BAD ID failed successfully");
    }

    //Rider exist
    cycPort.createTeam("AlanTeam",null);
    cycPort.createRider(0,"alan",1901);
    cycPort.removeRider(0);
    assert cycPort.getRiderIdsToRiders().get(0) == null;
  }

  public static void testGetTeamRiders() throws InvalidNameException, IllegalNameException, IDNotRecognisedException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // team doesn't exist
    try {
      cycPort.getTeamRiders(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Team no exist fail");
    }

    // Team has no riders
    cycPort.createTeam("team", null);
    assert cycPort.getTeamRiders(0).length == 0;

    // Team has riders
    cycPort.createRider(0, "alan", 1901);
    assert cycPort.getTeamRiders(0).length == 1;
  }

  public static void testGetTeams() throws InvalidNameException, IllegalNameException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // There are no teams
    assert cycPort.getTeams().length == 0;

    // There are teams
    cycPort.createTeam("name", null);
    assert cycPort.getTeams().length == 1;
  }

  public static void testGetRaceIds() throws InvalidNameException, IllegalNameException {
    //Setup
    CyclingPortal cycPort = new CyclingPortal();

    //no race
    assert cycPort.getRaceIds().length == 0;

    //race yes
    cycPort.createRace("Alanracesequel",null);
    assert cycPort.getRaceIds().length == 1;
  }

  public static void testGetNumberOfStages() throws IDNotRecognisedException, InvalidNameException, IllegalNameException, InvalidLengthException {
    //Setup
    CyclingPortal cycPort = new CyclingPortal();

    //race no exist
    try{
      cycPort.getNumberOfStages(69420);
    }catch (IDNotRecognisedException ex) {
      System.out.println("Race not exist failed successfully");
    }

    //race exist
    cycPort.createRace("alan",null);
    assert cycPort.getNumberOfStages(0) == 0;

    //race stage
    cycPort.addStageToRace(0,"name",null,10.0,LocalDateTime.now(),StageType.FLAT);
    assert cycPort.getNumberOfStages(0) == 1;
  }

  public static void testViewRaceDetails() throws IDNotRecognisedException, InvalidNameException, IllegalNameException, InvalidLengthException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Race no exist
    try {
      cycPort.viewRaceDetails(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Raace not exist fail successfully");
    }

    // There are no stages
    cycPort.createRace("name", null);
    System.out.println(cycPort.viewRaceDetails(0));

    // There are stages
    cycPort.addStageToRace(0, "name", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    System.out.println(cycPort.viewRaceDetails(0));
  }

  public static void testRemoveStageById() throws InvalidNameException, IllegalNameException, NameNotRecognisedException, IDNotRecognisedException, InvalidLengthException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // No race exist
    try {
      cycPort.removeStageById(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("No races successfully failed");
    }

    // No stage exists
    cycPort.createRace("Race 1", "Race Description");
    try {
      cycPort.removeStageById(69420);
    } catch (IDNotRecognisedException ex) {
      System.out.println("No stage failed successfully");
    }

    // Remove a race that exists
    cycPort.addStageToRace(0,"alanstage",null,5.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.removeStageById(0);
    assert cycPort.getRaceStages(0).length == 0 : "Race not removed!";


  }

  public static void testGetStageLength() throws InvalidNameException, IDNotRecognisedException, InvalidLengthException, IllegalNameException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // stage doesn't exist
    try {
      cycPort.getStageLength(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Stage doesnt exist failed good");
    }

    // stage exists
    cycPort.createRace("Race 1", "Race Description");
    cycPort.addStageToRace(0,"alanstage",null,5.0,LocalDateTime.now(),StageType.FLAT);
    assert cycPort.getStageLength(0) == 5.0 : "Stage length incorrect";
  }

  public static void testGetRaceStages() throws IDNotRecognisedException, InvalidNameException, IllegalNameException, InvalidLengthException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Race does not exist
    try {
      cycPort.getRaceStages(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("No race exists success");
    }

    // Race has no stages
    cycPort.createRace("A", null);
    assert cycPort.getRaceStages(0).length == 0 : "Incorrect number of stages: meant to be 0";

    // Race has stages
    cycPort.addStageToRace(0, "a2", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    assert cycPort.getRaceStages(0).length == 1 : "Incorrect number of stages: meant to be 1";
  }

  public static void testAddIntermediateSprintToStage() throws InvalidNameException, IllegalNameException, InvalidStageStateException, InvalidLocationException, IDNotRecognisedException, InvalidStageTypeException, InvalidLengthException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Stage doesn't exist
    cycPort.createRace("2", null);
    try {
      cycPort.addIntermediateSprintToStage(0, 5.0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Stage doesn't exist failed successfully");
    }

    // Stage exists, location valid, under development
    cycPort.addStageToRace(0, "name", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addIntermediateSprintToStage(0, 5.0);
    assert cycPort.getSegmentIdsToSegments().size() == 1 : "Int. sprint not added!";

    // Location too large
    try {
      cycPort.addIntermediateSprintToStage(0, 69.420);
    } catch (InvalidLocationException ex) {
      System.out.println("Location too large failed successfully");
    }

    // Location negative
    try {
      cycPort.addIntermediateSprintToStage(0, -69.420);
    } catch (InvalidLocationException ex) {
      System.out.println("Location negative failed successfully");
    }

    // Waiting for results
    cycPort.concludeStagePreparation(0);
    try {
      cycPort.addIntermediateSprintToStage(0, 5.0);
    } catch (InvalidStageStateException ex) {
      System.out.println("Waiting for results failed successfully");
    }

    // stage type time trial
    cycPort.addStageToRace(0,"stagensdfasgham2e",null,8.0,LocalDateTime.now(),StageType.TT);
    try {
      cycPort.addIntermediateSprintToStage(1, 5.0);
    } catch (InvalidStageTypeException ex) {
      System.out.println("Time trial with int sprint failed good");
    }

  }

  public static void testAddCategorizedClimbToStage() throws InvalidNameException, IllegalNameException, InvalidStageStateException, InvalidLocationException, IDNotRecognisedException, InvalidStageTypeException, InvalidLengthException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Stage doesn't exist
    cycPort.createRace("2", null);
    try {
      cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,10.0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Stage doesn't exist failed successfully");
    }

    // Stage exists, location valid, under development
    cycPort.addStageToRace(0, "name", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,3.0);
    assert cycPort.getSegmentIdsToSegments().size() == 1 : "CC. sprint not added!";

    // Location too large
    try {
      cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,69420.0);
    } catch (InvalidLocationException ex) {
      System.out.println("Location too large failed successfully");
    }

    // Location negative
    try {
      cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,-69420.0);
    } catch (InvalidLocationException ex) {
      System.out.println("Location negative failed successfully");
    }

    // Type is sprint
    try {
      cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.SPRINT,4.0,10.0);
    } catch (InvalidStageTypeException ex) {
      System.out.println("Invalid stage Type exception");
    }

    // Waiting for results
    cycPort.concludeStagePreparation(0);
    try {
      cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,10.0);
    } catch (InvalidStageStateException ex) {
      System.out.println("Waiting for results failed successfully");
    }

    //C1
    cycPort.addStageToRace(0, "name1", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(1, 5.0,SegmentType.C1,4.0,3.0);
    assert cycPort.getSegmentIdsToSegments().size() == 2 : "CC. sprint not added!";

    //C2
    cycPort.addStageToRace(0, "name2", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(2, 5.0,SegmentType.C2,4.0,3.0);
    assert cycPort.getSegmentIdsToSegments().size() == 3 : "CC. sprint not added!";

    //C3
    cycPort.addStageToRace(0, "name3", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(3, 5.0,SegmentType.C3,4.0,3.0);
    assert cycPort.getSegmentIdsToSegments().size() == 4 : "CC. sprint not added!";

    //C4
    cycPort.addStageToRace(0, "name4", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(4, 5.0,SegmentType.C4,4.0,3.0);
    assert cycPort.getSegmentIdsToSegments().size() == 5 : "CC. sprint not added!";

    // stage type time trial
    cycPort.addStageToRace(0,"stagensdfasgham2e",null,8.0,LocalDateTime.now(),StageType.TT);
    try {
      cycPort.addCategorizedClimbToStage(5, 5.0,SegmentType.C4,4.0,3.0);
    } catch (InvalidStageTypeException ex) {
      System.out.println("Time trial with int sprint failed good");
    }
  }

  public static void testGetStageSegments() throws IDNotRecognisedException, InvalidNameException, IllegalNameException, InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException {
    //Setup
    CyclingPortal cycPort = new CyclingPortal();

    //Stage no existy
    try {
      cycPort.getStageSegments(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("NO stage failed yes");
    }

    //Stage has no segments
    cycPort.createRace("2", null);
    cycPort.addStageToRace(0, "name", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    assert cycPort.getStageSegments(0).length == 0;

    //Stage has int sprints
    cycPort.addIntermediateSprintToStage(0, 5.0);
    assert  cycPort.getStageSegments(0).length == 1;

    //Stage has CC.
    cycPort.addStageToRace(0, "name2", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(1, 5.0,SegmentType.HC,4.0,3.0);
    assert  cycPort.getStageSegments(1).length == 1;

    //Stage has Int & CC.
    cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,3.0);
    assert  cycPort.getStageSegments(0).length == 2;
  }

  public static void testRemoveSegment() throws InvalidNameException, IllegalNameException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Segment doesn't exist
    cycPort.createRace("name", null);
    cycPort.addStageToRace(0, "nanme2", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    try {
      cycPort.removeSegment(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("No segments exist failed successfully");
    }

    // Intermediate sprint exists
    cycPort.addIntermediateSprintToStage(0, 5.0);
    assert cycPort.getSegmentIdsToSegments().size() == 1;
    cycPort.removeSegment(0);
    assert cycPort.getSegmentIdsToSegments().size() == 0;

    // Categorised climb exists
    cycPort.addCategorizedClimbToStage(0, 5.0,SegmentType.HC,4.0,3.0);
    assert cycPort.getSegmentIdsToSegments().size() == 1;
    cycPort.removeSegment(1);
    assert cycPort.getSegmentIdsToSegments().size() == 0;
  }

  public static void testRegisterRiderResultsInStage() throws InvalidNameException, IllegalNameException, DuplicatedResultException, InvalidCheckpointsException, InvalidStageStateException, IDNotRecognisedException, InvalidLengthException, InvalidLocationException, InvalidStageTypeException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();
    LocalTime[] good = {LocalTime.of(0,0,0), LocalTime.of(0,0,1), LocalTime.of(0,0,2)};
    LocalTime[] tooFew = {LocalTime.of(0,0,0)};
    LocalTime[] tooMany = {LocalTime.of(0,0,0), LocalTime.of(0,0,1), LocalTime.of(0,0,2), LocalTime.of(1,1,1)};
    LocalTime[] nonChrono = {LocalTime.of(1,0,0), LocalTime.of(0,0,0)};

    // Stage doesn't exist
    cycPort.createRace("race", null);
    try {
      cycPort.registerRiderResultsInStage(0, 0, good);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Stage doesn't exist failed successfully");
    }

    // Stage under development
    cycPort.addStageToRace(0, "stage", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.createTeam("team", null);
    cycPort.createRider(0, "rider", 1900);
    try {
      cycPort.registerRiderResultsInStage(0, 0, good);
    } catch (InvalidStageStateException ex) {
      System.out.println("Stage not waiting for results failed successfully");
    }

    // Stage valid & Rider doesn't exist
    cycPort.addStageToRace(0, "stage2", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addIntermediateSprintToStage(1, 5.0);
    cycPort.concludeStagePreparation(1);
    try {
      cycPort.registerRiderResultsInStage(1, 69420, good);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Rider doesn't exist failed successfully");
    }

    // Stage valid sprint & Rider exists and has no results in stage
    cycPort.registerRiderResultsInStage(1, 0, good);
    assert cycPort.getStageIdsToStages().get(1).getResults().size() == 1 : "Valid result not added!";

    // Stage valid CC & Rider exists and has no results in stage
    cycPort.addStageToRace(0, "name3", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addCategorizedClimbToStage(2, 3.0, SegmentType.HC, 10.0, 3.0);
    cycPort.concludeStagePreparation(2);
    cycPort.registerRiderResultsInStage(2, 0, good);
    assert cycPort.getStageIdsToStages().get(2).getResults().size() == 1 : "Valid result not added!";

    // Rider already has result in stage
    try {
      cycPort.registerRiderResultsInStage(1, 0, good);
    } catch (DuplicatedResultException ex) {
      System.out.println("Rider already has results failed successfully");
    }

    // Too few checkpoints
    try {
      cycPort.createRider(0, "aaa", 1999);
      cycPort.registerRiderResultsInStage(1, 1, tooFew);
    } catch (InvalidCheckpointsException ex) {
      System.out.println("Too few checkpoints failed successfully");
    }

    // Too many checkpoints
    try {
      cycPort.createRider(0, "afgggaa", 1999);
      cycPort.registerRiderResultsInStage(1, 2, tooMany);
    } catch (InvalidCheckpointsException ex) {
      System.out.println("Too many checkpoints failed successfully");
    }

    // Checkpoints not in chronological order
    try {
      cycPort.createRider(0, "asdaa", 1999);
      cycPort.registerRiderResultsInStage(1, 3, nonChrono);
    } catch (InvalidCheckpointsException ex) {
      System.out.println("Non-chronological order failed successfully");
    }
  }

  public static void testGetRidersRankInStage() throws IDNotRecognisedException, InvalidStageStateException, InvalidNameException, InvalidLengthException, IllegalNameException, DuplicatedResultException, InvalidCheckpointsException, InvalidLocationException, InvalidStageTypeException {
    //setup
    CyclingPortal cycPort = new CyclingPortal();

    //stage no exist
    try {
      cycPort.getRidersRankInStage(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("STage no exist good");
    }

    //stage has no results
    cycPort.createRace("name",null);
    cycPort.addStageToRace(0,"stagename",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addIntermediateSprintToStage(0, 5.0);
    assert cycPort.getRidersRankInStage(0).length == 0 : "There should be no resultsa";

    //stage exist and has results
    cycPort.createTeam("t",null);
    cycPort.createRider(0, "rider", 1900);
    LocalTime[] good = {LocalTime.of(0,0,0), LocalTime.of(0,0,1), LocalTime.of(0,0,2)};
    cycPort.concludeStagePreparation(0);
    cycPort.registerRiderResultsInStage(0, 0, good);
    assert cycPort.getRidersRankInStage(0).length == 1 : "There should be 1 resultsa";

    //correct ranking
    cycPort.createRider(0, "ride1r", 1900);
    cycPort.createRider(0, "ri22341er", 1900);
    LocalTime[] good1 = {LocalTime.of(0,0,0), LocalTime.of(0,0,1), LocalTime.of(0,0,3)};
    LocalTime[] good2 = {LocalTime.of(0,0,0), LocalTime.of(0,0,1), LocalTime.of(0,0,4)};
    cycPort.registerRiderResultsInStage(0, 1, good2);
    cycPort.registerRiderResultsInStage(0, 2, good1);
    assert cycPort.getRidersRankInStage(0).length == 3 : "There should be 1 resultsa";
    assert cycPort.getRidersRankInStage(0)[1] == 2 : "Ranking incorrect";
  }

  public static void testGetRidersPointsInStage() throws IDNotRecognisedException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, InvalidNameException, InvalidLengthException, IllegalNameException, DuplicatedResultException, InvalidCheckpointsException {
    //setup
    CyclingPortal cycPort = new CyclingPortal();

    //stage no exist
    try {
      cycPort.getRidersPointsInStage(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("STage no exist good");
    }

    //stage no results
    cycPort.createRace("name",null);
    cycPort.addStageToRace(0,"stagename",null,8.0,LocalDateTime.now(),StageType.FLAT);
    assert cycPort.getRidersPointsInStage(0).length == 0 : "There should be no resultsa";

    //stage type flat
    cycPort.createTeam("t",null);
    cycPort.createRider(0, "rider", 1900);
    LocalTime[] good = {LocalTime.of(0,0,0), LocalTime.of(0,0,2)};
    cycPort.concludeStagePreparation(0);
    cycPort.registerRiderResultsInStage(0, 0, good);
    assert cycPort.getRidersPointsInStage(0)[0] == 50 : "There should be 50 points for first place";

    // stage type medium mountain
    cycPort.addStageToRace(0,"stagenam2e",null,8.0,LocalDateTime.now(),StageType.MEDIUM_MOUNTAIN);
    cycPort.concludeStagePreparation(1);
    cycPort.registerRiderResultsInStage(1, 0, good);
    assert cycPort.getRidersPointsInStage(1)[0] == 30 : "There should be 30 points for first place";

    // stage type high mountain
    cycPort.addStageToRace(0,"stagedfgsnam2e",null,8.0,LocalDateTime.now(),StageType.HIGH_MOUNTAIN);
    cycPort.concludeStagePreparation(2);
    cycPort.registerRiderResultsInStage(2, 0, good);
    assert cycPort.getRidersPointsInStage(2)[0] == 20 : "There should be 20 points for first place";

    // segment int sprint
    cycPort.addStageToRace(0,"stagenadfm2e",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addIntermediateSprintToStage(3, 5.0);
    cycPort.concludeStagePreparation(3);
    LocalTime[] good2 = {LocalTime.of(0,0,0),LocalTime.of(0,0,1), LocalTime.of(0,0,2)};
    cycPort.registerRiderResultsInStage(3, 0, good2);
    assert cycPort.getRidersPointsInStage(3)[0] == 70 : "There should be 70 points for first place";

    //time trial points
    cycPort.addStageToRace(0,"stagedfgsnam2e",null,8.0,LocalDateTime.now(),StageType.TT);
    cycPort.concludeStagePreparation(4);
    cycPort.registerRiderResultsInStage(4, 0, good);
    assert cycPort.getRidersPointsInStage(4)[0] == 20 : "There should be 20 points for first place";

    LocalTime t0 = LocalTime.of(0,0,0);
    LocalTime t1 = LocalTime.of(0,0,10);
    LocalTime t2 = LocalTime.of(0,0,20);
    LocalTime t3 = LocalTime.of(0,0,30);
    LocalTime t4 = LocalTime.of(0,0,40);
    LocalTime t5 = LocalTime.of(0,0,50);
    LocalTime t6 = LocalTime.of(0,1,0);
    LocalTime t7 = LocalTime.of(0,1,10);
    LocalTime t8= LocalTime.of(0,1,20);
    LocalTime t9 = LocalTime.of(0,1,30);
    LocalTime t10 = LocalTime.of(0,1,40);
    LocalTime t11 = LocalTime.of(0,1,50);
    LocalTime t12 = LocalTime.of(0,2,0);
    LocalTime t13 = LocalTime.of(0,2,10);
    LocalTime t14 = LocalTime.of(0,2,20);
    LocalTime t15 = LocalTime.of(0,2,30);
    LocalTime t16 = LocalTime.of(0,2,40);
    LocalTime t17 = LocalTime.of(0,2,50);

    cycPort.createRider(0,"1",1998);
    cycPort.createRider(0,"2",2015);
    cycPort.createRider(0,"3",1988);
    cycPort.createRider(0,"4",2015);
    cycPort.createRider(0,"5",1998);
    cycPort.createRider(0,"6",2015);
    cycPort.createRider(0,"7",2000);
    cycPort.createRider(0,"8",2015);
    cycPort.createRider(0,"9",1998);
    cycPort.createRider(0,"10",2015);
    cycPort.createRider(0,"11",1998);
    cycPort.createRider(0,"12",2015);
    cycPort.createRider(0,"13",1998);
    cycPort.createRider(0,"14",2015);
    cycPort.createRider(0,"15",1998);
    cycPort.createRider(0,"16",2015);

    //stage type flat with 16 riders
    cycPort.addStageToRace(0,"Flat",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.concludeStagePreparation(5);
    cycPort.registerRiderResultsInStage(5, 1, t0,t1);
    cycPort.registerRiderResultsInStage(5, 2, t0,t2);
    cycPort.registerRiderResultsInStage(5, 3, t0,t3);
    cycPort.registerRiderResultsInStage(5, 4, t0,t4);
    cycPort.registerRiderResultsInStage(5, 5, t0,t5);
    cycPort.registerRiderResultsInStage(5, 6, t0,t6);
    cycPort.registerRiderResultsInStage(5, 7, t0,t7);
    cycPort.registerRiderResultsInStage(5, 8, t0,t8);
    cycPort.registerRiderResultsInStage(5, 9, t0,t9);
    cycPort.registerRiderResultsInStage(5, 10, t0,t10);
    cycPort.registerRiderResultsInStage(5, 11, t0,t11);
    cycPort.registerRiderResultsInStage(5, 12, t0,t12);
    cycPort.registerRiderResultsInStage(5, 13, t0,t13);
    cycPort.registerRiderResultsInStage(5, 14, t0,t14);
    cycPort.registerRiderResultsInStage(5, 15, t0,t15);
    cycPort.registerRiderResultsInStage(5, 16, t0,t16);

    assert cycPort.getRidersPointsInStage(5)[0] == 50;
    assert cycPort.getRidersPointsInStage(5)[1] == 30;
    assert cycPort.getRidersPointsInStage(5)[2] == 20;
    assert cycPort.getRidersPointsInStage(5)[3] == 18;
    assert cycPort.getRidersPointsInStage(5)[4] == 16;
    assert cycPort.getRidersPointsInStage(5)[5] == 14;
    assert cycPort.getRidersPointsInStage(5)[6] == 12;
    assert cycPort.getRidersPointsInStage(5)[7] == 10;
    assert cycPort.getRidersPointsInStage(5)[8] == 8;
    assert cycPort.getRidersPointsInStage(5)[9] == 7;
    assert cycPort.getRidersPointsInStage(5)[10] == 6;
    assert cycPort.getRidersPointsInStage(5)[11] == 5;
    assert cycPort.getRidersPointsInStage(5)[12] == 4;
    assert cycPort.getRidersPointsInStage(5)[13] == 3;
    assert cycPort.getRidersPointsInStage(5)[14] == 2;
    assert cycPort.getRidersPointsInStage(5)[15] == 0;

    // stage type medium mountain with 16 riders
    cycPort.addStageToRace(0,"MedMoun",null,8.0,LocalDateTime.now(),StageType.MEDIUM_MOUNTAIN);
    cycPort.concludeStagePreparation(6);
    cycPort.registerRiderResultsInStage(6, 1, t0,t1);
    cycPort.registerRiderResultsInStage(6, 2, t0,t2);
    cycPort.registerRiderResultsInStage(6, 3, t0,t3);
    cycPort.registerRiderResultsInStage(6, 4, t0,t4);
    cycPort.registerRiderResultsInStage(6, 5, t0,t5);
    cycPort.registerRiderResultsInStage(6, 6, t0,t6);
    cycPort.registerRiderResultsInStage(6, 7, t0,t7);
    cycPort.registerRiderResultsInStage(6, 8, t0,t8);
    cycPort.registerRiderResultsInStage(6, 9, t0,t9);
    cycPort.registerRiderResultsInStage(6, 10, t0,t10);
    cycPort.registerRiderResultsInStage(6, 11, t0,t11);
    cycPort.registerRiderResultsInStage(6, 12, t0,t12);
    cycPort.registerRiderResultsInStage(6, 13, t0,t13);
    cycPort.registerRiderResultsInStage(6, 14, t0,t14);
    cycPort.registerRiderResultsInStage(6, 15, t0,t15);
    cycPort.registerRiderResultsInStage(6, 16, t0,t16);

    assert cycPort.getRidersPointsInStage(6)[0] == 30;
    assert cycPort.getRidersPointsInStage(6)[1]  == 25;
    assert cycPort.getRidersPointsInStage(6)[2]  == 22;
    assert cycPort.getRidersPointsInStage(6)[3]  == 19;
    assert cycPort.getRidersPointsInStage(6)[4]  == 17;
    assert cycPort.getRidersPointsInStage(6)[5]  == 15;
    assert cycPort.getRidersPointsInStage(6)[6]  == 13;
    assert cycPort.getRidersPointsInStage(6)[7]  == 11;
    assert cycPort.getRidersPointsInStage(6)[8]  == 9;
    assert cycPort.getRidersPointsInStage(6)[9]  == 7;
    assert cycPort.getRidersPointsInStage(6)[10] == 6;
    assert cycPort.getRidersPointsInStage(6)[11] == 5;
    assert cycPort.getRidersPointsInStage(6)[12] == 4;
    assert cycPort.getRidersPointsInStage(6)[13] == 3;
    assert cycPort.getRidersPointsInStage(6)[14] == 2;
    assert cycPort.getRidersPointsInStage(6)[15] == 0;

    // stage type high mountain with 16 riders
    cycPort.addStageToRace(0,"HighMoun",null,8.0,LocalDateTime.now(),StageType.HIGH_MOUNTAIN);
    cycPort.concludeStagePreparation(7);
    cycPort.registerRiderResultsInStage(7, 1, t0,t1);
    cycPort.registerRiderResultsInStage(7, 2, t0,t2);
    cycPort.registerRiderResultsInStage(7, 3, t0,t3);
    cycPort.registerRiderResultsInStage(7, 4, t0,t4);
    cycPort.registerRiderResultsInStage(7, 5, t0,t5);
    cycPort.registerRiderResultsInStage(7, 6, t0,t6);
    cycPort.registerRiderResultsInStage(7, 7, t0,t7);
    cycPort.registerRiderResultsInStage(7, 8, t0,t8);
    cycPort.registerRiderResultsInStage(7, 9, t0,t9);
    cycPort.registerRiderResultsInStage(7, 10, t0,t10);
    cycPort.registerRiderResultsInStage(7, 11, t0,t11);
    cycPort.registerRiderResultsInStage(7, 12, t0,t12);
    cycPort.registerRiderResultsInStage(7, 13, t0,t13);
    cycPort.registerRiderResultsInStage(7, 14, t0,t14);
    cycPort.registerRiderResultsInStage(7, 15, t0,t15);
    cycPort.registerRiderResultsInStage(7, 16, t0,t16);

    assert cycPort.getRidersPointsInStage(7)[0] == 20;
    assert cycPort.getRidersPointsInStage(7)[1]  == 17;
    assert cycPort.getRidersPointsInStage(7)[2]  == 15;
    assert cycPort.getRidersPointsInStage(7)[3]  == 13;
    assert cycPort.getRidersPointsInStage(7)[4]  == 11;
    assert cycPort.getRidersPointsInStage(7)[5]  == 10;
    assert cycPort.getRidersPointsInStage(7)[6]  == 9;
    assert cycPort.getRidersPointsInStage(7)[7]  == 8;
    assert cycPort.getRidersPointsInStage(7)[8]  == 7;
    assert cycPort.getRidersPointsInStage(7)[9]  == 6;
    assert cycPort.getRidersPointsInStage(7)[10] == 5;
    assert cycPort.getRidersPointsInStage(7)[11] == 4;
    assert cycPort.getRidersPointsInStage(7)[12] == 3;
    assert cycPort.getRidersPointsInStage(7)[13] == 2;
    assert cycPort.getRidersPointsInStage(7)[14] == 1;
    assert cycPort.getRidersPointsInStage(7)[15] == 0;

    // segment int sprint with 16 riders
    cycPort.addStageToRace(0,"Flat+Sprint",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addIntermediateSprintToStage(8, 5.0);
    cycPort.concludeStagePreparation(8);
    cycPort.registerRiderResultsInStage(8, 1, t0,t1,t2);
    cycPort.registerRiderResultsInStage(8, 2, t0,t2,t3);
    cycPort.registerRiderResultsInStage(8, 3, t0,t3,t4);
    cycPort.registerRiderResultsInStage(8, 4, t0,t4,t5);
    cycPort.registerRiderResultsInStage(8, 5, t0,t5,t6);
    cycPort.registerRiderResultsInStage(8, 6, t0,t6,t7);
    cycPort.registerRiderResultsInStage(8, 7, t0,t7,t8);
    cycPort.registerRiderResultsInStage(8, 8, t0,t8,t9);
    cycPort.registerRiderResultsInStage(8, 9, t0,t9,t10);
    cycPort.registerRiderResultsInStage(8, 10, t0,t10,t11);
    cycPort.registerRiderResultsInStage(8, 11, t0,t11,t12);
    cycPort.registerRiderResultsInStage(8, 12, t0,t12,t13);
    cycPort.registerRiderResultsInStage(8, 13, t0,t13,t14);
    cycPort.registerRiderResultsInStage(8, 14, t0,t14,t15);
    cycPort.registerRiderResultsInStage(8, 15, t0,t15,t16);
    cycPort.registerRiderResultsInStage(8, 16, t0,t16,t17);

    assert cycPort.getRidersPointsInStage(8)[0] == 70;
    assert cycPort.getRidersPointsInStage(8)[1]  == 47;
    assert cycPort.getRidersPointsInStage(8)[2]  == 35;
    assert cycPort.getRidersPointsInStage(8)[3]  == 31;
    assert cycPort.getRidersPointsInStage(8)[4]  == 27;
    assert cycPort.getRidersPointsInStage(8)[5]  == 24;
    assert cycPort.getRidersPointsInStage(8)[6]  == 21;
    assert cycPort.getRidersPointsInStage(8)[7]  == 18;
    assert cycPort.getRidersPointsInStage(8)[8]  == 15;
    assert cycPort.getRidersPointsInStage(8)[9]  == 13;
    assert cycPort.getRidersPointsInStage(8)[10] == 11;
    assert cycPort.getRidersPointsInStage(8)[11] == 9;
    assert cycPort.getRidersPointsInStage(8)[12] == 7;
    assert cycPort.getRidersPointsInStage(8)[13] ==5;
    assert cycPort.getRidersPointsInStage(8)[14] == 3;
    assert cycPort.getRidersPointsInStage(8)[15] == 0;

    //time trial points with 16 riders
    cycPort.addStageToRace(0,"TT",null,8.0,LocalDateTime.now(),StageType.TT);
    cycPort.concludeStagePreparation(9);
    cycPort.registerRiderResultsInStage(9, 1, t0,t1);
    cycPort.registerRiderResultsInStage(9, 2, t0,t2);
    cycPort.registerRiderResultsInStage(9, 3, t0,t3);
    cycPort.registerRiderResultsInStage(9, 4, t0,t4);
    cycPort.registerRiderResultsInStage(9, 5, t0,t5);
    cycPort.registerRiderResultsInStage(9, 6, t0,t6);
    cycPort.registerRiderResultsInStage(9, 7, t0,t7);
    cycPort.registerRiderResultsInStage(9, 8, t0,t8);
    cycPort.registerRiderResultsInStage(9, 9, t0,t9);
    cycPort.registerRiderResultsInStage(9, 10, t0,t10);
    cycPort.registerRiderResultsInStage(9, 11, t0,t11);
    cycPort.registerRiderResultsInStage(9, 12, t0,t12);
    cycPort.registerRiderResultsInStage(9, 13, t0,t13);
    cycPort.registerRiderResultsInStage(9, 14, t0,t14);
    cycPort.registerRiderResultsInStage(9, 15, t0,t15);
    cycPort.registerRiderResultsInStage(9, 16, t0,t16);

    assert cycPort.getRidersPointsInStage(7)[0] == 20;
    assert cycPort.getRidersPointsInStage(7)[1]  == 17;
    assert cycPort.getRidersPointsInStage(7)[2]  == 15;
    assert cycPort.getRidersPointsInStage(7)[3]  == 13;
    assert cycPort.getRidersPointsInStage(7)[4]  == 11;
    assert cycPort.getRidersPointsInStage(7)[5]  == 10;
    assert cycPort.getRidersPointsInStage(7)[6]  == 9;
    assert cycPort.getRidersPointsInStage(7)[7]  == 8;
    assert cycPort.getRidersPointsInStage(7)[8]  == 7;
    assert cycPort.getRidersPointsInStage(7)[9]  == 6;
    assert cycPort.getRidersPointsInStage(7)[10] == 5;
    assert cycPort.getRidersPointsInStage(7)[11] == 4;
    assert cycPort.getRidersPointsInStage(7)[12] == 3;
    assert cycPort.getRidersPointsInStage(7)[13] == 2;
    assert cycPort.getRidersPointsInStage(7)[14] == 1;
    assert cycPort.getRidersPointsInStage(7)[15] == 0;

  }

  public static void testGetRidersMountainPointsInStage() throws InvalidNameException, IllegalNameException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, DuplicatedResultException, InvalidCheckpointsException {
    //setup
    CyclingPortal cycPort = new CyclingPortal();

    //stage no exist
    try {
      cycPort.getRidersMountainPointsInStage(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("STage no exist good");
    }

    //stage no results
    cycPort.createRace("name",null);
    cycPort.addStageToRace(0,"stagename",null,8.0,LocalDateTime.now(),StageType.FLAT);
    assert cycPort.getRidersMountainPointsInStage(0).length == 0 : "There should be no resultsa";

    // Valid categorised climb setup
    LocalTime t0 = LocalTime.of(0,0,0);
    LocalTime t1 = LocalTime.of(0,0,10);
    LocalTime t2 = LocalTime.of(0,0,20);
    LocalTime t3 = LocalTime.of(0,0,30);
    LocalTime t4 = LocalTime.of(0,0,40);
    LocalTime t5 = LocalTime.of(0,0,50);
    LocalTime t6 = LocalTime.of(0,1,0);
    LocalTime t7 = LocalTime.of(0,1,10);
    LocalTime t8= LocalTime.of(0,1,20);
    LocalTime t9 = LocalTime.of(0,1,30);
    LocalTime t10 = LocalTime.of(0,1,40);

    cycPort.createTeam("team", null);
    cycPort.createRider(0,"1",1998);
    cycPort.createRider(0,"2",2015);
    cycPort.createRider(0,"3",1988);
    cycPort.createRider(0,"4",2015);
    cycPort.createRider(0,"5",1998);
    cycPort.createRider(0,"6",2015);
    cycPort.createRider(0,"7",2000);
    cycPort.createRider(0,"8",2015);
    cycPort.createRider(0,"9",1998);

    // Test HC valid SWAPPED 3 and 1
    cycPort.addStageToRace(0,"Climb",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addCategorizedClimbToStage(0, 5.0, SegmentType.HC, 10.0, 3.0);
    cycPort.concludeStagePreparation(0);
    cycPort.registerRiderResultsInStage(0, 0, t0,t1,t2);
    cycPort.registerRiderResultsInStage(0, 1, t0,t2,t3);
    cycPort.registerRiderResultsInStage(0, 2, t0,t3,t4);
    cycPort.registerRiderResultsInStage(0, 3, t0,t4,t5);
    cycPort.registerRiderResultsInStage(0, 4, t0,t5,t6);
    cycPort.registerRiderResultsInStage(0, 5, t0,t6,t7);
    cycPort.registerRiderResultsInStage(0, 6, t0,t7,t8);
    cycPort.registerRiderResultsInStage(0, 7, t0,t8,t9);
    cycPort.registerRiderResultsInStage(0, 8, t0,t9,t10);

    assert cycPort.getRidersMountainPointsInStage(0)[0] == 20;
    assert cycPort.getRidersMountainPointsInStage(0)[1] == 15;
    assert cycPort.getRidersMountainPointsInStage(0)[2] == 12;
    assert cycPort.getRidersMountainPointsInStage(0)[3] == 10;
    assert cycPort.getRidersMountainPointsInStage(0)[4] == 8;
    assert cycPort.getRidersMountainPointsInStage(0)[5] == 6;
    assert cycPort.getRidersMountainPointsInStage(0)[6] == 4;
    assert cycPort.getRidersMountainPointsInStage(0)[7] == 2;
    assert cycPort.getRidersMountainPointsInStage(0)[8] == 0;

    // Test C1 valid
    cycPort.addStageToRace(0,"Climb",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addCategorizedClimbToStage(1, 5.0, SegmentType.C1, 10.0, 3.0);
    cycPort.concludeStagePreparation(1);
    cycPort.registerRiderResultsInStage(1, 0, t0,t1,t2);
    cycPort.registerRiderResultsInStage(1, 1, t0,t2,t3);
    cycPort.registerRiderResultsInStage(1, 2, t0,t3,t4);
    cycPort.registerRiderResultsInStage(1, 3, t0,t4,t5);
    cycPort.registerRiderResultsInStage(1, 4, t0,t5,t6);
    cycPort.registerRiderResultsInStage(1, 5, t0,t6,t7);
    cycPort.registerRiderResultsInStage(1, 6, t0,t7,t8);
    cycPort.registerRiderResultsInStage(1, 7, t0,t8,t9);
    cycPort.registerRiderResultsInStage(1, 8, t0,t9,t10);

    assert cycPort.getRidersMountainPointsInStage(1)[0] == 10;
    assert cycPort.getRidersMountainPointsInStage(1)[1] == 8;
    assert cycPort.getRidersMountainPointsInStage(1)[2] == 6;
    assert cycPort.getRidersMountainPointsInStage(1)[3] == 4;
    assert cycPort.getRidersMountainPointsInStage(1)[4] == 2;
    assert cycPort.getRidersMountainPointsInStage(1)[5] == 1;
    assert cycPort.getRidersMountainPointsInStage(1)[6] == 0;
    assert cycPort.getRidersMountainPointsInStage(1)[7] == 0;
    assert cycPort.getRidersMountainPointsInStage(1)[8] == 0;

    // Test C2 valid
    cycPort.addStageToRace(0,"Climb",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addCategorizedClimbToStage(2, 5.0, SegmentType.C2, 10.0, 3.0);
    cycPort.concludeStagePreparation(2);
    cycPort.registerRiderResultsInStage(2, 0, t0,t1,t2);
    cycPort.registerRiderResultsInStage(2, 1, t0,t2,t3);
    cycPort.registerRiderResultsInStage(2, 2, t0,t3,t4);
    cycPort.registerRiderResultsInStage(2, 3, t0,t4,t5);
    cycPort.registerRiderResultsInStage(2, 4, t0,t5,t6);
    cycPort.registerRiderResultsInStage(2, 5, t0,t6,t7);
    cycPort.registerRiderResultsInStage(2, 6, t0,t7,t8);
    cycPort.registerRiderResultsInStage(2, 7, t0,t8,t9);
    cycPort.registerRiderResultsInStage(2, 8, t0,t9,t10);

    assert cycPort.getRidersMountainPointsInStage(2)[0] == 5;
    assert cycPort.getRidersMountainPointsInStage(2)[1] == 3;
    assert cycPort.getRidersMountainPointsInStage(2)[2] == 2;
    assert cycPort.getRidersMountainPointsInStage(2)[3] == 1;
    assert cycPort.getRidersMountainPointsInStage(2)[4] == 0;
    assert cycPort.getRidersMountainPointsInStage(2)[5] == 0;
    assert cycPort.getRidersMountainPointsInStage(2)[6] == 0;
    assert cycPort.getRidersMountainPointsInStage(2)[7] == 0;
    assert cycPort.getRidersMountainPointsInStage(2)[8] == 0;

    // Test C3 valid
    cycPort.addStageToRace(0,"Climb",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addCategorizedClimbToStage(3, 5.0, SegmentType.C3, 10.0, 3.0);
    cycPort.concludeStagePreparation(3);
    cycPort.registerRiderResultsInStage(3, 0, t0,t1,t2);
    cycPort.registerRiderResultsInStage(3, 1, t0,t2,t3);
    cycPort.registerRiderResultsInStage(3, 2, t0,t3,t4);
    cycPort.registerRiderResultsInStage(3, 3, t0,t4,t5);
    cycPort.registerRiderResultsInStage(3, 4, t0,t5,t6);
    cycPort.registerRiderResultsInStage(3, 5, t0,t6,t7);
    cycPort.registerRiderResultsInStage(3, 6, t0,t7,t8);
    cycPort.registerRiderResultsInStage(3, 7, t0,t8,t9);
    cycPort.registerRiderResultsInStage(3, 8, t0,t9,t10);

    assert cycPort.getRidersMountainPointsInStage(3)[0] == 2;
    assert cycPort.getRidersMountainPointsInStage(3)[1] == 1;
    assert cycPort.getRidersMountainPointsInStage(3)[2] == 0;
    assert cycPort.getRidersMountainPointsInStage(3)[3] == 0;
    assert cycPort.getRidersMountainPointsInStage(3)[4] == 0;
    assert cycPort.getRidersMountainPointsInStage(3)[5] == 0;
    assert cycPort.getRidersMountainPointsInStage(3)[6] == 0;
    assert cycPort.getRidersMountainPointsInStage(3)[7] == 0;
    assert cycPort.getRidersMountainPointsInStage(3)[8] == 0;

    // Test C4 valid
    cycPort.addStageToRace(0,"Climb",null,8.0,LocalDateTime.now(),StageType.FLAT);
    cycPort.addCategorizedClimbToStage(4, 5.0, SegmentType.C4, 10.0, 3.0);
    cycPort.concludeStagePreparation(4);
    cycPort.registerRiderResultsInStage(4, 0, t0,t1,t2);
    cycPort.registerRiderResultsInStage(4, 1, t0,t2,t3);
    cycPort.registerRiderResultsInStage(4, 2, t0,t3,t4);
    cycPort.registerRiderResultsInStage(4, 3, t0,t4,t5);
    cycPort.registerRiderResultsInStage(4, 4, t0,t5,t6);
    cycPort.registerRiderResultsInStage(4, 5, t0,t6,t7);
    cycPort.registerRiderResultsInStage(4, 6, t0,t7,t8);
    cycPort.registerRiderResultsInStage(4, 7, t0,t8,t9);
    cycPort.registerRiderResultsInStage(4, 8, t0,t9,t10);

    assert cycPort.getRidersMountainPointsInStage(4)[0] == 1;
    assert cycPort.getRidersMountainPointsInStage(4)[1] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[2] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[3] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[4] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[5] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[6] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[7] == 0;
    assert cycPort.getRidersMountainPointsInStage(4)[8] == 0;
  }

  public static void testGetRidersPointsInRace() throws InvalidNameException, IllegalNameException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException, DuplicatedResultException, InvalidCheckpointsException, InvalidLocationException, InvalidStageTypeException {
    // Setup
    CyclingPortal cycPort = new CyclingPortal();

    // Race doesn't exist
    try {
      cycPort.getRidersPointsInRace(0);
    } catch (IDNotRecognisedException ex) {
      System.out.println("Race doesn't exist failed successfully");
    }

    //// No stages in race
    cycPort.createRace("Race", null);
    //System.out.println("first assert");
    //assert cycPort.getRidersPointsInRace(0).length == 0;
//
    //// No results in stage
    cycPort.addStageToRace(0, "stage", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.concludeStagePreparation(0);
    //System.out.println("second assert");
    //assert cycPort.getRidersPointsInRace(0).length == 0;

    // Riders and times setup
    LocalTime t0 = LocalTime.of(0,0,0);
    LocalTime t1 = LocalTime.of(0,0,10);
    LocalTime t2 = LocalTime.of(0,0,20);
    LocalTime t3 = LocalTime.of(0,0,30);
    LocalTime t4 = LocalTime.of(0,0,40);
    LocalTime t5 = LocalTime.of(0,0,50);
    LocalTime t6 = LocalTime.of(0,1,0);
    LocalTime t7 = LocalTime.of(0,1,10);
    LocalTime t8= LocalTime.of(0,1,20);
    LocalTime t9 = LocalTime.of(0,1,30);
    LocalTime t10 = LocalTime.of(0,1,40);
    LocalTime t11 = LocalTime.of(0,1,50);
    LocalTime t12 = LocalTime.of(0,2,0);
    LocalTime t13 = LocalTime.of(0,2,10);
    LocalTime t14 = LocalTime.of(0,2,20);
    LocalTime t15 = LocalTime.of(0,2,30);
    LocalTime t16 = LocalTime.of(0,2,40);
    LocalTime t17 = LocalTime.of(0,2,50);

    cycPort.createTeam("tean", null);
    cycPort.createRider(0,"1",1998);
    cycPort.createRider(0,"2",2015);
    cycPort.createRider(0,"3",1988);
    cycPort.createRider(0,"4",2015);
    cycPort.createRider(0,"5",1998);
    cycPort.createRider(0,"6",2015);
    cycPort.createRider(0,"7",2000);
    cycPort.createRider(0,"8",2015);
    cycPort.createRider(0,"9",1998);
    cycPort.createRider(0,"10",2015);
    cycPort.createRider(0,"11",1998);
    cycPort.createRider(0,"12",2015);
    cycPort.createRider(0,"13",1998);
    cycPort.createRider(0,"14",2015);
    cycPort.createRider(0,"15",1998);
    cycPort.createRider(0,"16",2015);

    // 1 stage, No segments in stage (fine)
    cycPort.registerRiderResultsInStage(0, 0, t0, t1 );
    cycPort.registerRiderResultsInStage(0, 1, t0, t2 );
    cycPort.registerRiderResultsInStage(0, 2, t0, t3 );
    cycPort.registerRiderResultsInStage(0, 3, t0, t4 );
    cycPort.registerRiderResultsInStage(0, 4, t0, t5 );
    cycPort.registerRiderResultsInStage(0, 5, t0, t6 );
    cycPort.registerRiderResultsInStage(0, 6, t0, t7 );
    cycPort.registerRiderResultsInStage(0, 7, t0, t8 );
    cycPort.registerRiderResultsInStage(0, 8, t0, t9 );
    cycPort.registerRiderResultsInStage(0, 9, t0, t10);
    cycPort.registerRiderResultsInStage(0, 10, t0, t11);
    cycPort.registerRiderResultsInStage(0, 11, t0, t12);
    cycPort.registerRiderResultsInStage(0, 12, t0, t13);
    cycPort.registerRiderResultsInStage(0, 13, t0, t14);
    cycPort.registerRiderResultsInStage(0, 14, t0, t15);
    cycPort.registerRiderResultsInStage(0, 15, t0, t16);

    System.out.println(Arrays.toString(cycPort.getRidersPointsInRace(0))+" getRidersPointsInStage 0");
    assert cycPort.getRidersPointsInRace(0)[0]  == 50;
    assert cycPort.getRidersPointsInStage(0)[1]  == 30;
    assert cycPort.getRidersPointsInStage(0)[2]  == 20;
    assert cycPort.getRidersPointsInStage(0)[3]  == 18;
    assert cycPort.getRidersPointsInStage(0)[4]  == 16;
    assert cycPort.getRidersPointsInStage(0)[5]  == 14;
    assert cycPort.getRidersPointsInStage(0)[6]  == 12;
    assert cycPort.getRidersPointsInStage(0)[7]  == 10;
    assert cycPort.getRidersPointsInStage(0)[8]  == 8;
    assert cycPort.getRidersPointsInStage(0)[9]  == 7;
    assert cycPort.getRidersPointsInStage(0)[10] == 6;
    assert cycPort.getRidersPointsInStage(0)[11] == 5;
    assert cycPort.getRidersPointsInStage(0)[12] == 4;
    assert cycPort.getRidersPointsInStage(0)[13] == 3;
    assert cycPort.getRidersPointsInStage(0)[14] == 2;
    assert cycPort.getRidersPointsInStage(0)[15] == 0;

    // 1 stage, Points with finish time + a sprint
    cycPort.createRace("Race 2", null);
    cycPort.addStageToRace(1, "stage2", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.addIntermediateSprintToStage(1, 5.0);
    cycPort.concludeStagePreparation(1);
    cycPort.registerRiderResultsInStage(1, 0, t0,t1,t2);
    cycPort.registerRiderResultsInStage(1, 1, t0,t2,t3);
    cycPort.registerRiderResultsInStage(1, 2, t0,t3,t4);
    cycPort.registerRiderResultsInStage(1, 3, t0,t4,t5);
    cycPort.registerRiderResultsInStage(1, 4, t0,t5,t6);
    cycPort.registerRiderResultsInStage(1, 5, t0,t6,t7);
    cycPort.registerRiderResultsInStage(1, 6, t0,t7,t8);
    cycPort.registerRiderResultsInStage(1, 7, t0,t8,t9);
    cycPort.registerRiderResultsInStage(1, 8, t0,t9,t10);
    cycPort.registerRiderResultsInStage(1, 9, t0,t10,t11);
    cycPort.registerRiderResultsInStage(1, 10, t0,t11,t12);
    cycPort.registerRiderResultsInStage(1, 11, t0,t12,t13);
    cycPort.registerRiderResultsInStage(1, 12, t0,t13,t14);
    cycPort.registerRiderResultsInStage(1, 13, t0,t14,t15);
    cycPort.registerRiderResultsInStage(1, 14, t0,t15,t16);
    cycPort.registerRiderResultsInStage(1, 15, t0,t16,t17);

    assert cycPort.getRidersPointsInStage(1)[0]  == 70;
    assert cycPort.getRidersPointsInStage(1)[1]  == 47;
    assert cycPort.getRidersPointsInStage(1)[2]  == 35;
    assert cycPort.getRidersPointsInStage(1)[3]  == 31;
    assert cycPort.getRidersPointsInStage(1)[4]  == 27;
    assert cycPort.getRidersPointsInStage(1)[5]  == 24;
    assert cycPort.getRidersPointsInStage(1)[6]  == 21;
    assert cycPort.getRidersPointsInStage(1)[7]  == 18;
    assert cycPort.getRidersPointsInStage(1)[8]  == 15;
    assert cycPort.getRidersPointsInStage(1)[9]  == 13;
    assert cycPort.getRidersPointsInStage(1)[10] == 11;
    assert cycPort.getRidersPointsInStage(1)[11] == 9;
    assert cycPort.getRidersPointsInStage(1)[12] == 7;
    assert cycPort.getRidersPointsInStage(1)[13] == 5;
    assert cycPort.getRidersPointsInStage(1)[14] == 3;
    assert cycPort.getRidersPointsInStage(1)[15] == 0;

    // 2 stages with no segments
    cycPort.addStageToRace(0, "stage3", null, 10.0, LocalDateTime.now(), StageType.FLAT);
    cycPort.concludeStagePreparation(2);
    cycPort.registerRiderResultsInStage(2, 0, t0, t1 );
    cycPort.registerRiderResultsInStage(2, 1, t0, t2 );
    cycPort.registerRiderResultsInStage(2, 2, t0, t3 );
    cycPort.registerRiderResultsInStage(2, 3, t0, t4 );
    cycPort.registerRiderResultsInStage(2, 4, t0, t5 );
    cycPort.registerRiderResultsInStage(2, 5, t0, t6 );
    cycPort.registerRiderResultsInStage(2, 6, t0, t7 );
    cycPort.registerRiderResultsInStage(2, 7, t0, t8 );
    cycPort.registerRiderResultsInStage(2, 8, t0, t9 );
    cycPort.registerRiderResultsInStage(2, 9, t0, t10);
    cycPort.registerRiderResultsInStage(2, 10, t0, t11);
    cycPort.registerRiderResultsInStage(2, 11, t0, t12);
    cycPort.registerRiderResultsInStage(2, 12, t0, t13);
    cycPort.registerRiderResultsInStage(2, 13, t0, t14);
    cycPort.registerRiderResultsInStage(2, 14, t0, t15);
    cycPort.registerRiderResultsInStage(2, 15, t0, t16);

    //System.out.println(Arrays.toString(cycPort.getRidersPointsInRace(0))+" final");
    //System.out.println(Arrays.toString(cycPort.getRidersPointsInRace(0))+" final");
    //System.out.println(Arrays.toString(cycPort.getRaceStages(0)));

    assert cycPort.getRidersPointsInRace(0)[0]  == 100;
    assert cycPort.getRidersPointsInRace(0)[1]  == 60;
    assert cycPort.getRidersPointsInRace(0)[2]  == 40;
    assert cycPort.getRidersPointsInRace(0)[3]  == 36;
    assert cycPort.getRidersPointsInRace(0)[4]  == 32;
    assert cycPort.getRidersPointsInRace(0)[5]  == 28;
    assert cycPort.getRidersPointsInRace(0)[6]  == 24;
    assert cycPort.getRidersPointsInRace(0)[7]  == 20;
    assert cycPort.getRidersPointsInRace(0)[8]  == 16;
    assert cycPort.getRidersPointsInRace(0)[9]  == 14;
    assert cycPort.getRidersPointsInRace(0)[10] == 12;
    assert cycPort.getRidersPointsInRace(0)[11] == 10;
    assert cycPort.getRidersPointsInRace(0)[12] == 8;
    assert cycPort.getRidersPointsInRace(0)[13] == 6;
    assert cycPort.getRidersPointsInRace(0)[14] == 4;
    assert cycPort.getRidersPointsInRace(0)[15] == 0;
  }

  public static void main(String[] args) throws InvalidNameException, IllegalNameException, NameNotRecognisedException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException, InvalidLocationException, InvalidStageTypeException, DuplicatedResultException, InvalidCheckpointsException {
    //testCreateRace();
    //testRemoveRaceByName();
    //testCreateTeam();
    //testCreateRider();
    //testAddStageToRace();
    //testConcludeStagePreparations();
    //testRemoveTeam();
    //testRemoveRaceById();
    //testRemoveRider();
    //testGetTeamRiders();
    //testGetTeams();
    //testGetRaceIds();
    //testGetNumberOfStages();
    //testViewRaceDetails();
    //testRemoveStageById();
    //testGetStageLength();
    //testGetRaceStages();
    //testAddIntermediateSprintToStage();
    //testAddCategorizedClimbToStage();
    //testGetStageSegments();
    //testRemoveSegment();
    //testRegisterRiderResultsInStage();
    //testGetRidersRankInStage();
    //testGetRidersPointsInStage();
    //testGetRidersMountainPointsInStage();
    testGetRidersPointsInRace();
  }
}
