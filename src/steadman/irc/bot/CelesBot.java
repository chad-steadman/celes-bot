package steadman.irc.bot;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;

import java.io.File;
import java.io.IOException;

/**
 * Created by Chad Steadman on 5/10/2015.
 */
public class CelesBot extends PircBot {
    // CONSTANTS
    private final String BOT_CONFIG_PATH = "";
    private final String BOT_CONFIG_FILENAME = BOT_CONFIG_PATH + "bot.properties";

    // OBJECTS
    private BotConfig cfg;
    private String server_address;
    private int server_port;
    private String server_password;
    private String nickname;
    private String nickserv_password;
    private String username;
    private String realname;
    private String[] admins;
    private String[] operators;
    private String[] autojoin_channels;
    private String[] banned_channels;
    private boolean auto_reconnect;

    // MODULES
    private CommandModule command_module;

    // CONSTRUCTOR
    public CelesBot() {
        // ENSURE PATH TO CONFIGURATION FILE EXISTS
        if(!BOT_CONFIG_PATH.equalsIgnoreCase("")) {
            File dir = new File(BOT_CONFIG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        // LOADING BOT CONFIGURATION SETTINGS
        cfg = new BotConfig(BOT_CONFIG_FILENAME);
        server_address = cfg.getServerAddress();
        server_port = cfg.getServerPort();
        server_password = cfg.getServerPassword();
        nickname = cfg.getNickname();
        nickserv_password = cfg.getNickservPassword();
        username = cfg.getUsername();
        realname = cfg.getRealname();
        admins = cfg.getAdmins();
        operators = cfg.getOperators();
        autojoin_channels = cfg.getAutojoinChannels();
        banned_channels = cfg.getBannedChannels();
        auto_reconnect = cfg.getAutoReconnect().equalsIgnoreCase("true") ||
                cfg.getAutoReconnect().equalsIgnoreCase("1");

        // INITIALIZING MODULES
        command_module = new CommandModule();

        // SET BOT INFORMATION
        setName(nickname);
        setLogin(username);
        setVersion(realname);
        if(Launcher.getWindowlessMode()) {
            setVerbose(false);
        } else {
            setVerbose(true);
        }
    }


    // START CONNECTION TO IRC SERVER
    public void startConnection() {
        System.out.println("> Connecting to " + server_address + ":" + server_port + "... ");
        // CONNECTING TO THE SERVER
        try {
            this.connect(server_address, server_port, server_password);

        // I/O ERROR
        } catch(IOException e) {
            System.out.println("[ERROR] - Failed to connect to " + server_address + ":" + server_port +
                    ". An I/O error occurred...");
            try {
                Thread.sleep(20000);
            } catch(InterruptedException ie) {
                System.out.println("[ERROR] - InterruptedException! (CelesBot.java, onConnect(), line 91)");
            }

        // NICKNAME IS ALREADY IN USE
        } catch(NickAlreadyInUseException e) {
            System.out.println("[ERROR] - Failed to connect to " + server_address + ":" + server_port +
                    ". Nickname already in use!");
            try {
                Thread.sleep(20000);
            } catch(InterruptedException ie) {
                System.out.println("[ERROR] - InterruptedException! (CelesBot.java, onConnect(), line 101)");
            }

        // IRC ERROR
        } catch(IrcException e) {
            System.out.println("[ERROR] - Failed to connect to " + server_address + ":" + server_port +
                    ". An IRC error occurred. Bad server password, perhaps?");
            try {
                Thread.sleep(20000);
            } catch(InterruptedException ie) {
                System.out.println("[ERROR] - InterruptedException! (CelesBot.java, onConnect(), line 111)");
            }
        }
    }


    // THIS FIRES WHEN THE BOT CONNECTS TO THE SERVER
    @Override
    public void onConnect() {
        // IDENTIFY WITH NICKSERV
        if(!nickserv_password.equalsIgnoreCase("")) {
            identify(nickserv_password);

            // ALLOW THE SERVER A COUPLE SECONDS TO RESPOND TO OUR IDENTIFICATION
            try {
                Thread.sleep(2000);
            } catch(InterruptedException e) {
                System.out.println("[ERROR] - InterruptedException! (CelesBot.java, onConnect(), line 128)");
            }
        }

        // AUTOMATICALLY JOIN CHANNELS
        if(autojoin_channels != null) {
            for(String channel : autojoin_channels) {
                if(!channel.equalsIgnoreCase("")) {
                    joinChannel(channel);
                }
            }
        }
    }


    // THIS FIRES WHEN THE BOT DISCONNECTS FROM THE SERVER
    @Override
    public void onDisconnect() {
        // END PROGRAM
        System.out.println("Disconnected... ");
        try {
            Thread.sleep(20000);
        } catch(InterruptedException e) {
            System.out.println("[ERROR] - InterruptedException! (CelesBot.java, onConnect(), line 151)");
        }
    }


    // THIS FIRES WHENEVER THE BOT SEES A MESSAGE IN A CHANNEL
    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        parseCommands(channel, sender, login, hostname, message);
        checkForPendingMemos(sender);
    }


    // THIS FIRES WHENEVER THE BOT RECEIVES A PRIVATE MESSAGE
    @Override
    public void onPrivateMessage(String sender, String login, String hostname, String message) {
        parseCommands(sender, sender, login, hostname, message);
        checkForPendingMemos(sender);
    }


    // THIS FIRES WHENEVER THE BOT RECEIVES AN /INVITE REQUEST TO JOIN A CHANNEL
    @Override
    public void onInvite(String targetNick, String sourceNick, String sourceLogin, String sourceHostname,
                         String channel) {
        boolean permitted = false;
        boolean banned = false;

        if(admins != null) {
            for(String user : admins) {
                if((sourceLogin + "@" + sourceHostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        if(!permitted && operators != null) {
            for(String user : operators) {
                if((sourceLogin + "@" + sourceHostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        if(permitted) {
            joinChannel(channel);
        } else {
            if(banned_channels != null) {
                for(String c : banned_channels) {
                    if(channel.equalsIgnoreCase(c)) {
                        banned = true;
                        break;
                    }
                }
            }
            if(!banned) {
                joinChannel(channel);
            }
        }
    }


    // PARSES MESSAGES TO MATCH COMMANDS
    private void parseCommands(String target, String sender, String login, String hostname, String message) {
        if(command_module.matchesDiceCommand(message)) {
            command_module.dice(this, sender, target, message);
        }

        else if(command_module.matchesDiceExpressionOnly(message)) {
            command_module.dice(this, sender, target, ".roll " + message);
        }

        else if(command_module.matchesPingCommand(message)) {
            command_module.ping(this, sender, target);
        }

        else if(command_module.matchesMemoCommand(message)) {
            command_module.memo(this, sender, target, message);
        }

        else if(command_module.matchesShowMemosCommand(message)) {
            command_module.showmemos(this, sender, false);
        }

        else if(command_module.matchesRememberCommand(message)) {
            command_module.remember(this, sender, target, message);
        }

        else if(command_module.matchesRecallCommand(message)) {
            command_module.recall(this, sender, target, message);
        }

        else if(command_module.matchesQuoteCommand(message)) {
            command_module.quote(this, admins, operators, sender, login, hostname, target, message);
        }

        else if(command_module.matchesHelpCommand(message)) {
            command_module.help(this, admins, operators, sender, login, hostname, message);
        }

        else if(command_module.matchesInfoCommand(message)) {
            command_module.info(this, sender, target);
        }

        else if(command_module.matchesPartCommand(message)) {
            command_module.part(this, admins, operators, sender, target, login, hostname, message);
        }

        else if(command_module.matchesForgetCommand(message)) {
            command_module.forget(this, admins, operators, sender, login, hostname, target, message);
        }

        else if(command_module.matchesSayCommand(message)) {
            command_module.say(this, admins, sender, login, hostname, message);
        }

        else if(command_module.matchesMeCommand(message)) {
            command_module.me(this, admins, sender, login, hostname, message);
        }

        else if(command_module.matchesQuitCommand(message)) {
            command_module.quit(this, admins, login, hostname);
        }
    }


    // CHECKS IF A USER HAS ANY PENDING MEMOS
    private void checkForPendingMemos(String sender) {
        command_module.showmemos(this, sender, true);
    }


    // CHANGES AUTO RECONNECT STATUS
    public void changeAutoReconnect(boolean flag) {
        auto_reconnect = flag;
    }

    // RETURNS AUTO RECONNECT STATUS
    public boolean checkAutoReconnect() {
        return auto_reconnect;
    }
}
