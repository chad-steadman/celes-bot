package steadman.irc.bot;

/**
 * Created by Chad Steadman on 5/10/2015.
 */
public class BotConfig extends Config {
    private final String DEFAULT_USERNAME = "CelesBot";
    private final String DEFAULT_REALNAME = "CelesBot v" + Launcher.getVersionNumber();
    private final String DEFAULT_PORT = "6667";
    private final String DEFAULT_AUTO_RECONNECT = "true";


    public BotConfig(String filename) {
        super(filename);
        addKeyValue("server_address", "");
        addKeyValue("server_port", DEFAULT_PORT);
        addKeyValue("server_password", "");
        addKeyValue("nickname", "");
        addKeyValue("nickserv_password", "");
        addKeyValue("username", DEFAULT_USERNAME);
        addKeyValue("realname", DEFAULT_REALNAME);
        addKeyValue("admins", "");
        addKeyValue("operators", "");
        addKeyValue("autojoin_channels", "");
        addKeyValue("banned_channels", "");
        addKeyValue("auto_reconnect", "");

        // LOAD EXISTING CONFIGURATION FROM FILE
        System.out.printf("> Loading bot configuration from %s... ", getFilename());

        int load_error_code = loadFromFile();
        if(load_error_code == 0) {
            // CONFIGURATION LOADED SUCCESSFULLY - Checking critical data
            System.out.println("Done!");

            if(getKeyValue("server_address").equalsIgnoreCase("")) {
                addKeyValue("server_address", "");
                System.out.println("[ERROR] - A server address was not specified in the configuration file.\n" +
                        "Please provide a server address in the \"" + filename + "\" file.");
                saveToFile();
                Launcher.pause();
                System.exit(0);
            }

            if(getKeyValue("nickname").equalsIgnoreCase("")) {
                addKeyValue("nickname", "");
                System.out.println("[ERROR] - A nickname was not specified in the configuration file.\n" +
                        "Please provide a nickname in the \"" + filename + "\" file.");
                saveToFile();
                Launcher.pause();
                System.exit(0);
            }

            if(getKeyValue("server_port").equalsIgnoreCase("")) {
                addKeyValue("server_port", DEFAULT_PORT);
                System.out.println("[NOTICE] - A server port was not specified in the configuration file.\n" +
                        "Defaulting to port " + DEFAULT_PORT + ".");
                saveToFile();
            }

            if(getKeyValue("username").equalsIgnoreCase("")) {
                addKeyValue("username", DEFAULT_USERNAME);
                System.out.println("[NOTICE] - A username was not specified in the configuration file.\n" +
                        "Defaulting to " + DEFAULT_USERNAME + ".");
                saveToFile();
            }

            if(getKeyValue("realname").equalsIgnoreCase("")) {
                addKeyValue("realname", DEFAULT_REALNAME);
                System.out.println("[NOTICE] - A real name was not specified in the configuration file.\n" +
                        "Defaulting to " + DEFAULT_REALNAME + ".");
                saveToFile();
            }

            if(getKeyValue("server_password").equalsIgnoreCase("")) {
                addKeyValue("server_password", "");
                saveToFile();
            }

            if(getKeyValue("nickserv_password").equalsIgnoreCase("")) {
                addKeyValue("nickserv_password", "");
                saveToFile();
            }

            if(getKeyValue("admins").equalsIgnoreCase("")) {
                addKeyValue("admins", "");
                saveToFile();
            }

            if(getKeyValue("operators").equalsIgnoreCase("")) {
                addKeyValue("operators", "");
                saveToFile();
            }

            if(getKeyValue("autojoin_channels").equalsIgnoreCase("")) {
                addKeyValue("autojoin_channels", "");
                saveToFile();
            }

            if(getKeyValue("banned_channels").equalsIgnoreCase("")) {
                addKeyValue("banned_channels", "");
                saveToFile();
            }

            if(getKeyValue("auto_reconnect").equalsIgnoreCase("")) {
                addKeyValue("auto_reconnect", DEFAULT_AUTO_RECONNECT);
                saveToFile();
            }

        } else if(load_error_code == 1) {
            // CONFIGURATION FILE DOESN'T EXIST - Create new default file
            System.out.println("Failed! (File not found)");
            System.out.print("> Creating new bot configuration file... ");

            int save_error_code = saveToFile();

            if(save_error_code == 0) {
                System.out.println("Done!");
                System.out.println("Please edit the contents of \"" + getFilename() + "\" to configure your\n" +
                        "bot before attempting to connect to a server.");
                Launcher.pause();
                System.exit(0);

            } else if(save_error_code == 1) {
                System.out.println("Failed (I/O error)");
                System.out.println("Could not save \"" + getFilename() + "\" because the process encountered\n" +
                        "an error. The program will now exit...");
                Launcher.pause();
                System.exit(1);
            }

        } else if(load_error_code == 2) {
            // ERROR OCCURRED - Exit program
            System.out.println("Failed! (I/O error)");
            System.out.println("Could not open \"" + getFilename() + "\" because the process encountered\n" +
                    "an error. The program will now exit...");
            Launcher.pause();
            System.exit(1);
        }
    }


    public String getServerAddress() {
        return getKeyValue("server_address");
    }


    public int getServerPort() {
        return Integer.parseInt(getKeyValue("server_port"));
    }


    public String getServerPassword() {
        return getKeyValue("server_password");
    }


    public String getNickname() {
        return getKeyValue("nickname");
    }


    public String getNickservPassword() {
        return getKeyValue("nickserv_password");
    }


    public String getUsername() {
        return getKeyValue("username");
    }


    public String getRealname() {
        return getKeyValue("realname");
    }


    public String[] getAdmins() {
        return getKeyValue("admins").split(",");
    }


    public String[] getOperators() {
        return getKeyValue("operators").split(",");
    }


    public String[] getAutojoinChannels() {
        return getKeyValue("autojoin_channels").split(",");
    }


    public String[] getBannedChannels() {
        return getKeyValue("banned_channels").split(",");
    }

    public String getAutoReconnect() {
        return getKeyValue("auto_reconnect");
    }
}
