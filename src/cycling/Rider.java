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
  private String riderName;
  private int riderId;
  private int riderTeamId;
  private int riderYearOfBirth;

  private static int latestRiderId = 0; // enumerates to get unique id, with 2^32 possible ids

  /**
   * @return The rider's name.
   */
  public String getRiderName() {
    return this.riderName;
  }

  /**
   * @return The rider's ID.
   */
  public int getRiderId() {
    return this.riderId;
  }

  /**
   * @return The ID of the team the rider is in.
   */
  public int getRiderTeamId() {
    return this.riderTeamId;
  }

  /**
   * @return The rider's year of birth.
   */
  public int getRiderYearOfBirth() {
    return riderYearOfBirth;
  }

  /**
   * @param newRiderName
   */
  public void setRiderName(String newRiderName) {
    this.riderName = newRiderName;
  }

  /**
   * @param riderTeamId
   */
  public void setRiderTeamId(int riderTeamId) {
    this.riderTeamId = riderTeamId;
  }

  /**
   * @param riderYearOfBirth
   */
  public void setRiderYearOfBirth(int riderYearOfBirth) {
    this.riderYearOfBirth = riderYearOfBirth;
  }

  /**
   * Constructor
   * @param riderName Rider's name
   * @param riderTeamId The ID of the team the rider belongs to
   * @param riderYearOfBirth The rider's year of birth
   */
  public Rider(String riderName, int riderTeamId, int riderYearOfBirth) {
    this.riderName = riderName;
    this.riderTeamId = riderTeamId;
    this.riderYearOfBirth = riderYearOfBirth;
    this.riderId = latestRiderId++;
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
