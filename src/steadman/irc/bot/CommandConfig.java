package steadman.irc.bot;

/**
 * Created by Chad Steadman on 5/10/2015.
 */
public class CommandConfig extends Config {
    // MISC
    private final String DEFAULT_CMD_PREFIX = ".";
    private final String DEFAULT_PART_MESSAGE = "Farewell";
    private final String DEFAULT_QUIT_MESSAGE = "Farewell";
    private final String DEFAULT_SKIP_PREFIX_FOR_DICE = "false";

    // ADMIN-ONLY COMMANDS
    private final String DEFAULT_CMD_QUIT = "quit";
    private final String DEFAULT_CMD_SAY = "say";
    private final String DEFAULT_CMD_ME = "me";

    // ADMIN/OPERATOR COMMANDS
    private final String DEFAULT_CMD_PART = "leave";
    private final String DEFAULT_CMD_FORGET = "forget";

    // COMMON COMMANDS
    private final String DEFAULT_CMD_MEMO = "tell";
    private final String DEFAULT_CMD_SHOWMEMOS = "showtells";
    private final String DEFAULT_CMD_QUOTE = "quote";
    private final String DEFAULT_CMD_REMEMBER = "rem";
    private final String DEFAULT_CMD_RECALL = "rec";
    private final String DEFAULT_CMD_INFO = "info";
    private final String DEFAULT_CMD_HELP = "help";
    private final String DEFAULT_CMD_DICE = "roll";
    private final String DEFAULT_CMD_PING = "ping";


