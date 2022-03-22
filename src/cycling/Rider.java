package src.cycling;

import java.io.Serializable;

/**
 * Each cyclist is treated as an object that can be assigned to a team and compete in competitions.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class Rider implements Serializable {

  private static int latestId = 0; // enumerates to get unique id, with 2^32 possible ids
  private String name;
  private final int id;
  private int teamId;
  private int yearOfBirth;

  /**
   * Constructor
   *
   * @param name        Rider's name
   * @param teamId      The ID of the team the rider belongs to
   * @param yearOfBirth The rider's year of birth
   */
  public Rider(String name, int teamId, int yearOfBirth) {
    this.name = name;
    this.teamId = teamId;
    this.yearOfBirth = yearOfBirth;
    this.id = latestId++;
  }

  /**
   * Reset the internal ID counter
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  /**
   * @return The rider's name.
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param newRiderName
   */
  public void setName(String newRiderName) {
    this.name = newRiderName;
  }

  /**
   * @return The rider's ID.
   */
  public int getId() {
    return this.id;
  }

  /**
   * @return The ID of the team the rider is in.
   */
  public int getTeamId() {
    return this.teamId;
  }

  /**
   * @param teamId
   */
  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }

  /**
   * @return The rider's year of birth.
   */
  public int getYearOfBirth() {
    return yearOfBirth;
  }

  /**
   * @param yearOfBirth
   */
  public void setYearOfBirth(int yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }
}
