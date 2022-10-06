package org.wain.Binance;

public class Calculator {

    public static String calculateChangesPercents(String value, String previousValue) {
        double val = Double.parseDouble(value);
        double preVal = Double.parseDouble(previousValue);
        Double percentsChangingResult = ((val - preVal) / preVal) * 100;
        return String.valueOf(percentsChangingResult);
    }
}