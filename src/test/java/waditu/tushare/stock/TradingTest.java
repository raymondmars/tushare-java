package waditu.tushare.stock;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Raymond on 24/12/2016.
 */
public class TradingTest {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Test
    public void testGetTradeList() {
        try {
            Date startDate = dateFormat.parse("2016-12-21");
            Date endDate   = dateFormat.parse("2016-12-23");
            Trading.getTradeList("300118", "D", startDate, endDate);
//            Trading.getTradeList("sh", "D", startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testGetTickData() {
        try {
            Date date = dateFormat.parse("2016-12-21");
            Trading.getTickData("300118", date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void testGetQuotesData() {
        try {
//            System.out.println(Utility.generateRandom(1000_0000, 10_0000_0000));
            Trading.getRealtimeQuotes("000581");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
