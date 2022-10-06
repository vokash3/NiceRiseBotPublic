package org.wain.Models.fromDB;

import lombok.Data;

@Data
public class CryptoModel {

    //    private int cryptoID;
    private String cryptoName;

    public CryptoModel() {
    }

    public CryptoModel(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    @Override
    public String toString() {
        return "cryptoName='" + cryptoName + '\'' + '}';
    }

}
