/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathan
 */
public class Sem {
    private final Semaphore sem;
    
    public Sem(int i){
        this.sem = new Semaphore(i, true);
    }
    
    public void stop(){
        try {
            this.sem.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Sem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void go(){
        this.sem.release();
    }
    
}
