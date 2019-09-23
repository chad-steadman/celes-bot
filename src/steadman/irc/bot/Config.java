package steadman.irc.bot;

import java.io.*;
import java.util.Properties;

/**
 * Created by Chad Steadman on 5/10/2015.
 */
public class Config {
    private Properties prop;
    private String filename;


    public Config(String filename) {
        this.prop = new Properties();
        this.filename = filename.toLowerCase();
    }


    protected String getKeyValue(String key) {
        return prop.getProperty(key.toLowerCase());
    }


    protected String addKeyValue(String key, String value) {
        Object obj = prop.setProperty(key.toLowerCase(), value);

        if(obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }


    protected String removeKeyValue(String key) {
        Object obj = prop.remove(key.toLowerCase());

        if(obj != null) {
            return obj.toString();
        } else {
            return "";
        }
    }


    protected String getFilename() {
        return filename;
    }


    protected String setFilename(String filename) {
        String old_filename = this.filename;
        this.filename = filename.toLowerCase();
        return old_filename;
    }


    protected int saveToFile() {
        try {
            OutputStream os = new FileOutputStream(filename);
            prop.store(os, null);
            os.close();

            return 0; // return 0 - no error occurred

        } catch(IOException e) {
            return 1; // return 1 - IOException occurred
        }
    }


    protected int loadFromFile() {
        try {
            InputStream is = new FileInputStream(filename);
            prop.load(is);
            is.close();

            return 0; // return 0 - no error occurred

        } catch(FileNotFoundException e) {
            return 1; // return 1 - FileNotFoundException occurred

        } catch(IOException e) {
            return 2; // return 2 - IOException occurred
        }
    }
}
