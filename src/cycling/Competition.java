package src.cycling;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashMap;

public class Competition implements Serializable {
  //protected int competitionType;// 0 = General 1 = Mountains 2 = Points
  protected HashMap<Integer, Rider> riderIdsToRiders = new HashMap<>();
  protected int id;
  protected HashMap<Integer, Integer> finalResults = new HashMap<>();

  private static int latestId = 0; // enumerates to get unique id, with 2^32 possible ids.

  public int getId() {
    return id;
  }

  public void addRider(int riderId, Rider rider){
    riderIdsToRiders.put(riderId, rider);
  }

  public HashMap<Integer, Integer> getFinalResults() {
    return finalResults;
  }

  public HashMap<Integer, LocalTime> getStageResults(int stageId, int rankLimit){// might be easier directly from register results
    // TODO get the top rankLimit stage results
    return null;
  }

  public HashMap<Integer, LocalTime> getAdjustedTimes(){
    // TODO getRiderResultsInStage for all riders, sort these times and check a rider with the cached time
    // and if time < 1 then cache riders time and then change the riders time to the previous riders time
    return null;
  }

  public Competition() {
    //this.competitionType = competitionType;
    //this.riderIdsToRiders = riderIdsToRiders;
    this.id = latestId++;
  }
}
