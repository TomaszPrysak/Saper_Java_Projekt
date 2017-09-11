package app;

import java.util.ArrayList;

public class TestOverall {

	public static void main(String[] args) {
		
		int [] pos1 = {1,1};
		int [] pos2 = {4,8};
		int [] pos3 = {4,8};

		ArrayList<int[]> tymczasowy = new ArrayList<int []>();
		
		tymczasowy.add(pos1);
		tymczasowy.add(pos2);
		tymczasowy.add(pos3);
		
//		System.out.println(pos1[0]);
		
//		System.out.println(tymczasowy);
		
		System.out.println(tymczasowy.get(1).length);
		
		
		
	}

}
