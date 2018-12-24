package net.sattler22.timeseeker;

import static org.junit.Assert.assertEquals;

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
    public void minimumTimeTestCase() throws TimeFittingException {
        assertValieSolution(new int[] { 0, 0, 0, 0, 0, 0 }, "00:00:00");
    }

    @Test
    public void maximumTimeTestCase() throws TimeFittingException {
        assertValieSolution(new int[] { 2, 9, 5, 3, 5, 9 }, "23:59:59");
    }

    @Test
    public void happyPathTestCase1() throws TimeFittingException {
        assertValieSolution(new int[] { 0, 0, 0, 0, 0, 1 }, "00:00:01");
    }

    @Test
    public void happyPathTestCase2() throws TimeFittingException {
        assertValieSolution(new int[] { 1, 9, 9, 9, 1, 1 }, "19:19:19");
    }

    @Test
    public void happyPathTestCase3() throws TimeFittingException {
        assertValieSolution(new int[] { 1, 6, 2, 3, 4, 8 }, "12:36:48");
    }

    @Test
    public void happyPathTestCase4() throws TimeFittingException {
        assertValieSolution(new int[] { 0, 7, 0, 8, 0, 9 }, "07:08:09");
    }

    @Test
    public void happyPathTestCase5() throws TimeFittingException {
        assertValieSolution(new int[] { 1, 2, 3, 4, 5, 6 }, "12:34:56");
    }

    @Test
    public void happyPathTestCase6() throws TimeFittingException {
        assertValieSolution(new int[] { 0, 0, 2, 4, 0, 0 }, "00:00:24");
    }

    @Test
    public void happyPathTestCase7() throws TimeFittingException {
        assertValieSolution(new int[] { 1, 6, 2, 7, 3, 8 }, "16:27:38");
    }

    @Test
    public void happyPathTestCase8() throws TimeFittingException {
        assertValieSolution(new int[] { 2, 3, 4, 4, 4, 4 }, "23:44:44");
    }

    @Test
    public void happyPathTestCase9() throws TimeFittingException {
        assertValieSolution(new int[] { 2, 0, 0, 0, 2, 2 }, "00:02:22");
    }

    @Test
    public void happyPathTestCase10() throws TimeFittingException {
        assertValieSolution(new int[] { 2, 4, 0, 0, 0, 0 }, "00:00:24");
    }

    @Test(expected = TimeFittingException.class)
    public void noSolutionTestCase1() throws TimeFittingException {
        findEarliestImpl(new int[] { 2, 4, 5, 9, 5, 9 });
    }

    @Test(expected = TimeFittingException.class)
    public void noSolutionTestCase2() throws TimeFittingException {
        findEarliestImpl(new int[] { 2, 5, 5, 9, 5, 9 });
    }

    @Test(expected = TimeFittingException.class)
    public void noSolutionTestCase3() throws TimeFittingException {
        findEarliestImpl(new int[] { 9, 2, 8, 6, 7, 0 });
    }

    @Test(expected = TimeFittingException.class)
    public void noSolutionTestCase4() throws TimeFittingException {
        findEarliestImpl(new int[] { 4, 4, 4, 5, 9, 9 });
    }

    @Test(expected = TimeFittingException.class)
    public void noSolutionTestCase5() throws TimeFittingException {
        findEarliestImpl(new int[] { 4, -1, 4, 5, 9, 9 });
    }

    /**
     * Valid solution check
     */
    private void assertValieSolution(int[] values, String expected) throws TimeFittingException {
        assertEquals(expected, findEarliestImpl(values));
    }

    private String findEarliestImpl(int[] values) throws TimeFittingException {
        LOGGER.info("Testing values: {}", values);
        final TimeSeeker formatter = new TimeSeeker(values);
        final String actual = formatter.fitEarliest();
        LOGGER.info("Earliest time: [{}]", actual);
        return actual;
    }
}
