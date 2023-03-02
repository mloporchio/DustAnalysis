import java.util.Objects;

/**
 * Represents a reference to a transaction output.
 * For efficiency reasons, we do not store the associated value, 
 * as it can be deduced from the corresponding input when it is spent.
 * @author Matteo Loporchio
 */
public class Output {
    /**
     * Identifier of the transaction containing the output.
     */
    public final long txId;
    
    /**
     * The position of the output inside the transaction.
     */
    public final int offset; 
    
    /**
     * Constructs a new output reference.
     * @param txId transaction identifier
     * @param offset position of the output in the transaction
     */
    public Output(long txId, int offset) {
        this.txId = txId;
        this.offset = offset;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.txId, this.offset);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Output)) return false;
        if (o == this) return true;
        Output out = (Output) o;
        return (this.txId == out.txId && this.offset == out.offset);
    }
}