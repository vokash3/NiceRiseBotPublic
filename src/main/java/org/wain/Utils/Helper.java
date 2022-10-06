package org.wain.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;

public class Helper {
    public static Path coins;
    public static Path lastMessage;
    public static Path pricesHour;
    public static Path userList;
    public static Path users;

    public static void makeFiles() throws IOException {
        Path dataDirectory = null;
        if(Files.exists(Paths.get("data"))) dataDirectory = Paths.get("data");
        else {
            try {
                dataDirectory = Files.createDirectory(Paths.get("data"));
            } catch (FileAlreadyExistsException e) {
                System.out.println("data директория уже существует!");
                dataDirectory = Paths.get("data");
            }
        }
        System.out.println(dataDirectory.toUri().getPath());
        makeFilesHelper(dataDirectory);
    }

    private static void makeFilesHelper(Path dataDirectory){
        Path coinsPath = Paths.get(dataDirectory.toUri().getPath() + "coins");
        Path lastMessagePath = Paths.get(dataDirectory.toUri().getPath() + "lastMessage");
        Path pricesHourPath = Paths.get(dataDirectory.toUri().getPath() + "pricesHour");
        Path userListPath = Paths.get(dataDirectory.toUri().getPath() + "userList");
        Path usersPath = Paths.get(dataDirectory.toUri().getPath() + "users");
        try {
            if(!Files.exists(coinsPath)){
                coins = Files.createFile(coinsPath);
                makeCoins();
            }else {
                coins = coinsPath;
            }
            if(!Files.exists(pricesHourPath)){
                pricesHour = Files.createFile(pricesHourPath);
                makePricesHour();
            }else {
                pricesHour = pricesHourPath;
            }
            if(!Files.exists(usersPath))
                users = Files.createFile(usersPath);
            else {
                users = usersPath;
            }
            if(!Files.exists(lastMessagePath))
                lastMessage = Files.createFile(lastMessagePath);
            else
                lastMessage = lastMessagePath;

            if(!Files.exists(userListPath))
                userList = Files.createFile(userListPath);
            else
                userList = userListPath;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makeCoins(){
        String listCoins = PropertyTaker.getCustomProperty("coins");

        try(FileWriter writer = new FileWriter(coins.toFile());) {
            writer.write(listCoins);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void makePricesHour(){
        String prices = PropertyTaker.getCustomProperty("prices.hour");
        try(FileWriter writer = new FileWriter(pricesHour.toFile());) {
            assert prices != null;
            writer.write(prices);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
