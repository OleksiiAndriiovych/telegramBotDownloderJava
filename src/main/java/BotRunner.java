import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class BotRunner extends TelegramLongPollingBot {

    private final static String NAME_OF_BOT = "";
    private final static String TOKEN = "";

    private final Handler handler;

    public BotRunner() {
        this.handler = new Handler(new Sender(this));
    }

    public static void main(String[] args) {
        new BotRunner().run();
    }

    @Override
    public void onUpdateReceived(Update update) {
        handler.tryOnUpdateReceived(update);
    }

    private void run() {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(this);
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