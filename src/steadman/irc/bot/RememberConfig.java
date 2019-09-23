package steadman.irc.bot;

/**
 * Created by Chad Steadman on 5/11/2015.
 */
public class RememberConfig extends Config {
    public RememberConfig(String filename) {
        super(filename);

        // LOAD EXISTING CONFIGURATION FROM FILE
        System.out.printf("> Loading remember database from %s... ", getFilename());

        int load_error_code = loadFromFile();
        if(load_error_code == 0) {
            // DATABASE LOADED SUCCESSFULLY
            System.out.println("Done!");

        } else if(load_error_code == 1) {
            // DATABASE FILE DOESN'T EXIST - Create new default file
            System.out.println("Failed! (File not found)");
            System.out.print("> Creating new remember database file... ");

            int save_error_code = saveToFile();

            if(save_error_code == 0) {
                System.out.println("Done!");

            } else if(save_error_code == 1) {
                System.out.println("Failed (I/O error)");
                System.out.println("Could not save \"" + getFilename() + "\" because the process encountered\n" +
                        "an error.");
            }

        } else if(load_error_code == 2) {
            // ERROR OCCURRED - Exit program
            System.out.println("Failed! (I/O error)");
            System.out.println("Could not open \"" + getFilename() + "\" because the process encountered\n" +
                    "an error.");
        }
    }
}
