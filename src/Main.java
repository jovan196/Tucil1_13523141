import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        List<PuzzlePiece> pieces = new ArrayList<>();
        try (Scanner consoleScanner = new Scanner(System.in)) {
            System.out.print("Masukkan path file test case (.txt): ");
            String filePath = consoleScanner.nextLine().trim();
            
            try (Scanner fileScanner = new Scanner(new File(filePath))) {
                // Baca dimensi papan dan jumlah blok puzzle
                int boardRows = fileScanner.nextInt();
                int boardCols = fileScanner.nextInt();
                int P = fileScanner.nextInt();
                fileScanner.nextLine(); // konsumsi baris sisa

                // Baca tipe konfigurasi (DEFAULT/CUSTOM/PYRAMID)
                String configType = fileScanner.nextLine().trim();
                System.out.println("Konfigurasi: " + configType);

                // Inisialisasi papan kosong
                PuzzleBoard puzzleBoard = new PuzzleBoard(boardRows, boardCols);

                // --- Perbaikan: Baca seluruh baris yang tersisa ke dalam list ---
                List<String> remainingLines = new ArrayList<>();
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        remainingLines.add(line);
                    }
                }

                // Baca blok puzzle satu per satu dari list tersebut.
                boolean[] used = new boolean[P];
                int pointer = 0;
                for (int i = 0; i < P; i++) {
                    char expectedChar = (char) ('A' + i);
                    List<String> shapeLines = new ArrayList<>();
                    // Ambil baris selama huruf awal sesuai dengan expectedChar
                    while (pointer < remainingLines.size() && remainingLines.get(pointer).charAt(0) == expectedChar) {
                        shapeLines.add(remainingLines.get(pointer));
                        pointer++;
                    }
                    if (shapeLines.isEmpty()) {
                        System.out.println("Tidak ditemukan bentuk untuk blok " + expectedChar);
                        return;
                    }
                    PuzzlePiece piece = new PuzzlePiece(expectedChar, shapeLines);
                    pieces.add(piece);
                }

                // Tampilkan informasi input
                System.out.println("Ukuran papan: " + boardRows + "x" + boardCols);
                System.out.println("Banyak blok puzzle: " + P);

                // Mulai waktu pencarian algoritma
                long startTime = System.currentTimeMillis();
                boolean solved = PuzzleSolver.solve(puzzleBoard, pieces, used);
                long endTime = System.currentTimeMillis();
                long searchTimeMs = endTime - startTime;

                if (solved) {
                    System.out.println("\nSolusi ditemukan:\n");
                    puzzleBoard.printBoardColored(pieces);
                    System.out.println("\nWaktu pencarian: " + searchTimeMs + " ms");
                    System.out.println("Banyak kasus yang ditinjau: " + PuzzleSolver.casesTried);
                } else {
                    System.out.println("Tidak ada solusi.");
                }

                // Tanyakan apakah solusi ingin disimpan
                System.out.print("\nApakah anda ingin menyimpan solusi? (ya/tidak): ");
                String response = consoleScanner.nextLine().trim().toLowerCase();
                if (response.equals("ya")) {
                    System.out.print("Masukkan nama file output (.txt): ");
                    String outFile = consoleScanner.nextLine();
                    PuzzleSolver.saveSolution(outFile, puzzleBoard, searchTimeMs, PuzzleSolver.casesTried);
                    System.out.println("Solusi telah disimpan pada file " + outFile);
                }

            } catch (FileNotFoundException e) {
                System.out.println("File tidak ditemukan: " + filePath);
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }
}
