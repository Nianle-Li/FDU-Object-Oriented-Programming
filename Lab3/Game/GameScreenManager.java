package Game;

import java.io.IOException;

// 游戏屏幕管理类，负责处理屏幕显示和清屏操作
public class GameScreenManager {
    public void displayBoard(Board board, Player player1, Player player2, Piece currentPlayerPiece) {
        board.printBoard(player1, player2, currentPlayerPiece);
    }

    public void clearScreen() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}