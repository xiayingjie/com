import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @classDescription:
 * @author:xiayingjie
 * @createTime:15/6/12
 */

public class JsoupTest {
    public static void main(String[] args) throws IOException {

        Map<String, String> map = getPM();
        for (String s : map.keySet()) {
            System.out.println(s + ":" + map.get(s));
        }


    }

    public static Map<String, String> getPM() {
        String url = "http://www.pm25.in/hefei";
        Document doc = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements es = doc.select("div .span1");
        for (Element e : es) {
            String caption = e.select(".caption").text();
            String value = e.select(".value").text();
            if (!"".equals(caption)) {
                map.put(caption, value);
            }
        }
        return map;
    }
}