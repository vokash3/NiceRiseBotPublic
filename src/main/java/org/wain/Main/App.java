package org.wain.Main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.wain.Bot.Bot;
import org.wain.Services.ServiceDAO;
import org.wain.Services.ServiceDAOImpl;
import org.wain.Utils.Helper;

import java.io.IOException;
import java.net.URL;

public class App {
    private static Bot bot;
    public static ServiceDAO serviceDAO = new ServiceDAOImpl(); // FIXME
    private final static Logger LOG = LogManager.getLogger(App.class);

    public static void main(String[] args) throws TelegramApiException {
        LOG.trace("Запуск...");
        URL location = App.class.getProtectionDomain().getCodeSource().getLocation();
        LOG.info(location.getFile());
        try {
            Helper.makeFiles();
        } catch (IOException e) {
            LOG.error(e);
        }
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        bot = new Bot();
        botsApi.registerBot(bot);
    }

    public static Bot getBot() {
        return bot;
    }
}
