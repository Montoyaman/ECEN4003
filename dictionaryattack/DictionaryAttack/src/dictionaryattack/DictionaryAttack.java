/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author RyanDavidMontoya
 */
public class DictionaryAttack {
    
    public static ReentrantLock dictCountLock = new ReentrantLock();
    public static int dictCount = 0;
    //public static BlockingQueue<String> dictionary;
    public static String[] dictionary;
    public static int numDictThreads = 1;
    public static int numHCheckThreads = 1;
    private static int MAXSYMBOLS = 2;
    public static int numGenThreads = MAXSYMBOLS - 1;
    private static Thread[] hCheckThreads;
    private static Thread[] dictThreads;
    private static Thread[] genThreads = new Thread[numGenThreads];
    private static HashChecker hChecker;
    private static LinkedList<LinkedList<String>>[] symbolTable = new LinkedList[MAXSYMBOLS];
    private static final CharSub charSub = new CharSub();
    private static PasswordMatrix passMatrix;
    public static int progType = 1;
    public static boolean stop = false;
    public static long overallStart = 0;
    public static long overallEnd = 0;
    
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {    
        
        //progName, numDictThreads, numHCheckThreads, (1:sequential, 2:passThrough, 3:readerWriter)
        numDictThreads = Integer.parseInt(args[0]);
        dictThreads = new Thread[numDictThreads];
        numHCheckThreads = Integer.parseInt(args[1]);
        hCheckThreads = new Thread[numHCheckThreads];
        progType = Integer.parseInt(args[2]);
        
        
        //Read the dictionary into variable dictionary
//        String workingPath = DictionaryAttack.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String workingPath = "./";
        String path = workingPath.concat("dictionaryattack/john/john.txt");
        DictionaryReader dict = new DictionaryReader(path);
        dictionary = dict.text;
        dict.OpenFile();
        passMatrix = new PasswordMatrix(dictionary.length);
        //dictionary = new ArrayBlockingQueue(dict.text.length);
        /*for(int i = 0; i < dict.text.length; i++) {
            //add index
            //dictionary.add(dict.text[i]);
        }*/
        
        //need hashed passwords
        path = workingPath.concat("dictionaryattack/hashes.txt");
        DictionaryReader hashes = new DictionaryReader(path);
        hashes.OpenFile();
        
        hChecker = new HashChecker(hashes);
        
        //Read the symbol table
        path = workingPath.concat("dictionaryattack/symbols/symbols.txt");
        DictionaryReader symbols = new DictionaryReader(path);
        symbols.OpenFile();
        
        //Read the numbers table
        path = workingPath.concat("dictionaryattack/numbers/numbers.txt");
        DictionaryReader numbers = new DictionaryReader(path);
        
        symbolTable[0] = new LinkedList();
        for(int i = 0; i < symbols.text.length; i++) {
            LinkedList<String> temp;
            temp = new LinkedList();
            temp.add(symbols.text[i]);
            symbolTable[0].add(temp);
        }
        
        for(int i = 0; i <= MAXSYMBOLS - 2; i++) {
            symbolTable[i+1] = new LinkedList();
            genThreads[i] = new Thread(new Generator(i+2, symbols));
            genThreads[i].start();
        }
                
        for(int i = 0; i < MAXSYMBOLS-1; i++) {
            genThreads[i].join();
        }
        
        for(int i = 0; i < numDictThreads; i++) {
            dictThreads[i] = new Thread(new DictTask());
            dictThreads[i].start();
        }
        if(progType == 2){
            for(int i = 0; i < numHCheckThreads; i++) {
                hCheckThreads[i] = new Thread(new HashDeqer());
                hCheckThreads[i].start();
            }
        }
            
        overallStart = System.currentTimeMillis();
        for(int i = 0; i < numDictThreads; i++) {
            dictThreads[i].join();
        }
        if(progType == 2){
            stop = true;
            for(int i = 0; i < numHCheckThreads; i++){
                hCheckThreads[i].join();
            }
        }
        overallEnd = System.currentTimeMillis();
        System.out.println((overallEnd-overallStart)/1000);
    }
    
