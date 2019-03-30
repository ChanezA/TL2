import com.sun.org.apache.xerces.internal.dom.AbortException;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RegisterTL2 implements Register {

    public Lock lock = new ReentrantLock();
    private volatile int value;
    public AtomicInteger date = new AtomicInteger();

    public RegisterTL2(){
        date.set(-1);
        this.value = 1;
    }

    public void write(TransactionTL2 t, int v) throws AbortException {

        if(!(t.lcx.contains(this))/*no local copy lcx of X*/){
            //allocate local space lcx for a copy
          // System.out.println("tu passe là pd ?");
            t.lcx.add(new RegisterTL2());
        }
       // System.out.println("tu birthDate"+t.birthDate);

        t.lcx.get(t.lcx.indexOf(this)).setValue(v);
        t.lcx.get(t.lcx.indexOf(this)).date = t.birthDate;
       // System.out.println("Valeur de lcx mais au moment d'ajouter "+v+" à celui ci avec une valeur  = "+t.lcx.get(t.lcx.size()-1).value);
        t.lws.add(this);
    }


    public int read(TransactionTL2 t) throws AbortException {
        if(t.lcx.contains(this)/*there is a local copy lcx of X*/){
            return t.lcx.get( t.lcx.indexOf(this)).value;
        }else{
            t.lcx.add(this/*.clone()*/);
            t.lrs.add(this);
            if (t.lcx.get(t.lcx.indexOf(this)).date.get() > t.birthDate.get()){
                throw new AbortException();
            }else{
                return t.lcx.get(t.lcx.indexOf(this)).value;
            }
        }
    }

    public RegisterTL2 clone(){
        RegisterTL2 clone = new RegisterTL2();
        clone.value = this.value;
        clone.date = this.date;
        return clone;
    }

    public int getValue(){
        return this.value;
    }

    public void setValue(int v){
        this.value = v;
    }

}

