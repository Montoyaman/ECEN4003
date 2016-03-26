/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionaryattack;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.LineNumberReader;

/**
 * This class is responsible for extracting the dictionary contents
 * @author RyanDavidMontoya
 */
public class DictionaryReader  {
    private final String dictionaryPath;
    public int numLines;
    private int[] choose; //This counts the number of times the X choose Y has been called, where Y indexes the array
    
    public DictionaryReader(String filepath) throws IOException{
        dictionaryPath = filepath;
        numLines = getNumLines();
        choose = new int[numLines];
        
        //Init the choose array
        for (int i = 0; i < numLines; i++) {
            choose[i] = 0;
        }
    }
    
    //This function extracts a specific line from the dictionary, start zero indexed
    private String getLine(int lineNum) throws IOException {
        FileReader Reader = new FileReader(dictionaryPath);
        BufferedReader tRead = new BufferedReader(Reader);        
        
        String line = new String();
        
        for (int i = 0; i <= lineNum;i++) {
            line = tRead.readLine(); //Get the line
        }
        
        return line; //Return the line
    }
    
    private int getNumLines() throws IOException {
        FileReader Reader = new FileReader(dictionaryPath);
        LineNumberReader lineReader = new LineNumberReader(Reader);
        
        int lines = 0;  //Initialize to zero lines
        
        while(lineReader.readLine() != null) {
            lines++; //Read another line
        }
        
        return lines; //Number of lines in the file
    }
    
    //This function returns subset strings from the table, ORDER DOESN'T MATTER
    public String[] ReadEntries(int k) throws IOException {        
        //Read how many times this choose order has been used
        choose[k]++; //Increment the number of times choose k has been called
        
        //Now, use this to determine the next combination of chosen strings
        String[] comb = combination(0,k);
        
        return comb;
    }
    
    private String[] combination(int cnt, int k) {
        //This function is recursive, and returns the combination using order k
        while (cnt < choose[k]) {
            
            combination(cnt,k);
            
        }
        
        return
    }
    
    public String[] OpenFile() throws IOException{
        FileReader Reader = new FileReader(dictionaryPath);
        BufferedReader tRead = new BufferedReader(Reader);
        
        String[] text = new String[numLines];
        
        for (int i = 0; i < numLines; i++) {
            text[i] = tRead.readLine(); //Populate string array
        }
        
        return text; //This is a big file potentially, might not be a good idea
    }
}
