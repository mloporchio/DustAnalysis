/**
 * This class contains global thresholds for dust amounts.
 * In accordance with the official Bitcoin core implementation [1],
 * for our analysis we label as dust all amounts within the range
 * between 1 and 545 satoshis. We ignore amounts of 0 satoshis
 * as they are not relevant for our purposes.
 * 
 * [1] Source: https://github.com/bitcoin/bitcoin/blob/c536dfbcb00fb15963bf5d507b7017c241718bf6/src/policy/policy.cpp
 * 
 * @author Matteo Loporchio
 */
public final class Dust {
    /**
     * Minimum value for the dust range.
     */
    public static final int DUST_MIN = 1;

    /**
     * Maximum value for the dust range.
     */
    public static final int DUST_MAX = 545;

    /**
     * Returns true if and only if the amount is within the dust range.
     * @param amount amount of value
     * @return true if and only if the amount is considered dust
     */
    public static final boolean isDust(long amount) {
        return (DUST_MIN <= amount && amount <= DUST_MAX);
    }
}
