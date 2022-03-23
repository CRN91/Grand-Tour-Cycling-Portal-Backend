package src.cycling;

/**
 * Represents a categorised climb segment in a race. This is a subclass of Segment (which
 * represents an intermediate sprint) as they share common features.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class CategorisedClimb extends Segment {

  private static int latestId = 0;
  private final int id;
  private int stageId;
  private final double averageGradient;
  private final double length;
  private SegmentType segmentType;

  /**
   * Constructor.
   *
   * @param stageId The ID of the stage that this segment is part of.
   * @param segmentType The SegmentType of this climb (i.e. HC, C1...3)
   * @param averageGradient The average gradient of the climb segment
   * @param length The length (distance from segment start point to end) of the climb segment.
   * @param location The location in the stage at which the categorised climb starts.
   */
  public CategorisedClimb(int stageId, SegmentType segmentType, Double averageGradient,
      Double length, Double location) {
    super(stageId, segmentType, location);
    this.averageGradient = averageGradient;
    this.length = length;
    this.id = latestId++;
  }

  /**
   * Reset the static ID counter to 0. Used after deletion of a CyclingPortal.
   */
  public static void resetIdCounter() {
    latestId = 0;
  }

  /**
   * @return The length (in distance) of the categorised climb.
   */
  public double getLength() {
    return this.length;
  }

  /**
   * @return The average gradient of the categorised climb.
   */
  public double getAverageGradient() {
    return this.averageGradient;
  }
}
