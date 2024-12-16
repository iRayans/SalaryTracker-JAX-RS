package com.rayan.salarytracker.core.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import java.io.IOException;
import java.net.URL;


/**
 * A utility class used to read the "config.properties" file in src/main/resources directory.
 */

public class ConfigFileUtil {
    
    private static final String CONFIGURATION_FILENAME = "config.properties";

    private ConfigFileUtil() {};

    public static Properties getPropertiesInstance() {
        try {
            URL configURL = Thread.currentThread().getContextClassLoader().getResource(CONFIGURATION_FILENAME);

            if (configURL == null) {
                throw new FileNotFoundException();
            }

            String configPath = configURL.getPath();
            Properties props = new Properties();
            props.load(new FileInputStream(configPath));
            return props;
        } catch (FileNotFoundException e1) {
            System.out.println("Config file not found.");
            return null;
        } catch (IOException e2) {
            System.out.println("IOException: {}"+ e2.getMessage());
            return null;
        }
    }
}
