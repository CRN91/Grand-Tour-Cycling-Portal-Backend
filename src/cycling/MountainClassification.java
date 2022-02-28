package src.cycling;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class MountainClassification extends Competition{

  public MountainClassification() {
    super();
  }

  protected ArrayList<Integer> sortResultsByRankOrder(HashMap<Integer, LocalTime> riderIdsToTimes){
    //


    return null;
  }

  protected HashMap<Integer,Integer> rankOrderToPoints(ArrayList<Integer> riderIdsInRankOrder){
    // TODO convert rank order to points with table
    return null;
  }

  public HashMap<Integer, LocalTime> getSegmentResults(int segmentId, int rankLimit){
    // TODO get the top rankLimit stage results
    return null;
  }

}
