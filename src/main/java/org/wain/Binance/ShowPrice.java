package org.wain.Binance;

import com.binance.api.client.BinanceApiRestClient;
import org.wain.Main.App;
import org.wain.Models.fromDB.Crypto;
import org.wain.Services.ServiceDAO;
import org.wain.Utils.CommonSimpleDateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/***
 * Для отображения разницы с часовым закрытием
 */
public class ShowPrice {

    private BinanceApiRestClient client;
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM");
    private ServiceDAO service = App.serviceDAO;

    public ShowPrice() {
        client = BinanceClient.client;
        format.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
    }

    public String getAllStats(long userId) {
        List<Crypto> coins = service.userCryptoList(userId);
        if (coins.size() == 0)
            return "У вас нет отслеживаемых пар";
        StringBuffer sb = new StringBuffer();
        coins.forEach(s -> {
            sb.append(CurrencyStatsCalculator.makeCurrencyStats(s.getCryptoName(), service.getUserPeriodName(userId))).append("\n");
        });
        sb.append("<i>По состоянию на ").append(CommonSimpleDateFormat.format.format(new Date())).append("</i>").append("\n");
        return sb.toString();
    }

}
