/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author RyanDavidMontoya
 */
public class DictionaryAttack {

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

        //Test the reader
//        String workingPath = new String();
        if (args.length != 4) {
            System.out.println("incorrect # of arguments");
            return;
        }
        
        
        String workingPath = DictionaryAttack.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        workingPath = workingPath.concat("dictionaryattack/");
        
        String dictPath = workingPath.concat(args[0]);
        String hashPath = workingPath.concat(args[1]);
        String rulesPath = workingPath.concat(args[2]);
        String rules = args[3];
        String charSet[] = rules.split("\\r?\\n");
        
        DictionaryReader dict = new DictionaryReader(dictPath);
        DictionaryReader hashes = new DictionaryReader(hashPath);
        
        //PullStrings puller = new PullStrings(path);
        //String[] dictionary = puller.OpenFile();
        int numLines = dict.getNumLines();
        
        for(int i = 0; i <= numLines; i++) {
            for(int j = 0; j <= charSet.length; j++) {
                PermutePassword pass = new PermutePassword();
                LinkedList<String> passCombo = new LinkedList();

                passCombo.add(dict.text[i]);
                passCombo.add(charSet[j]);
                pass.permute(passCombo);
                System.out.println(pass.passwords);
            }
        }
    }
    
    
    private class Task implements Runnable {
        
        
        
        public void run() {
            return;
        }
    }
}
