import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Handler {

    private final URLValidator urlValidator = new URLValidator();
    private final Downloader downloader = new Downloader();
    private final Sender sender;

    public Handler(Sender sender) {
        this.sender = sender;
    }

    public void tryOnUpdateReceived(Update update) {
        try {
            onUpdateReceived(update);
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendText(update, "smth wrong. i'm so sorry");
        }
    }

    public void onUpdateReceived(Update update) {
        String url;
        try {
            url = urlValidator.getURLFromMsg(update);
        } catch (NullPointerException e) {
            sender.sendText(update, "link is not correct:(");
            return;
        }
        sender.sendText(update, "working");
        String nameOfFile;
        try {
            nameOfFile = downloader.downloadFile(url);
        } catch (IOException e) {
            e.printStackTrace();
            sender.sendText(update, "smth wrong. i'm so sorry");
            return;
        }
        sender.sendText(update, "uploading...");
        File file = new File(nameOfFile);
        sender.sendAudio(update, file);
        file.deleteOnExit();
        sender.sendText(update, "this is your");
    }
}
