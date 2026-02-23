package net.sattler22.timeseeker;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Time Seeker Unit Tests
 *
 * @author Pete Sattler
 * @since December 2018
 * @version May 2026
 */
final class TimeSeekerTest {

    private static final Logger logger = LoggerFactory.getLogger(TimeSeekerTest.class);

    @Test
    void newInstance_whenDigitsIsNull_thenThrowNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new TimeSeeker(null));
    }

    @Test
    void newInstance_whenDigitsIsEmpty_thenThrowIllegalArgumentException() {
        checkNewInstanceSpeed(new int[] {});
    }

    @Test
    void newInstance_whenDigitIsNegative_thenThrowIllegalArgumentException() {
        checkNewInstanceSpeed(new int[] { 4, -1, 4, 5, 9, 9 });
    }

    @Test
    void newInstance_whenDigitExceedsMaximum_thenThrowIllegalArgumentException() {
        checkNewInstanceSpeed(new int[] { 1, 2, 3, 4, 5, 999 });
    }

    private static void checkNewInstanceSpeed(int[] digits) {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                new TimeSeeker(digits)
        );
        assertEquals("Must have exactly 6 non-negative digits [0-9]", exception.getMessage());
    }

    @Test
    void findEarliest_whenTimeIsMinimum_thenSuccessful() {
        checkEarliestFit(new int[] { 0, 0, 0, 0, 0, 0 }, "00:00:00");
    }

    @Test
    void findEarliest_whenTimeIsMaximum_thenSuccessful() {
        checkEarliestFit(new int[] { 3, 2, 5, 5, 9, 9 }, "23:59:59");
    }

    @Test
    void findEarliest_whenHappyPath1_thenSuccessful() {
        checkEarliestFit(new int[] { 0, 0, 1, 0, 0, 0 }, "00:00:01");
    }

    @Test
    void findEarliest_whenHappyPath2_thenSuccessful() {
        checkEarliestFit(new int[] { 1, 1, 1, 9, 9, 9 }, "19:19:19");
    }

    @Test
    void findEarliest_whenHappyPath3_thenSuccessful() {
        checkEarliestFit(new int[] { 2, 3, 8, 6, 4, 1 }, "12:36:48");
    }

    @Test
    void findEarliest_whenHappyPath4_thenSuccessful() {
        checkEarliestFit(new int[] { 8, 0, 9, 0, 7, 0 }, "07:08:09");
    }

    @Test
    void findEarliest_whenHappyPath5_thenSuccessful() {
        checkEarliestFit(new int[] { 1, 5, 2, 3, 6, 4 }, "12:34:56");
    }

    @Test
    void findEarliest_whenHappyPath6_thenSuccessful() {
        checkEarliestFit(new int[] { 0, 4, 0, 2, 0, 0 }, "00:00:24");
    }

    @Test
    void findEarliest_whenHappyPath7_thenSuccessful() {
        checkEarliestFit(new int[] { 1, 3, 7, 2, 6, 8 }, "16:27:38");
    }

    @Test
    void findEarliest_whenHappyPath8_thenSuccessful() {
        checkEarliestFit(new int[] { 4, 4, 3, 4, 2, 4 }, "23:44:44");
    }

    @Test
    void findEarliest_whenHappyPath9_thenSuccessful() {
        checkEarliestFit(new int[] { 2, 2, 0, 0, 2, 0 }, "00:02:22");
    }

    @Test
    void findEarliest_whenHappyPath10_thenSuccessful() {
        checkEarliestFit(new int[] { 4, 0, 0, 0, 2, 0 }, "00:00:24");
    }

    @Test
    void findEarliest_whenHappyPath11_thenSuccessful() {
        checkEarliestFit(new int[] { 1, 2, 9, 9, 3, 1 }, "11:29:39");
    }

    @Test
    void findEarliest_whenHappyPath12_thenSuccessful() {
        checkEarliestFit(new int[] { 2, 0, 6, 6, 4, 7 }, "06:26:47");
    }

    @Test
    void findEarliest_whenHappyPath13_thenSuccessful() {
        checkEarliestFit(new int[] { 1, 9, 5, 9, 4, 4 }, "14:49:59");
    }

    private static void checkEarliestFit(int[] digits, String expected) {
        final TimeSeeker timeSeeker = new TimeSeeker(digits);
        final String actual = DateTimeFormatter.ISO_TIME.format(timeSeeker.findEarliest());
        logger.info("Earliest time for {} is [{}]", digits, actual);
        assertEquals(expected, actual);
    }

    @Test
    void findEarliest_whenNoSolution1_thenThrowTimeFittingException() {
        assertThrows(TimeFittingException.class, () ->
            throwEarliestFit(new int[] { 2, 4, 5, 9, 5, 9 }));
    }

    @Test
    void findEarliest_whenNoSolution2_thenThrowTimeFittingException() {
        assertThrows(TimeFittingException.class, () ->
            throwEarliestFit(new int[] { 2, 5, 5, 9, 5, 9 }));
    }

    @Test
    void findEarliest_whenNoSolution3_thenThrowTimeFittingException() {
        assertThrows(TimeFittingException.class, () ->
            throwEarliestFit(new int[] { 9, 2, 8, 6, 7, 0 }));
    }

    @Test
    void findEarliest_whenNoSolution4_thenThrowTimeFittingException() {
        assertThrows(TimeFittingException.class, () ->
            throwEarliestFit(new int[] { 4, 4, 4, 5, 9, 9 }));
    }

    @Test
    void findEarliest_whenNoSolution5_thenThrowTimeFittingException() {
        assertThrows(TimeFittingException.class, () ->
            throwEarliestFit(new int[] { 7, 6, 3, 8, 9, 9 }));
    }

    private static void throwEarliestFit(int[] digits) {
        try {
            final TimeSeeker timeSeeker = new TimeSeeker(digits);
            timeSeeker.findEarliest();
        }
        catch (TimeFittingException e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
