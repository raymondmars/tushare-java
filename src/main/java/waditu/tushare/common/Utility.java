package waditu.tushare.common;

import java.util.HashMap;

/**
 * Created by Raymond on 26/11/2016.
 */
public class Utility {
    private Utility() {}

    public final static String[] K_LABELS = {"D", "W", "M"};
    public final static String[] K_MIN_LABELS = {"5", "15", "30", "60"};
    public final static HashMap<String,String> K_TYPE = new HashMap<String, String>() {
        {
            put("D", "akdaily");
            put("W", "akweekly");
            put("M", "akmonthly");
        }
    };
    public final static String[] INDEX_LABELS = {"sh", "sz", "hs300", "sz50", "cyb", "zxb", "zx300", "zh500"};
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


















}
