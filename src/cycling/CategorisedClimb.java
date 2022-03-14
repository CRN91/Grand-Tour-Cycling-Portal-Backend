package src.cycling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CategorisedClimb extends Segment {
  private int id;
  private int stageId;
  private double averageGradient;
  private double length;
  private SegmentType segmentType;

  private static int latestId = 0;

  public double getLength() {
    return this.length;
  }

  public double getAverageGradient() {
    return this.averageGradient;
  }

  public static void resetIdCounter() {
    latestId = 0;
  }

  public CategorisedClimb(int stageId, SegmentType segmentType, Double averageGradient,
              Double length, Double location) {
    super(stageId, segmentType, location);
    this.averageGradient = averageGradient;
    this.length = length;
    this.id = latestId++;
  }
}
