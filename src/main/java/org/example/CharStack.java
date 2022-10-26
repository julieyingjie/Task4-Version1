package org.example;

import org.example.CharStackExceptions.CharStackEmptyException;
import org.example.CharStackExceptions.CharStackFullException;
import org.example.CharStackExceptions.CharStackInvalidAceessException;
import org.example.CharStackExceptions.CharStackInvalidSizeException;

public class CharStack {
    /*
     * Some class constants.
     */
    public static final int MIN_SIZE = 7; // Minimum stack size
    public static final int MAX_SIZE = 32; // # of letters in the English alphabet + 6
    public static final int DEFAULT_SIZE = 10; // Default stack size
    /*
     * Class variables
     */
    private static int iSize = DEFAULT_SIZE;
    private static int iTop = 3; // stack[0:9] with four defined values
    private static char aCharStack[] = new char[]{'a', 'b', 'c', 'd', '$', '$', '$', '$', '$', '$'};

    private static int count = 0; // this is for stack access count

    // Default constructor
    public CharStack() {
        // Just do nothing
    }

    // Constructor with supplied size
    public CharStack(int piSize) throws CharStackInvalidSizeException {
        if (piSize < MIN_SIZE || piSize > MAX_SIZE)
            throw new CharStackInvalidSizeException(piSize);
        if (piSize != DEFAULT_SIZE) {
            this.aCharStack = new char[piSize];
            // Fill in with letters of the alphabet and keep
            // 6 free blocks
            for (int i = 0; i < piSize - 6; i++)
                this.aCharStack[i] = (char) ('a' + i);
            for (int i = 1; i <= 6; i++)
                this.aCharStack[piSize - i] = '$';
            this.iTop = piSize - 7;
            this.iSize = piSize;
        }
    }

    /*
     * Picks a value from the top without modifying the stack
     */
    public static char pick() throws CharStackEmptyException {
        count++;

        if (iTop == -1)
            throw new CharStackEmptyException();
        return aCharStack[iTop];
    }

    /*
     * Returns arbitrary value from the stack array
     */
    public char getAt(int piPosition) throws CharStackInvalidAceessException {
        count++;

        if (piPosition < 0 || piPosition >= iSize)
            throw new CharStackInvalidAceessException();
        return this.aCharStack[piPosition];
    }

    /*
     * Standard push operation
     */
    public static void push(char pcChar) throws CharStackFullException {
        count++;

        if (iTop == iSize - 1)
            throw new CharStackFullException();
        aCharStack[++iTop] = pcChar;
    }

    /*
     * Standard pop operation
     */
    public static char pop() throws CharStackEmptyException {
        count++;

        if (iTop == -1)
            throw new CharStackEmptyException();
        char cChar = aCharStack[iTop];
        aCharStack[iTop--] = '$'; // Leave prev. value undefined

        return cChar;
    }

    /* Getters */
    public int getSize() {
        return this.iSize;
    }

    public int getTop() {
        return this.iTop;
    }

    public int getAccessCounter() {
        return count;
    }

    // A new method to get the exact size of the content of the stack
    public int getCurrentStackSize() {
        return aCharStack.length;
    }

    // A new method to justify this stack is empty or not
    public boolean isEmpty() {
        if (aCharStack[0] == '$') {
            return true;
        } else {
            return false;
        }
    }
}
