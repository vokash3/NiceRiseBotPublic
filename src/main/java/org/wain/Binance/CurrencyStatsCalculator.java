package org.wain.Binance;

import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class CurrencyStatsCalculator {

    private static final Logger LOG = LogManager.getLogger(CurrencyStatsCalculator.class);

    private static final String MESSAGE_TEMPLATE = "<b>%-7s:</b>\n<b>%-10.2f %-8s</b>\nОбъём %-8s\n" +
            "_________________________\n";


    public static String makeCurrencyStats(String currency, String period) {

        try {
            List<Candlestick> candlestickList = BinanceClient.client.getCandlestickBars(currency, getUserIntervalInCandlestick(period));
            Candlestick candlestickCurrent = candlestickList.get(candlestickList.size() - 1);
            String price = candlestickCurrent.getClose();
            String volume = candlestickCurrent.getVolume();
            Candlestick candle = candlestickList.get(candlestickList.size() - 2);
            String candlePreviousPrice = candle.getOpen();
            String candlePreviousVolume = candle.getVolume();
            String priceChanging = Calculator.calculateChangesPercents(price, candlePreviousPrice);
            String volumeChanging = Calculator.calculateChangesPercents(volume, candlePreviousVolume);

            if (Double.parseDouble(priceChanging) > 0)
                priceChanging = "(+" + priceChanging.substring(0, 5) + "%" + ")✅";
            else if (Double.parseDouble(priceChanging) < 0)
                priceChanging = "(" + priceChanging.substring(0, 6) + "%" + ")\uD83D\uDD3B";
            else
                priceChanging = "0.0%";

            if (Double.parseDouble(volumeChanging) > 0)
                volumeChanging = "(+" + volumeChanging.substring(0, 5) + "%" + ")✅";
            else if (Double.parseDouble(volumeChanging) < 0)
                volumeChanging = "(" + volumeChanging.substring(0, 6) + "%" + ")\uD83D\uDD3B";
            else
                volumeChanging = "0.0%";

            return String.format(MESSAGE_TEMPLATE, currency, Double.valueOf(price), priceChanging, volumeChanging);
        } catch (Exception e) {
            LOG.error(e);
            return "ERROR!";
        }
    }

    private static CandlestickInterval getUserIntervalInCandlestick(String period) {
        switch (period.toUpperCase()) {
            case "15M":
                return CandlestickInterval.FIFTEEN_MINUTES;
            case "30M":
                return CandlestickInterval.HALF_HOURLY;
            case "1D":
                return CandlestickInterval.DAILY;
            default:
                return CandlestickInterval.HOURLY;
        }
    }
}
