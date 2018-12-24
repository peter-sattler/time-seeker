package net.sattler22.timeseeker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Time Seeker - Find the earliest valid 24-hour time (HH:MM:SS) that is possible with the given 6 digits
 * <p/>
 * <b>NOTE:</b> Encountered this problem at the 12/3/2018 Montefiore on-site interview.
 *
 * @author Pete Sattler
 * @version December 2018
 */
public final class TimeSeeker {

    private final int[] data;

    /**
     * Constructs a new time seeker
     *
     * @param data An array of exactly 6 digits
     */
    public TimeSeeker(int[] data) {
        Objects.requireNonNull(data, "Data is required");
        if (data.length != 6)
            throw new IllegalArgumentException("Must have exactly 6 digits");
        this.data = data;
    }

    /**
     * Find the earliest time
     *
     * @return The earliest 24-time in HH:MM:SS format
     * @throws TimeFittingException When no solution is possible
     */
    public synchronized String fitEarliest() throws TimeFittingException {
        //First slot gets assigned smallest value between 0 and 2:
        final TimeFittingEngine timeFittingEngine = new TimeFittingEngine(data);
        final TimeSlot hourLeftSlot = TimeSlot.HOUR_LEFT_SLOT;
        final int leftHourDigit = timeFittingEngine.fitMin(hourLeftSlot, 0, 2);
        if (leftHourDigit == Integer.MAX_VALUE)
            throw new TimeFittingException(String.format("%s could not be fitted", hourLeftSlot.getDescription()));

        //For the right hour slot, try each remaining digit and see if the minutes and seconds can be fitted correctly: 
        TimeFittingEngine timeFittingCheckpoint = new TimeFittingEngine(timeFittingEngine);
        for (int index = 0; index < timeFittingCheckpoint.getRemaining().length; index++) {
            final int rightHourDigit = timeFittingCheckpoint.getRemaining()[index];
            if (leftHourDigit == 2 && rightHourDigit > 3)
                continue;
            timeFittingCheckpoint.setMin(TimeSlot.HOUR_RIGHT_SLOT, rightHourDigit);
            if (fitMinutesAndSeconds(timeFittingCheckpoint))
                break;
            timeFittingCheckpoint = new TimeFittingEngine(timeFittingEngine);
        }
        timeFittingCheckpoint.assertHasAnswer();

        return timeFittingCheckpoint.format24Hour().get();
    }

    /**
     * Find two numbers between 0 and 59 for the minutes and seconds
     *
     * @return True if the fit was successful. Otherwise, returns false.
     */
    private boolean fitMinutesAndSeconds(TimeFittingEngine timeFittingEngine) {
        final int[] digits = timeFittingEngine.getRemaining();
        for (int tensDigitIndex = 0; tensDigitIndex < digits.length; tensDigitIndex++) {
            final int tensDigit = digits[tensDigitIndex];
            if (tensDigit >= 0 && tensDigit <= 5) {
                final List<Integer> remainingTensDigits = Arrays.stream(digits).boxed().collect(Collectors.toList());
                remainingTensDigits.remove(tensDigitIndex);  // Delete the ten's digit
                for (int onesDigitIndex = 0; onesDigitIndex < remainingTensDigits.size(); onesDigitIndex++) {
                    final List<Integer> remainingOnesDigits = new ArrayList<>(remainingTensDigits);
                    remainingOnesDigits.remove(onesDigitIndex); // Delete one's digit
                    final Integer seconds = remainingOnesDigits.get(0) * 10 + remainingOnesDigits.get(1);
                    if (seconds >= 0 && seconds <= 59) {
                        timeFittingEngine.setMin(TimeSlot.SECONDS_LEFT_SLOT, remainingOnesDigits.get(0));
                        timeFittingEngine.setMin(TimeSlot.SECONDS_RIGHT_SLOT, remainingOnesDigits.get(1));
                        timeFittingEngine.fitMin(TimeSlot.MINUTE_LEFT_SLOT, 0, 5);
                        timeFittingEngine.fitMin(TimeSlot.MINUTE_RIGHT_SLOT, 0, 9);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s [data=%s]", getClass().getSimpleName(), Arrays.toString(data));
    }
}
