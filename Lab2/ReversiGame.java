// ReversiGame.java
package Lab;

import java.io.IOException;
import java.util.Scanner;

// 游戏类
public class ReversiGame {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player player1 = new Player("Player 1", Piece.BLACK);
        Player player2 = new Player("Player 2", Piece.WHITE);
        Board board = new Board();
        Player currentPlayer = player1;

        while (!board.isFull()) {
            board.printBoard(player1, player2, currentPlayer.getPiece());
            System.out.println(currentPlayer.getName() + ", please enter the coordinates (eg.1a):");

            boolean validInput = false;
            while (!validInput) {
                String input = scanner.next();
                if (input.length() == 2 && input.charAt(0) >= '1' && input.charAt(0) <= '8'
                        && input.charAt(1) >= 'a' && input.charAt(1) <= 'h') {  
                    int row = input.charAt(0) - '1';
                    char colChar = input.charAt(1);
                    int col = colChar - 'a';

                    if (board.isPositionValid(row, col)) {
                        board.placePiece(row, col, currentPlayer.getPiece());
                        // 清屏操作，仅在合法输入后执行
                        try {
                            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                        } catch (InterruptedException | IOException e) {
                            e.printStackTrace();
                        }
                        currentPlayer = (currentPlayer == player1) ? player2 : player1;
                        validInput = true;
                    } else {
                        System.out.println("A piece is already here. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input format. Please try again.");
                }
            }
        }
        
        board.printBoard(player1, player2, currentPlayer.getPiece());
        System.out.println("Game over! The board is full.");
        scanner.close();
    }
}