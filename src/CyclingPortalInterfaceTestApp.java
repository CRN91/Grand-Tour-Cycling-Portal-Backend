package src;

import java.io.*;
import src.cycling.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    assert cycPort.getRiderIdsToRiders().get(0).getName().length() == 12;

    // name invalid whitespace
    try {
      cycPort.createRider(0, "        ", 1995);
    } catch (IllegalArgumentException ex) {
      System.out.println("Invalid whitespace dfailed successfully");
    }

    // yOB = 1900
    cycPort.createRider(0, "OldAlan", 1900);
    assert cycPort.getRiderIdsToRiders().get(0).getName() == "OldAlan";

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
    assert cycPort.getStageIdsToStages().get(1).getLength() == 5.0;


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
  }
}
