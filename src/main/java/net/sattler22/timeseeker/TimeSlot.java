package net.sattler22.timeseeker;

/**
 * Time Seeker Time Slot Definitions
 *
 * @author Pete Sattler
 * @version December 2018
 */
public enum TimeSlot {
    HOUR_LEFT_SLOT(0, "Hour slot #1"),
    HOUR_RIGHT_SLOT(1, "Hour slot #2"),
    MINUTE_LEFT_SLOT(2, "Minute slot #1"),
    MINUTE_RIGHT_SLOT(3, "Minute slot #2"),
    SECONDS_LEFT_SLOT(4, "Seconds slot #1"),
    SECONDS_RIGHT_SLOT(5, "Seconds slot #2");

    private int answerIndex;
    private String description;

    private TimeSlot(int answerIndex, String description) {
        this.description = description;
        this.answerIndex = answerIndex;
    }

    public int getAnswerIndex() {
        return answerIndex;
    }

    public String getDescription() {
        return description;
    }
}
