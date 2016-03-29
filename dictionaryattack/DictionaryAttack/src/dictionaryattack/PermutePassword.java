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
public class PermutePassword {
    LinkedList<String> passwords;
    LinkedList<String> temp;
    String item;
    
    //Constructor
    public PermutePassword() {
        passwords = new LinkedList<>();
        //Temporary string array for changing the input
        temp = new LinkedList<>();
        item = new String();
    }
    
    //Takes a list of strings, generates all permuted stings
    public void permute(LinkedList<String> list) {
        int len = list.size();
        //If there are no more elements
        if (len == 0) {
            //Generate the string by concatenating temp
            for (int i = 0; i < temp.size(); i++){
                item = item.concat(temp.get(i));
            }
            passwords.add(item);
            item = ""; //Clear the item
        } else {
            //Remove an element and pass array down
            for (int i = 0; i < len; i++){
                LinkedList<String> inputs = new LinkedList<>(); //Copy each time so we don't edit the original
                inputs.addAll(list); //Deep copy
                
                //Add the element to temporary list
                temp.add(inputs.get(i));
                
                //Remove the element, push further
                inputs.remove(i);
                permute(inputs);
                
                //Remove element from the list
                temp.removeLast();
            }
        }
    }
}