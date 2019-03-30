import com.sun.org.apache.xerces.internal.dom.AbortException;

import java.util.concurrent.atomic.AtomicInteger;

public class MyThread extends Thread {

    Transaction t;
    Register r1;
    Register r2;
    public MyThread( AtomicInteger clock,Register r1, Register r2){
        t = new TransactionTL2(clock);
        this.r1 = r1;
        this.r2 = r2;
        //System.out.println("Valeur de la clock ="+clock);
    }

  public void run() {

    while(!t.isCommited()){
        try {
            System.out.println("Valeur du thread ="+this+" valeur de iscommited ="+t.isCommited());
            t.begin();
            //int a = r1.read((TransactionTL2) t);
            r1.write((TransactionTL2)t,r1.read((TransactionTL2) t)+r2.read((TransactionTL2) t));
            System.out.println("Valeur de R1 ="+(r1.read((TransactionTL2) t)+r2.read((TransactionTL2) t)));

            t.try_to_commit();
            //System.out.println("Valeur de R1 Apres ="+r1.read((TransactionTL2)t));
        } catch (AbortException e) {

        }
     }
  }
}
