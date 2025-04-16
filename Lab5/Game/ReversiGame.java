package Game;

import java.util.ArrayList;
import java.util.List;

public class ReversiGame {
    public static void main(String[] args) {
        Player blackPlayer = new Player("Tom", Piece.BLACK);
        Player whitePlayer = new Player("Amy", Piece.WHITE);

        List<Board> boards = new ArrayList<>();

        // 初始化和平模式棋盘
        Board peaceBoard = new Board(false, false);
        peaceBoard.setCurrentPlayer(blackPlayer);
        boards.add(peaceBoard);

        // 初始化黑白棋模式棋盘
        Board reversiBoard = new Board(true, false);
        reversiBoard.setCurrentPlayer(blackPlayer);
        boards.add(reversiBoard);

        // 初始化五子棋模式棋盘
        Board gomokuBoard = new Board(false, true);
        gomokuBoard.setCurrentPlayer(blackPlayer);
        boards.add(gomokuBoard);

        int[] roundCounts = new int[boards.size()];
        for (int i = 0; i < roundCounts.length; i++) {
            roundCounts[i] = 1;
        }

        GameInputValidator inputValidator = new GameInputValidator();
        GameCommandHandler commandHandler = new GameCommandHandler(boards, blackPlayer, whitePlayer, inputValidator, roundCounts);
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
                            boards,
                            commandHandler.getRoundCounts()
                    );
                }

                String input = inputReader.getUserInput(
                        commandHandler.getCurrentPlayer(),
                        commandHandler.getCurrentBoardIndex(),
                        boards.size(),
                        boards // 添加这行，传入 List<Board> 类型的参数
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
                e.printStackTrace();
            }
        }

        inputReader.close();
        System.out.println("\nGame over! Thanks for playing!");
    }
}