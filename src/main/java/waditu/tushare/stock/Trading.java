package waditu.tushare.stock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import waditu.tushare.common.HTTParty;
import waditu.tushare.common.Utility;
import waditu.tushare.entity.QuoteData;
import waditu.tushare.entity.TickData;
import waditu.tushare.entity.TradeData;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static waditu.tushare.common.Utility.*;

/**
 * Created by Raymond on 26/11/2016.
 */
public class Trading {

    public static List<TradeData> getTradeList(String code) {
        return getTradeList(code, "D");
    }
    /**
     * 获取个股历史交易记录
     * @param code  股票代码
     * @param ktype 数据类型，D=日k线 W=周 M=月 5=5分钟 15=15分钟 30=30分钟 60=60分钟，默认为D
     * @return
     */
    public static List<TradeData> getTradeList(String code, String ktype) {
        String symbolCode = _codeToSymbol(code);
        if(_isBlank(symbolCode)) throw new RuntimeException("code is invalid.");
        String url ;
        if(Arrays.asList(K_LABELS).contains(ktype.toUpperCase())) {
           url = String.format(DAY_PRICE_URL, P_TYPE.get("http"), DOMAINS.get("ifeng"), K_TYPE.get(ktype.toUpperCase()), symbolCode);
        }else if(Arrays.asList(K_MIN_LABELS).contains(ktype.toUpperCase())) {
            url = String.format(DAY_PRICE_MIN_URL, P_TYPE.get("http"), DOMAINS.get("ifeng"), symbolCode, ktype);
        } else {
            throw new RuntimeException("ktype: " + ktype + " is invalid.");
        }
        List<TradeData> list = new ArrayList<TradeData>();

        String respContent = HTTParty.get(url);
        if(respContent != null) {
            JSONArray recordList = JSON.parseObject(respContent).getJSONArray("record");
            for (int i=0; i< recordList.size(); i++){
                JSONArray itemList = recordList.getJSONArray(i);
                TradeData item = new TradeData();
                item.date = (itemList.getDate(0));
                item.open = (itemList.getDouble(1));
                item.high = (itemList.getDouble(2));
                item.close = (itemList.getDouble(3));
                item.low = (itemList.getDouble(4));
                item.volume = (itemList.getDouble(5));
                item.price_change = (itemList.getDouble(6));
                item.p_change = (itemList.getDouble(7));
                item.ma5 = (itemList.getDouble(8));
                item.ma10 = (itemList.getDouble(9));
                item.ma20 = (itemList.getDouble(10));
                item.v_ma5 = (itemList.getDouble(11));
                item.v_ma10 = (itemList.getDouble(12));
                item.v_ma20 = (itemList.getDouble(13));
                if(itemList.size() > 14) {
                    item.turnover = (itemList.getDouble(14));
                }

                list.add(item);
            }
            //将列表倒叙排列
            list = list.stream().sorted(comparing(TradeData::getDate).reversed()).collect(Collectors.toList());

        }

        return  list;

    }
    public static List<TradeData> getTradeList(String code, String ktype, Date startDate, Date endDate) {
        List<TradeData> list = getTradeList(code, ktype);
        if(list.size() > 0) {
            list = list.stream().filter(s -> s.getDate().compareTo(startDate) >= 0 && s.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        }

        return list;
    }

    /**
     * 获取分笔数据
     * @param code 股票编码
     * @param date 日期
     * @return 返回 tick data list
     */
    public static List<TickData> getTickData(String code, Date date) {
        if(_isBlank(code) || code.trim().length() != 6 || date == null) return null;
        String symbolCode = _codeToSymbol(code);
        if(_isBlank(symbolCode)) throw new RuntimeException("code is invalid.");
        String url = String.format(TICK_PRICE_URL, P_TYPE.get("http"), DOMAINS.get("sf"), PAGES.get("dl"), QueryDateFormat.format(date), symbolCode);
        String respContent = HTTParty.get(url, "GBK");
        if(respContent != null) {
            List<TickData> list = new ArrayList<>();

            try {
                CSVParser parser = CSVParser.parse(respContent, CSVFormat.DEFAULT.withDelimiter('\t').withFirstRecordAsHeader());
                for (CSVRecord csvRecord : parser) {
                    TickData data = new TickData();
                    String timeStr = csvRecord.get("成交时间");
                    data.time = (LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("H:m:s")));
                    data.price = (csvRecord.get("成交价").equals("--") ? null : Double.parseDouble(csvRecord.get("成交价")));
                    data.change = (csvRecord.get("价格变动").equals("--") ? null : Double.parseDouble(csvRecord.get("价格变动")));
                    data.volume = (csvRecord.get("成交量(手)").equals("--") ? null : Double.parseDouble(csvRecord.get("成交量(手)")));
                    data.amount = (csvRecord.get("成交额(元)").equals("--") ? null : Double.parseDouble(csvRecord.get("成交额(元)")));
                    data.type = (csvRecord.get("性质"));
                    list.add(data);

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return list;

        } else {
            return null;
        }

    }
    public static QuoteData getRealtimeQuotes(String code) {
        String symbolCode = _codeToSymbol(code);
        if(_isBlank(symbolCode)) throw new RuntimeException("code is invalid.");

        int randNumber = Utility.generateRandom(1000_0000, 10_0000_0000);
        String url = String.format(LIVE_DATA_URL, P_TYPE.get("http"), DOMAINS.get("sinahq"), randNumber, symbolCode);
        String respContent = HTTParty.get(url, "GBK");
        String[] arr = respContent.split(",");
        QuoteData data = new QuoteData();
        data.name = ((arr[0].split("=")[1]).replace("\"",""));
        data.open = Double.parseDouble(arr[1]);
        data.pre_close = Double.parseDouble(arr[2]);
        data.price = Double.parseDouble(arr[3]);
        data.high = Double.parseDouble(arr[4]);
        data.low = Double.parseDouble(arr[5]);
        data.bid = Double.parseDouble(arr[6]);
        data.ask = Double.parseDouble(arr[7]);
        data.volume = Double.parseDouble(arr[8]);
        data.amount = Double.parseDouble(arr[9]);
        data.b1_v = Double.parseDouble(arr[10]);
        data.b1_p = Double.parseDouble(arr[11]);
        data.b2_v = Double.parseDouble(arr[12]);
        data.b2_p = Double.parseDouble(arr[13]);
        data.b3_v = Double.parseDouble(arr[14]);
        data.b3_p = Double.parseDouble(arr[15]);
        data.b4_v = Double.parseDouble(arr[16]);
        data.b4_p = Double.parseDouble(arr[17]);
        data.b5_v = Double.parseDouble(arr[18]);
        data.b5_p = Double.parseDouble(arr[19]);
        data.a1_v = Double.parseDouble(arr[20]);
        data.a1_p = Double.parseDouble(arr[21]);
        data.a2_v = Double.parseDouble(arr[22]);
        data.a2_p = Double.parseDouble(arr[23]);
        data.a3_v = Double.parseDouble(arr[24]);
        data.a3_p = Double.parseDouble(arr[25]);
        data.a4_v = Double.parseDouble(arr[26]);
        data.a4_p = Double.parseDouble(arr[27]);
        data.a5_v = Double.parseDouble(arr[28]);
        data.a5_p = Double.parseDouble(arr[29]);
        data.date = LocalDate.parse(arr[30]);

        data.time = LocalTime.parse(arr[31]);
        data.code = code;

        return data;
    }

}
