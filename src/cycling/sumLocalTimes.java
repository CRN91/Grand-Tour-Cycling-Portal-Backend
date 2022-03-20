package src.cycling;

import java.time.LocalTime;

public class sumLocalTimes {
  public static double localTimeToSeconds(LocalTime time) {
    return (time.getHour() * 3600)
        + (time.getMinute() * 60)
        + (time.getSecond());
  }

  public static LocalTime secondsToLocalTime(double timeSeconds) {
    double hours = Math.floor(timeSeconds / 3600);
    double minutes = Math.floor(timeSeconds / 60);
    double seconds = timeSeconds - (hours * 3600) - (minutes * 60);
    System.out.println(seconds+"seconds");
    return LocalTime.of((int)hours, (int)minutes, (int)seconds);
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