    public CommandConfig(String filename) {
        super(filename);
        addKeyValue("command_prefix", DEFAULT_CMD_PREFIX);
        addKeyValue("part_message", DEFAULT_PART_MESSAGE);
        addKeyValue("quit_message", DEFAULT_QUIT_MESSAGE);
        addKeyValue("skip_prefix_for_dice", DEFAULT_SKIP_PREFIX_FOR_DICE);
        addKeyValue("quit", DEFAULT_CMD_QUIT);
        addKeyValue("say", DEFAULT_CMD_SAY);
        addKeyValue("me", DEFAULT_CMD_ME);
        addKeyValue("part", DEFAULT_CMD_PART);
        addKeyValue("forget", DEFAULT_CMD_FORGET);
        addKeyValue("memo", DEFAULT_CMD_MEMO);
        addKeyValue("showmemos", DEFAULT_CMD_SHOWMEMOS);
        addKeyValue("quote", DEFAULT_CMD_QUOTE);
        addKeyValue("remember", DEFAULT_CMD_REMEMBER);
        addKeyValue("recall", DEFAULT_CMD_RECALL);
        addKeyValue("info", DEFAULT_CMD_INFO);
        addKeyValue("help", DEFAULT_CMD_HELP);
        addKeyValue("dice", DEFAULT_CMD_DICE);
        addKeyValue("ping", DEFAULT_CMD_PING);

        // LOAD EXISTING CONFIGURATION FROM FILE
        System.out.printf("> Loading command configuration from %s... ", getFilename());

        int load_error_code = loadFromFile();
        if(load_error_code == 0) {
            // CONFIGURATION LOADED SUCCESSFULLY - Checking critical data
            System.out.println("Done!");

            if(getKeyValue("command_prefix").equalsIgnoreCase("")) {
                addKeyValue("command_prefix", DEFAULT_CMD_PREFIX);
                saveToFile();
            }

            if(getKeyValue("part_message").equalsIgnoreCase("")) {
                addKeyValue("part_message", DEFAULT_PART_MESSAGE);
                saveToFile();
            }

            if(getKeyValue("quit_message").equalsIgnoreCase("")) {
                addKeyValue("quit_message", DEFAULT_QUIT_MESSAGE);
                saveToFile();
            }

            if(getKeyValue("skip_prefix_for_dice").equalsIgnoreCase("")) {
                addKeyValue("skip_prefix_for_dice", DEFAULT_SKIP_PREFIX_FOR_DICE);
                saveToFile();
            }

            if(getKeyValue("quit").equalsIgnoreCase("")) {
                addKeyValue("quit", DEFAULT_CMD_QUIT);
                saveToFile();
            }

            if(getKeyValue("say").equalsIgnoreCase("")) {
                addKeyValue("say", DEFAULT_CMD_SAY);
                saveToFile();
            }

            if(getKeyValue("me").equalsIgnoreCase("")) {
                addKeyValue("me", DEFAULT_CMD_ME);
                saveToFile();
            }

            if(getKeyValue("part").equalsIgnoreCase("")) {
                addKeyValue("part", DEFAULT_CMD_PART);
                saveToFile();
            }

            if(getKeyValue("forget").equalsIgnoreCase("")) {
                addKeyValue("forget", DEFAULT_CMD_FORGET);
                saveToFile();
            }

            if(getKeyValue("memo").equalsIgnoreCase("")) {
                addKeyValue("memo", "");
                saveToFile();
            }

            if(getKeyValue("showmemos").equalsIgnoreCase("")) {
                addKeyValue("showmemos", "");
                saveToFile();
            }

            if(getKeyValue("quote").equalsIgnoreCase("")) {
                addKeyValue("quote", "");
                saveToFile();
            }

            if(getKeyValue("remember").equalsIgnoreCase("")) {
                addKeyValue("remember", "");
                saveToFile();
            }

            if(getKeyValue("recall").equalsIgnoreCase("")) {
                addKeyValue("recall", "");
                saveToFile();
            }

            if(getKeyValue("info").equalsIgnoreCase("")) {
                addKeyValue("info", DEFAULT_CMD_INFO);
                saveToFile();
            }

            if(getKeyValue("help").equalsIgnoreCase("")) {
                addKeyValue("help", DEFAULT_CMD_HELP);
                saveToFile();
            }

            if(getKeyValue("dice").equalsIgnoreCase("")) {
                addKeyValue("dice", "");
                saveToFile();
            }

            if(getKeyValue("ping").equalsIgnoreCase("")) {
                addKeyValue("ping", "");
                saveToFile();
            }

        } else if(load_error_code == 1) {
            // CONFIGURATION FILE DOESN'T EXIST - Create new default file
            System.out.println("Failed! (File not found)");
            System.out.print("> Creating new command configuration file... ");

            int save_error_code = saveToFile();

            if (save_error_code == 0) {
                System.out.println("Done!");
                System.out.println("You may edit the contents of \"" + getFilename() + "\" to customize your\n" +
                        "commands before attempting to connect to a server.");

            } else if (save_error_code == 1) {
                System.out.println("Failed (I/O error)");
                System.out.println("Could not save \"" + getFilename() + "\" because the process encountered\n" +
                        "an error. Using default commands...");
            }

        } else if (load_error_code == 2) {
            // ERROR OCCURRED
            System.out.println("Failed! (I/O error)");
            System.out.println("Could not open \"" + getFilename() + "\" because the process encountered\n" +
                    "an error. Using default commands...");
        }
    }


    public String getCmdPrefix() {
        return getKeyValue("command_prefix");
    }


    public String getPartMessage() {
        return getKeyValue("part_message");
    }


    public String getQuitMessage() {
        return getKeyValue("quit_message");
    }


    public String getSkipPrefixForDice() {
        return getKeyValue("skip_prefix_for_dice");
    }


    public String getCmdQuit() {
        return getKeyValue("quit");
    }


    public String getCmdSay() {
        return getKeyValue("say");
    }


    public String getCmdMe() {
        return getKeyValue("me");
    }


    public String getCmdPart() {
        return getKeyValue("part");
    }


    public String getCmdForget() {
        return getKeyValue("forget");
    }


    public String getCmdMemo() {
        return getKeyValue("memo");
    }


    public String getCmdShowmemos() {
        return getKeyValue("showmemos");
    }


    public String getCmdQuote() {
        return getKeyValue("quote");
    }


    public String getCmdRemember() {
        return getKeyValue("remember");
    }


    public String getCmdRecall() {
        return getKeyValue("recall");
    }


    public String getCmdInfo() {
        return getKeyValue("info");
    }


    public String getCmdHelp() {
        return getKeyValue("help");
    }


    public String getCmdDice() {
        return getKeyValue("dice");
    }

    public String getCmdPing() {
        return getKeyValue("ping");
    }
}
