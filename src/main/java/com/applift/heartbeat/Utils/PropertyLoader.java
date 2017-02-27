package com.applift.heartbeat.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by nishantkumar on 25/01/17.
 */
public class PropertyLoader {
    private String fileName;
    public static Properties prop = null;
    private InputStream input = null;

    public PropertyLoader() {
        this("resources/config/config.properties");
    }

    public PropertyLoader(String file) {
        this.fileName = file;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // load a properties file
        try {
            prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties GetProperty() {
        if (prop == null) {
            new PropertyLoader();
        }
        return prop;
    }
}
