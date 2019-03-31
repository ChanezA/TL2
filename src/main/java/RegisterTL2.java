
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class RegisterTL2 implements Register {

    public Lock lock = new ReentrantLock();
    private volatile int value;
    public volatile int date ;
    public ThreadLocal<Integer> lcx;
    public ThreadLocal<Integer> ldate;


    public RegisterTL2(){
        lcx = new ThreadLocal<Integer>();
        ldate = new ThreadLocal<Integer>();
        date=0;
        this.value = 1;
    }

    public void write(TransactionTL2 t, int v){

        ldate.set(t.clock.get());
        lcx.set(v);
        t.lws.add(this);
    }


    public int read(TransactionTL2 t) throws AbortException {

        if(lcx.get()!=null/*there is a local copy lcx of X*/){
            return lcx.get();
        }else{
            ldate.set(date);
            lcx.set(value);
            t.lrs.add(this);
            if (ldate.get() > t.birthDate.get()){
                throw new AbortException();
            }else{
                return lcx.get();
            }
        }
    }


    public int getValue(){
        return this.value;
    }

    public void setValue(int v){
        this.value = v;
    }

}

