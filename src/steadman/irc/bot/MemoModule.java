package steadman.irc.bot;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Created by Chad Steadman on 5/11/2015.
 */
public class MemoModule {
    private final String MEMO_CONFIG_PATH = "data/memos/";


    public MemoModule() {
        // ENSURE PATH TO CONFIGURATION FILE EXISTS
        if(!MEMO_CONFIG_PATH.equalsIgnoreCase("")) {
            File dir = new File(MEMO_CONFIG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }


    public String checkForMemos(String recipient) {
        MemoConfig database = new MemoConfig(MEMO_CONFIG_PATH + recipient);
        int memo_count = Integer.parseInt(database.getKeyValue("memo_count")); // getting memo count

        // there is at least 1 memo
        if(memo_count > 0) {
            return receiveOne(recipient);
        }

        return "";
    }


    public String send(String sender, String recipient, String memo) {
        MemoConfig database = new MemoConfig(MEMO_CONFIG_PATH + recipient);
        int memo_count = Integer.parseInt(database.getKeyValue("memo_count")); // getting memo count
        ++memo_count;

        String[] date_time = ((LocalDateTime.now()).toString()).split("T");
        date_time[1] = date_time[1].substring(0, date_time[1].length() - 4);

        database.addKeyValue(Integer.toString(memo_count), sender + " said @ " + date_time[0] + " " + date_time[1] + ": " +
                memo);
        database.addKeyValue("memo_count", Integer.toString(memo_count));

        int save_error_code = database.saveToFile();
        if(save_error_code == 0) {
            // NO ERROR
            return "I'll see to it!";

        } else {
            // ERROR - COULD NOT SAVE MEMOS TO FILE
            return "Unfortunately, I am unable to send memos at this time... Please try " +
                    "again later.";
        }
    }


    public String receiveOne(String recipient) {
        MemoConfig database = new MemoConfig(MEMO_CONFIG_PATH + recipient);
        int memo_count = Integer.parseInt(database.getKeyValue("memo_count")); // getting memo count
        String memo;

        // there is at least 1 memo
        if(memo_count > 0) {
            if(memo_count > 1) {
                memo = database.getKeyValue("1") + " (+" + (memo_count - 1) + " more)";

            } else {
                memo = database.getKeyValue("1");
            }

            removeOne(database);
            return memo;
        }

        return "";
    }


    public String[] receiveAll(String recipient) {
        MemoConfig database = new MemoConfig(MEMO_CONFIG_PATH + recipient);
        int memo_count = Integer.parseInt(database.getKeyValue("memo_count")); // getting memo count

        String[] memos = new String[memo_count];

        // there is at least 1 memo
        if(memo_count > 0) {
            for(int i = 0; i < memo_count; ++i) {
                memos[i] = database.getKeyValue("1");
                removeOne(database);
            }

            return memos;
        }

        return null;
    }


    private void removeOne(MemoConfig database) {
        int memo_count = Integer.parseInt(database.getKeyValue("memo_count")); // getting memo count

        // there is at least 1 memo
        if(memo_count > 0) {
            for(int i = 1; i < memo_count; ++i) {
                database.addKeyValue(Integer.toString(i), database.getKeyValue(Integer.toString(i + 1)));
            }

            database.removeKeyValue(Integer.toString(memo_count));
            --memo_count;
            database.addKeyValue("memo_count", Integer.toString(memo_count));

            database.saveToFile(); // if this fails, the memo will still be there
        }
    }
}
