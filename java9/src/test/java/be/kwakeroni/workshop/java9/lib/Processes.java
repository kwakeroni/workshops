package be.kwakeroni.workshop.java9.lib;

import org.junit.Test;

/**
 * Created by kwakeroni on 07/11/17.
 */
public class Processes {

    @Test
    public void spinwait(){
        oldWait(2000);
        spinWait(2000);
        oldWait(5000);
        spinWait(5000);
    }

    private void oldWait(long millis){
        System.out.print("Wait " + millis + ": ");
        long start = System.currentTimeMillis();
        long end = start + millis;
        while (System.currentTimeMillis() < end){

            try {
                Thread.sleep(500);
                System.out.print(start - System.currentTimeMillis());
                start = System.currentTimeMillis();
            } catch (InterruptedException e) {
            }
        }
        System.out.println();
    }

    private void spinWait(long millis){
        System.out.print("Wait " + millis + ": ");
        long start = System.currentTimeMillis();
        long end = start + millis;
        while (System.currentTimeMillis() < end){

            try {
//                Thread.onSpinWait();
                Thread.sleep(500);
                System.out.print(start - System.currentTimeMillis());
                start = System.currentTimeMillis();
            } catch (InterruptedException e) {
            }
        }
        System.out.println();
    }



}
