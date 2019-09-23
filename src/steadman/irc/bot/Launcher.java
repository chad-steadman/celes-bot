package steadman.irc.bot;

import java.util.Scanner;

/**
 * Created by Chad Steadman on 5/10/2015.
 */
public class Launcher {
    // CONSTANTS
    private static final String VERSION_NUMBER = "2.1";

    // OBJECTS
    private static boolean windowless_mode = false;


    // MAIN
    public static void main(String[] args) {
        // PROCESSING COMMAND LINE ARGUMENTS
        for(String arg : args) {
            // WINDOWLESS - This flag indicates that the bot is supposed to run in windowless mode.
            if(arg.equalsIgnoreCase("windowless") || arg.equalsIgnoreCase("-windowless")) {
                windowless_mode = true;
            }
        }

        // PROGRAM TITLE
        System.out.printf("####### CELES IRC BOT v%s #######\n", VERSION_NUMBER);

        // INITIALIZING BOT
        CelesBot bot = new CelesBot();

        // START CONNECTION TO SERVER
        do {
            if(!bot.isConnected()) {
                bot.startConnection();
            }
        } while(bot.checkAutoReconnect());

        System.exit(0);
    }


    // PAUSE UNTIL THE USER PRESSES ENTER.
    public static void pause() {
        if(!windowless_mode) {
            Scanner in = new Scanner(System.in);
            System.out.print("Press Enter to continue...");
            in.nextLine();
        }
        // else if windowless mode is enabled, do nothing
    }


    // GET VERSION NUMBER
    public static String getVersionNumber() {
        return VERSION_NUMBER;
    }


    // GET WINDOWLESS MODE
    public static boolean getWindowlessMode() {
        return windowless_mode;
    }
}
