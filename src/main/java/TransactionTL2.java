

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionTL2 implements Transaction {

    public  Set<RegisterTL2> lws;
    public  Set<RegisterTL2> lrs;
    public AtomicInteger birthDate = new AtomicInteger();
    public AtomicInteger clock;
    private boolean passTrycommit;


    public TransactionTL2(AtomicInteger clockbis){

        lws= new HashSet<RegisterTL2>();
        lrs= new HashSet<RegisterTL2>();

        this.clock = clockbis;
        passTrycommit = false;

    }


    public void begin() {

        for (RegisterTL2 X : lws) {
            X.lcx.remove();
        }
        for (RegisterTL2 X : lrs) {
            X.lcx.remove();
        }
        lws= new HashSet<RegisterTL2>();
        lrs= new HashSet<RegisterTL2>();


        birthDate.set(clock.get());

        passTrycommit = false;

    }

    public void try_to_commit() throws AbortException {

        //lock all the objects in (lrsn[T] union lws[T]);
        Set<RegisterTL2> temp = new HashSet<RegisterTL2>(lrs);
        temp.addAll(lws);
        for (RegisterTL2 X : temp) {
            X.lock.lock();
        }


        for (RegisterTL2 X : lrs) {
            if(X.date>birthDate.get() ){
                //release all the locks;

                for (RegisterTL2 Y : temp) {
                    Y.lock.unlock();
                }
                System.out.println("Thread = "+Thread.currentThread());
                throw new AbortException();

            }
        }

        int commitDate = clock.getAndIncrement();

        for (RegisterTL2 X : lws) {

            X.setValue(X.lcx.get());
            X.date=commitDate;
        }

        for (RegisterTL2 Y : temp) {
            Y.lock.unlock();
        }
        passTrycommit = true;
    }

    public boolean isCommited() {
        return passTrycommit;
    }
}
