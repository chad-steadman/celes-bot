package steadman.irc.bot;

/**
 * DiceException.java (Written by Aelanna)
 */
public class DiceException extends RuntimeException {
    public static final int INSUFFICIENT_NUMBER = 1;
    public static final int EXCESSIVE_NUMBER = 2;
    public static final int INSUFFICIENT_SIDES = 3;
    public static final int EXCESSIVE_SIDES = 4;

    private int reason;

    public DiceException(int reason) {
        super();
        this.reason = reason;
    }

    public int getReason() {
        return reason;
    }
}