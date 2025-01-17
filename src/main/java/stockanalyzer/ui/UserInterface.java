package stockanalyzer.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import stockanalyzer.ctrl.Controller;
import stockanalyzer.downloader.ParallelDownloader;
import stockanalyzer.downloader.SequentialDownloader;

public class UserInterface 
{

	private Controller ctrl = new Controller();
	private SequentialDownloader sequentialDownloader = new SequentialDownloader();
	private ParallelDownloader parallelDownloader = new ParallelDownloader();

	public void getDataFromCtrl1(){
		try {
			ctrl.process("AAPL");
		} catch (YahooException y) {
			System.out.println(y.getMessage());
		}
	}

	public void getDataFromCtrl2() {
		try {
			ctrl.process("AMZN");
		} catch (YahooException y) {
			System.out.println(y.getMessage());
		}
	}

	public void getDataFromCtrl3() {
		try {
			ctrl.process("FB");
		} catch (YahooException y) {
			System.out.println(y.getMessage());
		}
	}
	
	public void getDataForCustomInput() {
		System.out.print("Enter the desired stock symbol(s), separated by a comma, without spaces: ");
		Scanner scanner = new Scanner(System.in);
		String ticker = scanner.next();
		try {
			ctrl.process(ticker);
		} catch (YahooException y) {
			System.out.println(y.getMessage());
		}
	}

	public void getDataFromCtrl4() {
		try {
			ctrl.process("AAPL,AMZN,FB");
		}catch (YahooException y){
			System.out.println(y.getMessage());
		}
	}

	public void getDataFromCtrl5() {
		List<String> tickers = new ArrayList<>();
		Collections.addAll(tickers, "AAPL", "AMZN", "FB");
		long startTime = System.currentTimeMillis();
		sequentialDownloader.process(tickers);
		long endTime = System.currentTimeMillis();
		System.out.println("Time elapsed sequentially: " + (endTime-startTime) + " ms");

		startTime = System.currentTimeMillis();
		parallelDownloader.process(tickers);
		endTime = System.currentTimeMillis();
		System.out.println("Time elapsed parallelly: " + (endTime-startTime) + " ms");
	}

	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interface");
		menu.setTitel("Wählen Sie aus:");
		menu.insert("a", "Apple", this::getDataFromCtrl1);
		menu.insert("b", "Amazon", this::getDataFromCtrl2);
		menu.insert("c", "Facebook", this::getDataFromCtrl3);
		menu.insert("d", "Stock of your choice",this::getDataForCustomInput);
		menu.insert("z", "All data of Apple, Amazon, and Facebook at once",this::getDataFromCtrl4);
		menu.insert("s", "Download tickers",this::getDataFromCtrl5);
		menu.insert("q", "Quit", null);
		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		ctrl.closeConnection();
		System.out.println("Program finished");
	}


	protected String readLine() 
	{
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
		} catch (IOException e) {
			System.out.println("Wrong input!");
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 
	{
		Double number = null;
		while(number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
			}catch(NumberFormatException e) {
				number=null;
				System.out.println("Please enter a valid number:");
				continue;
			}
			if(number<lowerlimit) {
				System.out.println("Please enter a higher number:");
				number=null;
			}else if(number>upperlimit) {
				System.out.println("Please enter a lower number:");
				number=null;
			}
		}
		return number;
	}
}
