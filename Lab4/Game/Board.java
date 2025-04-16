package Game;

public class Board {
    public static final int SIZE = 8;
    private final Piece[][] board;
    private Player currentPlayer;
    private final boolean isReversiMode;
    private boolean isGameEnded;
    private int blackScore; 
    private int whiteScore; 
    private String gameResult; 

    public Board(boolean isReversiMode) {
        this.isReversiMode = isReversiMode;
        this.board = new Piece[SIZE][SIZE];
        initializeBoard();
        this.isGameEnded = false;
        this.blackScore = 0;
        this.whiteScore = 0;
        this.gameResult = "";
    }

    private void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = Piece.EMPTY;
            }
        }
        board[3][4] = Piece.BLACK;
        board[4][3] = Piece.BLACK;
        board[3][3] = Piece.WHITE;
        board[4][4] = Piece.WHITE;
    
        
    }

    public boolean isValidMove(int row, int col, Piece piece) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != Piece.EMPTY) {
            return false;
        }

        if (!isReversiMode) {
            return true; 
        }

        // Reversi模式需要检查可翻转棋子
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (checkDirection(row, col, dx, dy, piece)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkDirection(int row, int col, int dx, int dy, Piece piece) {
        int r = row + dx;
        int c = col + dy;
        boolean foundOpponent = false;

        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
            if (board[r][c] == Piece.EMPTY) return false;
            if (board[r][c] == piece) return foundOpponent;
            foundOpponent = true;
            r += dx;
            c += dy;
        }
        return false;
    }

    public void makeMove(int row, int col, Piece piece) {
        if (!isValidMove(row, col, piece)) return;

        board[row][col] = piece;

        if (isReversiMode) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    flipInDirection(row, col, dx, dy, piece);
                }
            }
        }
    }

    private void flipInDirection(int row, int col, int dx, int dy, Piece piece) {
        int r = row + dx;
        int c = col + dy;
        boolean shouldFlip = false;

        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE) {
            if (board[r][c] == Piece.EMPTY) return;
            if (board[r][c] == piece) {
                shouldFlip = true;
                break;
            }
            r += dx;
            c += dy;
        }

        if (shouldFlip) {
            r = row + dx;
            c = col + dy;
            while (board[r][c] != piece) {
                board[r][c] = piece;
                r += dx;
                c += dy;
            }
        }
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

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isReversiMode() {
        return isReversiMode;
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public int getPieceCount(Piece piece) {
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == piece) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean hasValidMove(Player player) {
        if (!isReversiMode) {
            return false;
        }
        Piece piece = player.getPiece();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isValidMove(i, j, piece)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver(Player blackPlayer, Player whitePlayer) {
        if (isReversiMode) {
            return isFull() || (!hasValidMove(getCurrentPlayer()) &&!hasValidMove(getCurrentPlayer() == blackPlayer? whitePlayer : blackPlayer));
        } else {
            return isFull();
        }
    }

    // 设置游戏结束信息
    public void setGameEnded(Player blackPlayer, Player whitePlayer) {
        if (isGameOver(blackPlayer, whitePlayer)) {
            isGameEnded = true;
            blackScore = getPieceCount(Piece.BLACK);
            whiteScore = getPieceCount(Piece.WHITE);
            if (isReversiMode) {
                if (blackScore > whiteScore) {
                    gameResult = blackPlayer.getName() + " wins!";
                } else if (whiteScore > blackScore) {
                    gameResult = whitePlayer.getName() + " wins!";
                } else {
                    gameResult = "Draw!";
                }
            } else {
                gameResult = "The game on this board is over.";
            }
        }
    }

    // 获取游戏结束信息
    public String getGameEndMessage() {
        if (isReversiMode) {
            return "The game on this board is over. \nPlayer1 Tom ● : " + blackScore + "\nPlayer2 Amy ○ : " + whiteScore + "\n" + gameResult;
        } else {
            return gameResult;
        }
    }

    // 判断游戏是否结束
    public boolean isBoardGameEnded() {
        return isGameEnded;
    }
}    