import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {
    private Connection conn;

    public BukuDAO() throws SQLException {
        conn = DatabaseConnection.getConnection();
        createTableIfNotExists();
        insertSampleDataIfEmpty();
    }

    private void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS buku (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "judul TEXT NOT NULL," +
                "pengarang TEXT NOT NULL," +
                "penerbit TEXT NOT NULL," +
                "tahun_terbit INTEGER," +
                "harga REAL," +
                "stok INTEGER" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void insertSampleDataIfEmpty() throws SQLException {
        String countSql = "SELECT COUNT(*) FROM buku";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(countSql)) {
            if (rs.next() && rs.getInt(1) == 0) {
                String insertSql = "INSERT INTO buku (judul, pengarang, penerbit, tahun_terbit, harga, stok) VALUES " +
                        "('Pemrograman Java', 'Budi', 'Erlangga', 2020, 75000, 10)," +
                        "('Basis Data', 'Sari', 'Andi', 2019, 65000, 5)," +
                        "('Algoritma dan Struktur Data', 'Joko', 'Gramedia', 2021, 85000, 7)";
                try (Statement insertStmt = conn.createStatement()) {
                    insertStmt.executeUpdate(insertSql);
                }
            }
        }
    }

    public List<Buku> getAllBuku() throws SQLException {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT * FROM buku";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Buku buku = new Buku(
                        rs.getInt("id"),
                        rs.getString("judul"),
                        rs.getString("pengarang"),
                        rs.getString("penerbit"),
                        rs.getInt("tahun_terbit"),
                        rs.getDouble("harga"),
                        rs.getInt("stok")
                );
                list.add(buku);
            }
        }
        return list;
    }

    public void tambahBuku(Buku buku) throws SQLException {
        String sql = "INSERT INTO buku (judul, pengarang, penerbit, tahun_terbit, harga, stok) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, buku.getJudul());
            pstmt.setString(2, buku.getPengarang());
            pstmt.setString(3, buku.getPenerbit());
            pstmt.setInt(4, buku.getTahunTerbit());
            pstmt.setDouble(5, buku.getHarga());
            pstmt.setInt(6, buku.getStok());
            pstmt.executeUpdate();
        }
    }

    public void updateBuku(Buku buku) throws SQLException {
        String sql = "UPDATE buku SET judul = ?, pengarang = ?, penerbit = ?, tahun_terbit = ?, harga = ?, stok = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, buku.getJudul());
            pstmt.setString(2, buku.getPengarang());
            pstmt.setString(3, buku.getPenerbit());
            pstmt.setInt(4, buku.getTahunTerbit());
            pstmt.setDouble(5, buku.getHarga());
            pstmt.setInt(6, buku.getStok());
            pstmt.setInt(7, buku.getId());
            pstmt.executeUpdate();
        }
    }

    public void hapusBuku(int id) throws SQLException {
        String sql = "DELETE FROM buku WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
