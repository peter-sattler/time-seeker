package net.sattler22.timeseeker;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Time Seeker Unit Test Harness
 *
 * @author Pete Sattler
 * @version December 2018
 */
public final class TimeSeekerUnitTestHarness {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeSeekerUnitTestHarness.class);

    @Test
    public void minimumTimeTestCase() {
        checkValidSolutionImpl(new int[] { 0, 0, 0, 0, 0, 0 }, "00:00:00");
    }

    @Test
    public void happyPathTestCase1() {
        checkValidSolutionImpl(new int[] { 0, 0, 0, 0, 0, 1 }, "00:00:01");
    }

    @Test
    public void happyPathTestCase2() {
        checkValidSolutionImpl(new int[] { 1, 9, 9, 9, 1, 1 }, "19:19:19");
    }

    // @Test
    public void happyPathTestCase3() {
        checkValidSolutionImpl(new int[] { 1, 6, 2, 3, 4, 8 }, "16:23:48");
    }

    // @Test
    public void happyPathTestCase4() {
        checkValidSolutionImpl(new int[] { 0, 7, 0, 8, 0, 9 }, "07:08:09");
    }

    @Test
    public void happyPathTestCase5() {
        checkValidSolutionImpl(new int[] { 1, 2, 3, 4, 5, 6 }, "12:34:56");
    }

    @Test
    public void happyPathTestCase6() {
        checkValidSolutionImpl(new int[] { 0, 0, 2, 4, 0, 0 }, "00:00:24");
    }

    // @Test
    public void happyPathTestCase7() {
        checkValidSolutionImpl(new int[] { 1, 6, 2, 7, 3, 8 }, "16:27:38");
    }

    @Test
    public void happyPathTestCase8() {
        checkValidSolutionImpl(new int[] { 2, 3, 4, 4, 4, 4 }, "23:44:44");
    }

    // @Test
    public void noSolutionTestCase1() {
        checkNoSolutionImpl(new int[] { 2, 4, 5, 9, 5, 9 });
    }

    // @Test
    public void noSolutionTestCase2() {
        checkNoSolutionImpl(new int[] { 2, 5, 5, 9, 5, 9 });
    }

    // @Test
    public void noSolutionTestCase3() {
        checkNoSolutionImpl(new int[] { 9, 2, 8, 6, 7, 0 });
    }

    // @Test
    public void noSolutionTestCase4() {
        checkNoSolutionImpl(new int[] { 4, 4, 4, 5, 9, 9 });
    }

    // @Test
    public void noSolutionTestCase5() {
        checkNoSolutionImpl(new int[] { 4, -1, 4, 5, 9, 9 });
    }

    // @Test
    public void noSolutionTestCase6() {
        checkNoSolutionImpl(new int[] { 2, 4, 0, 0, 0, 0 });
    }

    // @Test
    public void maximumTimeTestCase() {
        checkValidSolutionImpl(new int[] { 2, 9, 5, 3, 5, 9 }, "23:59:59");
    }

    /**
     * Valid solution check
     */
    private void checkValidSolutionImpl(int[] values, String expected) {
        assertEquals(Optional.of(expected), findEarliestImpl(values));
    }

    /**
     * No solution check
     */
    private void checkNoSolutionImpl(int[] values) {
        assertEquals(Optional.empty(), findEarliestImpl(values));
    }

    private Optional<String> findEarliestImpl(int[] values) {
        LOGGER.info("Formatting values: {}", values);
        final TimeSeeker formatter = new TimeSeeker(values);
        final Optional<String> actual = formatter.earliest();
        LOGGER.info("Earliest time: [{}]", actual);
        return actual;
    }
}