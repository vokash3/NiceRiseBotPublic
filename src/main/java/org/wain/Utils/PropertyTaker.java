package org.wain.Utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyTaker { //Обеспечивает получение свойств из properties файла
    static Properties property = new Properties();

    public static String getCustomProperty(String propertyName){
        try(InputStream is = PropertyTaker.class.getResourceAsStream("/data.properties");) {
            property.load(is);
            return property.getProperty(propertyName);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
