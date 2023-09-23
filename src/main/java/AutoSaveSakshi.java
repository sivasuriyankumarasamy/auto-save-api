import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.Optional;

//@SpringBootApplication
public class AutoSaveSakshi {

    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
////        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
////        headers.setAccept("*/*");
//        headers.set("Accept", "*/*");
//        headers.set("Accept-Encoding", "gzip, deflate, br");
//        headers.set("Cookie", "ak_bmsc=8A6278F27A95E499AF8CCE32320D5133~000000000000000000000000000000~YAAQdSdzaIzY0pCKAQAAJTXzwRXBZjvujx2JEOh5V4QqF4dmRJZ4+2HJY7EngG+HKVchCn6yMZDqTfi4BM15SU/LrQP636hb27Z6yo6WdSlM2j1xqb5FeBax0XZvfxrJWdGHisTwEzBXkRTiuxKMPfOeIEYUBf5IJqRHgP437k54h88t7WvRN+nB4Bys5XsCAlFj4KtPH43tLhHuApjtyyDSkaCZXwLYHCQJ4VEOQFSazrwDR3ZCbjKcoYerOENBR4eAC2mtm57wG6SXjBk6DES8Fc1qTtZDtXqAioPMR+rEJliKgyeF7D88IA+3GRuFQ0Psf8MRCdfgtW6ilKZ9YMx3GQZRQwGQTz016r4h+RGK122E8l4U6VLb+Ms6zPoMXTIV6kbuqKaTrS4=; bm_sv=3056C9F5955898212226343E65CE38BD~YAAQdSdzaN1q05CKAQAAgUUGwhWeYYVvDvGr2XYthQtv26vEneD8X5ZtuBNmCfsBcbiR57QNVR3Tlc7liNRaG3/387M4xK7gbKOfmIccPEOwJyc7mw5F6SZiJDry3h6k/hgg+ekbYClu/fqAH2K5NJmPc8Z+OD8PZjwfPVctXuhhWTCwKZSIQ92g/nyUxoWJEZ5r2qRNMENPVQT8WUoV8v0TAcZyfMff58mnYdjUSkOsbBsT6yml76U5/R3HMIkuuiRD~1");
////        headers.setAccept(Collections.singletonList(MediaType.));
//        HttpEntity<String> entity = new HttpEntity<>(null, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange("https://www.nseindia.com/api/quote-derivative?symbol=NIFTY", HttpMethod.GET, entity, String.class);
//        //.getForObject("https://www.nseindia.com/api/quote-derivative?symbol=NIFTY", ResponseEntity.class);
////        ResponseEntity<StockData> stockData = restTemplate.getForEntity("https://www.nseindia.com/api/quote-derivative?symbol=NIFTY", StockData.class);
////        System.out.println(stockData.getBody().getStocks().getMetaData().getInstrumentType());
//        System.out.println(response);



        WebClient client = WebClient.builder()
                .baseUrl("https://www.nseindia.com")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();


        Optional<StockData> block = null;
        while (true) {
            System.out.println("While looping");
            block = getStockData(client);
            if(!block.isEmpty()) {
                break;
            }
        }
        System.out.println("block is : " + block.get().getStocks());

    }

    public static Optional<StockData> getStockData(WebClient client) {
        Mono<StockData> result = client.get()
                .uri("/api/quote-derivative?symbol=NIFTY").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(StockData.class);
        try {
            return result.blockOptional();
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }


//    ak_bmsc=8A6278F27A95E499AF8CCE32320D5133~000000000000000000000000000000~YAAQdSdzaMSz0pCKAQAAf8LuwRVEY33/MEUswfKQt6GRQZzSzlLgxI+DqMOymbKcmZnb7xtn5fkDxG0YRFSV36MvYIGOrkDmWln7tLnbSQRsKWdwKeXBEOgGiyHinrDmToGxqJV9ULG9+CeqLLO5PUJlunstdroVoUoNkRwNWFV4GixW4gkUJR4ynh44Kh7V/wLg6qwIPdPDLyLbV8A/fetVMlayCa0jTsGEoO0GWT0IWTxQnL4B4bizGFs8b/6MVI4vR2bprA7YtEsOXEm5GxShi7r0w1bjXWHl3BLeEKc1OYerIIJqCLF9UcCTvT0pupy1WCrI1fB1VIwZl/aNyd+DzBhUSUB1pWoxny0wy7qKJ97QJQPdKno0V3FZevU=; bm_sv=3056C9F5955898212226343E65CE38BD~YAAQdSdzaEW00pCKAQAAldnuwRUWofrYVizfcXOuWsRdjo67VKzOyNECM7C3w6wNamKXHppBd7HAe92PZDEXBdXy5AY+FDklYfxqmCKLVhrYVaR8h/Ey9g2TcQmZFiTCuP0bYX5rqu35rqoObzB3WntJSXAYD1JBqYD7apar2BCfSn/4p/ceZ7BNT0hHTaK0Bj6nOjC30cd0sZU+tcAsnuU/fprYMs9jP+qyYurc7UITpvhbHMSSINwzq8BogVtsTH4=~1
}
