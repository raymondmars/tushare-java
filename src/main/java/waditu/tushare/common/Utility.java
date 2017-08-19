package waditu.tushare.common;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Raymond on 26/11/2016.
 */
public class Utility {

    private Utility() {}

    public static final SimpleDateFormat QueryDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public final static String[] K_LABELS = {"D", "W", "M"};
    public final static String[] K_MIN_LABELS = {"5", "15", "30", "60"};
    public final static HashMap<String,String> K_TYPE = new HashMap<String, String>() {
        {
            put("D", "akdaily");
            put("W", "akweekly");
            put("M", "akmonthly");
        }
    };
//    public final static String[] INDEX_LABELS = {"sh", "sz", "hs300", "sz50", "cyb", "zxb", "zx300", "zh500"};
    public final static HashMap<String,String> INDEX_LIST = new HashMap<String, String>() {
        {
            put("sh", "sh000001");
            put("sz", "sz399001");
            put("hs300", "sz399300");
            put("sz50", "sh000016");
            put("zxb", "sz399005");
            put("cyb", "sz399006");
            put("zx300", "sz399008");
            put("zh500", "sh000905");
        }
    };
    public final static HashMap<String,String> P_TYPE = new HashMap<String, String>() {
        {
            put("http", "http://");
            put("ftp", "ftp://");
        }
    };
    public final static int[] PAGE_NUM = {38, 60, 80,100};

    public final static HashMap<String, String> DOMAINS = new HashMap<String, String>(){
        {
            put("sina", "sina.com.cn");
            put("sinahq", "sinajs.cn");
            put("ifeng", "ifeng.com");
            put("sf", "finance.sina.com.cn");
            put("vsf", "vip.stock.finance.sina.com.cn");
            put("idx", "www.csindex.com.cn");
            put("163", "money.163.com");
            put("em", "eastmoney.com");
            put("sseq", "query.sse.com.cn");
            put("sse", "www.sse.com.cn");
            put("szse", "www.szse.cn");
            put("oss", "218.244.146.57");
            put("idxip", "115.29.204.48");
            put("shibor", "www.shibor.org");
            put("mbox", "www.cbooo.cn");
        }
    };
    public final static HashMap<String, String> PAGES = new HashMap<String, String>(){
        {
            put("fd", "index.phtml");
            put("dl", "downxls.php");
            put("jv", "json_v2.php");
            put("cpt", "newFLJK.php");
            put("ids", "newSinaHy.php");
            put("lnews", "rollnews_ch_out_interface.php");
            put("ntinfo", "vCB_BulletinGather.php");
            put("hs300b", "000300cons.xls");
            put("hs300w", "000300closeweight.xls");
            put("sz50b", "000016cons.xls");
            put("dp", "all_fpya.php");
            put("163dp", "fpyg.html");
            put("emxsg", "JS.aspx");
            put("163fh", "jjcgph.php");
            put("newstock", "vRPD_NewStockIssue.php");
            put("zz500b", "000905cons.xls");
            put("zz500wt", "000905closeweight.xls");
            put("t_ticks", "vMS_tradedetail.php");
            put("dw", "downLoad.html");
            put("qmd", "queryMargin.do");
            put("szsefc", "ShowReport.szse");
            put("ssecq", "commonQuery.do");
            put("sinadd", "cn_bill_download.php");
            put("ids_sw", "SwHy.php");
        }
    };

    public static final String DAY_PRICE_URL = "%sapi.finance.%s/%s/?code=%s&type=last";
    public static final String DAY_PRICE_MIN_URL = "%sapi.finance.%s/akmin?scode=%s&type=%s";
    public static final String TICK_PRICE_URL = "%smarket.%s/%s?date=%s&symbol=%s";
    public static final String LIVE_DATA_URL = "%shq.%s/rn=%s&list=%s";


    public static boolean _isBlank(String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 对股指代码进行处理，转换，以便更好的获取数据
     * @param code 股指代码
     * @return 格式处理后的股指代码
     */
    public static String _codeToSymbol(String code) {
        if(_isBlank(code)) return "";

        if(INDEX_LIST.containsKey(code)) {
            return INDEX_LIST.get(code);
        }
        if(code.trim().length() != 6) return "";

        char firstChar = code.charAt(0);
        return (firstChar == '5' || firstChar == '6' || firstChar == '9') ? String.format("sh%s", code.trim()) : String.format("sz%s", code.trim());
    }

    public static int generateRandom(int min, int max) {
        Random random = new Random();

        return random.nextInt(max) + min;
    }






















}
