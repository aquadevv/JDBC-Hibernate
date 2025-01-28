package jm.task.core.jdbc.util;

import jm.task.core.jdbc.Main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public final class PropertiesUtil {
    private static final Logger logger = Logger.getLogger(PropertiesUtil.class.getName());
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "application.properties";

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            logger.severe("Failed to load properties file. Reason: " + e.getMessage());
        }
    }
}
