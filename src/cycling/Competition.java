package src.cycling;

import java.time.LocalTime;
import java.util.HashMap;

public class Competition {
  protected int competitionType;// 0 = General 1 = Mountains 2 = Points
  protected HashMap<Integer, Rider> riderIdsToRiders;
  protected int id;
  protected HashMap<Integer, Integer> finalResults;

  private static int latestId = 0; // enumerates to get unique id, with 2^32 possible ids.

  public int getId() {
    return id;
  }

  public HashMap<Integer, Integer> getFinalResults() {
    return finalResults;
  }

  public HashMap<Integer, LocalTime> getStageResults(int stageId, int rankLimit){// might be easier directly from register results
    // TODO get the top rankLimit stage results
    return null;
  }

  public Competition(int competitionType, HashMap<Integer, Rider> riderIdsToRiders) {
    this.competitionType = competitionType;
    this.riderIdsToRiders = riderIdsToRiders;
    this.id = latestId++;
  }
}
