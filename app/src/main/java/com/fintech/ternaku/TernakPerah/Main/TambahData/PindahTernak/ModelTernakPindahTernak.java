package com.fintech.ternaku.TernakPerah.Main.TambahData.PindahTernak;

/**
 * Created by Pandhu on 10/4/16.
 */

public class ModelTernakPindahTernak {
        private String id_ternak;
        private String rfid;

        private String nama_ternak;
        private float berat_badan;
        private int hari_produksi_susu;
        private String status_kesuburan;
        private String diagnosis;
        private String tgl_subur;
        private String tgl_lahir;
        private String aktivitas;
        private String jenis;
        private String breed;
        private String kawanan;
        private String id_kawanan;
        private String kandang;
        private String id_kandang;
        private float max_susu;
        private float min_susu;
        private float avg_susu;
        private float total_susu;
        private float jml_susu;

        public ModelTernakPindahTernak() {

        }
        public ModelTernakPindahTernak(String id_ternak, String nama_ternak, float berat_badan, int hari_produksi_susu, String status_kesuburan, String diagnosis, String tgl_subur, String tgl_lahir, String aktivitas, String jenis, String breed, float total_susu, float jml_susu, String kawanan, String kandang, String id_kawanan, String id_kandang, String rfid) {
            this.setId_ternak(id_ternak);
            this.setNama_ternak(nama_ternak);
            this.setBerat_badan(berat_badan);
            this.setProduksi_susu(hari_produksi_susu);
            this.setStatus_kesuburan(status_kesuburan);
            this.setDiagnosis(diagnosis);
            this.setTgl_subur(tgl_subur);
            this.setTgl_lahir(tgl_lahir);
            this.setAktivitas(aktivitas);
            this.setMax_susu(getMax_susu());
            this.setMin_susu(getMin_susu());
            this.setAvg_susu(getAvg_susu());
            this.setTotal_susu(total_susu);
            this.setJml_susu(jml_susu);
            this.setJenis(jenis);
            this.setBreed(breed);
            this.setKandang(kandang);
            this.setKawanan(kawanan);
            this.setId_kawanan(kawanan);
            this.setId_kandang(id_kandang);
            this.setRfid(rfid);
        }

        public String getId_ternak() {
            return id_ternak;
        }

        public void setId_ternak(String id_ternak) {
            this.id_ternak = id_ternak;
        }

        public String getNama_ternak() {
            return nama_ternak;
        }

        public void setNama_ternak(String nama_ternak) {
            this.nama_ternak = nama_ternak;
        }

        public float getBerat_badan() {
            return berat_badan;
        }

        public void setBerat_badan(float berat_badan) {
            this.berat_badan = berat_badan;
        }

        public int getProduksi_susu() {
            return hari_produksi_susu;
        }

        public void setProduksi_susu(int hari_produksi_susu) {
            this.hari_produksi_susu = hari_produksi_susu;
        }

        public String getStatus_kesuburan() {
            return status_kesuburan;
        }

        public void setStatus_kesuburan(String status_kesuburan) {
            this.status_kesuburan = status_kesuburan;
        }

        public String getDiagnosis() {
            return diagnosis;
        }

        public void setDiagnosis(String diagnosis) {
            this.diagnosis = diagnosis;
        }

        public String getTgl_subur() {
            return tgl_subur;
        }

        public void setTgl_subur(String tgl_subur) {
            this.tgl_subur = tgl_subur;
        }

        public String getTgl_lahir() {
            return tgl_lahir;
        }

        public void setTgl_lahir(String tgl_lahir) {
            this.tgl_lahir = tgl_lahir;
        }

        public String getAktivitas() {
            return aktivitas;
        }

        public void setAktivitas(String aktivitas) {
            this.aktivitas = aktivitas;
        }

        public float getMax_susu() {
            return max_susu;
        }

        public void setMax_susu(float max_susu) {
            this.max_susu = max_susu;
        }

        public float getMin_susu() {
            return min_susu;
        }

        public void setMin_susu(float min_susu) {
            this.min_susu = min_susu;
        }

        public float getAvg_susu() {
            return avg_susu;
        }

        public void setAvg_susu(float avg_susu) {
            this.avg_susu = avg_susu;
        }

        public float getTotal_susu() {
            return total_susu;
        }

        public void setTotal_susu(float total_susu) {
            this.total_susu = total_susu;
        }

        public float getJml_susu() {
            return jml_susu;
        }

        public void setJml_susu(float jml_susu) {
            this.jml_susu = jml_susu;
        }

        public String getJenis() {
            return jenis;
        }

        public void setJenis(String jenis) {
            this.jenis = jenis;
        }

        public String getBreed() {
            return breed;
        }

        public void setBreed(String breed) {
            this.breed = breed;
        }

        public String getKawanan() {
            return kawanan;
        }

        public void setKawanan(String kawanan) {
            this.kawanan = kawanan;
        }

        public String getKandang() {
            return kandang;
        }

        public void setKandang(String kandang) {
            this.kandang = kandang;
        }

        public String getId_kawanan() {
            return id_kawanan;
        }

        public void setId_kawanan(String id_kawanan) {
            this.id_kawanan = id_kawanan;
        }

        public String getId_kandang() {
            return id_kandang;
        }

        public void setId_kandang(String id_kandang) {
            this.id_kandang = id_kandang;
        }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }
}
