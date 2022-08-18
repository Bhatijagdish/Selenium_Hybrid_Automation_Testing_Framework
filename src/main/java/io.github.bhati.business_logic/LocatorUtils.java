package io.github.bhati.business_logic;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class LocatorUtils {

    public static String getPageObject(String pageName, String Key){
        /**
        This method will extract the locator from the properties file.
        Properties' file which has dedicated to each page and their properties.
         */
        String object = null;
        try {
            String propertyFileName = "./src/main/resources/repository/" + pageName + ".properties";
            Properties prop = new Properties();
            InputStream input = new FileInputStream(propertyFileName);
            prop.load(input);
            object = prop.getProperty(Key);
            }catch (Exception e){
            e.printStackTrace();
        }
        return object;
    }
}
