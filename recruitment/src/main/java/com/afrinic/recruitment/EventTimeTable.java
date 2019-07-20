package com.afrinic.recruitment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class EventTimeTable{

	/* Function that helps to read data from input file*/
	public ArrayList<String> fileReader(String srcFile){
		ArrayList<String> returned=new ArrayList<String>();
		try (BufferedReader inputFile = new BufferedReader(new FileReader(srcFile))) {
			String line="";
			while( (line=inputFile.readLine()) != null) {
				String [] words = line.split(";"); 
				if(words[1].equals("PechaKucha"))
					words[1]="005min";
				else
					if(words[1].length()<6)
						words[1]="0"+words[1];
				words[1]=words[1].substring(0, 3);
				line=words[1]+";"+words[0]+" ("+words[2]+")";
				returned.add(line);
			}
		} catch (FileNotFoundException fnfe) {
			// the passed file is not found ...
			System.err.println("Cannot open the file 1 " + fnfe.getMessage());
		}
		catch(IOException ioe) {
			// some IO error occurred when reading the file ...
			System.err.printf("Error when processing file; exiting 1... ");
		}
		Collections.sort(returned);
		return returned;
	}

	/* Function that helps to write results in an output file*/
	public void fileWriter(HashMap<Integer, ArrayList<String>> inData,String dstFile){
		try (BufferedWriter outputFile = new BufferedWriter(new FileWriter(dstFile))) {
			for(int q=0;q<inData.size();q++) {
				int k=q+1;

				ArrayList<String> liste=inData.get(q);
				int tail=liste.size();
				int j, i=0;
				Boolean b=false;
				while(i<tail && !b) {
					if(liste.get(i).equals("12:30-14:00: Lunch"))
						b=true;
					i++;
				}
				j=i-1;
				i=0;
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Day "+k+':');
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Morning session:");
				outputFile.newLine();
				while( i<j) {
					outputFile.write(liste.get(i));
					outputFile.newLine();
					i++;
				}
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write(liste.get(j));
				outputFile.newLine();
				outputFile.newLine();
				outputFile.write("Afternoon session:");
				outputFile.newLine();
				i=j+1;
				while( i<tail) {
					outputFile.write(liste.get(i));
					outputFile.newLine();
					i++;
				}
			}
		}
		catch (FileNotFoundException fnfe) {
			// the passed file is not found ...
			System.err.println("Cannot open the file 2 " + fnfe.getMessage());
		}
		catch(IOException ioe) {
			// some IO error occurred when reading the file ...
			System.err.printf("Error when processing file; exiting 2... ");
		}
	}
	//Function that classifies the sessions per day without arrangement
	public HashMap<Integer, ArrayList<String>> orderDate(ArrayList<String> list){
		HashMap<Integer, ArrayList<String>> returned=new HashMap<Integer, ArrayList<String>>();
		ArrayList<Integer> myArray=new ArrayList<Integer>();
		int totalDuration=0;
		for (String string : list) {
			String [] words = string.split(";"); 
			int value=new Integer(words[0]);
			totalDuration+=value;
			myArray.add(value);
		}
		//The number of working hour per day is 7, from 9 to 12:30 am and from 2 to 5:30 pm, i.e 420mn

		double numOfDayDouble=totalDuration/420.0;
		double test=numOfDayDouble-(int)numOfDayDouble;
		int numOfDay=0;
		if(test==0)
			numOfDay=(int)numOfDayDouble;
		else
			numOfDay=(int)numOfDayDouble+1;

		int i=0;
		int [][] tab=new int[numOfDay][2];//tab[i}[j] represents the time already consumed for a day
		for(i=0;i<numOfDay;i++) {
			returned.put(i, new ArrayList<String>());
		}
		int k=myArray.size();//myArray contains all the periods for differents presentations
		for(int j=k-1;j>=0;j--) {
			for(int l=0;l<numOfDay;l++) {
				int aValMorning=tab[l][0]+myArray.get(j);
				int aValAfternoon=tab[l][1]+myArray.get(j);
				if(aValMorning<=210) {//It can be schedule in the morning
					int v=myArray.get(j);
					tab[l][0]+=v;
					String valS=v+"";
					returned.get(l).add(valS);
					break;
				}else {
					if(aValAfternoon<=210) {
						int v=myArray.get(j);
						tab[l][1]+=myArray.get(j);
						String valS=v+"";
						returned.get(l).add(valS);
						break;
					}
				}
			}
		}
		return returned;
	}

	//Function that orders sessions in a given day
	public ArrayList<String> dayEvent(ArrayList<String> list){
		//The argument list is suppose to contain all the event of a day, but not yet schedulled
		ArrayList<String> returned=new ArrayList<String>();
		String morning = "09:00";
		String afternoon="14:00";
		String endMorning = "12:30";
		String endAfternoon="17:30";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Date dMorning,dAfternoon,dEndMorning,dEndAfternoon;
		for (String string : list) {
			String [] words = string.split(";"); 
			int time=new Integer(words[0]);
			try {
				dEndMorning=df.parse(endMorning);
				Calendar calEndMorning=Calendar.getInstance();
				calEndMorning.setTime(dEndMorning);

				dEndAfternoon=df.parse(endAfternoon);
				Calendar calEndAfternoon=Calendar.getInstance();
				calEndAfternoon.setTime(dEndAfternoon);

				dMorning = df.parse(morning);
				dAfternoon = df.parse(afternoon);
				//Morning
				Calendar calMorning = Calendar.getInstance();
				calMorning.setTime(dMorning);

				Calendar calAfternoon = Calendar.getInstance();
				calAfternoon.setTime(dAfternoon);

				calMorning.add(Calendar.MINUTE, time);

				if(calMorning.compareTo(calEndMorning)<=0) {
					String [] val = morning.split(":");
					String valRet=morning+"-"+df.format(calMorning.getTime())+": "+words[1];
					morning=df.format(calMorning.getTime());

					returned.add(valRet);
				}
				else {
					calAfternoon.add(Calendar.MINUTE, time);
					if(calAfternoon.compareTo(calEndAfternoon)<=0) {
						String valRet=afternoon+"-"+df.format(calAfternoon.getTime())+": "+words[1];
						afternoon=df.format(calAfternoon.getTime());
						returned.add(valRet);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//We ensured that Networking session is put after 4:30pm
		try {
			Date aDate;
			String time="16:30";
			aDate=df.parse(time);
			Calendar calADate=Calendar.getInstance();
			calADate.setTime(aDate);

			dAfternoon = df.parse(afternoon);
			Calendar calAfternoon = Calendar.getInstance();
			calAfternoon.setTime(dAfternoon);
			String networking=null;
			if(calAfternoon.compareTo(calADate)<=0) {
				networking=time+": "+"Networking";
			}
			else {
				networking=afternoon+": "+"Networking";
			}
			returned.add(networking);
		}catch(Exception e) {
			e.printStackTrace();
		}

		String lunch="12:30-14:00: Lunch";
		returned.add(lunch);
		Collections.sort(returned);


		return returned;
	}
	
	//Function that returns all the sessions for all the days in the form of Map<k,V> where k 
	//represents the day and V the arrays list containing all the sessions of that day
	public HashMap<Integer, ArrayList<String>> addDate(HashMap<Integer, ArrayList<String>> listOfElements, ArrayList<String> list){
		HashMap<Integer,ArrayList<String>> returned=new HashMap<Integer, ArrayList<String>>();

		int i=listOfElements.size();
		for(int j=0;j<i;j++) {
			ArrayList<String> arrayToReturn=new ArrayList<String>();
			ArrayList<String> myElements=listOfElements.get(j);
			for (String string : myElements) {
				for (int m=0;m<list.size();m++) {
					String string2=list.get(m);
					if(string2.contains(string)) {
						arrayToReturn.add(string2);
						list.remove(m);
					}
				}
			}
			returned.put(j, dayEvent(arrayToReturn));
		}
		
		return returned;
	}
}