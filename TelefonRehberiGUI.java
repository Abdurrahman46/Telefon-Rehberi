package TelefonRehberiProje;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TelefonRehberiGUI {
    private OzelKisiDizisi rehber;
    private JFrame pencere;
    private JTextField alan1, alan2, alan3, aramaAlani;
    private JTable tablo;
    private DefaultTableModel tabloModeli;
    private final String dosyaAdi = "rehber.txt";

    
    private final Color beyaz = new Color(255, 255, 255);
    private final Color koyu = new Color(44, 62, 80);
    private final Color yesil = new Color(46, 204, 113);
    private final Color kirmizi = new Color(231, 76, 60);
    private final Color mavi = new Color(52, 152, 219);
    private final Color turuncu = new Color(243, 156, 18);

    
    private final Font yaziBasi = new Font("Segoe UI", Font.PLAIN, 13);
    private final Font yaziKalin = new Font("Segoe UI", Font.BOLD, 13);
    private final Font yaziBuyuk = new Font("Segoe UI", Font.BOLD, 14);

    public TelefonRehberiGUI() {
        rehber = new OzelKisiDizisi();
        dosyaOku();
        arayuzu();
    }

    class ArkaPlan extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D cizim = (Graphics2D) g;
            cizim.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int en = getWidth();
            int boy = getHeight();

            Color renk1 = new Color(236, 240, 241);
            Color renk2 = new Color(189, 195, 199);
            GradientPaint gc = new GradientPaint(0, 0, renk1, 0, boy, renk2);
            cizim.setPaint(gc);
            cizim.fillRect(0, 0, en, boy);

            cizim.setColor(new Color(255, 255, 255, 90));
            cizim.fillOval(-80, boy - 200, 350, 350);
            cizim.fillOval(en - 250, -100, 450, 450);
        }
    }

    private void arayuzu() {
        pencere = new JFrame("Adres Defteri Yönetim Sistemi Projesi");
        pencere.setSize(780, 550);
        pencere.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArkaPlan anaPanel = new ArkaPlan();
        anaPanel.setLayout(new BorderLayout(15, 15));
        pencere.setContentPane(anaPanel);

        JPanel ustPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 12));
        ustPanel.setBackground(koyu);
        ustPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel etiket1 = new JLabel("İsim Ara:");
        etiket1.setForeground(beyaz);
        etiket1.setFont(yaziKalin);
        ustPanel.add(etiket1);

        aramaAlani = textAlaniOlustur();
        aramaAlani.setColumns(22);
        aramaAlani.setText("Aramak istediğiniz ismi yazın...");
        aramaAlani.setForeground(Color.GRAY);

        aramaAlani.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (aramaAlani.getText().equals("Aramak istediğiniz ismi yazın...")) {
                    aramaAlani.setText("");
                    aramaAlani.setForeground(koyu);
                }
            }

            public void focusLost(java.awt.event.FocusEvent evt) {
                if (aramaAlani.getText().isEmpty()) {
                    aramaAlani.setForeground(Color.GRAY);
                    aramaAlani.setText("Aramak istediğiniz ismi yazın...");
                }
            }
        });
        ustPanel.add(aramaAlani);

        JButton buton1 = butonOlustur("Bul", mavi);
        JButton buton2 = butonOlustur("Tümünü Göster", mavi);
        JButton buton3 = butonOlustur("A-Z Sırala", mavi);

        ustPanel.add(buton1);
        ustPanel.add(buton2);
        ustPanel.add(buton3);
        anaPanel.add(ustPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 18));
        formPanel.setBackground(beyaz);

        javax.swing.border.TitledBorder formBaslik = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true),
                "Kişi Bilgileri Formu");
        formBaslik.setTitleFont(yaziBuyuk);
        formBaslik.setTitleColor(koyu);

        formPanel.setBorder(BorderFactory.createCompoundBorder(
                formBaslik,
                BorderFactory.createEmptyBorder(20, 15, 20, 15)));

        JLabel etiket2 = new JLabel("Ad:");
        etiket2.setFont(yaziKalin);
        etiket2.setForeground(koyu);
        alan1 = textAlaniOlustur();
        formPanel.add(etiket2);
        formPanel.add(alan1);

        JLabel etiket3 = new JLabel("Soyad:");
        etiket3.setFont(yaziKalin);
        etiket3.setForeground(koyu);
        alan2 = textAlaniOlustur();
        formPanel.add(etiket3);
        formPanel.add(alan2);

        JLabel etiket4 = new JLabel("Telefon:");
        etiket4.setFont(yaziKalin);
        etiket4.setForeground(koyu);
        alan3 = textAlaniOlustur();
        formPanel.add(etiket4);
        formPanel.add(alan3);

        JButton buton4 = butonOlustur("Ekle", yesil);
        JButton buton5 = butonOlustur("Güncelle", turuncu);

        formPanel.add(buton4);
        formPanel.add(buton5);

        JPanel solKapla = new JPanel(new BorderLayout());
        solKapla.setOpaque(false);
        solKapla.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 0));
        solKapla.add(formPanel, BorderLayout.NORTH);
        anaPanel.add(solKapla, BorderLayout.WEST);

        String[] sutunlar = { "Ad", "Soyad", "Telefon" };
        tabloModeli = new DefaultTableModel(sutunlar, 0);
        tablo = new JTable(tabloModeli);
        tablo.setFont(yaziBasi);
        tablo.setRowHeight(30);
        tablo.setSelectionBackground(new Color(52, 152, 219, 40));
        tablo.setSelectionForeground(Color.BLACK);
        tablo.setGridColor(new Color(236, 240, 241));

        JTableHeader baslik = tablo.getTableHeader();
        baslik.setBackground(koyu);
        baslik.setForeground(beyaz);
        baslik.setFont(yaziBuyuk);
        baslik.setOpaque(true);

        JScrollPane kaydirma = new JScrollPane(tablo);
        kaydirma.setBorder(BorderFactory.createLineBorder(new Color(220, 221, 225), 1));

        JPanel ortaKapla = new JPanel(new BorderLayout());
        ortaKapla.setOpaque(false);
        ortaKapla.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 15));
        ortaKapla.add(kaydirma, BorderLayout.CENTER);
        anaPanel.add(ortaKapla, BorderLayout.CENTER);

        JPanel altPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        altPanel.setOpaque(false);
        altPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 15));

        JButton buton6 = butonOlustur("Seçili Kişiyi Sil", kirmizi);
        altPanel.add(buton6);
        anaPanel.add(altPanel, BorderLayout.SOUTH);

        tabloGuncelle(rehber);

        tablo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tablo.getSelectedRow() != -1) {
                    int seciliSatir = tablo.getSelectedRow();
                    alan1.setText(tabloModeli.getValueAt(seciliSatir, 0).toString());
                    alan2.setText(tabloModeli.getValueAt(seciliSatir, 1).toString());
                    alan3.setText(tabloModeli.getValueAt(seciliSatir, 2).toString());
                }
            }
        });

        buton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ad = alan1.getText().trim();
                String soyad = alan2.getText().trim();
                String telefon = alan3.getText().trim();

                if (!ad.isEmpty() && !soyad.isEmpty() && !telefon.isEmpty()) {

                    if (rehber.numaraVarMi(telefon)) {
                        JOptionPane.showMessageDialog(pencere,
                                "Bu telefon numarası zaten rehberde kayıtlı! Farklı kişilere aynı numarayı ekleyemezsin.",
                                "Numara Çakışması", JOptionPane.ERROR_MESSAGE);
                    } else {
                        rehber.ekle(new Kisi(ad, soyad, telefon));
                        dosyaKaydet();
                        tabloGuncelle(rehber);
                        alan1.setText("");
                        alan2.setText("");
                        alan3.setText("");
                        tablo.clearSelection();
                    }

                } else {
                    JOptionPane.showMessageDialog(pencere, "Lütfen tüm alanları doldurun!", "Hata",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seciliSatir = tablo.getSelectedRow();
                if (seciliSatir != -1) {
                    String eskiAd = (String) tabloModeli.getValueAt(seciliSatir, 0);
                    String eskiSoyad = (String) tabloModeli.getValueAt(seciliSatir, 1);
                    String eskiTelefon = (String) tabloModeli.getValueAt(seciliSatir, 2);

                    String yeniAd = alan1.getText().trim();
                    String yeniSoyad = alan2.getText().trim();
                    String yeniTelefon = alan3.getText().trim();

                    if (!yeniAd.isEmpty() && !yeniSoyad.isEmpty() && !yeniTelefon.isEmpty()) {
                        rehber.kisiGuncelle(eskiAd, eskiSoyad, eskiTelefon, yeniAd, yeniSoyad, yeniTelefon);
                        dosyaKaydet();

                        aramaAlani.setForeground(Color.GRAY);
                        aramaAlani.setText("Aramak istediğiniz ismi yazın...");

                        tabloGuncelle(rehber);
                        tablo.clearSelection();
                        alan1.setText("");
                        alan2.setText("");
                        alan3.setText("");
                        JOptionPane.showMessageDialog(pencere, "Kişi bilgileri başarıyla güncellendi!", "Başarılı",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(pencere, "Lütfen güncellenecek tüm alanları doldurun!",
                                "Eksik Bilgi", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(pencere, "Lütfen güncellemek için tablodan bir kişi seçin.",
                            "Seçim Yapılmadı", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        buton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int seciliSatir = tablo.getSelectedRow();
                if (seciliSatir != -1) {
                    String seciliAd = (String) tabloModeli.getValueAt(seciliSatir, 0);
                    String seciliSoyad = (String) tabloModeli.getValueAt(seciliSatir, 1);
                    String seciliTelefon = (String) tabloModeli.getValueAt(seciliSatir, 2);

                    Object[] secenekler = { "Evet", "Hayır" };
                    int onay = JOptionPane.showOptionDialog(pencere,
                            seciliAd + " kişisini silmek istediğinize emin misiniz?",
                            "Silme Onayı", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, secenekler,
                            secenekler[0]);

                    if (onay == JOptionPane.YES_OPTION) {
                        rehber.kisiSil(seciliAd, seciliSoyad, seciliTelefon);
                        dosyaKaydet();

                        aramaAlani.setForeground(Color.GRAY);
                        aramaAlani.setText("Aramak istediğiniz ismi yazın...");

                        tabloGuncelle(rehber);
                        alan1.setText("");
                        alan2.setText("");
                        alan3.setText("");
                    }
                } else {
                    JOptionPane.showMessageDialog(pencere, "Lütfen silmek için tablodan bir kişi seçin.",
                            "Seçim Yapılmadı", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        buton3.addActionListener(e -> {
            rehber.ismeGoreSirala();
            dosyaKaydet();
            tabloGuncelle(rehber);
        });

        buton1.addActionListener(e -> {
            String aranan = aramaAlani.getText().trim();

            if (!aranan.isEmpty() && !aranan.equals("Aramak istediğiniz ismi yazın...")) {
                tabloGuncelle(rehber.ismeGoreFiltrele(aranan));
            }
        });

        buton2.addActionListener(e -> {
            aramaAlani.setForeground(Color.GRAY);
            aramaAlani.setText("Aramak istediğiniz ismi yazın...");
            tabloGuncelle(rehber);

            tablo.requestFocus();
        });

        pencere.setLocationRelativeTo(null);
        pencere.setVisible(true);
        anaPanel.requestFocusInWindow();
    }

    private JTextField textAlaniOlustur() {
        JTextField alan = new JTextField();
        alan.setFont(yaziBasi);
        alan.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        return alan;
    }

    private JButton butonOlustur(String metin, Color renk) {
        JButton buton = new JButton(metin);
        buton.setFont(yaziKalin);
        buton.setBackground(renk);
        buton.setForeground(beyaz);
        buton.setFocusPainted(false);
        buton.setOpaque(true);
        buton.setBorderPainted(false);
        buton.setBorder(BorderFactory.createEmptyBorder(7, 15, 7, 15));
        buton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return buton;
    }

    private void tabloGuncelle(OzelKisiDizisi liste) {
        tabloModeli.setRowCount(0);
        for (int i = 0; i < liste.getElemanSayisi(); i++) {
            Kisi kisi = liste.getKisi(i);
            Object[] satir = { kisi.getAd(), kisi.getSoyad(), kisi.getTelefon() };
            tabloModeli.addRow(satir);
        }
    }

    private void dosyaKaydet() {
        try (BufferedWriter yazan = new BufferedWriter(new FileWriter(dosyaAdi))) {
            for (int i = 0; i < rehber.getElemanSayisi(); i++) {
                Kisi kisi = rehber.getKisi(i);
                yazan.write(kisi.getAd() + "," + kisi.getSoyad() + "," + kisi.getTelefon());
                yazan.newLine();
            }
        } catch (IOException e) {
            System.out.println("Dosya yazılamadı: " + e.getMessage());
        }
    }

    private void dosyaOku() {
        File dosya = new File(dosyaAdi);
        if (!dosya.exists())
            return;
        try (BufferedReader okuyan = new BufferedReader(new FileReader(dosya))) {
            String satir;
            while ((satir = okuyan.readLine()) != null) {
                String[] parcalar = satir.split(",");
                if (parcalar.length == 3)
                    rehber.ekle(new Kisi(parcalar[0], parcalar[1], parcalar[2]));
            }
        } catch (IOException e) {
            System.out.println("Dosya okunamadı: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        UIManager.put("OptionPane.okButtonText", "Tamam");
        new TelefonRehberiGUI();
    }
}
