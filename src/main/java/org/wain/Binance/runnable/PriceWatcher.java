package org.wain.Binance.runnable;

import com.binance.api.client.BinanceApiRestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.wain.Binance.BinanceClient;
import org.wain.Binance.CurrencyStatsCalculator;
import org.wain.Main.App;
import org.wain.Models.fromDB.Crypto;
import org.wain.Models.fromDB.User;
import org.wain.Services.ServiceDAO;
import org.wain.Utils.CommonSimpleDateFormat;

import java.util.*;

public class PriceWatcher implements Runnable {

    private final static Logger LOG = LogManager.getLogger(PriceWatcher.class);
    private final ServiceDAO serviceDAO = App.serviceDAO;
    private final int YEAR = 2022;
    private final int MONTH = Calendar.OCTOBER;
    private final int DAY = 5;
    private final int HOUR = 23;
    private final int MINUTE = 0;
    private final int SECOND = 0;
    private final long HOUR_MILLIS = 3_600_000L;
    private final long HALF_HOUR_MILLIS = 1_800_000L;
    private final long QUARTER_HOUR_MILLIS = 900_000L;
    private final long DAY_MILLIS = 24 * 3600 * 1000;

    private final BinanceApiRestClient client = BinanceClient.client;

    public PriceWatcher() {
    }

    @Override
    public void run() {
        Timer timerHourly = new Timer();
        timerHourly.schedule(new HourlyTimer(), new GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).getTime(), HOUR_MILLIS);

        Timer timer30Minutes = new Timer();
        timer30Minutes.schedule(new ThirtyMinutesTimer(), new GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).getTime(), HALF_HOUR_MILLIS);

        Timer timer15Minutes = new Timer();
        timer15Minutes.schedule(new FifteenMinutesTimer(), new GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).getTime(), QUARTER_HOUR_MILLIS);

        Timer dailyTimer = new Timer();
        dailyTimer.schedule(new DailyTimer(), new GregorianCalendar(YEAR, MONTH, DAY, HOUR, MINUTE, SECOND).getTime(), DAY_MILLIS);
    }


    public void sendMessage(List<User> users) {
        SendMessage sm = new SendMessage();
        sm.enableHtml(true);
        for (User user : users) {
            try {
                long userID = user.getUserId();
                // TODO Исключаем уже неподписанных на бот юзеров
                String message = messageCreator(userID);
                if (message.equals("None")) // Не отправлять ничего пользователям без пар (пропуск)
                    continue;
                sm.setText(message);
                sm.setChatId(String.valueOf(userID));
                App.getBot().execute(sm); // FIXME - нехорошо
                serviceDAO.updateLastGotMessage(userID);//Обновляем время последнего сообщения пользователю
                //TODO Refactor to updateUserLastUpdate
            } catch (TelegramApiException e) {
                LOG.error(e);
            }
        }
    }

    private String messageCreator(long userID) {
        List<Crypto> coins = serviceDAO.userCryptoList(userID); //В БД изначально криптопары - UNIQUE
        if (coins.size() == 0)
            return "None";
        StringBuilder sb = new StringBuilder();
        for (Crypto crypto : coins)
            sb.append(CurrencyStatsCalculator.makeCurrencyStats(crypto.getCryptoName().trim(), serviceDAO.getUserPeriodName(userID))).append("\n");
        sb.append("<i>По состоянию на ").append(CommonSimpleDateFormat.format.format(new Date())).append("</i>").append("\n");
        return sb.toString();
    }

    private class HourlyTimer extends TimerTask {
        @Override
        public void run() {
            sendMessage(serviceDAO.getUsersByPeriodId(1));
        }
    }

    private class ThirtyMinutesTimer extends TimerTask {
        @Override
        public void run() {
            sendMessage(serviceDAO.getUsersByPeriodId(2));
        }
    }

    private class FifteenMinutesTimer extends TimerTask {
        @Override
        public void run() {
            sendMessage(serviceDAO.getUsersByPeriodId(3));
        }
    }

    private class DailyTimer extends TimerTask {
        @Override
        public void run() {
            sendMessage(serviceDAO.getUsersByPeriodId(4));
        }
    }

}
