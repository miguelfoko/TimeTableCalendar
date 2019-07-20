package recruitment;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.afrinic.recruitment.EventTimeTable;

public class EventTimeTableTest {
	EventTimeTable ett=new EventTimeTable();

	//	@Test
	public void testFileReader() {
		ArrayList<String> list=new ArrayList<String>();
		list.add("005;IoT impact on Internet Governance Processes (Alain)");
		list.add("005;Tech talk: Raiders of the lost ark (Jordi)");
		list.add("030;Connecting the Unconnected in Africa (Mukom)");
		list.add("180;Annual General Members Meeting (Alan)");
		list.add("090;FIRE Workshop (Olatunde)");
		list.add("090;Deployathon (David)");
		list.add("060;Internet Governance (Patricia)");
		list.add("030;Regional TTLDs (Daheda)");
		list.add("045;Benefits of THD networks (Amreesh)");
		list.add("045;Building registry ATI experience (Wafa)");
		assertEquals(10, list.size());
	}

	//	@Test
	public void testFileWriter() {
		fail("Not yet implemented");
	}

	//		@Test
	public void testOrderDate() {
		ArrayList<String> list=new ArrayList<String>();
		list.add("005;IoT impact on Internet Governance Processes (Alain)");
		list.add("005;Tech talk: Raiders of the lost ark (Jordi)");
		list.add("030;Connecting the Unconnected in Africa (Mukom)");
		list.add("180;Annual General Members Meeting (Alan)");
		list.add("090;FIRE Workshop (Olatunde)");
		list.add("090;Deployathon (David)");
		list.add("060;Internet Governance (Patricia)");
		list.add("030;Regional TTLDs (Daheda)");
		list.add("045;Benefits of THD networks (Amreesh)");
		list.add("045;Building registry ATI experience (Wafa)");
		list.add("120;Building registry ATI experience (Wafa)");
		list.add("200;Building registry ATI experience (Wafa)");

		HashMap<Integer, ArrayList<String>>map=ett.orderDate(list);
		assertEquals(2, map.size());//Testing the number of days for planning
	}

	@Test
	public void testAddDate() {
		ArrayList<String> list=new ArrayList<String>();
		list.add("005;IoT impact on Internet Governance Processes (Alain)");
		list.add("005;Tech talk: Raiders of the lost ark (Jordi)");
		list.add("030;Connecting the Unconnected in Africa (Mukom)");
		list.add("180;Annual General Members Meeting (Alan)");
		list.add("090;FIRE Workshop (Olatunde)");
		list.add("090;Deployathon (David)");
		list.add("060;Internet Governance (Patricia)");
		list.add("030;Regional TTLDs (Daheda)");
		list.add("045;Benefits of THD networks (Amreesh)");
		list.add("045;Building registry ATI experience (Wafa)");
		list.add("120;Building registry ATI experience (Wafa)");

		HashMap<Integer, ArrayList<String>>map1=ett.orderDate(list);
		HashMap<Integer, ArrayList<String>>map=ett.addDate(map1,list);
		assertEquals(2, map.size());
	}

//	@Test
	public void testDayEvent() {
		ArrayList<String> list=new ArrayList<String>();
		list.add("005;IoT impact on Internet Governance Processes (Alain)");
		list.add("005;Tech talk: Raiders of the lost ark (Jordi)");
		list.add("030;Connecting the Unconnected in Africa (Mukom)");
		list.add("180;Annual General Members Meeting (Alan)");
		list.add("090;FIRE Workshop (Olatunde)");
		list.add("090;Deployathon (David)");
		list.add("060;Internet Governance (Patricia)");
		list.add("030;Regional TTLDs (Daheda)");
		list.add("045;Benefits of THD networks (Amreesh)");
		list.add("045;Building registry ATI experience (Wafa)");
		list.add("120;Building registry ATI experience (Wafa)");

		HashMap<Integer, ArrayList<String>>map1=ett.orderDate(list);
		HashMap<Integer, ArrayList<String>>map=ett.addDate(map1,list);
		ArrayList<String> liste=ett.dayEvent(map.get(0));
		assertEquals(9, liste.size());
		
		ArrayList<String> liste2=ett.dayEvent(map.get(1));
		assertEquals(5, liste2.size());
	}
}
