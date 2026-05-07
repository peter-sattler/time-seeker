package net.sattler22.timeseeker;

/**
 * Time Seeker Time Fitting Exception
 * <p>
 *     Thrown when no valid 24 hour time can be formed from the supplied digits
 * </p>
 *
 * @author Pete Sattler
 * @since December 2018
 * @version May 2026
 */
final class TimeFittingException extends IllegalStateException {

    TimeFittingException(String message) {
        super(message);
    }
}
