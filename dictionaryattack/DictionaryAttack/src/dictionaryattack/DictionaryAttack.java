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

        //Read the dictionary into variable dictionary
        String workingPath = DictionaryAttack.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String path = workingPath.concat("dictionaryattack/john/john.txt");
        DictionaryReader dict = new DictionaryReader(path);
        String[] dictionary = dict.OpenFile();
        
        //Read the symbol table
        path = workingPath.concat("dictionaryattack/symbols/symbols.txt");
        DictionaryReader syms = new DictionaryReader(path);
        String[] symbols = syms.OpenFile(); 
        
        //Read the numbers table
        path = workingPath.concat("dictionaryattack/numbers/numbers.txt");
        DictionaryReader nums = new DictionaryReader(path);
        String[] numbers = nums.OpenFile(); 
        
        //Lets test the combination generator
        CombGen gen = new CombGen(symbols,2);
        
        //Pull the first value
        LinkedList<String> test = gen.combin();
        test = gen.combin();

    }
    
}
