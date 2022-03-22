package src.cycling;

import java.time.LocalTime;
import java.util.HashMap;

public class GeneralClassification extends Competition {

  protected HashMap<Integer, LocalTime> finalResults;

  public GeneralClassification() {
    super();
  }

  public void sortTimesByAscendingOrder() { // probably will not be used here will be in  getGeneralClassificationTimesInRace
    // TODO sort finalResults or something by ascending order
  }

}

