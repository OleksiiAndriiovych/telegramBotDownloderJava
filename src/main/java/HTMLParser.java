import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class HTMLParser {

    private final String URL;
    private String context = null;

    public HTMLParser(String URL) {
        super();
        this.URL = URL;
    }

    private String getContext() {
        try {
            loadContext();
        } catch (IOException e) {
            e.printStackTrace();
            context = "";
        }
        return context;
    }

    private void loadContext() throws IOException {
        java.net.URL website = new URL(URL);
        URLConnection connection = website.openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
        scanner.useDelimiter("\\Z");
        context = scanner.next();
        scanner.close();
    }

    public String getBetween(String pre, String post) {
        String content = getContext();
        content = content.substring(content.indexOf(pre) + pre.length());
        return content.substring(0, content.indexOf(post));
    }
}

