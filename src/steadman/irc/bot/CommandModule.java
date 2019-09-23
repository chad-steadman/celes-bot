package steadman.irc.bot;

import org.jibble.pircbot.Colors;

import java.io.File;

/**
 * Created by Chad Steadman on 5/11/2015.
 */
public class CommandModule {
    // CONSTANTS
    private final String COMMAND_CONFIG_PATH = "";
    private final String COMMAND_CONFIG_FILENAME = COMMAND_CONFIG_PATH + "command.properties";

    // OBJECTS
    private CommandConfig cfg;

    private String cmd_prefix;              // command prefix - e.g. . , ! ~ etc.
    private String part_message;            // message to be displayed when the bot parts a channel
    private String quit_message;            // message to be displayed when the bot quits the server
    private boolean skip_prefix_for_dice;   // whether or not the dice command requires the command prefix

    private String cmd_quit;         // quit command
    private String cmd_say;          // say command
    private String cmd_me;           // me command
    private String cmd_part;         // part command
    private String cmd_forget;       // forget command
    private String cmd_memo;         // memo command
    private String cmd_showmemos;    // showmemos command
    private String cmd_quote;        // quote command
    private String cmd_remember;     // remember command
    private String cmd_recall;       // recall command
    private String cmd_info;         // info command
    private String cmd_help;         // help command
    private String cmd_dice;         // dice command
    private String cmd_ping;         // ping command

    private String help_quit;        // quit command help message
    private String help_say;         // say command help message
    private String help_me;          // me command help message
    private String help_part;        // part command help message
    private String help_forget;      // forget command help message
    private String help_memo;        // memo command help message
    private String help_showmemos;   // showmemos command help message
    private String help_quote;       // quote command help message
    private String help_remember;    // remember command help message
    private String help_recall;      // recall command help message
    private String help_info;        // info command help message
    private String help_help;        // help command help message
    private String help_dice;        // dice command help message
    private String help_ping;        // ping command help message

    // MODULES
    private RememberModule remember_module;
    private QuoteModule quote_module;
    private MemoModule memo_module;


