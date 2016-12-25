package waditu.tushare.stock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import waditu.tushare.common.HTTParty;
import waditu.tushare.entity.StockData;
import waditu.tushare.entity.TickData;

import static waditu.tushare.common.Utility.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import static java.util.Comparator.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Raymond on 26/11/2016.
 */
public class Trading {



    public static List<StockData> getTradeList(String code) {
        return getTradeList(code, "D");
    }
    /**
     * 获取个股历史交易记录
     * @param code  股票代码
     * @param ktype 数据类型，D=日k线 W=周 M=月 5=5分钟 15=15分钟 30=30分钟 60=60分钟，默认为D
     * @return
     */
    public static List<StockData> getTradeList(String code, String ktype) {
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
        List<StockData> list = new ArrayList<StockData>();

        String respContent = HTTParty.get(url);
        if(respContent != null) {
            JSONArray recordList = JSON.parseObject(respContent).getJSONArray("record");
            for (int i=0; i< recordList.size(); i++){
                //System.out.println(recordList.getJSONArray(i));
                JSONArray itemList = recordList.getJSONArray(i);
                StockData item = new StockData();
                item.setDate(itemList.getDate(0));
                item.setOpen(itemList.getDouble(1));
                item.setHigh(itemList.getDouble(2));
                item.setClose(itemList.getDouble(3));
                item.setLow(itemList.getDouble(4));
                item.setVolume(itemList.getDouble(5));
                item.setPrice_change(itemList.getDouble(6));
                item.setP_change(itemList.getDouble(7));
                item.setMa5(itemList.getDouble(8));
                item.setMa10(itemList.getDouble(9));
                item.setMa20(itemList.getDouble(10));
                item.setV_ma5(itemList.getDouble(11));
                item.setV_ma10(itemList.getDouble(12));
                item.setV_ma20(itemList.getDouble(13));
                if(itemList.size() > 14) {
                    item.setTurnover(itemList.getDouble(14));
                }

                list.add(item);
            }
            //将列表倒叙排列
            list = list.stream().sorted(comparing(StockData::getDate).reversed()).collect(Collectors.toList());

        }

        return  list;

    }
    public static List<StockData> getTradeList(String code, String ktype, Date startDate, Date endDate) {
        List<StockData> list = getTradeList(code, ktype);
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
                    data.setTime(LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("H:m:s")));
                    data.setPrice(csvRecord.get("成交价").equals("--") ? null : Double.parseDouble(csvRecord.get("成交价")));
                    data.setChange(csvRecord.get("价格变动").equals("--") ? null : Double.parseDouble(csvRecord.get("价格变动")));
                    data.setVolume(csvRecord.get("成交量(手)").equals("--") ? null : Double.parseDouble(csvRecord.get("成交量(手)")));
                    data.setAmount(csvRecord.get("成交额(元)").equals("--") ? null : Double.parseDouble(csvRecord.get("成交额(元)")));
                    data.setType(csvRecord.get("性质"));
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
}
