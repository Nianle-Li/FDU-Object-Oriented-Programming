# FDU-Object-Oriented-Programming
The Object-Oriented-Programming course in Fudan University
# 运行方法
在位于`lab5`的文件夹时，在终端输入`java -cp . Game.ReversiGame`。
# 源代码文件名称及主要功能
## 1. Board.java
- **主要功能**：
    - 表示游戏棋盘，包含棋盘的大小、棋子状态等信息。
    - 支持多种游戏模式，如黑白棋（Reversi）、五子棋（Gomoku）和和平模式（Peace）。
    - 负责初始化棋盘，检查落子的合法性，处理落子操作，判断游戏是否结束以及计算得分等。
## 2. GameCommandHandler.java
- **主要功能**：
    - 处理用户输入的命令，如落子、切换棋盘、开始新游戏模式、放弃回合、退出游戏等。
    - 管理多个棋盘的状态，包括当前棋盘索引、当前玩家等。
    - 检查用户输入的合法性，并根据不同的游戏模式执行相应的操作。
## 3. GameInputValidator.java
- **主要功能**：
    - 验证用户输入的合法性，包括棋盘编号和落子位置的格式。
    - 提供方法来检查输入是否为有效的棋盘编号或有效的落子位置。
## 4. GameScreenManager.java
- **主要功能**：
    - 负责显示游戏棋盘，包括棋盘的布局、棋子状态、玩家信息、得分等。
    - 支持清屏操作，以提供更好的游戏界面体验。
    - 根据不同的游戏模式和棋盘状态显示相应的信息，如有效落子提示、游戏结束信息等。
## 5. GameUserInputReader.java
- **主要功能**：
    - 从用户处读取输入，提示用户输入落子位置、棋盘编号、新游戏模式、放弃回合或退出游戏等。
    - 根据当前游戏模式和棋盘状态调整提示信息，如在五子棋游戏结束后，修改提示信息以避免误导用户。
## 6. Piece.java
- **主要功能**：
    - 定义棋子的枚举类型，包括黑子（`BLACK`）、白子（`WHITE`）和空位（`EMPTY`）。
    - 为每个棋子类型提供对应的符号表示，用于在棋盘上显示。
## 7. Player.java
- **主要功能**：
    - 表示游戏玩家，包含玩家的名称和所使用的棋子类型。
    - 提供方法来获取玩家的名称和棋子类型。
## 8. ReversiGame.java
- **主要功能**：
    - 程序的入口点，负责初始化游戏，包括创建玩家、棋盘列表、输入验证器、命令处理器、屏幕管理器和输入读取器等。
    - 控制游戏的主循环，不断读取用户输入，处理命令，并更新游戏状态。
    - 当用户输入退出命令时，结束游戏并关闭输入读取器。
# 关键代码及设计思路
### 1. 落子合法性检查

```java
public boolean isValidMove(int row, int col, Piece piece) {
    if (isGameEnded && isGomokuMode) {
        return false;
    }
    return row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == Piece.EMPTY;
}
```

**设计思路**：  
此方法用来判定落子位置是否合法。在五子棋模式下，需满足三个条件：一是游戏未结束；二是落子位置在棋盘范围内；三是该位置为空。只有这三个条件都满足，落子才被认为是合法的。

### 2. 五子棋获胜判断

```java
private boolean checkGomokuWin(int row, int col, Piece piece) {
    int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    for (int[] dir : directions) {
        int count = checkLine(row, col, dir[0], dir[1], piece) 
                  + checkLine(row, col, -dir[0], -dir[1], piece) - 1;
        if (count >= 5) {
            return true;
        }
    }
    return false;
}

private int checkLine(int row, int col, int dx, int dy, Piece piece) {
    int count = 0;
    for (int i = 0; ; i++) {
        int newRow = row + i * dx;
        int newCol = col + i * dy;
        if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE 
            && board[newRow][newCol] == piece) {
            count++;
        } else {
            break;
        }
    }
    return count;
}
```

**设计思路**：  
`checkGomokuWin` 方法通过检查四个方向（水平、垂直、正斜、反斜）上是否有连续五个相同的棋子来判断是否获胜。它调用 `checkLine` 方法分别向正反两个方向统计连续相同棋子的数量，最后将两个方向的数量相加并减去重复计算的落子位置本身。若某个方向上的连续棋子数量达到或超过 5 个，则判定该玩家获胜。
### 3. 棋盘满局判断

```java
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
```
**设计思路**：  
该方法用于检查棋盘是否已满。通过遍历整个棋盘，若发现有一个位置为空，则判定棋盘未满；只有当所有位置都不为空时，才认为棋盘已满。
### 4. 五子棋落子操作
```java
public void makeMove(int row, int col, Piece piece) {
    if (isValidMove(row, col, piece)) {
        board[row][col] = piece;
        if (checkGomokuWin(row, col, piece)) {
            isGameEnded = true;
            gameResult = currentPlayer.getName() + " wins!";
        } else if (isFull()) {
            isGameEnded = true;
            gameResult = "Draw!";
        }
    }
}
```

**设计思路**：  
`makeMove` 方法是处理落子操作的核心方法。首先调用 `isValidMove` 方法检查落子是否合法，若合法则更新棋盘状态。落子后，调用 `checkGomokuWin` 方法判断是否有玩家获胜，若获胜则更新游戏结束状态和游戏结果；若未获胜，则检查棋盘是否已满，若已满则判定为平局并更新相应状态。
### 5. 五子棋游戏结束提示语修改

```java
public String getUserInput(Player currentPlayer, int currentBoardIndex, List<Board> boards) {
    Board currentBoard = boards.get(currentBoardIndex);
    if (currentBoard.isGomokuMode() && currentBoard.isGameEnded()) {
        System.out.printf("%s's turn. Enter game number / new mode / end the game: ", currentPlayer.getName());
    } else {
        System.out.printf("%s's turn. Enter move / game number / new mode / abandon move / end the game: ", currentPlayer.getName());
    }
    return scanner.nextLine();
}
```

**设计思路**：  
在 `getUserInput` 方法中，会检查当前是否为五子棋模式且游戏已结束。若满足条件，会修改提示语，去除关于落子位置的提示，避免在游戏结束后误导用户。
### 6. 对于合法落子的判断与相关提示语
```java
// GameInputValidator.java
public class GameInputValidator {
    public boolean isValidMove(String input) {
        return input.matches("\\d[a-z]");
    }
}

// GameCommandHandler.java
public boolean handleCommand(String input) {
    input = input.trim().toLowerCase();
    if (input.matches("quit|reversi|peace|gomoku")) {
        return true;
    }
    if (input.matches("\\d+")) {
        int num = Integer.parseInt(input);
        if (num >= 1 && num <= boards.size()) {
            return true;
        }
        System.out.println("Invalid board number!");
        return false;
    }
    if (inputValidator.isValidMove(input)) {
        Board board = getCurrentBoard();
        if (board.isValidMove(...)) {
            return true;
        }
        System.out.println("Position not playable.");
    } else {
        System.out.println("Invalid input!");
    }
    return false;
}
```
### 设计思路
- **分工验证**：`GameInputValidator` 用正则表达式验证落子输入格式，`GameCommandHandler` 验证模式切换、棋盘切换和落子位置合法性。
- **错误反馈**：输入不合法时输出明确提示语，如 “Invalid board number!”，帮助用户了解错误并调整输入。
