import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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

    // Konstruktor alternatif untuk konfigurasi CUSTOM
    public PuzzleBoard(String[] layout) {
        boardRows = layout.length;
        boardCols = layout[0].length();
        board = new char[boardRows][boardCols];
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                // 'X' artinya sel kosong, '#' artinya sel terisi
                board[i][j] = (layout[i].charAt(j) == 'X') ? '.' : '#';
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

    // New method to generate an image result (JPG) with colored cells like an IQ puzzle.
    public void generateImageResult(String outputFile, List<PuzzlePiece> pieces) {
        // Create a mapping from piece id to a Color using a predefined palette.
        Map<Character, Color> colorMap = new HashMap<>();
        Color[] palette = {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,
            Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK,
            new Color(128, 0, 0), new Color(0, 128, 0), new Color(0, 0, 128),
            new Color(128, 128, 0), new Color(128, 0, 128), new Color(0, 128, 128),
            Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, new Color(255, 105, 180),
            new Color(255, 20, 147), new Color(221, 160, 221), new Color(102, 205, 170),
            new Color(95, 158, 160), new Color(186, 85, 211), new Color(240, 128, 128),
            new Color(144, 238, 144), new Color(173, 216, 230)
        };
        for (PuzzlePiece piece : pieces) {
            int index = piece.id - 'A';
            colorMap.put(piece.id, palette[index % palette.length]);
        }

        int cellSize = 50; // The size of each puzzle cell in pixels.
        int imgWidth = boardCols * cellSize;
        int imgHeight = boardRows * cellSize;

        BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();

        // Fill background with white.
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, imgWidth, imgHeight);

        // Draw each cell.
        for (int i = 0; i < boardRows; i++) {
            for (int j = 0; j < boardCols; j++) {
                char ch = board[i][j];
                int x = j * cellSize;
                int y = i * cellSize;
                if (ch != '.' && ch != '#') {
                    // Draw the puzzle piece with its corresponding color.
                    Color color = colorMap.get(ch);
                    if (color == null) {
                        color = Color.LIGHT_GRAY;
                    }
                    g2d.setColor(color);
                    g2d.fillRect(x, y, cellSize, cellSize);
                } else {
                    // For barriers ('#') draw black; for empty cells draw white.
                    if (ch == '#') {
                        g2d.setColor(Color.BLACK);
                        g2d.fillRect(x, y, cellSize, cellSize);
                    } else {
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(x, y, cellSize, cellSize);
                    }
                }
                // Draw cell border.
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, cellSize, cellSize);
            }
        }

        g2d.dispose();

        // Save the image as a JPG file.
        try {
            ImageIO.write(image, "jpg", new File(outputFile));
            System.out.println("Hasil image telah disimpan di folder test dengan nama " + outputFile);
        } catch (IOException e) {
            System.out.println("Error menyimpan file gambar: " + e.getMessage());
        }
    }
}
