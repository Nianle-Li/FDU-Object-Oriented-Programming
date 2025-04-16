// Board.java
package Game;

// 棋盘类
public class Board {
    private static final int SIZE = 8;
    private final Piece[][] board;

    public Board() {
        board = new Piece[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Piece.EMPTY;
            }
        }
        board[3][3] = Piece.WHITE;
        board[4][4] = Piece.WHITE;
        board[3][4] = Piece.BLACK;
        board[4][3] = Piece.BLACK;
    }

    public boolean isPositionValid(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE && board[x][y] == Piece.EMPTY;
    }

    public void placePiece(int x, int y, Piece piece) {
        board[x][y] = piece;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == Piece.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard(Player player1, Player player2, Piece currentPlayerPiece) {
        // 打印列标
        System.out.print("  ");
        for (char c = 'A'; c <= 'H'; c++) {
            System.out.print(c + " ");
        }
        System.out.println();

        for (int i = 0; i < SIZE; i++) {
            // 打印行标
            System.out.print((i + 1) + " ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j].getSymbol() + " ");
            }
            if (i == 2) {
                System.out.print("  " + player1.getName() + " " + (player1.getPiece() == currentPlayerPiece ? player1.getPiece().getSymbol() : ""));
            } else if (i == 3) {
                System.out.print("  " + player2.getName() + " " + (player2.getPiece() == currentPlayerPiece ? player2.getPiece().getSymbol() : ""));
            }
            System.out.println();
        }
    }
}
