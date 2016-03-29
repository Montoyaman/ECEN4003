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
    public int n;
    public String text[];
    
    public DictionaryReader(String filepath) throws IOException{
        dictionaryPath = filepath;
        n = getNumLines();
        text = new String[n];
    }
    
    //This function extracts a specific line from the dictionary, start zero indexed
    private String getLine(int lineNum) throws IOException {
        FileReader Reader = new FileReader(dictionaryPath);
        BufferedReader tRead = new BufferedReader(Reader);        
        
        String line = new String();
        
        for (int i = 0; i <= lineNum;i++) {
            line = tRead.readLine(); //Get the line
            
            if (line == null) return null;
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
    
    public void OpenFile() throws IOException{
        FileReader Reader = new FileReader(dictionaryPath);
        BufferedReader tRead = new BufferedReader(Reader);
        
        for (int i = 0; i < n; i++) {
            text[i] = tRead.readLine(); //Populate string array
        }
       
    }
}
