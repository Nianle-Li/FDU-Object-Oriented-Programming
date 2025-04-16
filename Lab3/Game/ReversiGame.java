package Game;

import java.util.ArrayList;
import java.util.List;

// 游戏主类，程序入口
public class ReversiGame {
    public static void main(String[] args) {
        Player player1 = new Player("Player 1", Piece.BLACK);
        Player player2 = new Player("Player 2", Piece.WHITE);

        List<Board> boards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            boards.add(new Board());
        }

        GameInputValidator inputValidator = new GameInputValidator();
        GameCommandHandler commandHandler = new GameCommandHandler(boards, player1, player2, inputValidator);
        GameScreenManager screenManager = new GameScreenManager();
        GameUserInputReader inputReader = new GameUserInputReader();

        boolean shouldPrintBoard = true;
        while (!commandHandler.areAllBoardsFull()) {
            if (shouldPrintBoard) {
                screenManager.displayBoard(commandHandler.getCurrentBoard(), player1, player2, commandHandler.getCurrentPlayer().getPiece());
            }
            String input = inputReader.getUserInput(commandHandler.getCurrentPlayer(), commandHandler.getCurrentBoardIndex());
            boolean isValidInput = commandHandler.handleCommand(input);
            if (isValidInput) {
                screenManager.clearScreen();
                shouldPrintBoard = true;
            } else {
                shouldPrintBoard = false;
                inputReader.setNeedPrompt(false);
            }
        }

        inputReader.close();
        System.out.println("Game over! All boards are full.");
    }
}