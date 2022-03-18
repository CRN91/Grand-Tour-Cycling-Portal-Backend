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

  public static void main(String[] args) throws InvalidNameException, IllegalNameException, NameNotRecognisedException, IDNotRecognisedException, InvalidLengthException, InvalidStageStateException {
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
    testViewRaceDetails();
  }
}
