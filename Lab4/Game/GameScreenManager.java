package Game;

import java.io.IOException;
import java.util.List;

public class GameScreenManager {
    public void displayBoard(Board board, Player player1, Player player2, Player currentPlayer, int currentBoardIndex, List<Board> boards) {
        if (board == null) {
            System.out.println("Board is not available!");
            return;
        }

        // 打印列标
        System.out.print("  A B C D E F G H" + "     Game " + (currentBoardIndex + 1));

        // 计算 Board List 的宽度
        int boardListWidth = 15;
        int padding = 10;
        int totalWidth = 2 * 8 + 2 + padding + boardListWidth;

        // 打印分隔空格和 Game List 标题
        for (int i = 0; i < totalWidth - 27; i++) {
            System.out.print(" ");
        }
        System.out.println(" Game List");

        for (int row = 0; row < Board.SIZE; row++) {
            System.out.print((row + 1) + " ");
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (board.isReversiMode() &&
                        piece == Piece.EMPTY &&
                        currentPlayer != null &&
                        board.isValidMove(row, col, currentPlayer.getPiece())) {
                    System.out.print("+ ");
                } else {
                    System.out.print(piece.getSymbol() + " ");
                }
            }

            if (row == 0) {
                // 显示玩家名字、当前落子玩家的棋子图标和得分（仅在 Reversi 模式下）
                if (board.isReversiMode()) {
                    int player1Score = board.getPieceCount(player1.getPiece());
                    String player1Display = "Player1 " + player1.getName() + (player1 == currentPlayer? " " + player1.getPiece().getSymbol() : "  ") + " " + player1Score+ " ";
                    System.out.printf("    %s", player1Display);
                } else {
                    String player1Display = "Player1 " + player1.getName() + (player1 == currentPlayer? " " + player1.getPiece().getSymbol() + "   ": "     ");
                    System.out.printf("    %s", player1Display);
                }
            }
            if (row == 1) {
                // 显示玩家名字、当前落子玩家的棋子图标和得分（仅在 Reversi 模式下）
                if (board.isReversiMode()) {
                    int player2Score = board.getPieceCount(player2.getPiece());
                    String player2Display = "Player2 " + player2.getName() + (player2 == currentPlayer? " " + player2.getPiece().getSymbol() : "  ") + " " + player2Score+ " ";
                    System.out.printf("    %s", player2Display);
                } else {
                    String player2Display = "Player2 " + player2.getName() + (player2 == currentPlayer? " " + player2.getPiece().getSymbol()+ "   " : "     ");
                    System.out.printf("    %s", player2Display);
                }
            }

            // 打印分隔空格
            if (row == 0 || row == 1) {
                for (int i = 0; i < padding - 8; i++) {
                    System.out.print(" ");
                }
            } else {
                for (int i = 0; i < padding + 12; i++) {
                    System.out.print(" ");
                }
            }

            // 在特定行打印 Board List
            if (row < boards.size()) {
                String mode = boards.get(row).isReversiMode()? "reversi" : "peace";
                System.out.printf("%" + (totalWidth - 38) + "s%d. %s\n", "", row + 1, mode);
            } else {
                System.out.println();
            }
        }

        if (board.isReversiMode() && currentPlayer != null) {
            System.out.println("\n'+' indicates valid moves for " + currentPlayer.getName());
        }

        if (board.isBoardGameEnded()) {
            System.out.println("\n" + board.getGameEndMessage());
        }

        // 检查是否为 Reversi 模式且当前玩家没有合法落子位置，再显示提示语
        if (board.isReversiMode() &&!board.hasValidMove(currentPlayer)) {
            System.out.println("The current player has no valid moves.");
        }
    }

    public void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("\n\n\n\n\n\n\n\n"); // 简单的换行作为后备
        }
    }
}