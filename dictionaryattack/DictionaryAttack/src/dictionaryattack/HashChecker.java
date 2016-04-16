/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cy
 */
public class HashChecker {
        private BlockingQueue<String> hashQ;
        private final DictionaryReader hashes;
        
        public HashChecker(DictionaryReader hashes) {
            hashQ = new LinkedBlockingQueue<>();
            this.hashes = hashes;
        }
        
        public void add(String word) {
            hashQ.add(word);
        } 
        
        public void checkOneMatch(){
            String word;
            Hasher hash;
            
            word = hashQ.remove();
            hash = new Hasher(word, "MD5");
            for(int i = 0; i < hashes.text.length; i++){
                if (hashes.text[i].compareTo(hash.password_hash) == 0) {
                    //success!
                    //remove hash from hashes?
                    System.out.println("Password discovered at index: " + i + "     Cracked Password: " + hash.password );
                    System.out.flush();
                }
            }            
        }
        
        public void checkMatch() {
            String word;
            Hasher hash;
            while(hashQ.peek() != null){
                try {
                   word = hashQ.poll(10, TimeUnit.MILLISECONDS);
                   if(word == null){
                       continue;
                   }
                   hash = new Hasher(word , "MD5");
                } catch (InterruptedException ex) {
                    Logger.getLogger(HashChecker.class.getName()).log(Level.SEVERE, null, ex);
                    continue;
                }
                
                for(int i = 0; i < hashes.text.length; i++){
                    if (hashes.text[i].compareTo(hash.password_hash) == 0) {
                        //success!
                        //remove hash from hashes?
                        System.out.println("Password discovered at index: " + i + "     Cracked Password: " + hash.password );
                        System.out.flush();
                    }
                }
            }
        }
}
