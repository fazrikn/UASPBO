import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class BukuPanel extends JFrame {
    private BukuDAO bukuDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField tfJudul, tfPengarang, tfPenerbit, tfTahun, tfHarga, tfStok;
    private JButton btnTambah, btnUbah, btnHapus, btnBersihkan;

    public BukuPanel() {
        setTitle("Aplikasi Toko Buku");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        try {
            bukuDAO = new BukuDAO();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error koneksi database: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Judul", "Pengarang", "Penerbit", "Tahun Terbit", "Harga", "Stok"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable cells
            }
        };
        table = new JTable(tableModel);
        loadData();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Buku"));

        formPanel.add(new JLabel("Judul:"));
        tfJudul = new JTextField();
        formPanel.add(tfJudul);

        formPanel.add(new JLabel("Pengarang:"));
        tfPengarang = new JTextField();
        formPanel.add(tfPengarang);

        formPanel.add(new JLabel("Penerbit:"));
        tfPenerbit = new JTextField();
        formPanel.add(tfPenerbit);

        formPanel.add(new JLabel("Tahun Terbit:"));
        tfTahun = new JTextField();
        formPanel.add(tfTahun);

        formPanel.add(new JLabel("Harga:"));
        tfHarga = new JTextField();
        formPanel.add(tfHarga);

        formPanel.add(new JLabel("Stok:"));
        tfStok = new JTextField();
        formPanel.add(tfStok);

        btnTambah = new JButton("Tambah");
        formPanel.add(btnTambah);
        btnUbah = new JButton("Ubah");
        formPanel.add(btnUbah);
        btnHapus = new JButton("Hapus");
        formPanel.add(btnHapus);
        btnBersihkan = new JButton("Bersihkan");
        formPanel.add(btnBersihkan);

        add(formPanel, BorderLayout.SOUTH);

        // Event listeners
        btnTambah.addActionListener(e -> tambahBuku());
        btnUbah.addActionListener(e -> ubahBuku());
        btnHapus.addActionListener(e -> hapusBuku());
        btnBersihkan.addActionListener(e -> bersihkanForm());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                tfJudul.setText(tableModel.getValueAt(row, 1).toString());
                tfPengarang.setText(tableModel.getValueAt(row, 2).toString());
                tfPenerbit.setText(tableModel.getValueAt(row, 3).toString());
                tfTahun.setText(tableModel.getValueAt(row, 4).toString());
                tfHarga.setText(tableModel.getValueAt(row, 5).toString());
                tfStok.setText(tableModel.getValueAt(row, 6).toString());
            }
        });
    }

    private void loadData() {
        try {
            List<Buku> list = bukuDAO.getAllBuku();
            tableModel.setRowCount(0);
            for (Buku buku : list) {
                tableModel.addRow(new Object[]{
                        buku.getId(),
                        buku.getJudul(),
                        buku.getPengarang(),
                        buku.getPenerbit(),
                        buku.getTahunTerbit(),
                        buku.getHarga(),
                        buku.getStok()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error mengambil data buku: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tambahBuku() {
        try {
            Buku buku = new Buku(0,
                    tfJudul.getText(),
                    tfPengarang.getText(),
                    tfPenerbit.getText(),
                    Integer.parseInt(tfTahun.getText()),
                    Double.parseDouble(tfHarga.getText()),
                    Integer.parseInt(tfStok.getText())
            );
            bukuDAO.tambahBuku(buku);
            loadData();
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Data buku berhasil ditambahkan.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data buku: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubahBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data buku yang akan diubah.");
            return;
        }
        try {
            int id = (int) tableModel.getValueAt(row, 0);
            Buku buku = new Buku(id,
                    tfJudul.getText(),
                    tfPengarang.getText(),
                    tfPenerbit.getText(),
                    Integer.parseInt(tfTahun.getText()),
                    Double.parseDouble(tfHarga.getText()),
                    Integer.parseInt(tfStok.getText())
            );
            bukuDAO.updateBuku(buku);
            loadData();
            bersihkanForm();
            JOptionPane.showMessageDialog(this, "Data buku berhasil diubah.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah data buku: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusBuku() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data buku yang akan dihapus.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data buku ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                bukuDAO.hapusBuku(id);
                loadData();
                bersihkanForm();
                JOptionPane.showMessageDialog(this, "Data buku berhasil dihapus.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data buku: " + e.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void bersihkanForm() {
        tfJudul.setText("");
        tfPengarang.setText("");
        tfPenerbit.setText("");
        tfTahun.setText("");
        tfHarga.setText("");
        tfStok.setText("");
        table.clearSelection();
    }
}
