package Game;

import java.util.Scanner;

public class GameUserInputReader {
    private final Scanner scanner;

    public GameUserInputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String getUserInput(Player currentPlayer, int currentBoardIndex, int totalBoards) {
        System.out.printf("\n %s's turn. Enter move (1a) / game number (1-%d) / new mode (peace/reversi) / abandon a move [only can be used in reversi mode] (pass) / end the game (quit) : ",
                currentPlayer.getName(),
                totalBoards);
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}    