package com.afrinic.recruitment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App{
    public static void main( String[] args )    {
    	//The first argument should be the input file and the second shound be the output file
    	EventTimeTable ett=new EventTimeTable();
    	ArrayList<String> reader=ett.fileReader(args[0]);
    	HashMap<Integer, ArrayList<String>>map1=ett.orderDate(reader);
		HashMap<Integer, ArrayList<String>>map=ett.addDate(map1,reader);
		ett.fileWriter(map, args[1]);
        
    }
}
