package src.cycling;

import src.cycling.Rider;
import java.util.HashMap; // import the HashMap class

public class Team {
  private String teamName;
  private int teamId;
  //Creates a hash map between the rider's team's Ids and the rider objects
  HashMap<Integer, Rider> riderIdsToRiders = new HashMap<Integer, Rider>();

  private static int latestTeamId = 0;

  /**
   * Return teamName.
   * @return Team.teamName - The name of the team.
   */
  public String getTeamName() {
    return this.teamName;
  }

  /**
   * Return teamId.
   * @return teamId The ID of this particular team.
   */
  public int getTeamId() {
    return this.teamId;
  }

  /**
   * Set teamName.
   * @param newTeamName The team's new name.
   */
  public void setTeamName(String newTeamName) {
    this.teamName = newTeamName;
  }

  /**
   * Return riderIdsToRiders.
   * @return riderIdsToRiders The hash map between the team's rider's ID's and their
   * corresponding objects.
   */
  public HashMap<Integer, Rider> getRiderIdsToRiders() {
    return riderIdsToRiders;
  }

  /**
   * Add a rider to the team.
   * @param rider A rider object.
   */
  public void addRider(Rider rider) {
    this.riderIdsToRiders.put(rider.getRiderId(), rider);
  }

  /**
   * Remove a rider from the team.
   * @param riderId A rider's Id.
   */
  public void removeRider(Integer riderId) {
    this.riderIdsToRiders.remove(riderId);
  }

  /**
   * Constructor
   * @param teamName
   */
  public Team(String teamName) {
    this.teamName = teamName;
    this.teamId = latestTeamId++;
  }

  /**
   * For back of the napkin testing purposes, comment out when not testing
   * @param args Command line args.
   */
   /**public static void main(String[] args) {
    Team testTeam1 = new Team("PricklyPenguins");
    System.out.println(testTeam1.getTeamName());
    System.out.println(testTeam1.getTeamId());
    Rider testRider1 = new Rider("Brian", testTeam1.getTeamId(), 1999);
    testTeam1.addRider(testRider1);
    System.out.println(testTeam1.getRiderIdsToRiders());

    Team testTeam2 = new Team("AltruisticAlbatrosses");
    System.out.println(testTeam2.getTeamName());
    System.out.println(testTeam2.getTeamId());
     Rider testRider2 = new Rider("Ocean", testTeam2.getTeamId(), 1997);
     Rider testRider3 = new Rider("Frank", testTeam2.getTeamId(), 1989);
     testTeam2.addRider(testRider2);
     testTeam2.addRider(testRider3);
     System.out.println(testTeam2.getRiderIdsToRiders());
    }
    */
}
