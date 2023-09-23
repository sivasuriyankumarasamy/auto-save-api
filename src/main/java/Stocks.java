public class Stocks {
    private MetaData metadata;

    public MetaData getMetadata() {
        return metadata;
    }

    public void setMetadata(MetaData metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return "Stocks{" +
                "metadata=" + metadata +
                '}';
    }
}
