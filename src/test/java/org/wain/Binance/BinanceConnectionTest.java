package org.wain.Binance;

import org.junit.Assert;
import org.junit.Test;

public class BinanceConnectionTest {

    @Test
    public void accountShouldBeAbleToDoDeposit() {
        Assert.assertTrue(BinanceClient.client.getAccount().isCanDeposit());
    }
}
