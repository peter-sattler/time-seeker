package net.sattler22.timeseeker;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Time Seeker Time Fitting Engine
 *
 * @author Pete Sattler
 * @version December 2018
 */
public final class TimeFittingEngine implements Serializable {

    private static final long serialVersionUID = 5216294670014465095L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeFittingEngine.class);
    private transient int[] answer;
    private transient int[] remaining;

    /**
     * Constructs a new time fitting engine
     */
    public TimeFittingEngine(int[] data) {
        this.answer = new int[data.length];
        Arrays.fill(this.answer, -1);
        this.remaining = Arrays.stream(data).sorted().toArray();
    }

    /**
     * Copy constructs a new time fitting engine
     */
    public TimeFittingEngine(TimeFittingEngine source) {
        this.answer = source.getAnswer();
        this.remaining = source.getRemaining();
    }

    /**
     * Find and set the minimum value
     *
     * @param timeSlot The time slot
     * @param lowerBound The lower value (inclusive) bound
     * @param upperBound The upper value (inclusive) bound
     * @return The minimum value or <code>Integer.MAX_VALUE</code> if the minimum value can not be fitted
     */
    public int fitMin(TimeSlot timeSlot, int lowerBound, int upperBound) {
        int minValue = Integer.MAX_VALUE;
        for (int index = 0; index < remaining.length; index++) {
            final int currentValue = remaining[index];
            if (currentValue >= lowerBound && currentValue <= upperBound && currentValue < minValue) {
                minValue = currentValue;
                setMin(timeSlot, minValue);
                break;
            }
        }
        return minValue;
    }

    /**
     * Set the minimum value
     *
     * @param timeSlot The time slot
     * @param minValue The minimum value
     */
    public void setMin(TimeSlot timeSlot, int minValue) {
        answer[timeSlot.getAnswerIndex()] = minValue;
        LOGGER.info("{} assigned [{}]", timeSlot.getDescription(), minValue);
        boolean deleteMe = true;
        int[] temp = new int[remaining.length - 1];
        for (int index = 0, tempIndex = 0; index < remaining.length; index++) {
            if (deleteMe && remaining[index] == minValue) {
                deleteMe = false;
                continue;
            }
            temp[tempIndex++] = remaining[index];
        }
        remaining = temp;
    }

    /**
     * Check for valid answer
     * 
     * @throws TimeFittingException When there is no solution is possible
     */
    public void assertHasAnswer() throws TimeFittingException {
        if (remaining.length > 0)
            throw new TimeFittingException("No solution is possible");
    }

    /**
     * Format answer to 24-hour format
     * 
     * @return The answer formatted in HH:MM:SS format or <code>Optional.empty</code> for no solution possible
     */
    public Optional<String> format24Hour() {
        if (remaining.length == 0) {
            final StringBuilder builder = new StringBuilder();
            for (int index = 0; index < answer.length; index++) {
                builder.append(answer[index]);
                if (index == 1 || index == 3) {
                    builder.append(":");
                }
            }
            return Optional.of(builder.toString());
        }
        return Optional.empty();
    }

    /**
     * Get answer
     * 
     * @return A copy of the answer
     */
    public int[] getAnswer() {
        return Arrays.copyOf(answer, answer.length);
    }

    /**
     * Get remaining digits
     * 
     * @return A copy of the remaining digits not yet processed
     */
    public int[] getRemaining() {
        return Arrays.copyOf(remaining, remaining.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(answer, remaining);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null)
            return false;
        if (this.getClass() != other.getClass())
            return false;
        final TimeFittingEngine that = (TimeFittingEngine) other;
        return Arrays.equals(this.answer, that.answer) && Arrays.equals(this.remaining, that.remaining);
    }

    @Override
    public String toString() {
        return String.format("%s [answer=%s, remaining=%s]", getClass().getSimpleName(), Arrays.toString(answer), Arrays.toString(remaining));
    }
}
