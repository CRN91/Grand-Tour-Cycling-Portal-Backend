package src.cycling;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class Result implements Comparable<Result> {
  private int id;
  private int riderId;
  //private ArrayList<LocalTime> times; //LATER
  private LocalTime finishTime;

  private static int latestId;

  @Override
  public int compareTo(Result result) {
    return this.getFinishTime().compareTo(result.getFinishTime());
  }

  public int getId() {
    return id;
  }

  public int getRiderId() {
    return riderId;
  }

  public LocalTime getFinishTime() {
    return finishTime;
  }

  public Result(int riderId, LocalTime finishTime) {
    this.riderId = riderId;
    this.finishTime = finishTime;
    this.id = latestId++;
  }
}
