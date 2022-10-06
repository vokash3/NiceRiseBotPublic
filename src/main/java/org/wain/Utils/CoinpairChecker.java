package org.wain.Utils;

import com.binance.api.client.BinanceApiRestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wain.Binance.BinanceClient;
import org.wain.Models.fromDB.Crypto;
import org.wain.Models.fromDB.CryptoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CoinpairChecker {

    public static BinanceApiRestClient client = BinanceClient.client;

    private static final Logger LOG = LogManager.getLogger(CoinpairChecker.class);

    public static List<CryptoModel> getCheckedListOfPairs(Set<CryptoModel> coinPairsSet) {
        List<CryptoModel> cryptoModelList = new ArrayList<>();
        coinPairsSet.forEach(crypto -> {
            if (isPairValid(crypto.getCryptoName()))
                cryptoModelList.add(crypto);
        });
        return cryptoModelList;
    }

    public static boolean isPairValid(String pair) {
        try {
            client.getPrice(pair).getPrice();
            return true;
        } catch (Exception e) {
            LOG.error("CoinpairChecker.isPairValid(): {}", e.getMessage());
            return false;
        }

    }

    public static boolean checkUserPair(String pairName, List<Crypto> usersCryptoPairs) {
        boolean result = false;
        for (Crypto crypto1 : usersCryptoPairs) {
            if (pairName.equals(crypto1.getCryptoName())) {
                result = true;
                break;
            }
        }
        return result;
    }

}
