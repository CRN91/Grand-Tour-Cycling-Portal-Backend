package src.cycling;

import java.time.LocalTime;

/**
 * A helper object whose static methods deal with the addition of LocalTimes
 * and conversion between seconds (doubles) and LocalTime objects, of times.
 */
public class SumLocalTimes {

  /**
   * Convert a LocalTime object to the number of seconds after midnight.
   * @param time
   * @return The time after midnight in seconds that a LocalTime object represents.
   */
  public static double localTimeToSeconds(LocalTime time) {
    if (time == null) {
      return 0;
    }
    return (time.getHour() * 3600)
        + (time.getMinute() * 60)
        + (time.getSecond());
  }

  /**
   * Create a LocalTime object from the number of seconds after midnight.
   * @param timeSeconds
   * @return The LocalTime object representing the time given.
   */
  public static LocalTime secondsToLocalTime(double timeSeconds) {
    int hours = (int) (timeSeconds / 3600);
    int minutes = (int) (timeSeconds / 60) - (hours * 60);
    int seconds = (int) ((timeSeconds % 3600) % 60);
    return LocalTime.of(hours, minutes, seconds);
  }

  /**
   * Add an arbitrary number of LocalTimes together.
   * @param times
   * @return The sum of the given LocalTimes.
   */
  public static LocalTime addLocalTimes(LocalTime... times) {
    // Convert the LocalTimes to seconds, sum them all up, then convert back to LocalTime
    double finalTimeSeconds = 0;
    for (LocalTime time : times) {
      double timeSeconds = localTimeToSeconds(time);
      finalTimeSeconds += timeSeconds;
    }

    return secondsToLocalTime(finalTimeSeconds);
  }
}
