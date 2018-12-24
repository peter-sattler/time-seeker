package net.sattler22.timeseeker;

/**
 * Time Seeker Time Fitting Checked Exception
 *
 * @author Pete Sattler
 * @version December 2018
 */
public final class TimeFittingException extends Exception {

    private static final long serialVersionUID = 4305763619415566313L;

    public TimeFittingException() {
    }

    public TimeFittingException(String message) {
        super(message);
    }

    public TimeFittingException(Throwable cause) {
        super(cause);
    }

    public TimeFittingException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeFittingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
