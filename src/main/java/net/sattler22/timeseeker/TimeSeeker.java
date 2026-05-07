package net.sattler22.timeseeker;

import net.jcip.annotations.Immutable;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * Time Seeker
 * <p>
 * Find the earliest valid 24-hour time (HH:MM:SS) that is possible with the given six digits
 * </p>
 *
 * @author Pete Sattler
 * @since December 2018
 * @version May 2026
 */
@Immutable
final class TimeSeeker {

    private final int[] digits;

    /**
     * Constructs a new time seeker
     *
     * @param digits An array of exactly six non-negative digits [0-9]
     */
    TimeSeeker(int[] digits) {
        Objects.requireNonNull(digits, "Digits is required");
        this.digits = digits.clone();  //CAREFUL: Non-zero length arrays are always mutable!!!
        if (this.digits.length != 6 || Arrays.stream(this.digits).anyMatch(digit -> digit < 0 || digit > 9)) {
            throw new IllegalArgumentException("Must have exactly 6 non-negative digits [0-9]");
        }
    }

    /**
     * Find the earliest time
     *
     * @return The earliest local time
     * @throws TimeFittingException When no solution is possible
     */
    LocalTime findEarliest() {
        //Loop through each unique combination of indices, using each digit exactly once per attempt:
        LocalTime result = null;
        for (int hoursLeft = 0; hoursLeft < digits.length; hoursLeft++) {
            for (int hoursRight = 0; hoursRight < digits.length; hoursRight++) {
                if (hoursRight == hoursLeft)
                    continue;
                for (int minutesLeft = 0; minutesLeft < digits.length; minutesLeft++) {
                    if (minutesLeft == hoursLeft || minutesLeft == hoursRight)
                        continue;
                    for (int minutesRight = 0; minutesRight < digits.length; minutesRight++) {
                        if (minutesRight == hoursLeft || minutesRight == hoursRight || minutesRight == minutesLeft)
                            continue;
                        for (int secondsRight = 0; secondsRight < digits.length; secondsRight++) {
                            if (secondsRight == hoursLeft || secondsRight == hoursRight ||
                                secondsRight == minutesLeft || secondsRight == minutesRight)
                                continue;
                            for (int secondsLeft = 0; secondsLeft < digits.length; secondsLeft++) {
                                if (secondsLeft == hoursLeft || secondsLeft == hoursRight ||
                                        secondsLeft == minutesLeft || secondsLeft == minutesRight || secondsLeft == secondsRight)
                                    continue;
                                final int hours = toTimeComponent(hoursLeft, hoursRight);
                                final int minutes = toTimeComponent(minutesLeft, minutesRight);
                                final int seconds = toTimeComponent(secondsLeft, secondsRight);
                                //Validate the time components:
                                if (hours < 24 && minutes < 60 && seconds < 60) {
                                    final LocalTime newResult = LocalTime.of(hours, minutes, seconds);
                                    result = result == null ? newResult : min(result, newResult);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (result == null)
            throw new TimeFittingException("No solution is possible for %s".formatted(Arrays.toString(digits)));
        return result;
    }

    /**
     * Convert two digits into a time component
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
     * @param left A local time
     * @param right Another local time
     * @return The lesser of the two {@code LocalTime} values or the value itself if they are equal
     */
    private static LocalTime min(LocalTime left, LocalTime right) {
        return !left.isAfter(right) ? left : right;
    }

    @Override
    public String toString() {
        return "%s [digits=%s]".formatted(getClass().getSimpleName(), Arrays.toString(digits));
    }
}
