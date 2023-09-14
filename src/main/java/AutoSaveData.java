import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.Duration;

@SpringBootApplication
public class AutoSaveData {

//    public static void main(String[] args) {
//        RestTemplate restTemplate = RestTemplateBuilder.setConnectTimeout(Duration.ofMillis(3000))
//                .setReadTimeout(Duration.ofMillis(3000))
//                .build();
//        ResponseEntity<StockData> stockData = restTemplate.getForEntity("http://www.nseindia.com/api/quote-derivative?symbol=NIFTY", StockData.class);
//        System.out.println(stockData.getBody().getStocks().getMetaData().getInstrumentType());
//    }

    @PostConstruct
    public void saveData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<StockData> stockData = restTemplate.getForEntity("http://www.nseindia.com/api/quote-derivative?symbol=NIFTY", StockData.class);
        System.out.println(stockData.getBody().getStocks().getMetaData().getInstrumentType());
    }
}
