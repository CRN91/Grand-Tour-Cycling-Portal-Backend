package src.cycling;

import java.util.ArrayList;
import java.util.HashMap;

public class PointsClassification extends MountainClassification{

  public PointsClassification(int competitionType, HashMap<Integer, Rider> riderIdsToRiders) {
    super(competitionType, riderIdsToRiders);
  }

  protected HashMap<Integer,Integer> rankOrderToPoints(ArrayList<Integer> riderIdsInRankOrder){
    // TODO convert rank order to points with table
    return null;
}
