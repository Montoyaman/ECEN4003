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
    private static Thread[] hCheckThreads = new Thread[numHCheckThreads];
    private static Thread[] dictThreads = new Thread[numDictThreads];
    private static Thread[] genThreads = new Thread[numGenThreads];
    private static HashChecker hChecker;
    private static LinkedList<LinkedList<String>>[] symbolTable = new LinkedList[MAXSYMBOLS];
    private static final CharSub charSub = new CharSub();
    
     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {    
        
        //progName, numDictThreads, numHCheckThreads, (1:sequential, 2:passThrough, 3:readerWriter)
        numDictThreads = Integer.parseInt(args[0]);
        numHCheckThreads = Integer.parseInt(args[1]);
        int progType = Integer.parseInt(args[2]);
        
        
        //Read the dictionary into variable dictionary
        String workingPath = DictionaryAttack.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path = workingPath.concat("dictionaryattack/john/john.txt");
        DictionaryReader dict = new DictionaryReader(path);
        dictionary = dict.text;
        dict.OpenFile();
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
        for(int i = 0; i < numDictThreads; i++) {
            dictThreads[i].join();
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
        
        @Override
        public void run() {
            String word;
            LinkedList<String> symbol;
            PermutePassword perm = new PermutePassword(); 
            LinkedList<String> permutedPasswords;
            long start = 0;
            long end = 0;
            int myIndex;
            //for MAXSYMBOLS
            
            //while(dictionary.peek() != null){
            while(dictCount != dictionary.length)
                dictCountLock.lock();
                try {
                    word = dictionary[dictCount];
                    myIndex = dictCount;
                    dictCount++;
                } finally {
                      dictCountLock.unlock();  
                }
                
                //try {
                    //word = dictionary.poll(10, TimeUnit.MILLISECONDS);
                for(int idxCol = 0; idxCol < dictionary.length; idxCol++){
                    
                    if()
                    LinkedList<String> words = charSub.sub(word);
                    //if(startList(words.size()))
                    //

                    for(int len = 0; len < words.size(); len++){

                        for( int i = 0; i < MAXSYMBOLS; i++){

                            start = System.currentTimeMillis();
                            for(int j = 0; j < symbolTable[i].size() ; j++) {
                                symbol = new LinkedList(symbolTable[i].get(j));
                                symbol.add(words.get(len));
                                permutedPasswords = perm.permute(symbol);
                                //System.out.println("WORD: " + word + "      SYMBOL: " + symbol);
                                for(int k = 0; k < permutedPasswords.size(); k++) {
                                    //System.out.println(permutedPasswords.get(k));
                                    hChecker.add(permutedPasswords.get(k));
                                    hChecker.checkMatch();
                                }
                            }
                        }
                    }
                    end = System.currentTimeMillis();
                    System.out.println("WORDs: " + words + "      TIME: " + (end-start));
                    hChecker.add(word);

                    //} catch (InterruptedException ex) {
                    //    Logger.getLogger(DictionaryAttack.class.getName()).log(Level.SEVERE, null, ex);
                    //}

                    hChecker.checkMatch();
                }
                        
        }
    }    
}
