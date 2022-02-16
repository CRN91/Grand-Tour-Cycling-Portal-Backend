package src.cycling;

import java.util.Random;
import java.util.UUID;

public class Rider {
    private String riderName;
    private int riderId;
    private int riderTeamId;
    private int riderYearOfBirth;

    private static int latestRiderId = 0;

    /**
     * Return riderName.
     * @return Rider.riderName - The name of the rider.
     */
    public String getRiderName() {
        return this.riderName;
    }

    /**
     * Return riderId.
     * @return riderId The ID of this particular rider.
     */
    public int getRiderId() {
        return this.riderId;
    }

    /**
     * Return riderTeamId.
     * @return riderTeamId The ID of the team the rider belongs to.
     */
    public int getRiderTeamId() {
        return this.riderTeamId;
    }

    /**
     * Return riderYearOfBirth.
     * @return riderYearOfBirth The rider's year of birth
     */
    public int getRiderYearOfBirth() {
        return riderYearOfBirth;
    }

    /**
     * Set riderName.
     * @param newRiderName The rider's new name.
     */
    public void setRiderName(String newRiderName) {
        this.riderName = newRiderName;
    }

    /**
     * Set riderTeamId.
     * @param riderTeamId The ID of the team the rider belongs to.
     */
    public void setRiderTeamId(int riderTeamId) {
        this.riderTeamId = riderTeamId;
    }

    /**
     * Set riderYearOfBirth.
     * @param riderYearOfBirth The rider's new year of birth
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
