package src.cycling;

import java.util.Random;
import java.util.UUID;


public class Rider {
    private String riderName;
    private int riderId;
    private int riderTeamId;

    private int riderYearOfBirth;

    Random rand;
    // Initialise proper random and instantiate

    private int latestRiderId = rand.nextInt();
    // THEN CHECK THE ID AGAINST ALL OTHERS AND REASSIGN IF NECESSARY

    /**
     * Return riderName.
     * @return Rider.riderName - The name of the rider.
     */
    public String getRiderName() {
        return this.riderName;
    }

    /**
     * Return riderId.
     * @return
     */
    public int getRiderId() {
        return this.riderId;
    }

    /**
     * Return riderTeamId.
     * @return
     */
    public int getRiderTeamId() {
        return this.riderTeamId;
    }

    /**
     * Return riderYearOfBirth.
     * @return
     */
    public int getRiderYearOfBirth() {
        return riderYearOfBirth;
    }

    /**
     * Set riderName.
     * @param newRiderName
     */
    public void setRiderName(String newRiderName) {
        this.riderName = newRiderName;
    }

    /**
     * Set riderTeamId.
     * @param newRiderTeamId
     */
    public void setRiderTeamId(int newRiderTeamId) {
        this.riderTeamId = newRiderTeamId;
    }

    /**
     * Set riderYearOfBirth.
     * @param riderYearOfBirth
     */
    public void setRiderYearOfBirth(int riderYearOfBirth) {
        this.riderYearOfBirth = riderYearOfBirth;
    }

    /**
     * Constructor for Rider.
     * @param riderName
     */
    public Rider(String riderName) {
        this.riderName = riderName;
        // PLACEHOLDER FOR ID ASSIGNMENT
    }

    /**
     * For back of the napkin testing purposes, comment out when not testing
     * @param args
     */
    public static void main(String[] args) {
        Rider testRider1 = new Rider("John Doe");
        System.out.println(testRider1.getRiderName());
        System.out.println(testRider1.getRiderId());
        Rider testRider2 = new Rider("Jane Doe");
        System.out.println(testRider2.getRiderName());
        System.out.println(testRider2.getRiderId());
    }
}
