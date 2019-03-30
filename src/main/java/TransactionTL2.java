import com.sun.org.apache.xerces.internal.dom.AbortException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionTL2 implements Transaction {

    public List<RegisterTL2> lws;
    public List<RegisterTL2> lrs;
    public List<RegisterTL2> lcx;
    public AtomicInteger birthDate = new AtomicInteger();
    private AtomicInteger clock;
    private boolean passTrycommit = false;
    private int begin =0;

    public TransactionTL2(AtomicInteger clockbis){
        lws = new ArrayList<RegisterTL2>();
        lrs = new ArrayList<RegisterTL2>();
        lcx = new ArrayList<RegisterTL2>();
        this.clock = clockbis;
    }


    public void begin() {
        //re-initialize local variabes;
        birthDate.set(clock.get());
        System.out.println("Valeur de la clock ="+clock+" birthDate ="+birthDate);
        ++begin;
    }

    public void try_to_commit() throws AbortException {

        //lock all the objects in (lrsn[T] union lws[T]);
        for (RegisterTL2 X : lrs) {
            X.lock.lock();
        }
        for (RegisterTL2 X : lws) {
            X.lock.lock();
        }

        for (RegisterTL2 X : lrs) {
            if(X.date.get()>birthDate.get() ){
                //release all the locks;
                for (RegisterTL2 Y : lrs) {
                    Y.lock.unlock();
                }
                for (RegisterTL2 Y : lws) {
                    Y.lock.unlock();
                }
                throw new AbortException();
            }
        }
        int commitDate = clock.getAndIncrement();

        for (int i =0;i<lws.size(); ++i) {
           // System.out.println("Valeur de lcx ="+lcx.indexOf(lws.get(i)));
            lws.get(i).setValue(lcx.get(lcx.indexOf(lws.get(i))).getValue());
            lws.get(i).date.set(commitDate);
        }
        //release all the locks;
        for (RegisterTL2 X : lrs) {
            X.lock.unlock();
        }
        for (RegisterTL2 Y : lws) {
            Y.lock.unlock();
        }
        passTrycommit = true;
        --begin;
    }

    public boolean isCommited() {
        return passTrycommit && (begin == 0);
    }
}
