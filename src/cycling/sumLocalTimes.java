package src.cycling;

import java.time.LocalTime;

public class sumLocalTimes {
  public static double localTimeToSeconds(LocalTime time) {
    if (time == null) {
      return 0;
    }
    return (time.getHour() * 3600)
        + (time.getMinute() * 60)
        + (time.getSecond());
  }

  public static LocalTime secondsToLocalTime(double timeSeconds) {
    int hours = (int) (timeSeconds/3600);
    int minutes = (int) (timeSeconds/60) - (hours * 60);
    int seconds = (int) ((timeSeconds % 3600) % 60);
    return LocalTime.of(hours, minutes, seconds);
  }

  public static LocalTime addLocalTimes(LocalTime... times) {
    double finalTimeSeconds = 0;
    for (LocalTime time : times) {
      double timeSeconds = localTimeToSeconds(time);
      finalTimeSeconds += timeSeconds;
    }

    return secondsToLocalTime(finalTimeSeconds);
  }
}
