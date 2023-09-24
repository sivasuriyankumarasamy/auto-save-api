public class MetaData {
    private String instrumentType;
    private String expiryDate;
    private String optionType;
    private String strikePrice;
    private String identifier;
    private String openPrice;
    private String highPrice;
    private String lowPrice;
    private String closePrice;
    private String prevClose;
    private String lastPrice;
    private String change;
    private String pChange;
    private String numberOfContractsTraded;
    private String totalTurnover;

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(String strikePrice) {
        this.strikePrice = strikePrice;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getPrevClose() {
        return prevClose;
    }

    public void setPrevClose(String prevClose) {
        this.prevClose = prevClose;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getpChange() {
        return pChange;
    }

    public void setpChange(String pChange) {
        this.pChange = pChange;
    }

    public String getNumberOfContractsTraded() {
        return numberOfContractsTraded;
    }

    public void setNumberOfContractsTraded(String numberOfContractsTraded) {
        this.numberOfContractsTraded = numberOfContractsTraded;
    }

    public String getTotalTurnover() {
        return totalTurnover;
    }

    public void setTotalTurnover(String totalTurnover) {
        this.totalTurnover = totalTurnover;
    }

    @Override
    public String toString() {
        return "MetaData{" +
                "instrumentType='" + instrumentType + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", optionType='" + optionType + '\'' +
                ", strikePrice='" + strikePrice + '\'' +
                ", identifier='" + identifier + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", highPrice='" + highPrice + '\'' +
                ", lowPrice='" + lowPrice + '\'' +
                ", closePrice='" + closePrice + '\'' +
                ", prevClose='" + prevClose + '\'' +
                ", lastPrice='" + lastPrice + '\'' +
                ", change='" + change + '\'' +
                ", pChange='" + pChange + '\'' +
                ", numberOfContractsTraded='" + numberOfContractsTraded + '\'' +
                ", totalTurnover='" + totalTurnover + '\'' +
                '}';
    }
}
