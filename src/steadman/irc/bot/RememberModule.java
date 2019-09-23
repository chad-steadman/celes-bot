package steadman.irc.bot;

import org.jibble.pircbot.Colors;

import java.io.File;

/**
 * Created by Chad Steadman on 5/11/2015.
 */
public class RememberModule {
    private final String REMEMBER_CONFIG_PATH = "data/";
    private final String REMEMBER_CONFIG_FILENAME = REMEMBER_CONFIG_PATH + "remember.database";

    private RememberConfig database;


    public RememberModule() {
        // ENSURE PATH TO CONFIGURATION FILE EXISTS
        if(!REMEMBER_CONFIG_PATH.equalsIgnoreCase("")) {
            File dir = new File(REMEMBER_CONFIG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        // LOADING REMEMBER DATABASE INFORMATION
        database = new RememberConfig(REMEMBER_CONFIG_FILENAME);
    }


    public String recall(String key) {
        String value = database.getKeyValue(key);

        // value exists for the specified key
        if(value != null) {
            return (key + " " + value);
        }

        return "";
    }


    public String remember(String key, String value) {
        // we are remembering a new value
        if(!value.equalsIgnoreCase(database.getKeyValue(key))) {

            String prev_value = database.addKeyValue(key, value);
            int save_error_code = database.saveToFile();

            // previous value is not null - display update message
            if(!prev_value.equalsIgnoreCase("") && save_error_code == 0) {
                return "I'll forget " + Colors.BOLD + prev_value + Colors.BOLD + " and remember " +
                       "this instead!";

            // previous value is null - display new remember message
            } else if(save_error_code == 0) {
                return "I will never forget it!";

            // an error occurred when attempting to save remember database to disk
            } else {
                return "I'll try to remember it for now, but I can't guarantee that I will still " +
                       "remember it later... You might want to ask me again some time just to be safe.";
            }

        // new value is the exact same as the old value! :|
        } else {
            return "I already remember that!";
        }
    }


    public String forget(String key) {
        String value = database.getKeyValue(key);

        // value is not null - forget it
        if(value != null) {
            database.removeKeyValue(key);
            database.saveToFile();

            return "I'll forget " + Colors.BOLD + value + Colors.BOLD + ". Wait, what were we talking about again?";

        // value is already null
        } else {
            return "I'm sorry, but I can't forget something that I don't remember!";
        }
    }
}
