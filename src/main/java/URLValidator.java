import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class URLValidator {
    private final List<String> youTubeDomains;

    public URLValidator() {
        youTubeDomains = new ArrayList<>(3);
        youTubeDomains.add("https://youtu.be/");
        youTubeDomains.add("https://www.youtube.com/watch?v=");
        youTubeDomains.add("https://music.youtube.com/watch?v=");
    }

    public String getURLFromMsg(Update update) throws NullPointerException {
        Message msg = update.getMessage();
        if (msg == null) {
            throw new NullPointerException();
        }
        String message_text = msg.getText();
        if (message_text == null) {
            throw new NullPointerException();
        }
        for (String domain : youTubeDomains) {
            if (message_text.startsWith(domain)) {
                return message_text.substring(domain.length());
            }
        }
        throw new NullPointerException();
    }
}
