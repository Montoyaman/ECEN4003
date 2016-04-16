/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.util.concurrent.atomic.AtomicBoolean;

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
    //Array of atomic booleans
    Node[] passArray;
    private final int length;
    //Array of pointers
    
    //Initialize the password matrix with dimension n
    public PasswordMatrix(int n) {
        length = n;
        
        //Find how many elements there are
        int elements = 0;
        for (int i = 1; i <=n; i++) {
            elements += i;
        }
        passArray = new Node[elements];
        //Allocate the boolean array
        for (int i = 0; i < elements; i++) {
            passArray[i] = new Node();
        }
    }
    
    //Returns if the node is complete
    public boolean getComplete(int r, int c) {
        int index = 0;
        
        //Swap values if below diagonal
        if (r > c) {
            int t = c;
            r = c;
            c = t;
        }
        
        for (int i = 0; i < r; i++){
            index += (length-r);
        }
        index += (c-r);
        return passArray[index].isComplete();        
    }
    
    //Returns the node
    public Node getNode(int r, int c) {
        int index = 0;
        
        //Swap values if below diagonal
        if (r > c) {
            int t = c;
            r = c;
            c = t;
        }
        
        for (int i = 0; i < r; i++){
            index += (length-r);
        }
        index += (c-r);
        
        return passArray[index];        
    }
    
    public boolean setComplete(int r, int c) {
        int index = 0;
        
        //Swap values if below diagonal
        if (r > c) {
            int t = c;
            r = c;
            c = t;
        }
        
        for (int i = 0; i < r; i++){
            index += (length-r);
        }
        index += (c-r);
        return passArray[index].setComplete();
    }
    
    public static void main(String[] args)  {    
        //Lets test!
        int n = 8;
        PasswordMatrix matrix = new PasswordMatrix(n);
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Node test = matrix.getNode(i, j);
                test.startList(i);
                test.setComplete();
                System.out.println(test.isComplete());
                test.elements = null; //Clear it out
            }
        }
    }
}
