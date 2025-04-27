package net.sattler22.timeseeker;

/**
 * Time Seeker Time Fitting Exception
 *
 * @author Pete Sattler
 * @version December 2018 (brute-force)
 * @version February 2022 (rewrite)
 */
final class TimeFittingException extends IllegalStateException {

    TimeFittingException(String message) {
        super(message);
    }
}
