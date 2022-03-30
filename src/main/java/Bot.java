import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private final Downloader downloader = new Downloader();
    private final static List<String> youTubeDomains = getYouTubeDomains();
    private final static String NAME_OF_BOT = "";
    private final static String TOKEN = "";


    private static List<String> getYouTubeDomains() {
        List<String> youTubeDomains = new ArrayList<>(3);
        youTubeDomains.add("https://youtu.be/");
        youTubeDomains.add("https://www.youtube.com/watch?");
        youTubeDomains.add("https://music.youtube.com/watch?");
        return youTubeDomains;
    }

    public static void main(String[] args) {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String url = getURLFromMsg(update);
        if (url == null) {
            sendText(update, "link is not correct:(");
            return;
        }
        sendText(update, "working");
        String nameOfFile = downloader.downloadFile(url);
        if (nameOfFile == null) {
            sendText(update, "smth wrong. i'm so sorry");
            return;
        }
        sendText(update, "uploading...");
        File file = new File(nameOfFile);
        sendAudio(update, file);
        file.deleteOnExit();
        sendText(update, "this is your");
    }

    private static String getURLFromMsg(Update update) {
        String url = null;
        Message msg = update.getMessage();
        if (msg == null) {
            return null;
        }
        String message_text = msg.getText();
        if (message_text == null) {
            return null;
        }
        for (String domain : youTubeDomains) {
            if (message_text.startsWith(domain)) {
                url = message_text.substring(domain.length());
                break;
            }
        }
        return url;
    }

    private void sendAudio(Update update, File file) {
        SendAudio sendMessage = new SendAudio();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        InputFile i_f = new InputFile();
        i_f.setMedia(file);
        sendMessage.setAudio(i_f);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendText(Update update, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return NAME_OF_BOT;
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}