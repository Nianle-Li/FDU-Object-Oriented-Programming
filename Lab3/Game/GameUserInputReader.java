package Game;

import java.util.Scanner;

// 游戏用户输入读取类，负责从用户处读取输入
public class GameUserInputReader {
    private final Scanner scanner;
    private boolean needPrompt = true;

    public GameUserInputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String getUserInput(Player currentPlayer, int currentBoardIndex) {
        if (needPrompt) {
            System.out.println("Current Board: " + (currentBoardIndex + 1));
            System.out.println(currentPlayer.getName() + ", please enter the coordinates (eg. 1a) or a board number (1-3):");
        }
        String input = scanner.next();
        needPrompt = true;
        return input;
    }

    public void setNeedPrompt(boolean needPrompt) {
        this.needPrompt = needPrompt;
    }

    public void close() {
        scanner.close();
    }
}