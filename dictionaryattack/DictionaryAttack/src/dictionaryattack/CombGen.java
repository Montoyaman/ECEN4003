/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.util.LinkedList;

/**
 *
 * @author RyanDavidMontoya
 */
//Pass a single word into the combination generator and it will build and permute
//All combinations of the word and the symbol and number table

//This seems stupid, but its so I can pass by reference
class counter {
    private int cnt;
    
    public counter() {
        cnt = 0;
    }
    
    public void incre() {
        cnt++;
    }
    
    public int get() {
        return cnt;
    }
}

public class CombGen {
    private String[] symbols;
    
    //Iteration markers
    private int Is; //What index of symbol table
    private int k; //How many we choose
    
    //CREATE A NEW ONE EVERY TIME YOU CHANGE THE NUMBER OF SYMBOLS CHOSEN
    public CombGen(String[] inSyms, int K) {
        symbols = inSyms;
        k = K;
        Is = 0;
    }
    
    //This function pulls the next combination, number of symbols
    public LinkedList<String> combin() {
        //Use recursion to pull each combination
      
        counter cnt = new counter();
        LinkedList<String> comb = new LinkedList();
        //Pull a symbol combination (initialize cnt and index to 0

        comb = combRecur(Is, cnt, k, 0, symbols);
        Is++;   

        
        return comb;
    }
    
    //K is the choose number, index is the last value, inString is the full table
    private LinkedList<String> combRecur(int target, counter cnt, int k, int index, String[] inString) {
        int ki = k; //Deep copy, don't want this changing between levels
        
        if (k == 0) {//Lowest level, trying next variable
            //If we are at the target value
            if (target == cnt.get()) {
                LinkedList<String> comb = new LinkedList();
                return comb;
            } else {
                cnt.incre(); //Try another value
                return null;
            }
        } else {
            //Iterate through the elements, stop before the last choose elements
            for (int i = index; i <= inString.length - k; i++) {
                //Shift down a level (ki-1), start at the next index (i+1)
                LinkedList<String> comb = combRecur(target, cnt, ki-1, i+1, inString);

                if (comb != null) { //We are at our target, return the string
                    comb.add(inString[i]);
                    return comb;
                }
            }
        }
        
        //Exceeded dimensions, simply return null
        return null;
    }
}