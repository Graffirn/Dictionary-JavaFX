import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Api {
    public static String googleTranslateApi(String text) {
        try {
            String urlStr = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=vi&dt=t&q=";
            String stm = text;
            try {
                urlStr = urlStr + URLEncoder.encode(stm, "UTF-8");
                URL url = new URL(urlStr);
                StringBuilder response = new StringBuilder();
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                ArrayList<String> result = new ArrayList<String>();
                String explain = response.toString();
                while (explain.indexOf('\"') != -1) {
                    String temp;
                    explain = explain.substring(explain.indexOf('"') + 1);
                    int index = explain.indexOf('"');
                    temp = explain.substring(0, index);
                    result.add(temp);
                    explain = explain.substring(index + 1);
                }
                String sentence = "";
                for (int i = 0; i < result.size() - 1; ++i) {
                    if (i % 2 == 0 && !result.get(i).isEmpty()) {
                        sentence = sentence + result.get(i);
                    }
                }
                return sentence;
            } catch (Exception e) {
                return e.getMessage();
            }
        } catch (Exception t) {
            return t.getMessage();
        }
    }
}
