import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PuzzleBoard {
    // ANSI color codes untuk output berwarna (maksimal 26 warna)
    static final String[] ANSI_COLORS = {
        "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m",
        "\u001B[35m", "\u001B[36m", "\u001B[91m", "\u001B[92m",
        "\u001B[93m", "\u001B[94m", "\u001B[95m", "\u001B[96m",
        "\u001B[37m", "\u001B[90m", "\u001B[31m", "\u001B[32m",
        "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m",
        "\u001B[91m", "\u001B[92m", "\u001B[93m", "\u001B[94m",
        "\u001B[95m", "\u001B[96m"
    };
    static final String ANSI_RESET = "\u001B[0m";

    int boardRows, boardCols;
    char[][] board;

    public PuzzleBoard(int rows, int cols) {
        boardRows = rows;
        boardCols = cols;
        board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '.';
            }
        }
    }

    public int getRows() {
        return boardRows;
    }
    public int getCols() {
        return boardCols;
    }
    public char getCell(int r, int c) {
        return board[r][c];
    }
    public void setCell(int r, int c, char val) {
        board[r][c] = val;
    }

    public void printBoardColored(List<PuzzlePiece> pieces) {
        Map<Character, String> colorMap = new HashMap<>();
        for (PuzzlePiece piece : pieces) {
            int index = piece.id - 'A';
            colorMap.put(piece.id, ANSI_COLORS[index % ANSI_COLORS.length]);
        }
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                char ch = board[i][j];
                if (ch != '.') {
                    String color = colorMap.getOrDefault(ch, "");
                    System.out.print(color + ch + ANSI_RESET);
                } else {
                    System.out.print(ch);
                }
            }
            System.out.println();
        }
    }
}
