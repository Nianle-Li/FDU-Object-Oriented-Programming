package Game;

public class Board {
    public static final int SIZE = 8;
    private final Piece[][] board;
    private Player currentPlayer;
    private final boolean isReversiMode;
    private final boolean isGomokuMode;
    private boolean isGameEnded;
    private int blackScore;
    private int whiteScore;
    private String gameResult;

    public Board(boolean isReversiMode, boolean isGomokuMode) {
        this.isReversiMode = isReversiMode;
        this.isGomokuMode = isGomokuMode;
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
        if (!isGomokuMode) {
            board[3][4] = Piece.BLACK;
            board[4][3] = Piece.BLACK;
            board[3][3] = Piece.WHITE;
            board[4][4] = Piece.WHITE;
        }
    }

    public boolean isValidMove(int row, int col, Piece piece) {
        if (isGameEnded && isGomokuMode) {
            return false;
        }
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || board[row][col] != Piece.EMPTY) {
            return false;
        }

        if (isReversiMode) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    if (checkDirection(row, col, dx, dy, piece)) {
                        return true;
                    }
                }
            }
            return false;
        } else if (isGomokuMode) {
            return true;
        }
        return true;
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
        if (!isValidMove(row, col, piece)) {
            return;
        }

        board[row][col] = piece;

        if (isReversiMode) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    flipInDirection(row, col, dx, dy, piece);
                }
            }
        }

        if (isGomokuMode) {
            if (checkGomokuWin(row, col, piece)) {
                isGameEnded = true;
                if (piece == Piece.BLACK) {
                    blackScore = 1;
                    whiteScore = 0;
                    gameResult = currentPlayer.getName() + " wins!";
                } else {
                    whiteScore = 1;
                    blackScore = 0;
                    gameResult = currentPlayer.getName() + " wins!";
                }
            } else if (isFull()) {
                isGameEnded = true;
                gameResult = "Draw!";
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

    public boolean isGomokuMode() {
        return isGomokuMode;
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
        if (isReversiMode) {
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
        return false;
    }

    public boolean isGameOver(Player blackPlayer, Player whitePlayer) {
        if (isReversiMode) {
            return isFull() || (!hasValidMove(getCurrentPlayer()) &&!hasValidMove(getCurrentPlayer() == blackPlayer? whitePlayer : blackPlayer));
        } else if (isGomokuMode) {
            return isGameEnded;
        } else {
            return isFull();
        }
    }

    public void setGameEnded(Player blackPlayer, Player whitePlayer) {
        if (isGameOver(blackPlayer, whitePlayer)) {
            isGameEnded = true;
            if (isReversiMode) {
                blackScore = getPieceCount(Piece.BLACK);
                whiteScore = getPieceCount(Piece.WHITE);
                if (blackScore > whiteScore) {
                    gameResult = blackPlayer.getName() + " wins!";
                } else if (whiteScore > blackScore) {
                    gameResult = whitePlayer.getName() + " wins!";
                } else {
                    gameResult = "Draw!";
                }
            } else if (isGomokuMode) {
                if (blackScore > whiteScore) {
                    gameResult = blackPlayer.getName() + " wins!";
                } else if (whiteScore > blackScore) {
                    gameResult = whitePlayer.getName() + " wins!";
                } else {
                    gameResult = gameResult.isEmpty()? "Draw!" : gameResult;
                }
            } else {
                gameResult = "The game on this board is over.";
            }
        }
    }

    public String getGameEndMessage() {
        if (isReversiMode) {
            return "The game on this board is over. \nPlayer1 Tom ● : " + blackScore + "\nPlayer2 Amy ○ : " + whiteScore + "\n" + gameResult;
        } else if (isGomokuMode) {
            return "The game on this board is over. \n" + gameResult;
        } else {
            return gameResult;
        }
    }

    public boolean isBoardGameEnded() {
        return isGameEnded;
    }

    private boolean checkGomokuWin(int row, int col, Piece piece) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int count = 1;

            for (int i = 1; i < 5; i++) {
                int newRow = row + i * dx;
                int newCol = col + i * dy;
                if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && board[newRow][newCol] == piece) {
                    count++;
                } else {
                    break;
                }
            }

            for (int i = 1; i < 5; i++) {
                int newRow = row - i * dx;
                int newCol = col - i * dy;
                if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE && board[newRow][newCol] == piece) {
                    count++;
                } else {
                    break;
                }
            }

            if (count >= 5) {
                return true;
            }
        }
        return false;
    }
}    