public class Wettbewerbe {
    private int wettId;
    private int spId;
    private int saId;
    private String title;
    private String date;
    private int punkte;

    public Wettbewerbe(int wettId, int spId, int saId, String title, String date, int platzierung) {
        this.wettId = wettId;
        this.spId = spId;
        this.saId = saId;
        this.title = title;
        this.date = date;
        this.punkte = platzierung;
    }

    public int getWettId() {
        return wettId;
    }

    public void setWettId(int wettId) {
        this.wettId = wettId;
    }

    public int getSpId() {
        return spId;
    }

    public void setSpId(int spId) {
        this.spId = spId;
    }

    public int getSaId() {
        return saId;
    }

    public void setSaId(int saId) {
        this.saId = saId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPunkte() {
        return punkte;
    }

    public void setPunkte(int punkte) {
        this.punkte = punkte;
    }

    @Override
    public String toString() {
        return "Wettbewerbe{" +
                "wettId=" + wettId +
                ", spId=" + spId +
                ", saId=" + saId +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", platzierung=" + punkte +
                '}';
    }
}
