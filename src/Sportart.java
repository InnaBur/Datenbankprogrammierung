public class Sportart {

    private int saId;
    private String code;
    private String bezeichnung;

    public Sportart(int saId, String code, String bezeichnung) {
        this.saId = saId;
        this.code = code;
        this.bezeichnung = bezeichnung;
    }

    public int getSaId() {
        return saId;
    }

    public void setSaId(int saId) {
        this.saId = saId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public String toString() {
        return "Sportart: " +
                "code='" + code + '\'' +
                ", bezeichnung='" + bezeichnung + '\'';
    }
}
