package src.cycling;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Teams are made up of riders and can be placed into races.
 *
 * @author Adam Kaizra, Sam Barker
 * @version 1.0
 */
public class Team implements Serializable {

  private static int latestId = 0; // Enumerates to get unique id, with 2^32 possible ids.
  private String name;
  private String description;
  private int id;
  // Creates a hash map between the team's rider's Ids and the rider objects.
  private HashMap<Integer, Rider> riderIdsToRiders = new HashMap<Integer, Rider>();

  /**
   * Constructor.
   *
   * @param name
   */
  public Team(String name, String description) {
    this.name = name;
    this.description = description;
    this.id = latestId++;
  }

  /**
   * Reset the internal ID counter.
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  /**
   * @return The name of the team.
   */
  public String getName() {
    return this.name;
  }

  /**
   * @param newTeamName
   */
  public void setName(String newTeamName) {
    this.name = newTeamName;
  }

  /**
   * @return The description of the team.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * @return The ID of the team.
   */
  public int getId() {
    return this.id;
  }

  /**
   * @return A hash map between the team's rider's IDs and their corresponding rider objects.
   */
  public HashMap<Integer, Rider> getRiderIdsToRiders() {
    return riderIdsToRiders;
  }

  /**
   * @param rider A rider object to be added to the team's internal hashmap of rider ID's to riders.
   */
  public void addRider(Rider rider) {
    this.riderIdsToRiders.put(rider.getId(), rider);
  }
}