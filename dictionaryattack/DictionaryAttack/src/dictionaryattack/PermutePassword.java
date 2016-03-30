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
    private LinkedList<String> temp;
    private String item;
    
    //Constructor
    public PermutePassword() {
        //Temporary string array for changing the input
        temp = new LinkedList<>();
        item = new String();
    }
    public LinkedList<String> permute(LinkedList<String> list){
        LinkedList<String> passwords = new LinkedList();
        permuteRecur(list, passwords);
        return passwords;
    }
    //Takes a list of strings, generates all permuted stings
    private void permuteRecur(LinkedList<String> list, LinkedList<String> passwords) {
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
                permuteRecur(inputs, passwords);
                
                //Remove element from the list
                temp.removeLast();
            }
        }
    }
}