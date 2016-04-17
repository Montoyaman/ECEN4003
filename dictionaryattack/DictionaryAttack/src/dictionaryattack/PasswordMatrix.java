/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class Node {
    public AtomicBoolean[] elements; //Charsub elements
    private AtomicBoolean complete; //Have all entries been tried?
    private AtomicBoolean flag; //Has been started already, no need to reallocate
    
    public Node() {
        complete = new AtomicBoolean(false);
        flag = new AtomicBoolean(false);
    }
    
    public boolean isStarted() {
        return flag.get();
    }
    
    public boolean isComplete() {
        return complete.get();
    }
    
    public boolean setComplete() {
        return complete.compareAndSet(false, true);
    }
    
    public void startList(int n) {
        if (flag.compareAndSet(false, true)) {
            elements = new AtomicBoolean[n];

            for (int i = 0; i < n; i ++){
                elements[i] = new AtomicBoolean(false);
            }
        }
    }
}

/**
 *
 * @author RyanDavidMontoya
 */
public class PasswordMatrix {
    //Array of node elements
    Node[][] passArray;
    public final int length;
    
    //Initialize the password matrix with dimension n
    public PasswordMatrix(int n) {
        length = n;

        passArray = new Node[n][n];
        //Allocate the boolean array
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                passArray[i][j] = new Node();
            }
        }
    }
    
    //Returns if the node is complete
    public boolean getComplete(int r, int c) {
        return passArray[r][c].isComplete();        
    }
    
    //Returns the node
    public Node getNode(int r, int c) {
        return passArray[r][c];        
    }
    
    public boolean setComplete(int r, int c) {
        return passArray[r][c].setComplete();
    }
    
    public static void main(String[] args)  {    
        //Lets test!
        int n = 8;
        PasswordMatrix matrix = new PasswordMatrix(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Node test = matrix.getNode(i, j);
 
                if (test.elements == null) {
                    int debug = 1;
                }
                
                test.startList(i);
                test.setComplete();
                System.out.println(test.isComplete());
                test.elements = null; //Clear it out
            }
        }
    }
}
