package steadman.irc.bot;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Dice.java (Written by Aelanna)
 */
public class Dice {
    private Random random;

    public Dice() {
        random = new Random();
    }

    public String parse(String text) {
        return parse(text, false);
    }

    public String parse(String text, boolean explode) {
        List<String> rolls = new LinkedList<String>();
        StringBuilder response = new StringBuilder(text.toLowerCase());
        response.append('=');
        response.append(parseExpression(text, rolls, explode));
        if (rolls.size() > 0) {
            response.append(" (");
            for (int i = 0; i < rolls.size(); i++) {
                if (i > 0) {
                    response.append(',');
                }
                response.append(rolls.get(i));
            }
            response.append(")");
        }
        return response.toString();
    }

    private int parseExpression(String text, List<String> rolls, boolean explode) {
        if (text.equals("%")) return 100;

        int i, val, total;
        int num, sides;
        boolean fudge = false;
        String[] tokens;
        tokens = text.split("\\*");
        if (tokens.length > 1) {
            val = parseExpression(tokens[0], rolls, explode) * parseExpression(tokens[1], rolls, explode);
            for (i = 2; i < tokens.length; i++) {
                val *= parseExpression(tokens[i], rolls, explode);
            }
            return val;
        }
        tokens = text.split("/");
        if (tokens.length > 1) {
            val = parseExpression(tokens[0], rolls, explode) / parseExpression(tokens[1], rolls, explode);
            for (i = 2; i < tokens.length; i++) {
                val /= parseExpression(tokens[i], rolls, explode);
            }
            return val;
        }
        tokens = text.split("\\+");
        if (tokens.length > 1) {
            val = parseExpression(tokens[0], rolls, explode) + parseExpression(tokens[1], rolls, explode);
            for (i = 2; i < tokens.length; i++) {
                val += parseExpression(tokens[i], rolls, explode);
            }
            return val;
        }
        tokens = text.split("-");
        if (tokens.length > 1) {
            val = parseExpression(tokens[0], rolls, explode) - parseExpression(tokens[1], rolls, explode);
            for (i = 2; i < tokens.length; i++) {
                val -= parseExpression(tokens[i], rolls, explode);
            }
            return val;
        }

        if (text.charAt(text.length() - 1) == 'e') {
            explode = true;
            text = text.substring(0, text.length() - 1);
        }

        tokens = text.split("[dD]");
        if (tokens.length == 2) {
            if (tokens[0] == null || tokens[0].equals("")) {
                num = 1;
            } else {
                num = parseExpression(tokens[0], rolls, explode);
            }

            if (num > 100) throw new DiceException(DiceException.EXCESSIVE_NUMBER);
            else if (num < 1) throw new DiceException(DiceException.INSUFFICIENT_NUMBER);

            if (tokens[1] == null || tokens[1].equals("")) {
                sides = 6;
            } else if (tokens[1].equalsIgnoreCase("f")) {
                sides = 6;
                fudge = true;
            } else {
                sides = parseExpression(tokens[1], rolls, explode);
            }

            if (sides > 1000000) throw new DiceException(DiceException.EXCESSIVE_SIDES);
            else if (sides < 2) throw new DiceException(DiceException.INSUFFICIENT_SIDES);
            total = 0;
            for (i = 0; i < num; i++) {
                if (fudge) {
                    val = random.nextInt(3) - 1;
                    rolls.add(val > 0 ? "+" : (val < 0 ? "-" : "0"));
                    total += val;
                } else {
                    val = random.nextInt(sides) + 1;
                    rolls.add(Integer.toString(val));
                    total += val;
                    if (explode && val == sides) i--;
                }
            }
            return total;
        } else if (tokens.length > 2) {
            throw new RuntimeException("Error: Malformed dice notation");
        }

        return Integer.parseInt(text);
    }
}