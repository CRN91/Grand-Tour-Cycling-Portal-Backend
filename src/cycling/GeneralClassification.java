package src.cycling;

import java.time.LocalTime;
import java.util.HashMap;

public class GeneralClassification extends Competition{
  public GeneralClassification() {
    super();
  }
  protected HashMap<Integer, LocalTime> finalResults;

  public void sortTimesByAscendingOrder(){ // probably will not be used here will be in  getGeneralClassificationTimesInRace
    // TODO sort finalResults or something by ascending order
  }

}

