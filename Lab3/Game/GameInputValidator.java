package Game;

// 输入验证器类，负责验证用户输入的合法性
public class GameInputValidator {
    private static final int BOARD_COUNT = 3;

    public boolean isValidBoardNumber(String input) {
        try {
            int boardNumber = Integer.parseInt(input);
            return boardNumber >= 1 && boardNumber <= BOARD_COUNT;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean isValidMove(String input) {
        return input.length() == 2 && input.charAt(0) >= '1' && input.charAt(0) <= '8'
                && (Character.toLowerCase(input.charAt(1)) >= 'a' && Character.toLowerCase(input.charAt(1)) <= 'h');
    }
}