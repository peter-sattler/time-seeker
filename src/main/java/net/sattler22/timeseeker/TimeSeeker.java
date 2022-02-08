package net.sattler22.timeseeker;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

import net.jcip.annotations.Immutable;

/**
 * Time Seeker
 * <p>
 * Find the earliest valid 24-hour time (HH:MM:SS) that is possible with the given 6 digits
 * </p>
 *
 * @author Pete Sattler
 * @version December 2018 (brute-force)
 * @version February 2022 (rewrite)
 */
@Immutable
final class TimeSeeker {

    private static final String NO_SOLUTION_ERROR_MESSAGE_TEMPLATE = "No solution is possible for %s";
    private final int[] digits;

    /**
     * Constructs a new time seeker
     *
     * @param digits An array of exactly 6 positive digits
     * @throws NullPointerException When digits is <code>NULL</code>
     * @throws IllegalArgumentException When digits does not contain 6 positive values
     */
    TimeSeeker(int[] digits) {
        Objects.requireNonNull(digits, "Digits is required");
        this.digits = digits.clone();  //CAREFUL: Non-zero length arrays are always mutable!!!
        if (this.digits.length != 6 || Arrays.stream(this.digits).filter(v -> v < 0).count() > 0)
            throw new IllegalArgumentException("Must have exactly 6 positive digits");
    }

    /**
     * Find the earliest time
     *
     * @return The earliest local time
     * @throws TimeFittingException When no solution is possible
     */
    LocalTime findEarliest() {
        //Loop thru each unique combination of indices, using each digit exactly once per attempt:
        LocalTime result = null;
        for (var hoursLeft = 0; hoursLeft < digits.length; hoursLeft++)
            for (var hoursRight = 0; hoursRight < digits.length; hoursRight++) {
                if (hoursRight == hoursLeft)
                    continue;
                for (var minutesLeft = 0; minutesLeft < digits.length; minutesLeft++) {
                    if (minutesLeft == hoursLeft || minutesLeft == hoursRight)
                        continue;
                    for (var minutesRight = 0; minutesRight < digits.length; minutesRight++) {
                        if (minutesRight == hoursLeft || minutesRight == hoursRight || minutesRight == minutesLeft)
                            continue;
                        for (var secondsRight = 0; secondsRight < digits.length; secondsRight++) {
                            if (secondsRight == hoursLeft || secondsRight == hoursRight ||
                                secondsRight == minutesLeft || secondsRight == minutesRight)
                                continue;
                            for (var secondsLeft = 0; secondsLeft < digits.length; secondsLeft++) {
                                if (secondsLeft == hoursLeft || secondsLeft == hoursRight ||
                                    secondsLeft == minutesLeft || secondsLeft == minutesRight || secondsLeft == secondsRight)
                                    continue;
                                final var hours = toTimeComponent(hoursLeft, hoursRight);
                                final var minutes = toTimeComponent(minutesLeft, minutesRight);
                                final var seconds = toTimeComponent(secondsLeft, secondsRight);
                                //Validate the time components:
                                if (hours < 24 && minutes < 60 && seconds < 60) {
                                    final var newResult = LocalTime.of(hours, minutes, seconds);
                                    result = result == null ? newResult : min(result, newResult);
                                }
                        }
                    }
                }
            }
        }
        if (result == null)
            throw new TimeFittingException(String.format(NO_SOLUTION_ERROR_MESSAGE_TEMPLATE, Arrays.toString(digits)));
        return result;
    }

    /**
     * Convert 2 digits into a time component
     *
     * @param tensIndex The index of the ten's (left) digit
     * @param onesIndex The index of the one's (right) digit
     * @return The time component value (hours, minutes or seconds)
     */
    private int toTimeComponent(int tensIndex, int onesIndex) {
        return digits[tensIndex] * 10 + digits[onesIndex];
    }

    /**
     * Find the minimum time
     *
     * @param localTime1 A local time
     * @param localTime2 Another local time
     * @return The lesser of the two <code>LocalTime</code> values or the value itself if they are equal
     */
    private static LocalTime min(LocalTime localTime1, LocalTime localTime2) {
        return localTime1.isBefore(localTime2) || localTime1.equals(localTime2) ? localTime1 : localTime2;
    }

    @Override
    public String toString() {
        return String.format("%s [data=%s]", getClass().getSimpleName(), Arrays.toString(digits));
    }
}
