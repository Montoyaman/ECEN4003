/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RyanDavidMontoya
 */
public class DictionaryAttack {
    
    public static BlockingQueue<String> dictionary;
    public static CombGen gen;
    public static int numThreads = 4;
    public static Thread[] threads = new Thread[numThreads];
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // Test the permute passwords class, first build a list of strings
//        LinkedList<String> list = new LinkedList<>();
//        list.add("test");
//        list.add("1");
//        list.add("#");
//        
//        PermutePassword pass = new PermutePassword();
//        
//        pass.permute(list);
//        
//        //Print
//        int len = pass.passwords.size();
//        for (int i = 0; i < len; i++) {
//            System.out.println(pass.passwords.remove());
//        }
        

        //Read the dictionary into variable dictionary
        String workingPath = DictionaryAttack.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path = workingPath.concat("dictionaryattack/john/john.txt");
        DictionaryReader dict = new DictionaryReader(path);
        dictionary = new ArrayBlockingQueue(dict.text.length);
        for(int i = 0; i < dict.text.length; i++) {
            dictionary.add((dict.text[i]));
        }
        
        //Read the symbol table
        path = workingPath.concat("dictionaryattack/symbols/symbols.txt");
        DictionaryReader symbols = new DictionaryReader(path);
        
        //Read the numbers table
        path = workingPath.concat("dictionaryattack/numbers/numbers.txt");
        DictionaryReader numbers = new DictionaryReader(path);
        
        //Lets test the combination generator
        CombGen symGen = new CombGen(symbols.text,2);
        LinkedList<String> symGens = symGen.combin();
        
        for(int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(new DictTask(symGens));
            threads[i].start();
        }

    }
       private class DictTask implements Runnable {
           private LinkedList<String> symbols;
                
        public void DictTask(LinkedList<String> syms){
            this.symbols = syms;
        }
        public void run() {
            String word;
            while(dictionary.peek() != null){
                try {
                    word = dictionary.poll(10, TimeUnit.MILLISECONDS);
                    
                    //passwordChecker.add()
                } catch (InterruptedException ex) {
                    Logger.getLogger(DictionaryAttack.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            return;
        }
    }
//    private class ComboTask implements Runnable {
//        String word;
//        DictionaryReader symbols;
//        DictionaryReader numbers;
//        int rule;
//                
//        public void Task(DictionaryReader symTab, DictionaryReader numTab, int rule){
//            this.symbols = symTab;
//            this.numbers = numTab;
//            this.rule = rule;
//        }
//        public void run() {
//            //make combos based on the rule!
//            return;
//        }
//    }
    
    
 
    
    
}
