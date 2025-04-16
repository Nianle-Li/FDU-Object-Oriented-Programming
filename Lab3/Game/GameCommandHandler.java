package Game;

import java.util.List;

// 游戏命令处理类，负责处理用户输入的命令
public class GameCommandHandler {
    private final List<Board> boards;
    private int currentBoardIndex;
    private Player currentPlayer;
    private final Player player1;
    private final Player player2;
    private final GameInputValidator inputValidator;

    public GameCommandHandler(List<Board> boards, Player player1, Player player2, GameInputValidator inputValidator) {
        this.boards = boards;
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
        this.currentBoardIndex = 0;
        this.inputValidator = inputValidator;
    }

    public boolean handleCommand(String input) {
        if (inputValidator.isValidBoardNumber(input)) {
            int newBoardIndex = Integer.parseInt(input) - 1;
            currentBoardIndex = newBoardIndex;
            return true;
        }

        Board currentBoard = boards.get(currentBoardIndex);
        if (currentBoard.isFull()) {
            System.out.println("This board is full. You can't make a move here. Please switch to another board.");
        } else if (inputValidator.isValidMove(input)) {
            int row = input.charAt(0) - '1';
            char colChar = Character.toLowerCase(input.charAt(1));
            int col = colChar - 'a';
            if (currentBoard.isPositionValid(row, col)) {
                currentBoard.placePiece(row, col, currentPlayer.getPiece());
                switchPlayer();
                return true;
            } else {
                System.out.println("A piece is already here. Please try again.");
            }
        } else {
            System.out.println("Invalid input format. Please try again.");
        }
        return false;
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Board getCurrentBoard() {
        return boards.get(currentBoardIndex);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentBoardIndex() {
        return currentBoardIndex;
    }

    public boolean areAllBoardsFull() {
        for (Board board : boards) {
            if (!board.isFull()) {
                return false;
            }
        }
        return true;
    }
}