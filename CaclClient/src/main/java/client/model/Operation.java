package client.model;

public enum Operation {

    ADDITION(" + "),
    SUBTRACTION(" - "),
    MULTIPLICATION(" x "),
    DIVISION(" / "),
    EQUAL(" = ");

    private final String symbol;

    Operation(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
