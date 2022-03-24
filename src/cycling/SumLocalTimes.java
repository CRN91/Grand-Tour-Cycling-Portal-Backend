package src.cycling;

import java.time.LocalTime;

/**
 * A helper object whose static methods deal with the addition of LocalTimes
 * and conversion between seconds (doubles) and LocalTime objects, of times.
 *
 * @author Sam Barker, Adam Kaizra
 * @version 1.0
 */
public class SumLocalTimes {

  /**
   * Convert a LocalTime object to the number of seconds after midnight.
   *
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
   *
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
   *
   * @param times
   * @return The sum of the given LocalTimes.
   */
  public static LocalTime addLocalTimes(LocalTime... times) {
    // Convert the LocalTimes to seconds, sum them all up, then convert back to LocalTime.
    double finalTimeSeconds = 0;
    for (LocalTime time : times) {
      double timeSeconds = localTimeToSeconds(time);
      finalTimeSeconds += timeSeconds;
    }

    return secondsToLocalTime(finalTimeSeconds);
  }

  /**
   * Subtract two LocalTime variables.
   *
   * @param t1 The time to have t2 taken away from.
   * @param t2 The time to take away from t1
   * @return t1 minus t2
   */
  public static LocalTime subtractLocalTimes(LocalTime t1, LocalTime t2) {
    LocalTime finalTime;

    // Convert times to seconds
    double t1s = localTimeToSeconds(t1);
    double t2s = localTimeToSeconds(t2);
    // Subtract
    double t3s = t1s - t2s;

    assert t3s >= 0 : "LocalTime cannot represent negative values!";

    // Convert back to LocalTime and return
    return secondsToLocalTime(t3s);
  }

}
