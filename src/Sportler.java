public class Sportler {

    private int spId;
    private String vorname;
    private String nachname;
    private int geburtsjahr;
    private Land land;
    private Sportart sportart;

    public Sportler(int spId, String vorname, String nachname, int geburtsjahr, Land land, Sportart sportart) {
        this.spId = spId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsjahr = geburtsjahr;
        this.land = land;
        this.sportart = sportart;
    }

    public Sportler() {
    }

    public int getSpId() {
        return spId;
    }

    public void setSpId(int spId) {
        this.spId = spId;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public int getGeburtsjahr() {
        return geburtsjahr;
    }

    public void setGeburtsjahr(int geburtsjahr) {
        this.geburtsjahr = geburtsjahr;
    }

    public Land getLand() {
        return land;
    }

    public Sportart getSportart() {
        return sportart;
    }

    public void setSportart(Sportart sportart) {
        this.sportart = sportart;
    }

    @Override
    public String toString() {
        return "Sportler mit " +
                "spId=" + spId +
                " heisst " + vorname + " " + nachname +
                ", geburtsjahr=" + geburtsjahr +
                ", from " + land +
                " " + sportart;
    }
}
