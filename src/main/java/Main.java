import com.sun.org.apache.xerces.internal.dom.AbortException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

        public static void main (String[] args){
            Register r1 = new RegisterTL2();
            Register r2 = new RegisterTL2();
            AtomicInteger clock = new AtomicInteger();
            clock.set(0);
            List<Thread> threads = new ArrayList<Thread>();
            int nbthread = 30;

            for(int j =0;j<nbthread;++j){
                threads.add(new MyThread(clock,r1,r2));
            }

            for(int j =0;j<nbthread;++j){

                threads.get(j).start();

            }

            for(int j =0;j<nbthread;++j){

                //System.out.println("Combien dans la boucle le j ? = "+j);
                threads.get(j).run();
               // System.out.println("Dans le for la valeur de R1 = "+((RegisterTL2) r1).getValue());

            }
            System.out.println("LA VALEUR DE R1 = "+((RegisterTL2) r1).getValue());

        }



}
