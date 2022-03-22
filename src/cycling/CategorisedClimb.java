package src.cycling;

public class CategorisedClimb extends Segment {

  private static int latestId = 0;
  private final int id;
  private int stageId;
  private final double averageGradient;
  private final double length;
  private SegmentType segmentType;

  public CategorisedClimb(int stageId, SegmentType segmentType, Double averageGradient,
      Double length, Double location) {
    super(stageId, segmentType, location);
    this.averageGradient = averageGradient;
    this.length = length;
    this.id = latestId++;
  }

  public static void resetIdCounter() {
    latestId = 0;
  }

  public double getLength() {
    return this.length;
  }

  public double getAverageGradient() {
    return this.averageGradient;
  }
}