    // CONSTRUCTOR
    public CommandModule() {
        // ENSURE PATH TO CONFIGURATION FILE EXISTS
        if(!COMMAND_CONFIG_PATH.equalsIgnoreCase("")) {
            File dir = new File(COMMAND_CONFIG_PATH);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        // LOADING COMMAND CONFIGURATION SETTINGS
        cfg = new CommandConfig(COMMAND_CONFIG_FILENAME);

        // ASSIGN COMMANDS
        cmd_prefix = cfg.getCmdPrefix();
        part_message = cfg.getPartMessage();
        quit_message = cfg.getQuitMessage();
        skip_prefix_for_dice = (cfg.getSkipPrefixForDice().equalsIgnoreCase("true") ||
                cfg.getSkipPrefixForDice().equalsIgnoreCase("1"));
        cmd_quit = cfg.getCmdQuit();
        cmd_say = cfg.getCmdSay();
        cmd_me = cfg.getCmdMe();
        cmd_part = cfg.getCmdPart();
        cmd_forget = cfg.getCmdForget();
        cmd_memo = cfg.getCmdMemo();
        cmd_showmemos = cfg.getCmdShowmemos();
        cmd_quote = cfg.getCmdQuote();
        cmd_remember = cfg.getCmdRemember();
        cmd_recall = cfg.getCmdRecall();
        cmd_info = cfg.getCmdInfo();
        cmd_help = cfg.getCmdHelp();
        cmd_dice = cfg.getCmdDice();
        cmd_ping = cfg.getCmdPing();

        // ASSIGN HELP MESSAGES
        help_quit = Colors.BOLD + cmd_quit + Colors.BOLD +
                ": Quits and disconnects from the server. (Admin-only)";

        help_say = Colors.BOLD + cmd_say + " <#channel/nick> <message>" + Colors.BOLD +
                ": Relays a custom message to the specified channel or nick. (Admin-only)";

        help_me = Colors.BOLD + cmd_me + " <#channel/nick> <action>" + Colors.BOLD +
                ": Relays a custom action message to the specified channel or nick. (Admin-only)";

        help_part = Colors.BOLD + cmd_part + " [#channel]" + Colors.BOLD +
                ": Parts the specified channel. If sent from within a private message, the channel argument " +
                "must be provided. (Operator-only)";

        help_forget = Colors.BOLD + cmd_forget + " <keyword>" + Colors.BOLD +
                ": Deletes the stored message (if any) associated with a supplied key word. (Operator-only)";

        help_memo = Colors.BOLD + cmd_memo + " <nick> <message>" + Colors.BOLD +
                ": Stores a message to send to another user. Use " + Colors.BOLD + cmd_showmemos + Colors.BOLD +
                " to retrieve unread messages sent to you by other users.";

        help_showmemos = Colors.BOLD + cmd_showmemos + Colors.BOLD +
                ": Retrieves all unread messages sent to you by other users.";

        help_quote = Colors.BOLD + cmd_quote + " <nick> [n | add <message> | remove <n>]" + Colors.BOLD +
                ": Retrieves the Nth or a random quote (if only 'nick' is supplied) for the specified nick. 'Add' saves " +
                "a new quote. 'Remove' deletes the Nth quote. (Remove is Operator-only)";

        help_remember = Colors.BOLD + cmd_remember + " <keyword> <message>" + Colors.BOLD +
                ": Stores a custom message associated with the supplied key word. Use " + Colors.BOLD + cmd_recall +
                Colors.BOLD + " to display the stored message.";

        help_recall = Colors.BOLD + cmd_recall + " <keyword>" + Colors.BOLD +
                ": Retrieves the stored message (if any) associated with a supplied key word.";

        help_info = Colors.BOLD + cmd_info + Colors.BOLD +
                ": Displays detailed information about the bot.";

        help_help = Colors.BOLD + cmd_help + " [command]" + Colors.BOLD +
                ": Displays all available commands. If the command argument is supplied, detailed information about " +
                "that command will be shown.";

        help_dice = Colors.BOLD + cmd_dice + " <expression> [message]" + Colors.BOLD +
                ": Rolls dice based on the supplied dice expression. (e.g. d20 rolls one 20-sided die, 3d6+5 rolls " +
                "three 6-sided dice and adds 5 to the total, 4df rolls four Fudge dice, 2d6e rolls 2 exploding 6-sided " +
                "dice, etc.) You can supply an optional message to accompany your roll.";

        help_ping = Colors.BOLD + cmd_ping +
                ": Pong!";

        // INITIALIZING MODULES
        remember_module = new RememberModule();
        quote_module = new QuoteModule();
        memo_module = new MemoModule();
    }


    public void quit(CelesBot bot, String[] admins, String login, String hostname) {
        boolean permitted = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        // user has permission - execute command
        if(permitted) {
            bot.changeAutoReconnect(false);
            bot.quitServer(quit_message);
        }
    }


    public void say(CelesBot bot, String[] admins, String sender, String login,
                    String hostname, String message) {
        boolean permitted = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        // user has permission - execute command
        if(permitted) {
            String[] expression = message.split(" +");
            StringBuilder custom_message = new StringBuilder();

            // command expression is in the form of ".say target thing otherthing morethings"
            if(expression.length > 2) {
                // building message to be sent
                for(int i = 2; i < expression.length; ++i) {
                    custom_message.append(expression[i]);

                    if(i != expression.length - 1) {
                        custom_message.append(" ");
                    }
                }

                // sending message
                bot.sendMessage(expression[1], custom_message.toString());

                // command expression is either missing a target or a message
            } else {
                bot.sendNotice(sender, help_say);
            }
        }
    }


    public void me(CelesBot bot, String[] admins, String sender, String login,
                   String hostname, String message) {
        boolean permitted = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        // user has permission - execute command
        if(permitted) {
            String[] expression = message.split(" +");
            StringBuilder custom_message = new StringBuilder();

            // command expression is in the form of ".say target thing otherthing morethings"
            if(expression.length > 2) {
                // building message to be sent
                for(int i = 2; i < expression.length; ++i) {
                    custom_message.append(expression[i]);

                    if(i != expression.length - 1) {
                        custom_message.append(" ");
                    }
                }

                // sending message
                bot.sendAction(expression[1], custom_message.toString());

                // command expression is either missing a target or a message
            } else {
                bot.sendNotice(sender, help_me);
            }
        }
    }


    public void part(CelesBot bot, String[] admins, String[] operators, String sender, String target,
                     String login, String hostname, String message) {
        boolean permitted = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        if(!permitted && operators != null) {
            for(String user : operators) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        // user has permission - execute command
        if(permitted) {
            String[] expression = message.split(" +");

            // command expression is in the form ".leave #arg"
            if(expression.length > 1) {
                bot.partChannel(expression[1], part_message);

            // command expression is in the form ".leave"
            } else {
                // command came from a channel
                if(target.startsWith("#")) {
                    bot.partChannel(target, part_message);

                // command came from a private message - you must specify a channel to part
                } else {
                    // send help message for command part
                    bot.sendNotice(sender, help_part);
                }
            }
        }
    }


    public void forget(CelesBot bot, String[] admins, String[] operators, String sender,
                       String login, String hostname, String target, String message) {
        boolean permitted = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        if(!permitted && operators != null) {
            for(String user : operators) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        // user has permission - execute command
        if(permitted) {
            String[] expression = message.split(" +");

            // command expression is in the form ".forget thing"
            if(expression.length > 1) {
                String result = remember_module.forget(expression[1]);
                bot.sendMessage(target, sender + ": " + result);

            // command expression is in the form ".forget"
            } else {
                bot.sendNotice(sender, help_forget);
            }
        }
    }


    public void memo(CelesBot bot, String sender, String target, String message) {
        String[] expression = message.split(" +");
        StringBuilder memo = new StringBuilder();

        // command expression is in the form of ".memo recipient thing otherthing morethings"
        if(expression.length > 2) {
            // building message to be sent
            for(int i = 2; i < expression.length; ++i) {
                memo.append(expression[i]);

                if(i != expression.length - 1) {
                    memo.append(" ");
                }
            }

            // saving memo
            String result = memo_module.send(sender, expression[1], memo.toString());
            bot.sendMessage(target, sender + ": " + result);

        // command expression is either missing a target or a message
        } else {
            bot.sendNotice(sender, help_memo);
        }
    }


    public void showmemos(CelesBot bot, String sender, boolean only_check_for_one) {
        if(!only_check_for_one) {
            String[] results = memo_module.receiveAll(sender);

            if (results != null) {
                for (String memo : results) {
                    bot.sendMessage(sender, memo);
                }
            } else {
                bot.sendNotice(sender, "You have no pending memos.");
            }

        } else {
            String result = memo_module.checkForMemos(sender);
            if(!result.equalsIgnoreCase("")) {
                bot.sendMessage(sender, result);
            }
        }
    }


    public void quote(CelesBot bot, String[] admins, String[] operators, String sender, String login,
                             String hostname, String target, String message) {
        boolean permitted = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        if(!permitted && operators != null) {
            for(String user : operators) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    permitted = true;
                    break;
                }
            }
        }

        // parse command options
        String[] expression = message.split(" +");

        // command expression is in the form ".quote nick n/add/remove etc etc"
        if(expression.length > 2) {
            // third argument is valid
            if(expression[1].equalsIgnoreCase("add") || expression[2].equalsIgnoreCase("add") ||
                    expression[2].equalsIgnoreCase("remove") ||
                    expression[2].matches("(-?\\d+)")) {
                // add quote
                if((expression[1].equalsIgnoreCase("add") || expression[2].equalsIgnoreCase("add")) && expression.length > 3) {
                    // building message
                    StringBuilder quote = new StringBuilder();
                    for(int i = 3; i < expression.length; ++i) {
                        quote.append(expression[i]);

                        if(i != expression.length - 1) {
                            quote.append(" ");
                        }
                    }

                    if(expression[1].equalsIgnoreCase("add")) {
                        String result = quote_module.add(expression[2], quote.toString());
                        bot.sendMessage(target, sender + ": " + result);
                    }
                    else {
                        String result = quote_module.add(expression[1], quote.toString());
                        bot.sendMessage(target, sender + ": " + result);
                    }

                // remove quote
                } else if(permitted && expression[2].equalsIgnoreCase("remove") && expression.length > 3) {
                    if(expression[3].matches("(-?\\d+)")) {
                        String result = quote_module.remove(expression[1], Integer.parseInt(expression[3]));
                        bot.sendMessage(target, sender + ": " + result);
                    } else {
                        bot.sendNotice(sender, help_quote);
                    }

                // retrieve Nth quote
                } else if(expression[2].matches("(-?\\d+)")) {
                    String result = quote_module.get(expression[1], Integer.parseInt(expression[2]));
                    bot.sendMessage(target, sender + ": " + result);

                // wrong number of parameters supplied
                } else {
                    bot.sendNotice(sender, help_quote);
                }

            // third argument is not valid (i.e. not a number, "add", or "remove"
            } else {
                bot.sendNotice(sender, help_quote);
            }

        // command expression is in the form ".quote nick"
        } else if(expression.length < 3 && expression.length > 1) {
            String result = quote_module.get(expression[1]);
            bot.sendMessage(target, sender + ": " + result);

        // command expression has an illegal amount of arguments
        } else {
            bot.sendNotice(sender, help_quote);
        }
    }


    public void remember(CelesBot bot, String sender, String target, String message) {
        String[] expression = message.split(" +");

        // command expression is in the form ".rem key morestuff lotsofstuff"
        if(expression.length > 2) {
            StringBuilder text = new StringBuilder();

            // building remember text
            for(int i = 2; i < expression.length; ++i) {
                text.append(expression[i]);

                if(i != expression.length - 1) {
                    text.append(" ");
                }
            }

            // remembering
            String result = remember_module.remember(expression[1], text.toString());
            bot.sendMessage(target, sender + ": " + result);

        // command expression is in the form ".rem"
        } else {
            bot.sendNotice(sender, help_remember);
        }
    }


    public void recall(CelesBot bot, String sender, String target, String message) {
        String[] expression = message.split(" +");

        // command expression is in the form ".rec key"
        if(expression.length > 1) {
            String result = remember_module.recall(expression[1]);

            if(!result.equalsIgnoreCase("")) {
                bot.sendMessage(target, result);
            } else {
                bot.sendMessage(target, "I don't remember that!");
            }

        // command expression is in the form ".rec"
        } else {
            bot.sendNotice(sender, help_recall);
        }
    }


    public void info(CelesBot bot, String sender, String target) {
        bot.sendNotice(sender, "I am an instance of " + Colors.BOLD + "CelesBot v" + Launcher.getVersionNumber() + Colors.BOLD +
                ", created by Chad Steadman in Java using the PircBot 1.5 library ( http://www.jibble.org/pircbot.php ). " +
                "Type " + Colors.BOLD + cmd_prefix + cmd_help + Colors.BOLD + " for a list of available commands.");
    }


    public void help(CelesBot bot, String[] admins, String[] operators, String sender, String login,
                     String hostname, String message) {
        boolean hasAdmin = false;
        boolean hasOperator = false;

        // checking if user has permission to use this command
        if(admins != null) {
            for(String user : admins) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    hasAdmin = true;
                    hasOperator = true;
                    break;
                }
            }
        }

