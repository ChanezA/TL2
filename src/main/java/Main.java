
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
            int nbthread = 5000;

            for(int j =0;j<nbthread;++j){
                threads.add(new MyThread(clock,r1,r2));
            }

            for(int j =0;j<nbthread;++j){

                threads.get(j).start();

            }

            for(int j =0;j<nbthread;++j){

                try {
                    threads.get(j).join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("LA VALEUR DE R1 = "+((RegisterTL2) r1).getValue());

        }



}
