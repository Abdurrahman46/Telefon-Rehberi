package TelefonRehberiProje;

public class OzelKisiDizisi {
    private Kisi[] kisiler;
    private int elemanSayisi;

    public OzelKisiDizisi() {
        this.kisiler = new Kisi[5];
        this.elemanSayisi = 0;
    }

    public void ekle(Kisi yeniKisi) {
        if (elemanSayisi == kisiler.length) {
            kapasiteyiArtir();
        }
        kisiler[elemanSayisi] = yeniKisi;
        elemanSayisi++;
    }

    private void kapasiteyiArtir() {
        Kisi[] yeniDizi = new Kisi[kisiler.length * 2];
        for (int i = 0; i < kisiler.length; i++) {
            yeniDizi[i] = kisiler[i];
        }
        kisiler = yeniDizi;
    }

    public void kisiSil(String ad, String soyad, String telefon) {
        for (int i = 0; i < elemanSayisi; i++) {
            if (kisiler[i].getAd().equals(ad) &&
                    kisiler[i].getSoyad().equals(soyad) &&
                    kisiler[i].getTelefon().equals(telefon)) {

                for (int j = i; j < elemanSayisi - 1; j++) {
                    kisiler[j] = kisiler[j + 1];
                }

                kisiler[elemanSayisi - 1] = null;
                elemanSayisi--;
                break;
            }
        }
    }

    public boolean numaraVarMi(String telefon) {
        for (int i = 0; i < elemanSayisi; i++) {
            if (kisiler[i].getTelefon().equals(telefon)) {
                return true;
            }
        }
        return false;
    }

    public void kisiGuncelle(String eskiAd, String eskiSoyad, String eskiTel,
            String yeniAd, String yeniSoyad, String yeniTel) {
        for (int i = 0; i < elemanSayisi; i++) {

            if (kisiler[i].getAd().equals(eskiAd) &&
                    kisiler[i].getSoyad().equals(eskiSoyad) &&
                    kisiler[i].getTelefon().equals(eskiTel)) {

                kisiler[i].setAd(yeniAd);
                kisiler[i].setSoyad(yeniSoyad);
                kisiler[i].setTelefon(yeniTel);
                break;
            }
        }
    }

    public void ismeGoreSirala() {
        for (int i = 0; i < elemanSayisi - 1; i++) {
            for (int j = 0; j < elemanSayisi - i - 1; j++) {
                if (kisiler[j].getAd().compareToIgnoreCase(kisiler[j + 1].getAd()) > 0) {
                    Kisi gecici = kisiler[j];
                    kisiler[j] = kisiler[j + 1];
                    kisiler[j + 1] = gecici;
                }
            }
        }
    }

    public OzelKisiDizisi ismeGoreFiltrele(String arananKelime) {
        OzelKisiDizisi filtrelenmisListe = new OzelKisiDizisi();
        for (int i = 0; i < elemanSayisi; i++) {
            if (kisiler[i].getAd().toLowerCase().contains(arananKelime.toLowerCase())) {
                filtrelenmisListe.ekle(kisiler[i]);
            }
        }
        return filtrelenmisListe;
    }

    public Kisi getKisi(int index) {
        if (index >= 0 && index < elemanSayisi) {
            return kisiler[index];
        }
        return null;
    }

    public int getElemanSayisi() {
        return elemanSayisi;
    }
}