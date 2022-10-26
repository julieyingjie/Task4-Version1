package org.example;

import org.example.CharStackExceptions.CharStackEmptyException;
import org.example.CharStackExceptions.CharStackFullException;
import org.example.CharStackExceptions.CharStackInvalidAceessException;

public class StackManager {
    // The Stack
    private static CharStack stack = new CharStack();
    private static final int NUM_ACQREL = 4; // Number of Producer/Consumer threads
    private static final int NUM_PROBERS = 1; // Number of threads dumping stack
    private static int iThreadSteps = 3; // Number of steps they take

    // Semaphore declarations. Insert your code in the following:
    // Declare a semaphore which is shared by the Producer, Consumer, and CharStackProber, initialize it to 1
    static Semaphore sem = new Semaphore(1);
    // Semaphores For Task 4

    // mutex_C is for control the Consumer couldn't execute before the producer release the mutex_C
    private static Semaphore mutex_C = new Semaphore(0);

    // mutex is for lock when 1 thread is changing the number of the producer or consumer
    private static Semaphore mutex = new Semaphore(1);

    // counter_P is for track the producer number
    private static int counter_P = 0;

    // The main()
    public static void main(String[] argv) {
        // Some initial stats...
        try {
            System.out.println("Main thread starts executing.");
            System.out.println("Initial value of top = " + stack.getTop() + ".");
            System.out.println("Initial value of stack top = " + stack.pick() + ".");
            System.out.println("Main thread will now fork several threads.");
        } catch (CharStackEmptyException e) {
            System.out.println("Caught exception: StackCharEmptyException");
            System.out.println("Message : " + e.getMessage());
            System.out.println("Stack Trace : ");
            e.printStackTrace();
        }
        /*
         * The birth of threads
         */
        Consumer ab1 = new Consumer();
        Consumer ab2 = new Consumer();
        System.out.println("Two Consumer threads have been created.");
        Producer rb1 = new Producer();
        Producer rb2 = new Producer();
        System.out.println("Two Producer threads have been created.");
        CharStackProber csp = new CharStackProber();
        System.out.println("One CharStackProber thread has been created.");
        /*
         * start executing
         */
        ab1.start();
        rb1.start();
        ab2.start();
        rb2.start();
        csp.start();
        /*
         * Wait by here for all forked threads to die
         */
        try {
            ab1.join();
            ab2.join();
            rb1.join();
            rb2.join();
            csp.join();
            // Some final stats after all the child threads terminated...
            System.out.println("System terminates normally.");
            System.out.println("Final value of top = " + stack.getTop() + ".");
            System.out.println("Final value of stack top = " + stack.pick() + ".");
            System.out.println("Final value of stack top-1 = " + stack.getAt(stack.getTop() - 1) + ".");
            System.out.println("Stack access count = " + stack.getAccessCounter());
        } catch (InterruptedException e) {
            System.out.println("Caught InterruptedException: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Caught exception: " + e.getClass().getName());
            System.out.println("Message : " + e.getMessage());
            System.out.println("Stack Trace : ");
            e.printStackTrace();
        }
    } // main()

    /*
     * Inner Consumer thread class
     */
    static class Consumer extends BaseThread {
        private char copy; // A copy of a block returned by pop()

        public void run() {
            mutex_C.P();

            System.out.println("Consumer thread [TID=" + this.iTID + "] starts executing.");
            for (int i = 0; i < StackManager.iThreadSteps; i++) {
                // Insert your code in the following:

                sem.P();//lock

                try {
                    copy = stack.pop();
                } catch (CharStackEmptyException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                sem.V();//release

                System.out.println("Consumer thread [TID=" + this.iTID + "] pops character =" + this.copy);

                mutex_C.V();
            }
            System.out.println("Consumer thread [TID=" + this.iTID + "] terminates.");


        }
   }


    // class Consumer

    /*
     * Inner class Producer
     */
    static class Producer extends BaseThread {
        private char block; // block to be returned

        public void run() {

            mutex.P();
            counter_P++;
            mutex.V();

            System.out.println("Producer thread [TID=" + this.iTID + "] starts executing.");
            for (int i = 0; i < StackManager.iThreadSteps; i++) {

                sem.P();//lock before the execution


                try {
                    block = (char) (stack.pick() + 1);

                } catch (CharStackEmptyException e) {
                    block = 'a';
                    e.printStackTrace();
                }

                // push the necessary character that the stack needs required by the assignment
                try {
                    stack.push(block);
                } catch (CharStackFullException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                sem.V();//release

                System.out.println("Producer thread [TID=" + this.iTID + "] pushes character =" + this.block);

                if (counter_P == 2){
                    mutex_C.V();
                    mutex_C.V();
                }
//                mutex_P.V();
            }
            System.out.println("Producer thread [TID=" + this.iTID + "] terminates.");
        }
    }// class Producer

    /*
     * Inner class CharStackProber to dump stack contents
     */
    static class CharStackProber extends BaseThread {
        public void run() {
            System.out.println("CharStackProber thread [TID=" + this.iTID + "] starts executing.");
            for (int i = 0; i < 2 * StackManager.iThreadSteps; i++) {
                sem.Wait(); //lock

                System.out.print("\"Stack S= (");
                for (int k = 0; k < stack.getCurrentStackSize(); k++) {
                    char temp = '$';
                    try {
                        temp = stack.getAt(k);
                    } catch (CharStackInvalidAceessException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    if (k + 1 != stack.getCurrentStackSize())//not at top
                    {
                        System.out.print("[" + temp + "],");
                    } else {
                        System.out.print("[" + temp + "]");//top
                    }
                }
                System.out.println(")\"");

                sem.Signal();//release
            }
        }
    } // class CharStackProber

}// StackManager