    private static class HashDeqer implements Runnable {
        
        public void run(){
            while(!stop){
                hChecker.checkMatch();
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    
                }
            }
            hChecker.checkMatch();
        }
    }
    
    private static class Generator implements Runnable {
        private final int combos;
        private final CombGen gen;
        private final DictionaryReader symbols;
        public Generator(int n, DictionaryReader symbols){
            combos = n;
            this.symbols = symbols;
            gen = new CombGen(this.symbols.text, combos);
        }
        @Override
        public void run() {
            LinkedList<String> temp;
            while (true) {
                temp = gen.combin();
                if(temp == null) {
                    break;
                }
                symbolTable[combos - 1].add(temp);
            }
           
           
        }
    }
    
    private static class DictTask implements Runnable {
               
        public DictTask(){
        }
        
        public void tryTwoPermute(String word1, String word2){
            LinkedList<String> symbol;
            PermutePassword perm = new PermutePassword(); 
            LinkedList<String> permutedPasswords;
            for( int i = 0; i < MAXSYMBOLS; i++){

                for(int j = 0; j < symbolTable[i].size() ; j++) {
                    symbol = new LinkedList(symbolTable[i].get(j));
                    symbol.add(word1);
                    symbol.add(word2);
                    permutedPasswords = perm.permute(symbol);
                    for(int k = 0; k < permutedPasswords.size(); k++) {
                        //System.out.println(permutedPasswords.get(k));
                        hChecker.add(permutedPasswords.get(k));
                        //progType (1)
                        if(progType == 1){
                            hChecker.checkOneMatch();
                        }                    
                    }
                }
            }
        }
        
        public void tryOnePermute(String word1){
            LinkedList<String> symbol;
            PermutePassword perm = new PermutePassword(); 
            LinkedList<String> permutedPasswords;

            for( int i = 0; i < MAXSYMBOLS; i++){

                for(int j = 0; j < symbolTable[i].size() ; j++) {
                    symbol = new LinkedList(symbolTable[i].get(j));
                    symbol.add(word1);
                    permutedPasswords = perm.permute(symbol);
                    for(int k = 0; k < permutedPasswords.size(); k++) {
                        //System.out.println(permutedPasswords.get(k));
                        hChecker.add(permutedPasswords.get(k));
                        if(progType == 1){
                            hChecker.checkOneMatch();
                        }
                    }
                }
            }
        }
        
        @Override
        public void run() {
            String rowWord;
            long start = 0;
            long end = 0;
            int myIndex;
            LinkedList<String> colWords;
            
            for(int idxRow = 0; idxRow < dictionary.length; idxRow++){
                
                for(int idxCol = 0; idxCol < dictionary.length; idxCol++){
                    Node node = passMatrix.getNode(idxRow, idxCol);
                    if(node.isComplete()){
                        continue;
                    }
                    
                    colWords = charSub.sub(dictionary[idxCol]);
                    if(!node.isStarted()){
                        node.startList(colWords.size());
                    }
                    
                    if(node.elements != null){
                        for(int idx = 0; idx < node.elements.length; idx++){
                            //Is it initialized?
                            if(node.elements[idx] == null){
                                break;
                            }
                            //Try to take that element
                            if(node.elements[idx].compareAndSet(false, true)) {
                                if(idxRow == idxCol){
                                    
                                    tryOnePermute(colWords.get(idx));
                                }
                                
                                tryTwoPermute(dictionary[idxRow], colWords.get(idx));
                            }
                            
                            if(idx == node.elements.length-1){
                                node.setComplete();
                            }
                        }//for idx                         
                    }//if node.elemnts
                }//idxCol
            }//idxRow
        }
    }
}
