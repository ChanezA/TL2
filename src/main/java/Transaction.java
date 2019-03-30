import com.sun.org.apache.xerces.internal.dom.AbortException;

public interface Transaction {

    void begin();
    void try_to_commit() throws AbortException;
    boolean isCommited();
}
