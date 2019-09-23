package steadman.irc.bot;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Created by Chad Steadman on 5/11/2015.
 */
public class QuoteModule {
    private final String QUOTE_CONFIG_PATH = "data/quotes/";


    public QuoteModule() {
        // ENSURE PATH TO CONFIGURATION FILE EXISTS
        if(!QUOTE_CONFIG_PATH.equalsIgnoreCase("")) {
            File dir = new File(QUOTE_CONFIG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }


    public String get(String nickname) {
        QuoteConfig database = new QuoteConfig(QUOTE_CONFIG_PATH + nickname);
        Random rand = new Random();
        int quote_count = Integer.parseInt(database.getKeyValue("quote_count")); // getting quote count

        // there is at least 1 quote
        if(quote_count > 0) {
            int index = rand.nextInt(quote_count) + 1;

            String quote = database.getKeyValue(Integer.toString(index));
            return String.format("[%d/%d] %s", index, quote_count, quote);

        // there are no quotes
        } else {
            return nickname + " hasn't said anything memorable yet!";
        }
    }


    public String get(String nickname, int index) {
        QuoteConfig database = new QuoteConfig(QUOTE_CONFIG_PATH + nickname);
        int quote_count = Integer.parseInt(database.getKeyValue("quote_count")); // getting quote count

        // index is higher than amount of quotes!
        if(index > quote_count) {
            if(quote_count > 0) {
                return "I'm sorry, but I only have " + quote_count + " quote(s) for " + nickname + "!";
            } else {
                return nickname + " hasn't said anything memorable yet!";
            }

        // index is 0 or lower!
        } else if(index < 1) {
            if(index == 0) {
                return "Oh, I get it. You think there's a super secret 0th quote. There isn't one. Believe me; " +
                        "I've looked!";
            } else {
                return "What."; // negative number
            }

        // index is between 1 and the amount of quotes, inclusive
        } else {
            String quote = database.getKeyValue(Integer.toString(index));
            return String.format("[%d/%d] %s", index, quote_count, quote);
        }
    }


    public String add(String nickname, String quote) {
        QuoteConfig database = new QuoteConfig(QUOTE_CONFIG_PATH + nickname);
        int quote_count = Integer.parseInt(database.getKeyValue("quote_count")); // getting quote count
        ++quote_count;

        String[] date_time = ((LocalDateTime.now()).toString()).split("T");
        date_time[1] = date_time[1].substring(0, date_time[1].length() - 4);

        database.addKeyValue(Integer.toString(quote_count), "[" + date_time[0] + " " + date_time[1] + "] <" + nickname + "> "
                + quote);
        database.addKeyValue("quote_count", Integer.toString(quote_count));

        int save_error_code = database.saveToFile();
        if(save_error_code == 0) {
            // NO ERROR
            return "Quote added!";

        } else {
            // ERROR - COULD NOT SAVE QUOTES TO FILE
            return "Unfortunately, I am unable to save quotes at this time... Please try " +
                   "again later.";
        }
    }


    public String remove(String nickname, int index) {
        QuoteConfig database = new QuoteConfig(QUOTE_CONFIG_PATH + nickname);
        int quote_count = Integer.parseInt(database.getKeyValue("quote_count")); // getting quote count

        // index is higher than amount of quotes!
        if(index > quote_count) {
            if(quote_count > 0) {
                return "I'm sorry, but I only have " + quote_count + " quote(s) for " + nickname + "!";
            } else {
                return nickname + " has no quotes to remove!";
            }

        // index is 0 or lower!
        } else if(index < 1) {
            if(index == 0) {
                return "Oh, I get it. You think there's a super secret 0th quote. There isn't one. Believe me; " +
                        "I've looked!";
            } else {
                return "What.";
            }

        // index is between 1 and the amount of quotes, inclusive
        } else {
            for(int i = index; i < quote_count; ++i) {
                database.addKeyValue(Integer.toString(i), database.getKeyValue(Integer.toString(i + 1)));
            }

            database.removeKeyValue(Integer.toString(quote_count));
            --quote_count;
            database.addKeyValue("quote_count", Integer.toString(quote_count));

            int save_error_code = database.saveToFile();
            if(save_error_code == 0) {
                // NO ERROR
                return "Quote deleted!";

            } else {
                // ERROR - COULD NOT SAVE QUOTES TO FILE
                return "Unfortunately, I am unable to delete quotes at this time... Please try " +
                        "again later.";
            }
        }
    }
}
