package net.sattler22.timeseeker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Find the earliest valid 24-hour time (HH:MM:SS) that is possible with the given 6 digits
 * <p/>
 * <b>NOTE:</b> Encountered this problem at the 12/3/2018 Montefiore on-site interview.
 *
 * @author Pete Sattler
 * @version December 2018
 */
public final class TimeSeeker {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeeker.class);
    private final FittingResult fittingResult;

    /**
     * Constructs a new time seeker
     *
     * @param data An array of exactly 6 digits
     */
    public TimeSeeker(int[] data) {
        Objects.requireNonNull(data, "Data is required");
        if (data.length != 6)
            throw new IllegalArgumentException("Must have exactly 6 digits");
        this.fittingResult = new FittingResult(data);
    }

    /**
     * Find the earliest time
     *
     * @return The earliest 24-time in HH:MM:SS format or <code>Optional.empty()</code> if no solution is possible
     */
    public synchronized Optional<String> earliest() {
        // Put the smallest digit between 0 and 2 in the FIRST slot:
        if (fittingResult.fitMin(Slot.HOUR_LEFT_SLOT, 0, 2) == 2) {
            // Populate hours 20 through 23 only:
            fittingResult.fitMin(Slot.HOUR_RIGHT_SLOT, 0, 3);
            fitMinutesAndSeconds();
        } else {
            // Populate hours 00 through 19 (if needed):
            // First try fitting remaining hour slot, then minutes and seconds:
            fittingResult.fitMin(Slot.HOUR_RIGHT_SLOT, 0, 9);
            if (!fitMinutesAndSeconds()) {
                // Finally try fitting minutes and seconds, and then the remaining hour slot:
                // TODO: MORE WORK TO DO HERE !!!
                fitMinutesAndSeconds();
                fittingResult.fitMin(Slot.HOUR_RIGHT_SLOT, 0, 9);
            }
        }
        return fittingResult.format24Hour();
    }

    /**
     * Find two numbers between 0 and 59 for the minutes and seconds
     *
     * @return True if the fit was successful. Otherwise, returns false.
     */
    private boolean fitMinutesAndSeconds() {
        int[] digits = fittingResult.remaining;
        for (int tensDigitIndex = 0; tensDigitIndex < digits.length; tensDigitIndex++) {
            final int tensDigit = digits[tensDigitIndex];
            if (tensDigit >= 0 && tensDigit <= 5) {
                List<Integer> remainingTensDigits = Arrays.stream(digits).boxed().collect(Collectors.toList());
                remainingTensDigits.remove(tensDigitIndex);  // Delete the ten's digit
                for (int onesDigitIndex = 0; onesDigitIndex < remainingTensDigits.size(); onesDigitIndex++) {
                    List<Integer> remainingOnesDigits = new ArrayList<>(remainingTensDigits);
                    remainingOnesDigits.remove(onesDigitIndex); // Delete one's digit
                    final Integer seconds = remainingOnesDigits.get(0) * 10 + remainingOnesDigits.get(1);
                    if (seconds >= 0 && seconds <= 59) {
                        fittingResult.fitMin(Slot.MINUTE_LEFT_SLOT, 0, 5);
                        fittingResult.fitMin(Slot.MINUTE_RIGHT_SLOT, 0, 9);
                        fittingResult.fitMin(Slot.SECONDS_LEFT_SLOT, 0, 5);
                        fittingResult.fitMin(Slot.SECONDS_RIGHT_SLOT, 0, 9);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    static enum Slot {
        HOUR_LEFT_SLOT(0, "Hour slot #1"),
        HOUR_RIGHT_SLOT(1, "Hour slot #2"),
        MINUTE_LEFT_SLOT(2, "Minute slot #1"),
        MINUTE_RIGHT_SLOT(3, "Minute slot #2"),
        SECONDS_LEFT_SLOT(4, "Seconds slot #1"),
        SECONDS_RIGHT_SLOT(5, "Seconds slot #2");

        private int answerIndex;
        private String description;

        Slot(int answerIndex, String description) {
            this.description = description;
            this.answerIndex = answerIndex;
        }
    }

    static class FittingResult implements Serializable {

        private static final long serialVersionUID = 5216294670014465095L;
        private final int[] data;
        private volatile int[] answer;
        private volatile int[] remaining;

        FittingResult(int[] data) {
            this.data = data;
            this.answer = new int[data.length];
            Arrays.fill(this.answer, -1);
            this.remaining = Arrays.stream(data).sorted().toArray();
        }

        /**
         * Find the minimum value
         *
         * @param slotDesc The slot description
         * @param index The slot index
         * @param lowerBound The lower value (inclusive) bound
         * @param upperBound The upper value (inclusive) bound
         * @return The minimum value
         */
        int fitMin(Slot slot, int lowerBound, int upperBound) {
            int minValue = Integer.MAX_VALUE;
            for (int minIndex = 0; minIndex < remaining.length; minIndex++) {
                final int currentValue = remaining[minIndex];
                if (currentValue >= lowerBound && currentValue <= upperBound && currentValue < minValue) {
                    minValue = currentValue;
                    answer[slot.answerIndex] = minValue;
                    // Update remaining elements:
                    int[] temp = new int[remaining.length - 1];
                    for (int remainingIndex = 0, tempIndex = 0; remainingIndex < remaining.length; remainingIndex++) {
                        if (minIndex == remainingIndex) {
                            continue;
                        }
                        temp[tempIndex++] = remaining[remainingIndex];
                    }
                    remaining = temp;
                    LOGGER.info("{} [{}-{}] assigned [{}]", slot.description, lowerBound, upperBound, minValue);
                    break;
                }
            }
            if (minValue == Integer.MAX_VALUE)
                throw new IllegalStateException(String.format("%s was not fitted due to invalid time", slot.description));
            return minValue;
        }

        Optional<String> format24Hour() {
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < answer.length; i++) {
                builder.append(answer[i]);
                if (i == 1 || i == 3) {
                    builder.append(":");
                }
            }
            return Optional.of(builder.toString());
        }

        @Override
        public String toString() {
            return String.format("%s [data=%s, answer=%s, remaining=%s]", getClass().getSimpleName(), Arrays.toString(data),
                    Arrays.toString(answer), Arrays.toString(remaining));
        }
    }

    @Override
    public String toString() {
        return String.format("%s [fittingResult=%s]", getClass().getSimpleName(), fittingResult);
    }
}