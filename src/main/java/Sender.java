import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

public class Sender {

    private final AbsSender instance;

    public Sender(AbsSender instance) {
        this.instance = instance;
    }

    public void sendAudio(Update update, File file) {
        SendAudio sendMessage = new SendAudio();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        InputFile i_f = new InputFile();
        i_f.setMedia(file);
        sendMessage.setAudio(i_f);
        try {
            instance.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendText(Update update, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChatId().toString());
        sendMessage.setText(text);
        try {
            instance.execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
