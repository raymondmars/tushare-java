package waditu.tushare.stock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import waditu.tushare.entity.StockData;
import static waditu.tushare.common.Utility.*;

import java.io.IOException;
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
     *
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

        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        try {
            HttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() == 200) {
                String jsonContent = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONArray recordList = JSON.parseObject(jsonContent).getJSONArray("record");
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
            }
            //将列表倒叙排列
            list = list.stream().sorted(comparing(StockData::getDate).reversed()).collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
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

}
