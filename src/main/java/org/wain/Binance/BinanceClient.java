package org.wain.Binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import org.wain.Utils.PropertyTaker;

public class BinanceClient {

    public static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance(PropertyTaker.getCustomProperty("api.key"), PropertyTaker.getCustomProperty("secret.key"));
    public static BinanceApiRestClient client = factory.newRestClient();

}
