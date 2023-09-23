public class MetaData {
    private String instrumentType;
    private String expiryDate;

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

    @Override
    public String toString() {
        return "MetaData{" +
                "instrumentType='" + instrumentType + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                '}';
    }
}
