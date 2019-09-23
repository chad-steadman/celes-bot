package steadman.irc.bot;

/**
 * Created by Chad Steadman on 5/11/2015.
 */
public class MemoConfig extends Config {
    private String user = "";


    public MemoConfig(String nickname) {
        super(nickname + ".memos");
        user = nickname;
        addKeyValue("memo_count", "0");

        loadFromFile();
/*
        // LOAD EXISTING MEMOS FROM FILE
        System.out.printf("> Loading memos from %s... ", getFilename());

        int load_error_code = loadFromFile();
        if(load_error_code == 0) {
            // MEMOS LOADED SUCCESSFULLY
            System.out.println("Done!");

        } else if(load_error_code == 1) {
            // CONFIGURATION FILE DOESN'T EXIST
            System.out.println("Failed! (File not found)");
            System.out.println("[NOTICE] - The user " + user + " has no memos!");

        } else if (load_error_code == 2) {
            // ERROR OCCURRED
            System.out.println("Failed! (I/O error)\n");
            System.out.println("[ERROR] - Could not open \"" + getFilename() + "\" because the process encountered\n" +
                    "an error.");
        }
*/
    }
}
