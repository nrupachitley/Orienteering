/**
 * Main class for Orienteering application
 */
package jp.co.worksap.global;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Ankit Tare
 * 
 */
public class Orienteering {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Enter width and height");
		Scanner sc = new Scanner(System.in);
		try {
			int w = sc.nextInt();
			int h = sc.nextInt();
			if (h < 1 || h > 100 || w < 1 || w > 100)
				throw new InputMismatchException();
			new BeginGame(w, h).start();
		} catch (InputMismatchException e) {
			System.err.println("Please Enter Corrent Dimensions");
		}
		finally{
			sc.close();
		}
		
	}

}
