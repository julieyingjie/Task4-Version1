package org.example;

public class Semaphore {

    private int value;

    public Semaphore(int value) {
        this.value = value;
    }

    public Semaphore() {
        this(0);
    }

    public synchronized void Wait() {
        this.value--;
        if (this.value < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


        public synchronized void Signal () {
            this.value++;
            if (this.value <= 0) {
                notify();
            }
        }


        public synchronized void P ()
        {
            this.Wait();
        }

        public synchronized void V ()
        {
            this.Signal();
        }

}



