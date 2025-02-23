import java.util.*;
import java.util.function.Consumer;

// Kelas untuk merepresentasikan blok puzzle
public class PuzzlePiece {
    char id;  // misalnya 'A', 'B', dst.
    // Setiap orientasi berupa list titik relatif (setelah dinormalisasi sehingga titik terkiri-atas = (0,0))
    List<List<Point>> orientations;
    
    // Konstruktor menerima blok puzzle dalam bentuk list baris (string) yang merupakan konfigurasi
    public PuzzlePiece(char id, List<String> shapeLines) {
        this.id = id;
        // Dapatkan list titik dari konfigurasi
        List<Point> baseShape = new ArrayList<>();
        for (int i = 0; i < shapeLines.size(); i++) {
            String line = shapeLines.get(i);
            for (int j = 0; j < line.length(); j++) {
                if (line.charAt(j) == id) {
                    baseShape.add(new Point(i, j));
                }
            }
        }
        // Normalisasi agar titik terkiri-atas = (0,0)
        baseShape = normalize(baseShape);
        // Hasilkan seluruh orientasi unik (rotasi dan cermin)
        orientations = generateOrientations(baseShape);
    }
    
    // Normalisasi: geser semua titik sehingga minimal r dan c = 0
    private List<Point> normalize(List<Point> pts) {
        int minR = Integer.MAX_VALUE, minC = Integer.MAX_VALUE;
        for (Point p : pts) {
            if (p.r < minR) minR = p.r;
            if (p.c < minC) minC = p.c;
        }
        List<Point> norm = new ArrayList<>();
        for (Point p : pts) {
            norm.add(new Point(p.r - minR, p.c - minC));
        }
        return norm;
    }
    
    // Menghasilkan semua orientasi (rotasi dan cermin) secara unik
    private List<List<Point>> generateOrientations(List<Point> base) {
        Set<String> seen = new HashSet<>();
        List<List<Point>> result = new ArrayList<>();
        
        // Fungsi untuk menambahkan orientasi jika belum ada (menggunakan signature)
        Consumer<List<Point>> addIfUnique = (List<Point> shape) -> {
            List<Point> norm = normalize(shape);
            String sig = signature(norm);
            if (!seen.contains(sig)) {
                seen.add(sig);
                result.add(norm);
            }
        };
        
        // Tambahkan rotasi dari bentuk dasar
        List<Point> current = base;
        for (int i = 0; i < 4; i++) {
            addIfUnique.accept(current);
            current = rotate90(current);
        }
        // Mendapatkan cermin (flip horizontal) dari bentuk dasar
        List<Point> mirrored = mirror(base);
        current = mirrored;
        for (int i = 0; i < 4; i++) {
            addIfUnique.accept(current);
            current = rotate90(current);
        }
        return result;
    }
    
    // Membuat signature string dari list titik (digunakan untuk mengecek duplikasi)
    private String signature(List<Point> pts) {
        List<String> list = new ArrayList<>();
        for (Point p : pts) {
            list.add(p.r + "_" + p.c);
        }
        Collections.sort(list);
        return String.join(",", list);
    }
    
    // Rotasi 90° searah jarum jam
    private List<Point> rotate90(List<Point> pts) {
        int maxR = 0, maxC = 0;
        for (Point p : pts) {
            if (p.r > maxR) maxR = p.r;
            if (p.c > maxC) maxC = p.c;
        }
        List<Point> rotated = new ArrayList<>();
        for (Point p : pts) {
            rotated.add(new Point(p.c, maxR - p.r));
        }
        return rotated;
    }
    
    // Mirror secara horizontal (flip terhadap sumbu vertikal)
    private List<Point> mirror(List<Point> pts) {
        int maxC = 0;
        for (Point p : pts) {
            if (p.c > maxC) maxC = p.c;
        }
        List<Point> mirrored = new ArrayList<>();
        for (Point p : pts) {
            mirrored.add(new Point(p.r, maxC - p.c));
        }
        return mirrored;
    }
}