        if(!hasAdmin && operators != null) {
            for(String user : operators) {
                if((login + "@" + hostname).equalsIgnoreCase(user)) {
                    hasOperator = true;
                    break;
                }
            }
        }

        String[] expression = message.split(" +");

        // command expression is in the form ".help cmd"
        if(expression.length > 1) {
            // display help for cmd if it matches an existing command
            if(expression[1].equalsIgnoreCase(cmd_quit) && hasAdmin) {
                bot.sendNotice(sender, help_quit);
            } else if(expression[1].equalsIgnoreCase(cmd_say) && hasAdmin) {
                bot.sendNotice(sender, help_say);
            } else if(expression[1].equalsIgnoreCase(cmd_me) && hasAdmin) {
                bot.sendNotice(sender, help_me);
            } else if(expression[1].equalsIgnoreCase(cmd_part) && hasOperator) {
                bot.sendNotice(sender, help_part);
            } else if(expression[1].equalsIgnoreCase(cmd_forget) && hasOperator) {
                bot.sendNotice(sender, help_forget);
            } else if(expression[1].equalsIgnoreCase(cmd_memo)) {
                bot.sendNotice(sender, help_memo);
            } else if(expression[1].equalsIgnoreCase(cmd_showmemos)) {
                bot.sendNotice(sender, help_showmemos);
            } else if(expression[1].equalsIgnoreCase(cmd_quote)) {
                bot.sendNotice(sender, help_quote);
            } else if(expression[1].equalsIgnoreCase(cmd_remember)) {
                bot.sendNotice(sender, help_remember);
            } else if(expression[1].equalsIgnoreCase(cmd_recall)) {
                bot.sendNotice(sender, help_recall);
            } else if(expression[1].equalsIgnoreCase(cmd_info)) {
                bot.sendNotice(sender, help_info);
            } else if(expression[1].equalsIgnoreCase(cmd_help)) {
                bot.sendNotice(sender, help_help);
            } else if(expression[1].equalsIgnoreCase(cmd_dice)) {
                bot.sendNotice(sender, help_dice);
            } else if(expression[1].equalsIgnoreCase(cmd_ping)) {
                bot.sendNotice(sender, help_ping);
            } else {
                bot.sendNotice(sender, "I'm sorry, but I don't recognize that command!");
            }

        // command expression is on the form ".help"
        } else {
            // user has admin rights - show all commands
            if(hasAdmin) {
                bot.sendNotice(sender, "Available commands: " +
                        cmd_quit + ", " +
                        cmd_say + ", " +
                        cmd_me + ", " +
                        cmd_part + ", " +
                        cmd_forget + ", " +
                        cmd_memo + ", " +
                        cmd_showmemos + ", " +
                        cmd_quote + ", " +
                        cmd_remember + ", " +
                        cmd_recall + ", " +
                        cmd_info + ", " +
                        cmd_help + ", " +
                        cmd_dice + ", " +
                        cmd_ping +
                        " (Type " + Colors.BOLD + cmd_prefix + cmd_help + " [command] " + Colors.BOLD + "for more " +
                        "information about a specific command.)");

            // user has operator rights - show operator commands but not admin commands
            } else if(hasOperator) {
                bot.sendNotice(sender, "Available commands: " +
                        cmd_part + ", " +
                        cmd_forget + ", " +
                        cmd_memo + ", " +
                        cmd_showmemos + ", " +
                        cmd_quote + ", " +
                        cmd_remember + ", " +
                        cmd_recall + ", " +
                        cmd_info + ", " +
                        cmd_help + ", " +
                        cmd_dice + ", " +
                        cmd_ping +
                        " (Type " + Colors.BOLD + cmd_prefix + cmd_help + " [command] " + Colors.BOLD + "for more " +
                        "information about a specific command.)");

            // user does not have admin rights - show all non-admin/operator commands
            } else {
                bot.sendNotice(sender, "Available commands: " +
                        cmd_memo + ", " +
                        cmd_showmemos + ", " +
                        cmd_quote + ", " +
                        cmd_remember + ", " +
                        cmd_recall + ", " +
                        cmd_info + ", " +
                        cmd_help + ", " +
                        cmd_dice + ", " +
                        cmd_ping +
                        " (Type " + Colors.BOLD + cmd_prefix + cmd_help + " [command] " + Colors.BOLD + "for more " +
                        "information about a specific command.)");
            }
        }
    }


    public void dice(CelesBot bot, String sender, String target, String message) {
        String[] expression = message.split(" +");
        String result = "";

        // parsing dice expression
        Dice dicebox = new Dice();

        try {
            result = dicebox.parse(expression[1]);

        // dice expression is not valid (illegal characters)
        } catch(NumberFormatException e) {
            bot.sendNotice(sender, "Uh oh. These dice don't look quite right...");
            return;

        // dice expression is not valid (too many/not enough dice/sides)
        } catch(DiceException e) {
            if(e.getReason() == 1) {
                bot.sendNotice(sender, "That doesn't make any sense.");
                return;
            } else if(e.getReason() == 2) {
                bot.sendNotice(sender, "I'm only allowed to roll up to 100 dice at a time... Sorry!");
                return;
            } else if(e.getReason() == 3) {
                bot.sendNotice(sender, "A 1-sided die is... a sphere, I guess? Um. A 0-sided die makes my head hurt.");
                return;
            } else if(e.getReason() == 4) {
                bot.sendNotice(sender, "1,000,000 sides isn't enough for you? Jeez...");
                return;
            }

        // there is no dice expression!
        } catch(IndexOutOfBoundsException e) {
            bot.sendNotice(sender, help_dice);
            return;
        }

        // message is in the form ".roll d20 Yarrr! Avast, ye scurvy dogs!"
        StringBuilder roll_message = new StringBuilder();

        // message contains a custom roll message
        if(expression.length > 2) {
            // building custom roll message
            for(int i = 2; i < expression.length; ++i) {
                roll_message.append(expression[i]);

                if(i != expression.length - 1) {
                    roll_message.append(" ");
                }
            }

            // displaying results
            bot.sendMessage(target, sender + ": " + roll_message + ": " + result);

        // message does not contain a custom roll message
        } else {
            // displaying results
            bot.sendMessage(target, sender + ": " + result);
        }
    }


    public void ping(CelesBot bot, String sender, String target) {
        bot.sendMessage(target, sender + ": Pong!");
    }


    public boolean matchesQuitCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_quit);
    }


    public boolean matchesSayCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_say) || message.startsWith(cmd_prefix + cmd_say + " ");
    }


    public boolean matchesMeCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_me) || message.startsWith(cmd_prefix + cmd_me + " ");
    }


    public boolean matchesPartCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_part) || message.startsWith(cmd_prefix + cmd_part + " ");
    }


    public boolean matchesForgetCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_forget) || message.startsWith(cmd_prefix + cmd_forget + " ");
    }


    public boolean matchesMemoCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_memo) || message.startsWith(cmd_prefix + cmd_memo + " ");
    }


    public boolean matchesShowMemosCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_showmemos);
    }


    public boolean matchesQuoteCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_quote) || message.startsWith(cmd_prefix + cmd_quote + " ");
    }


    public boolean matchesRememberCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_remember) || message.startsWith(cmd_prefix + cmd_remember + " ");
    }


    public boolean matchesRecallCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_recall) || message.startsWith(cmd_prefix + cmd_recall + " ");
    }


    public boolean matchesInfoCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_info);
    }


    public boolean matchesHelpCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_help) || message.startsWith(cmd_prefix + cmd_help + " ");
    }


    public boolean matchesDiceCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_dice) || message.startsWith(cmd_prefix + cmd_dice + " ");
    }


    public boolean matchesDiceExpressionOnly(String message) {
        return skip_prefix_for_dice && message.matches("(?i)(\\d+|)(d)(\\d+|[%f])([\\s\\S]+|)");
    }


    public boolean matchesPingCommand(String message) {
        return message.equalsIgnoreCase(cmd_prefix + cmd_ping);
    }
}
