package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());
    
	private boolean pausar, estaVivo;
    
    private ControlFrame CF;


    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb, ControlFrame CF) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue=defaultDamageValue;
        this.CF = CF;
        pausar = CF.getIsPause();
        estaVivo = true;
    }

    public void run() {
    	
        while (true) {
        	synchronized(this) {
    			while(pausar) {
    				try {
    					wait();
    				}catch(InterruptedException e) {
                        e.printStackTrace();
    				}
    			}
        	}
        	if (! pausar) {
        		Immortal im;

                int myIndex = immortalsPopulation.indexOf(this);

                int nextFighterIndex = r.nextInt(immortalsPopulation.size());

                //avoid self-fight
                if (nextFighterIndex == myIndex) {
                    nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
                }

                im = immortalsPopulation.get(nextFighterIndex);

                this.fight(im);

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        	}
        }
    }
    
    synchronized void pausarhilo(){
        pausar = CF.getIsPause();
        notifyAll();
    }

    synchronized void reanudarhilo(){
        pausar = CF.getIsPause();
        notifyAll();
    }
    
    public void fight(Immortal i2) {

		if (i2.getHealth() > 0) {
            i2.changeHealth(-defaultDamageValue);
            this.changeHealth(defaultDamageValue); 
            updateCallback.processReport("Fight: " + this + " vs " + i2+"\n");
        } 
        else {
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");
        }
		
    }

    
    public synchronized void changeHealth(int vida) {
        health.addAndGet(vida);
        if (health.get() <= 0) {
            dead();
        }
    }

    public synchronized int getHealth() {
        return health.get();
    }
     
    public void dead() {
        this.estaVivo = false;
        synchronized (immortalsPopulation) {
            immortalsPopulation.remove(this);
        }
    }
    
    public void detenerhilo() {
    	this.estaVivo = false;
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
