/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dictionaryattack;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author Cy
 * This class takes in a string and replaces common symbols for the characters in the word
 */
public class CharSub {
    Map<String, String> charToSymbol = new HashMap(18);
    String[] charsReplaced = new String[18];
    
    public CharSub(){
        charToSymbol.put("a", "@");
        charToSymbol.put("b", "8");
        charToSymbol.put("c", "(");
        charToSymbol.put("d", "6");
        charToSymbol.put("e", "3");
        charToSymbol.put("f", "#");
        charToSymbol.put("g", "9");
        charToSymbol.put("h", "#");
        charToSymbol.put("i", "!");
        charToSymbol.put("k", "<");
        charToSymbol.put("l", "1");
        charToSymbol.put("o", "0");
        charToSymbol.put("q", "9");
        charToSymbol.put("s", "$");
        charToSymbol.put("t", "+");
        charToSymbol.put("x", "%");
        charToSymbol.put("y", "?");
    }
    
    public LinkedList<String> sub(String word){
        LinkedList<String> ret = new LinkedList();
        int range = word.length() - 1;
        String character;
        String replace;
        StringBuilder newWord = new StringBuilder(word);
        //do repacements
        for(int i = 0; i <= range; i++) {
            character = word.substring(i, i);
            if(null != (replace = charToSymbol.get(character.toLowerCase()))){
                StringBuilder temp = new StringBuilder(word);
                temp.setCharAt(i, replace.toCharArray()[0]);
                ret.add(temp.toString());
            }
        }
        
        character = word.substring(0, 0);
        character = character.toUpperCase();
        StringBuilder temp = new StringBuilder(word);
        temp.setCharAt(0, character.toCharArray()[0]);
        ret.add(temp.toString());
        
        return ret;
    }
}
