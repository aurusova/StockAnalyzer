package stockanalyzer.downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ParallelDownloader extends Downloader {

    ExecutorService executorService = Executors.newCachedThreadPool();
    List<Future<String>> futures = new ArrayList<>();

    @Override
    public int process(List<String> tickers) {
        int count = 0;
        for (String ticker : tickers) {
            futures.add(executorService.submit(() -> saveJson2File(ticker)));
        }
        for (Future<String> future: futures) {
            try {
                future.get();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return count;
    }
}
