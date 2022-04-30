package stockanalyzer.ctrl;

import stockanalyzer.ui.YahooException;
import yahooApi.YahooFinance;
import yahooApi.beans.QuoteResponse;
import yahooApi.beans.Result;
import yahooApi.beans.YahooResponse;

import java.text.DecimalFormat;
import java.util.List;

public class Controller {

	private static final DecimalFormat df = new DecimalFormat("0.00");

	public void process(String ticker) throws YahooException {
		System.out.println("Start process...");
		try {
			QuoteResponse quote = getData(ticker).getQuoteResponse();
			quote.getResult().stream().forEach(q -> System.out.println("Share: " + q.getLongName() + "\nCurrent price: " + df.format(q.getAsk())));
			System.out.println("Highest market price: " + df.format(getHighest(quote)));
			System.out.println("Average market price: " + df.format(getAverage(quote)));
			System.out.println("Amount of stock shares: " + getNumSets(quote));
		} catch (Exception e) {
			throw new YahooException("An error occurred.");
		} finally {
			closeConnection();
		}
	}

	public YahooResponse getData(String searchString) throws YahooException {
		YahooFinance yahooFinance = new YahooFinance();
		List<String> tickers = List.of(searchString);
		return yahooFinance.getCurrentData(tickers);
	}

	public double getHighest(QuoteResponse quote) {
		return quote.getResult().stream().map(Result::getRegularMarketPrice).max(Double::compare).get();
	}

	public double getAverage(QuoteResponse quote) {
		return quote.getResult().stream().mapToDouble(Result::getRegularMarketPrice).average().getAsDouble();
	}

	public int getNumSets(QuoteResponse quote) {
		return (int) quote.getResult().stream().count();
	}

	public void closeConnection() {
	}
}
