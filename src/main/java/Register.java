
public interface Register {


    int read(TransactionTL2 t) throws AbortException;
    void write(TransactionTL2 t, int v) throws AbortException;
}
