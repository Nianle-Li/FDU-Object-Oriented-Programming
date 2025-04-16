package Game;

import java.util.List;

public class GameCommandHandler {
    private final List<Board> boards;
    private int currentBoardIndex;
    private final Player blackPlayer;
    private final Player whitePlayer;
    private final GameInputValidator inputValidator;

    public GameCommandHandler(List<Board> boards, Player blackPlayer, Player whitePlayer,
                              GameInputValidator inputValidator) {
        this.boards = boards;
        this.blackPlayer = blackPlayer;
        this.whitePlayer = whitePlayer;
        this.inputValidator = inputValidator;
        this.currentBoardIndex = 0;

        // 初始化所有棋盘为黑方先手
        for (Board board : boards) {
            board.setCurrentPlayer(blackPlayer);
        }
    }

    public boolean handleCommand(String input) {
        input = input.trim().toLowerCase();

        if (input.equals("quit")) {
            return false;
        }

        // 处理新建棋盘命令
        if (input.equals("reversi") || input.equals("peace")) {
            boolean isReversi = input.equals("reversi");
            Board newBoard = new Board(isReversi);
            newBoard.setCurrentPlayer(blackPlayer); // 新棋盘总是黑方先手
            boards.add(newBoard);
            // 这里不切换到新棋盘，不更新 currentBoardIndex
            return true;
        }

        // 处理切换棋盘命令
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
            // 不是数字，继续检查其他命令
        }

        // 处理落子命令
        Board currentBoard = getCurrentBoard();
        Player currentPlayer = currentBoard.getCurrentPlayer();

        if (input.equals("pass")) {
            if (currentBoard.isReversiMode()) {
                if (!currentBoard.hasValidMove(currentPlayer)) {
                    System.out.println(currentPlayer.getName() + " has no valid moves and passes.");
                    switchPlayer(currentBoard);
                    // 检查双方是否都无合法落子
                    if (!currentBoard.hasValidMove(currentBoard.getCurrentPlayer()) &&
                        !currentBoard.hasValidMove(currentPlayer)) {
                        currentBoard.setGameEnded(blackPlayer, whitePlayer);
                        System.out.println(currentBoard.getGameEndMessage()); // 添加这行输出游戏结束信息
                    }
                    return true;
                } else {
                    System.out.println("You have valid moves. You cannot pass.");
                    return false;
                }
            } else {
                // 在 peace 模式下，将 pass 视为非法输入
                System.out.println("Invalid input!");
                return false;
            }
        }

        if (inputValidator.isValidMove(input)) {
            int row = input.charAt(0) - '1';
            int col = Character.toLowerCase(input.charAt(1)) - 'a';

            if (currentBoard.isValidMove(row, col, currentPlayer.getPiece())) {
                currentBoard.makeMove(row, col, currentPlayer.getPiece());
                // 检查游戏是否结束，传入两个玩家
                if (currentBoard.isGameOver(blackPlayer, whitePlayer)) {
                    currentBoard.setGameEnded(blackPlayer, whitePlayer);
                    System.out.println(currentBoard.getGameEndMessage()); // 添加这行输出游戏结束信息
                }
                // 切换当前棋盘的玩家
                switchPlayer(currentBoard);
                return true;
            } else {
                System.out.println("Invalid move! Position not playable.");
            }
        } else {
            System.out.println("Invalid input!");
        }
        return false;
    }

    private void switchBoard(int newIndex) {
        currentBoardIndex = newIndex;
        System.out.println("Switched to Board " + (newIndex + 1) +
                " (" + (getCurrentBoard().isReversiMode()? "reversi" : "peace") + " mode)");
    }

    public Board getCurrentBoard() {
        return boards.get(currentBoardIndex);
    }

    public Player getCurrentPlayer() {
        return boards.get(currentBoardIndex).getCurrentPlayer();
    }

    public int getCurrentBoardIndex() {
        return currentBoardIndex;
    }

    public boolean areAllBoardsFull() {
        for (Board board : boards) {
            if (!board.isFull()) {
                return false;
            }
        }
        return true;
    }

    private void switchPlayer(Board board) {
        board.setCurrentPlayer(
                board.getCurrentPlayer() == blackPlayer? whitePlayer : blackPlayer
        );
    }
}