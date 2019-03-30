

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    Transaction t;
    Register r1;
    Register r2;
    public MyThread( AtomicInteger clock,Register r1, Register r2){
        t = new TransactionTL2(clock);
        this.r1 = r1;
        this.r2 = r2;
    }

  public void run() {

    while(!t.isCommited()){
        try {

            t.begin();
            r1.write((TransactionTL2)t,r1.read((TransactionTL2) t)+r2.read((TransactionTL2) t));

            t.try_to_commit();
        } catch (AbortException e) {
                e.printStackTrace();
        }
     }
  }
}
