package net.sattler22.timeseeker;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.time.format.DateTimeFormatter.ISO_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Time Seeker Unit Test Harness
 *
 * @author Pete Sattler
 * @version December 2018 (brute-force)
 * @version February 2022 (rewrite)
 */
final class TimeSeekerUnitTest {

    private static final Logger logger = LoggerFactory.getLogger(TimeSeekerUnitTest.class);

    @Test
    void nullDigitsParameterTestCase() {
        assertThrows(NullPointerException.class, () ->
                findEarliestImpl(null));
    }

    @Test
    void emptyDigitsParameterTestCase() {
        assertThrows(IllegalArgumentException.class, () ->
            findEarliestImpl(new int[] {}));
    }

    @Test
    void negativeParameterDigitsTestCase() {
        assertThrows(IllegalArgumentException.class, () ->
            findEarliestImpl(new int[] { 4, -1, 4, 5, 9, 9 }));
    }

    @Test
    void minimumTimeTestCase() {
        assertValidSolution(new int[] { 0, 0, 0, 0, 0, 0 }, "00:00:00");
    }

    @Test
    void maximumTimeTestCase() {
        assertValidSolution(new int[] { 3, 2, 5, 5, 9, 9 }, "23:59:59");
    }

    @Test
    void happyPathTestCase1() {
        assertValidSolution(new int[] { 0, 0, 1, 0, 0, 0 }, "00:00:01");
    }

    @Test
    void happyPathTestCase2() {
        assertValidSolution(new int[] { 1, 1, 1, 9, 9, 9 }, "19:19:19");
    }

    @Test
    void happyPathTestCase3() {
        assertValidSolution(new int[] { 2, 3, 8, 6, 4, 1 }, "12:36:48");
    }

    @Test
    void happyPathTestCase4() {
        assertValidSolution(new int[] { 8, 0, 9, 0, 7, 0 }, "07:08:09");
    }

    @Test
    void happyPathTestCase5() {
        assertValidSolution(new int[] { 1, 5, 2, 3, 6, 4 }, "12:34:56");
    }

    @Test
    void happyPathTestCase6() {
        assertValidSolution(new int[] { 0, 4, 0, 2, 0, 0 }, "00:00:24");
    }

    @Test
    void happyPathTestCase7() {
        assertValidSolution(new int[] { 1, 3, 7, 2, 6, 8 }, "16:27:38");
    }

    @Test
    void happyPathTestCase8() {
        assertValidSolution(new int[] { 4, 4, 3, 4, 2, 4 }, "23:44:44");
    }

    @Test
    void happyPathTestCase9() {
        assertValidSolution(new int[] { 2, 2, 0, 0, 2, 0 }, "00:02:22");
    }

    @Test
    void happyPathTestCase10() {
        assertValidSolution(new int[] { 4, 0, 0, 0, 2, 0 }, "00:00:24");
    }

    @Test
    void happyPathTestCase11() {
        assertValidSolution(new int[] { 1, 2, 9, 9, 3, 1 }, "11:29:39");
    }

    @Test
    void happyPathTestCase12() {
        assertValidSolution(new int[] { 2, 0, 6, 6, 4, 7 }, "06:26:47");
    }

    @Test
    void happyPathTestCase13() {
        assertValidSolution(new int[] { 1, 9, 5, 9, 4, 4 }, "14:49:59");
    }

    @Test
    void noSolutionTestCase1() {
        assertThrows(TimeFittingException.class, () ->
            findEarliestImpl(new int[] { 2, 4, 5, 9, 5, 9 }));
    }

    @Test
    void noSolutionTestCase2() {
        assertThrows(TimeFittingException.class, () ->
            findEarliestImpl(new int[] { 2, 5, 5, 9, 5, 9 }));
    }

    @Test
    void noSolutionTestCase3() {
        assertThrows(TimeFittingException.class, () ->
            findEarliestImpl(new int[] { 9, 2, 8, 6, 7, 0 }));
    }

    @Test
    void noSolutionTestCase4() {
        assertThrows(TimeFittingException.class, () ->
            findEarliestImpl(new int[] { 4, 4, 4, 5, 9, 9 }));
    }

    @Test
    void noSolutionTestCase5() {
        assertThrows(TimeFittingException.class, () ->
            findEarliestImpl(new int[] { 7, 6, 3, 8, 9, 9 }));
    }

    /**
     * Valid solution check
     */
    private static void assertValidSolution(int[] values, String expected) {
        assertEquals(expected, findEarliestImpl(values));
    }

    private static String findEarliestImpl(int[] digits) {
        try {
            final TimeSeeker timeSeeker = new TimeSeeker(digits);
            final String actual = ISO_TIME.format(timeSeeker.findEarliest());
            logger.info("Earliest time for {} is [{}]", digits, actual);
            return actual;
        }
        catch(Exception e) {
            logger.warn(e.getMessage());
            throw e;
        }
    }
}
