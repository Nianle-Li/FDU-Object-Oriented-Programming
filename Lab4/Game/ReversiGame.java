package Game;

import java.util.ArrayList;
import java.util.List;

public class ReversiGame {
    public static void main(String[] args) {
        Player blackPlayer = new Player("Tom", Piece.BLACK);
        Player whitePlayer = new Player("Amy", Piece.WHITE);

        List<Board> boards = new ArrayList<>();

        // 初始化两个棋盘，都设置为黑方先手
        Board peaceBoard = new Board(false);
        peaceBoard.setCurrentPlayer(blackPlayer);
        boards.add(peaceBoard);

        Board reversiBoard = new Board(true);
        reversiBoard.setCurrentPlayer(blackPlayer);
        boards.add(reversiBoard);

        GameInputValidator inputValidator = new GameInputValidator();
        GameCommandHandler commandHandler = new GameCommandHandler(boards, blackPlayer, whitePlayer, inputValidator);
        GameScreenManager screenManager = new GameScreenManager();
        GameUserInputReader inputReader = new GameUserInputReader();

        boolean gameRunning = true;
        boolean needToPrintBoard = true;

        while (gameRunning) {
            try {
                if (needToPrintBoard) {
                    screenManager.displayBoard(
                            commandHandler.getCurrentBoard(),
                            blackPlayer,
                            whitePlayer,
                            commandHandler.getCurrentPlayer(),
                            commandHandler.getCurrentBoardIndex(),
                            boards
                    );
                }

                String input = inputReader.getUserInput(
                        commandHandler.getCurrentPlayer(),
                        commandHandler.getCurrentBoardIndex(),
                        boards.size()
                );

                if ("quit".equalsIgnoreCase(input)) {
                    gameRunning = false;
                } else {
                    boolean success = commandHandler.handleCommand(input);
                    if (success) {
                        screenManager.clearScreen();
                        needToPrintBoard = true;
                    } else {
                        needToPrintBoard = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error occurred: " + e.getMessage());
            }
        }

        inputReader.close();
        System.out.println("\nGame over! Final results:");
        for (int i = 0; i < boards.size(); i++) {
            System.out.printf("Board %d (%s mode) - %s\n",
                    i + 1,
                    boards.get(i).isReversiMode()? "Reversi" : "Peace",
                    getBoardStatus(boards.get(i), blackPlayer, whitePlayer));
        }
    }

    private static String getBoardStatus(Board board, Player blackPlayer, Player whitePlayer) {
        int blackCount = 0, whiteCount = 0;
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (board.getPiece(i, j) == Piece.BLACK) blackCount++;
                else if (board.getPiece(i, j) == Piece.WHITE) whiteCount++;
            }
        }

        if (blackCount > whiteCount) return blackPlayer.getName() + " wins!";
        if (whiteCount > blackCount) return whitePlayer.getName() + " wins!";
        return "Draw!";
    }
}    