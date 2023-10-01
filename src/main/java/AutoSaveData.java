import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.ServerException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class AutoSaveData {

    private static List<String> API_SYMBOL_LIST = asList("NIFTY", "BANKNIFTY");

    public static void main(String[] args) {
        WebClient client = WebClient.builder()
                .baseUrl("https://www.nseindia.com")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               LocalDateTime localDateTime = LocalDateTime.now();
                               if (isTimePassed(localDateTime)) {
                                   System.out.println("Stopping the application");
                                   timer.cancel();
                               }

                               try {
                                   process(client);
                               } catch (IOException e) {
                                   System.out.println("Exception occurred: " + e.getMessage());
                               }

                           }
                       }, 15 * 1000, 15 * 1000
        );
    }

    private static void process(WebClient client) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        LocalDateTime localDateTime = LocalDateTime.now();

        for (String api : API_SYMBOL_LIST) {
            Optional<StockData> stockDataOptional = getStockData(client, api);
            if (stockDataOptional.isPresent()) {
                workbook = generateWorkBook(stockDataOptional.get(), api, workbook);
            }
        }

        System.out.println("Writing data as Excel file");
        FileOutputStream out = new FileOutputStream(
                "./nseindia/data_" + localDateTime.toLocalDate() + "(" + localDateTime.getHour() + "-" + localDateTime.getMinute() + "-" + localDateTime.getSecond() + ").xlsx");
        workbook.write(out);
        out.close();
    }

    private static boolean isTimePassed(LocalDateTime localDateTime) {
        return localDateTime.getHour() >= 15 && localDateTime.getMinute() >= 30;
    }

    public static XSSFWorkbook generateWorkBook(StockData stockData, String sheetName, XSSFWorkbook workbook) {
        XSSFSheet spreadsheet
                = workbook.createSheet(sheetName);

        // creating a row object
        int rowid = 0;
        XSSFRow row = spreadsheet.createRow(rowid++);


        // writing the data into the sheets...
        List<MetaData> stockMetaData = stockData.getStocks().stream()
                .map(stocks -> stocks.getMetadata())
                .collect(Collectors.toList());

        Object[] headerArr = new Object[]{
                "INSTRUMENT TYPE", "EXPIRY DATE", "OPTION", "STRIKE", "OPEN", "HIGH", "LOW", "CLOSE",
                "PREV.CLOSE", "LAST", "CHNG", "%CHNG", "VOLUME(Contracts)", "VALUE(₹ Lakhs)"
        };
        insertCellData(row, headerArr);

        for (MetaData stock : stockMetaData) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = new Object[]{stock.getInstrumentType(), stock.getExpiryDate(), stock.getOptionType(), stock.getStrikePrice(),
                    stock.getOpenPrice(), stock.getHighPrice(), stock.getLowPrice(), stock.getClosePrice(),
                    stock.getPrevClose(), stock.getLastPrice(), stock.getChange(), stock.getpChange(), stock.getNumberOfContractsTraded(), stock.getTotalTurnover()};
            insertCellData(row, objectArr);
        }
        return workbook;
    }

    private static void insertCellData(XSSFRow row, Object[] objectArr) {
        int cellid = 0;

        for (Object obj : objectArr) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
        }
    }

    public static Optional<StockData> getStockData(WebClient client, String api) {
        System.out.println("Fetching data from API");
        Mono<StockData> result = client.get()
                .uri("/api/quote-derivative?symbol=" + api).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ServerException(" server error")))
                .bodyToMono(StockData.class)
                .retryWhen(Retry.backoff(30, Duration.ofSeconds(3)));

        return result.blockOptional();
    }
}
