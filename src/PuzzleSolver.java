import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class PuzzleSolver {
    public static int casesTried = 0;
    // Metode rekursif untuk pencarian solusi dengan backtracking (brute force murni dengan backtracking)
    public static boolean solve(PuzzleBoard puzzleBoard, List<PuzzlePiece> pieces, boolean[] used) {
        int[] pos = findEmpty(puzzleBoard);
        if (pos == null) {
            // Papan penuh, solusi ditemukan
            return true;
        }
        int r = pos[0], c = pos[1];
        
        // Coba setiap blok puzzle yang belum terpakai
        for (int i = 0; i < pieces.size(); i++) {
            if (!used[i]) {
                PuzzlePiece piece = pieces.get(i);
                // Coba setiap orientasi dari blok
                for (List<Point> orient : piece.orientations) {
                    // Karena kita ingin menutupi sel (r, c), coba setiap titik pada orientasi sebagai "anchor"
                    for (Point anchor : orient) {
                        int offsetR = r - anchor.r;
                        int offsetC = c - anchor.c;
                        casesTried++; // hitung tiap kali kita mencoba penempatan
                        if (canPlace(puzzleBoard, orient, offsetR, offsetC)) {
                            placePiece(puzzleBoard, piece.id, orient, offsetR, offsetC);
                            used[i] = true;
                            if (solve(puzzleBoard, pieces, used)) {
                                return true;
                            }
                            // Backtrack
                            removePiece(puzzleBoard, orient, offsetR, offsetC);
                            used[i] = false;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    // Cari sel kosong pertama pada papan (diindeks dengan '.')
    public static int[] findEmpty(PuzzleBoard puzzleBoard) {
        for (int i = 0; i < puzzleBoard.getRows(); i++) {
            for (int j = 0; j < puzzleBoard.getCols(); j++) {
                if (puzzleBoard.getCell(i,j) == '.') {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }
    
    // Cek apakah blok puzzle dapat diletakkan pada papan pada posisi tertentu
    // Tanda '#' diabaikan, '.' dianggap kosong, karakter lain dianggap sudah terisi
    public static boolean canPlace(PuzzleBoard puzzleBoard, List<Point> orient, int offsetR, int offsetC) {
        for (Point p : orient) {
            int r = offsetR + p.r;
            int c = offsetC + p.c;
            if (r < 0 || r >= puzzleBoard.getRows() || c < 0 || c >= puzzleBoard.getCols())
                return false;
            char cell = puzzleBoard.getCell(r, c);
            if (cell == '#') {
                // Skip sel yang sudah terisi
                continue;
            }
            if (cell != '.')
                return false;
        }
        return true;
    }
    
    // Letakkan blok puzzle pada papan pada posisi tertentu
    // Karakter '#' diabaikan, karakter '.' diisi dengan karakter id
    public static void placePiece(PuzzleBoard puzzleBoard, char id, List<Point> orient, int offsetR, int offsetC) {
        for (Point p : orient) {
            int r = offsetR + p.r, c = offsetC + p.c;
            if (puzzleBoard.getCell(r, c) == '.')
                puzzleBoard.setCell(r, c, id);
        }
    }
    
    // Menghapus blok puzzle dari papan pada posisi tertentu
    // Karakter '#' diabaikan, karakter '.' diisi kembali dengan karakter '.'
    public static void removePiece(PuzzleBoard puzzleBoard, List<Point> orient, int offsetR, int offsetC) {
        for (Point p : orient) {
            int r = offsetR + p.r, c = offsetC + p.c;
            if (puzzleBoard.getCell(r, c) != '#')
                puzzleBoard.setCell(r, c, '.');
        }
    }
    
    
    // Simpan solusi ke file output
    public static void saveSolution(String fileName, PuzzleBoard puzzleBoard, long searchTimeMs, long casesTried) {
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {
            for (int i = 0; i < puzzleBoard.getRows(); i++) {
                for (int j = 0; j < puzzleBoard.getCols(); j++) {
                    writer.print(puzzleBoard.getCell(i,j));
                }
                writer.println();
            }
            writer.println();
            writer.println("Waktu pencarian: " + searchTimeMs + " ms");
            writer.println("Banyak kasus yang ditinjau: " + casesTried);
        } catch (IOException e) {
            System.out.println("Gagal menyimpan file: " + fileName);
        }
    }
}
