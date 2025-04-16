package Game;

public class GameInputValidator {
    public boolean isValidBoardNumber(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidMove(String input) {
        if (input == null || input.length() != 2) {
            return false;
        }
        char rowChar = input.charAt(0);
        char colChar = Character.toLowerCase(input.charAt(1));
        return rowChar >= '1' && rowChar <= '8' &&
                colChar >= 'a' && colChar <= 'h';
    }
}    