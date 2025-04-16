package Game;

import java.util.Scanner;
import java.util.List;

public class GameUserInputReader {
    private final Scanner scanner;

    public GameUserInputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String getUserInput(Player currentPlayer, int currentBoardIndex, int totalBoards, List<Board> boards) {
        Board currentBoard = boards.get(currentBoardIndex);
        boolean isGomokuEnded = currentBoard.isGomokuMode() && currentBoard.isBoardGameEnded();

        if (isGomokuEnded) {
            System.out.printf("\n %s's turn. Enter game number (1-%d) / new mode (peace/reversi/gomoku) / end the game (quit) : ",
                    currentPlayer.getName(),
                    totalBoards);
        } else {
            System.out.printf("\n %s's turn. Enter move (1a) / game number (1-%d) / new mode (peace/reversi/gomoku) / abandon a move [only can be used in reversi mode] (pass) / end the game (quit) : ",
                    currentPlayer.getName(),
                    totalBoards);
        }

        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}    