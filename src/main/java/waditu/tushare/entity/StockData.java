package waditu.tushare.entity;

/**
 * Created by Raymond on 24/12/2016.
 */
public class StockData extends TradeData {
    private double turnover;
    public double getTurnover() {
        return turnover;
    }

    public void setTurnover(double turnover) {
        this.turnover = turnover;
    }
}
