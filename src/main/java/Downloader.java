import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Downloader {

    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
    private static final String KEY = "var coreUrlPrefix = \"";
    private String host = null;

    public Downloader() {
        setHostOfDownloaderServer();
    }

    //for test
    public static void main(String[] args) {
        Downloader app = new Downloader();
        app.downloadFile("H-1WEJvasvE");
    }

    public String downloadFile(String youtubeURL) {
        String nameOfFile = null;
        JSONObject result = requestToServer(youtubeURL, true);
        if (result != null) {
            nameOfFile = result.getString("title");
            String url = host + result.getString("mp3");
            try {
                saveFile(url, nameOfFile);
            } catch (IOException e1) {
                e1.printStackTrace();
                try {
                    saveFile(url, nameOfFile);
                } catch (IOException e2) {
                    nameOfFile = null;
                }
            }
        }
        return nameOfFile;
    }

    private JSONObject requestToServer(String youtubeURL, boolean isFirstTime) {
        JSONObject result;
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("u", youtubeURL));
        HttpPost post = new HttpPost(host + "/p");
        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            CloseableHttpResponse response = HTTP_CLIENT.execute(post);
            HttpEntity httpEntity = response.getEntity();
            result = new JSONObject(EntityUtils.toString(httpEntity));
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
            if (isFirstTime) {
                setHostOfDownloaderServer();
                result = requestToServer(youtubeURL, false);
            } else {
                result = null;
            }
        }
        return result;
    }

    public static void saveFile(String url, String nameOfFile) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        InputStream is = conn.getInputStream();
        OutputStream outstream = new FileOutputStream(nameOfFile);
        byte[] buffer = new byte[4096];
        int len;
        while ((len = is.read(buffer)) > 0) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();
    }

    private void setHostOfDownloaderServer() {
        host = new HTMLParser("https://ytmp3.cc/en40/").getBetween(KEY, "\"");
    }
}
