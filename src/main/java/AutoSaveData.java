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

public class AutoSaveData {

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
        XSSFWorkbook workbook = new XSSFWorkbook();
        timer.schedule(new TimerTask() {
                           public void run() {
                               Optional<StockData> stockDataOptional = getStockData(client);
                               stockDataOptional.ifPresent(
                                       stockData -> {
                                           try {
                                               generateExcel(workbook, stockData);
                                           } catch (IOException e) {
                                               System.out.println("Could not save data");
                                           }
                                       }
                               );
                           }
                       }, 15 * 1000, 15 * 1000
        );

    }

    public static void generateExcel(XSSFWorkbook wb, StockData stockData) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        LocalDateTime localDateTime = LocalDateTime.now();
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int sec = localDateTime.getSecond();

        XSSFSheet spreadsheet
                = workbook.createSheet(" Stock data ");

        // creating a row object
        int rowid = 0;
        XSSFRow row = spreadsheet.createRow(rowid++);


        // writing the data into the sheets...

        List<MetaData> stockMetaData = stockData.getStocks().stream()
//                .filter(stocks -> "28-Sep-2023".equals(stocks.getMetadata().getExpiryDate()))
                .map(stocks -> stocks.getMetadata())
                .collect(Collectors.toList());

        Object[] headerArr = new Object[]{
                "INSTRUMENT TYPE", "EXPIRY DATE", "OPTION", "STRIKE", "OPEN", "HIGH", "LOW", "CLOSE", "PREV.CLOSE", "LAST", "CHNG", "%CHNG","VOLUME(Contracts)", "VALUE(₹ Lakhs)"
        };
//        row = spreadsheet.createRow(rowid++);
        insertCellData(row, headerArr);

        for (MetaData stock : stockMetaData) {
            row = spreadsheet.createRow(rowid++);
            Object[] objectArr = new Object[]{stock.getInstrumentType(), stock.getExpiryDate(), stock.getOptionType(), stock.getStrikePrice(),
                    stock.getOpenPrice(), stock.getHighPrice(), stock.getLowPrice(), stock.getClosePrice(),
                    stock.getPrevClose(), stock.getLastPrice(), stock.getChange(), stock.getpChange(), stock.getNumberOfContractsTraded(), stock.getTotalTurnover()};
            insertCellData(row, objectArr);
        }

        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
//                "D:/datasheet.xslx");
                "D:/nseindia/data_" + localDateTime.toLocalDate() + "(" + hour + "-" + minute + "-" + sec + ").xlsx");
//        FileOutputStream out = new FileOutputStream(
//                new File("C:/savedexcel/GFGsheet.xlsx"));
        workbook.write(out);
        out.close();
    }

    private static void insertCellData(XSSFRow row, Object[] objectArr) {
        int cellid = 0;

        for (Object obj : objectArr) {
            Cell cell = row.createCell(cellid++);
            cell.setCellValue((String) obj);
        }
    }

    public static Optional<StockData> getStockData(WebClient client) {
        Mono<StockData> result = client.get()
                .uri("/api/quote-derivative?symbol=NIFTY").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ServerException(" server error")))
                .bodyToMono(StockData.class)
                .retryWhen(Retry.backoff(30, Duration.ofSeconds(3)));

        Optional<StockData> stockDataOptional = result.blockOptional();

        return stockDataOptional;

    }
}
