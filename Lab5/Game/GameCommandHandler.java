package Game;

import java.util.List;

public class GameCommandHandler {
    private final List<Board> boards;
    private int currentBoardIndex;
    private final Player blackPlayer;
    private final Player whitePlayer;
    private final GameInputValidator inputValidator;
    private int[] roundCounts;

    public GameCommandHandler(List<Board> boards, Player blackPlayer, Player whitePlayer,
                              GameInputValidator inputValidator, int[] roundCounts) {
        this.boards = boards;
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.inputValidator = inputValidator;
        this.currentBoardIndex = 0;
        this.roundCounts = roundCounts;

        for (Board board : boards) {
            if (board != null) {
                board.setCurrentPlayer(blackPlayer);
            }
        }
    }

    public Board getCurrentBoard() {
        if (currentBoardIndex < 0 || currentBoardIndex >= boards.size()) {
            return null;
        }
        return boards.get(currentBoardIndex);
    }

    public Player getCurrentPlayer() {
        Board currentBoard = getCurrentBoard();
        if (currentBoard != null) {
            return currentBoard.getCurrentPlayer();
        }
        return null;
    }

    public int getCurrentBoardIndex() {
        return currentBoardIndex;
    }

    public boolean handleCommand(String input) {
        input = input.trim().toLowerCase();

        if (input.equals("quit")) {
            return false;
        }

        if (input.equals("reversi") || input.equals("peace") || input.equals("gomoku")) {
            boolean isReversi = input.equals("reversi");
            boolean isGomoku = input.equals("gomoku");
            Board newBoard = new Board(isReversi, isGomoku);
            if (newBoard != null) {
                newBoard.setCurrentPlayer(blackPlayer);
                boards.add(newBoard);
                int[] newRoundCounts = new int[roundCounts.length + 1];
                System.arraycopy(roundCounts, 0, newRoundCounts, 0, roundCounts.length);
                newRoundCounts[newRoundCounts.length - 1] = 1;
                roundCounts = newRoundCounts;
            }
            return true;
        }

        try {
            int boardNumber = Integer.parseInt(input);
            if (boardNumber >= 1 && boardNumber <= boards.size()) {
                switchBoard(boardNumber - 1);
                return true;
            } else {
                System.out.println("Invalid board number! Available: 1-" + boards.size());
                return false;
            }
        } catch (NumberFormatException e) {
            // 不做处理，继续后续检查
        }

        Board currentBoard = getCurrentBoard();
        if (currentBoard == null) {
            System.out.println("Current board is null!");
            return false;
        }
        Player currentPlayer = currentBoard.getCurrentPlayer();
        if (currentPlayer == null) {
            System.out.println("Current player is null!");
            return false;
        }

        if (input.equals("pass")) {
            if (currentBoard.isReversiMode()) {
                if (!currentBoard.hasValidMove(currentPlayer)) {
                    System.out.println(currentPlayer.getName() + " has no valid moves and passes.");
                    switchPlayer(currentBoard);
                    if (!currentBoard.hasValidMove(currentBoard.getCurrentPlayer()) &&
                            !currentBoard.hasValidMove(currentPlayer)) {
                        currentBoard.setGameEnded(blackPlayer, whitePlayer);
                        System.out.println(currentBoard.getGameEndMessage());
                    }
                    return true;
                } else {
                    System.out.println("You have valid moves. You cannot pass.");
                    return false;
                }
            } else {
                System.out.println("Invalid input!");
                return false;
            }
        }

        if (inputValidator.isValidMove(input)) {
            int row = input.charAt(0) - '1';
            int col = Character.toLowerCase(input.charAt(1)) - 'a';

            if (currentBoard.isValidMove(row, col, currentPlayer.getPiece())) {
                currentBoard.makeMove(row, col, currentPlayer.getPiece());
                if (currentBoard.isGameOver(blackPlayer, whitePlayer)) {
                    currentBoard.setGameEnded(blackPlayer, whitePlayer);
                    System.out.println(currentBoard.getGameEndMessage());
                }
                switchPlayer(currentBoard);
                if (currentBoard.isGomokuMode()) {
                    if (currentPlayer == whitePlayer && roundCounts[currentBoardIndex] < 32) {
                        roundCounts[currentBoardIndex]++;
                    }
                }
                return true;
            } else {
                System.out.println("Position not playable.");
            }
        } else {
            System.out.println("Invalid input!");
        }
        return false;
    }

    private void switchBoard(int newIndex) {
        if (newIndex >= 0 && newIndex < boards.size()) {
            currentBoardIndex = newIndex;
            System.out.println("Switched to Board " + (newIndex + 1) +
                    " (" + getBoardMode(currentBoardIndex) + " mode)");
        } else {
            System.out.println("Invalid board index!");
        }
    }

    public boolean areAllBoardsFull() {
        for (Board board : boards) {
            if (board != null && !board.isFull()) {
                return false;
            }
        }
        return true;
    }

    private void switchPlayer(Board board) {
        if (board != null) {
            board.setCurrentPlayer(
                    board.getCurrentPlayer() == blackPlayer? whitePlayer : blackPlayer
            );
        }
    }

    private String getBoardMode(int index) {
        if (index >= 0 && index < boards.size()) {
            Board board = boards.get(index);
            if (board.isReversiMode()) {
                return "reversi";
            } else if (board.isGomokuMode()) {
                return "gomoku";
            } else {
                return "peace";
            }
        }
        return "unknown";
    }

    public int[] getRoundCounts() {
        return roundCounts;
    }
}    