package src.cycling;

import java.util.Random;
import java.util.UUID;

/**
 * Each cyclist is treated as an object that can be assigned to a team and compete in competitions.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 *
 */
public class Rider {
  private String name;
  private int id;
  private int teamId;
  private int yearOfBirth;

  private static int latestId = 0; // enumerates to get unique id, with 2^32 possible ids

  /**
   * @return The rider's name.
   */
  public String getRiderName() {
    return this.name;
  }

  /**
   * @return The rider's ID.
   */
  public int getRiderId() {
    return this.id;
  }

  /**
   * @return The ID of the team the rider is in.
   */
  public int getRiderTeamId() {
    return this.teamId;
  }

  /**
   * @return The rider's year of birth.
   */
  public int getRiderYearOfBirth() {
    return yearOfBirth;
  }

  /**
   * @param newRiderName
   */
  public void setRiderName(String newRiderName) {
    this.name = newRiderName;
  }

  /**
   * @param teamId
   */
  public void setRiderTeamId(int teamId) {
    this.teamId = teamId;
  }

  /**
   * @param yearOfBirth
   */
  public void setRiderYearOfBirth(int yearOfBirth) {
    this.yearOfBirth = yearOfBirth;
  }

  /**
   * Constructor
   * @param name Rider's name
   * @param teamId The ID of the team the rider belongs to
   * @param yearOfBirth The rider's year of birth
   */
  public Rider(String name, int teamId, int yearOfBirth) {
    this.name = name;
    this.teamId = teamId;
    this.yearOfBirth = yearOfBirth;
    this.id = latestId++;
  }

  /**
   * For back of the napkin testing purposes, comment out when not testing
   * @param args Command line args.
   */
  /*public static void main(String[] args) {
    Rider testRider1 = new Rider("John Doe", 1997, 1997);
    System.out.println(testRider1.getRiderName());
    System.out.println(testRider1.getRiderId());
    System.out.println(testRider1.getRiderYearOfBirth());
    Rider testRider2 = new Rider("Jane Doe", 1998, 2001);
    System.out.println(testRider2.getRiderName());
    System.out.println(testRider2.getRiderId());
    System.out.println(testRider2.getRiderYearOfBirth());
  }*/
}
