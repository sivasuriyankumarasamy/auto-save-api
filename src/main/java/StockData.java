import java.util.List;

public class StockData {

    private List<Stocks> stocks;

    public List<Stocks> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stocks> stocks) {
        this.stocks = stocks;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "stocks=" + stocks +
                '}';
    }
}
