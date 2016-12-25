package waditu.tushare.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Raymond on 25/12/2016.
 */
public class HTTParty {
    public static String get(String url, String charEncode) {
        if(charEncode == null || charEncode.trim().length() == 0) {
            charEncode = "UTF-8";
        }
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        String responseContent = null;
        try {
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == 200) {
                responseContent = EntityUtils.toString(response.getEntity(), charEncode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent;
    }
    public static String get(String url) {
        return get(url, null);
    }
}
