import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DBHelper {

    final static String url = "jdbc:sqlite:/Users/inna/Documents/IT/SWENG_Campus02/DB_Programmierung/Pruefung_DB1/SportsCompetition.db";
    private Connection connection;

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(url);
            // language=SQLite
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void closeDatabaseConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }

    }

    public void displaySportler() {
        try {
            // language=SQLite
            String query = "SELECT spId, Vorname, Nachname, Geburtsjahr, Land, SportartId from Sportler";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next())
                System.out.printf("%d, Vorname: %s, Nachname: %s, Geburtsjahr: %d, Land: %s, %d %n",
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getInt(4),
                        rs.getString(5),
                        rs.getInt(6)
                );
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTableWettbewerbe() {
        // String sql = "CREATE TABLE IF NOT EXISTS Wettbewerbe ("
        // language=SQLite
        String sql = "CREATE TABLE Wettbewerbe ("
                + "WettId INTEGER PRIMARY KEY, "
                + "SpId INTEGER NOT NULL, "
                + "SportartId INTEGER NOT NULL, "
                + "Name TEXT NOT NULL, "
                + "Datum TEXT NOT NULL, "
                + "Platzierung INTEGER CHECK(Platzierung >= 1), "
                + "FOREIGN KEY (SpId) REFERENCES Sportler(SpId) "
                + "   ON DELETE CASCADE ON UPDATE CASCADE, "
                + "FOREIGN KEY (SportartId) REFERENCES Sportarten(SportartId) "
                + "   ON DELETE CASCADE ON UPDATE CASCADE"
                + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabelle 'Wettbewerbe' erfolgreich erstellt.");
        } catch (SQLException e) {
            if (e.getMessage().contains("already exists")) {
                System.out.println("Tabelle 'Wettbewerbe' existiert bereits!");
            } else {
                System.out.println("Fehler beim Erstellen der Tabelle Wettbewerbe: " + e.getMessage());
            }
        }
    }

    public Sportler getSportler(int spId) {
        Sportler sportler = null;
        // language=SQLite
        String sql = "SELECT s.spId, s.Vorname, s.Nachname, s.Geburtsjahr, s.Land,  sa.SportartId, sa.Code, sa.Bezeichnung " +
                " from Sportler as s JOIN Sportarten as sa ON s.SportartId = sa.SportartId WHERE spId = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, spId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("spId");
                String vorname = rs.getString("Vorname");
                String nachname = rs.getString("Nachname");
                int jahr = rs.getInt("Geburtsjahr");
                String landE = rs.getString("Land");
                Land land = Land.valueOf(landE);

                int saId = rs.getInt("SportartId");
                String code = rs.getString("Code");
                String bez = rs.getString("Bezeichnung");
                Sportart sportart = new Sportart(saId, code, bez);

                sportler = new Sportler(id, vorname, nachname, jahr, land, sportart);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return sportler;
    }

    public Sportart addSportart(Sportart sportart) {
        Sportart added = null;
        // language=SQLite
        String sql = "INSERT INTO Sportarten (Code, Bezeichnung)  VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sportart.getCode());
            ps.setString(2, sportart.getBezeichnung());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        sportart.setSaId(newId);
                        System.out.println("Neue Sportart " + sportart.getBezeichnung() + " hinzugefügt mit ID = " + newId);
                        added = new Sportart(newId, sportart.getCode(), sportart.getBezeichnung());

                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return added;
    }

    public void deleteSportart(String bez) {
        // language=SQLite
        String sql = "DELETE FROM Sportarten WHERE Bezeichnung = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, bez);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Sportart mit Bezeichnung " + bez + " wurde gelöscht.");
            } else {
                System.out.println("Kein Sportart mit Bezeichnung " + bez + " gefunden.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen des Sportlers: " + e.getMessage());
        }
    }

    public void addSportler(Sportler sportler) {

        // language=SQLite
        String sql = "INSERT INTO Sportler (Vorname, Nachname, Geburtsjahr, Land, SportartId)  VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, sportler.getVorname());
            ps.setString(2, sportler.getNachname());
            ps.setInt(3, sportler.getGeburtsjahr());
            ps.setString(4, sportler.getLand().name());
            if (sportler.getSportart() != null) {
                ps.setInt(5, sportler.getSportart().getSaId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            int rows = ps.executeUpdate();

            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        sportler.setSpId(newId);
                        System.out.println("Neuer Sportler hinzugefügt mit ID = " + newId);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void deleteSportler(int spId) {
        // language=SQLite
        String sql = "DELETE FROM Sportler WHERE spId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, spId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Sportler mit ID " + spId + " wurde gelöscht.");
            } else {
                System.out.println("Kein Sportler mit ID " + spId + " gefunden.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Löschen des Sportlers: " + e.getMessage());
        }
    }

    public void updateWettbewerb(int wId, String titel, String date) {
        // language=SQLite
        String sql = "UPDATE Wettbewerbe SET Name = ?, Datum = ? "
                + "WHERE WettId = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, titel);
            ps.setString(2, date);
            ps.setInt(3, wId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("Wettbewerb mit ID " + wId + " wurde aktualisiert.");
            } else {
                System.out.println("Kein Wettbewerb mit ID " + wId + " gefunden.");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Aktualisieren des Wettbewerbs: " + e.getMessage());
        }
    }

    public ArrayList<Sportler> getAllSportlerOrderedBySportart() {
        ArrayList<Sportler> allSportler = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT s.spId, s.Vorname, s.Nachname, s.Geburtsjahr, s.Land, sa.SportartId, sa.Code, sa.Bezeichnung " +
                "FROM Sportler s LEFT JOIN Sportarten sa ON s.SportartId = sa.SportartId " +
                "ORDER BY sa.Bezeichnung";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Sportler sportler = new Sportler();
                int id = rs.getInt("spId");
                String vorname = rs.getString("Vorname");
                String nachname = rs.getString("Nachname");
                int jahr = rs.getInt("Geburtsjahr");
                String landE = rs.getString("Land");
                Land land = Land.valueOf(landE);

                int saId = rs.getInt("SportartId");
                String code = rs.getString("Code");
                String bez = rs.getString("Bezeichnung");
                Sportart sportart = new Sportart(saId, code, bez);

                sportler = new Sportler(id, vorname, nachname, jahr, land, sportart);
                allSportler.add(sportler);
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return allSportler;
    }

    /* Diese Methode gibt das Map von der Sportler by Land

     */
    public Map<String, Integer> getSportlerCountByLand() {
        Map<String, Integer> result = new HashMap<>();
        // language=SQLite
        String sql = "SELECT Land, COUNT(*) AS Anzahl FROM Sportler GROUP BY Land";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.put(rs.getString("Land"), rs.getInt("Anzahl"));
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei getSportlerCountByLand: " + e.getMessage());
        }
        return result;
    }

    /* Diese Methode gibt die Anzahl der Sportler aus ausgewählten Land zurück

     */
    public int getCountForCountry(Map<String, Integer> map, String land) {
        return map.getOrDefault(land, 0);
    }

    public String getSportlerMitMeistenSiegen() {
        Map<Integer, Integer> sumById = new HashMap<>();
        Map<Integer, String> nameById = new HashMap<>();
        // language=SQLite
        String sql = "SELECT s.Vorname, s.Nachname, sa.Bezeichnung AS Sportart,  COUNT(*) AS Siege " +
                "FROM Wettbewerbe as w " +
                "JOIN Sportler as s ON w.SpId = s.SpId " +
                "JOIN Sportarten as sa ON sa.SportartId = s.SportartId " +
                "WHERE w.Platzierung = 1 " +
                "GROUP BY s.SpId " +
                "ORDER BY Siege DESC ";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("Name") + " mit " + rs.getInt("Siege") + " Siegen";
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei getSportlerMitMeistenSiegen: " + e.getMessage());
        }
        return "Kein Sieger gefunden.";

    }

    /**
     * Ermittelt die Sportler mit den meisten Siegen (Platzierung = 1).
     * Die Methode führt eine SQL-Abfrage auf der Tabelle Wettbewerbe aus,
     * zählt die Anzahl der Siege pro Sportler und sortiert nach der höchsten Anzahl.
     * Falls mehrere Sportler die gleiche Maximalanzahl an Siegen haben,
     * werden alle zurückgegeben
     */
    public ArrayList<String> getSportlerListMitMeistenSiegen() {
        ArrayList<String> sieger = new ArrayList<>();
        // language=SQLite
        String sql = "SELECT s.Vorname, s.Nachname, sa.Bezeichnung AS Sportart,  COUNT(*) AS Siege " +
                "FROM Wettbewerbe as w " +
                "JOIN Sportler as s ON w.SpId = s.SpId " +
                "JOIN Sportarten as sa ON sa.SportartId = s.SportartId " +
                "WHERE w.Platzierung = 1 " +
                "GROUP BY s.SpId " +
                "ORDER BY Siege DESC ";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            int maxSiege = -1;

            while (rs.next()) {
                int siege = rs.getInt("Siege");
                String name = rs.getString("Vorname");
                String nachname = rs.getString("Nachname");
                String sportart = rs.getString("Sportart");

                if (maxSiege == -1) {
                    maxSiege = siege;
                    sieger.add(name + " " + nachname + " (" + sportart + " mit " + siege + " Siegen)");
                } else if (siege == maxSiege) {
                    sieger.add(name + " " + nachname + " (" + sportart + ") mit " + siege + " Siegen)");
                } else {
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei getSportlerMitMeistenSiegen: " + e.getMessage());
        }
        return sieger;
    }

    public void printSportlerListMitMeistenSiegen(ArrayList<String> sieger) {
        if (sieger.isEmpty()) {
            System.out.println("Kein Sieger gefunden.");
        } else {
            for (String s : sieger) {
                System.out.println("Die meisten Siege hat " + s);
            }
        }
    }

    public void showWettbewerbByNameAndDatum(String name, String datum) {
        // language=SQLite
        String sql = "SELECT s.Vorname, s.Nachname, sa.Bezeichnung as Sportart, " +
                "w.Name, w.Datum, w.Platzierung " +
                "FROM Wettbewerbe as w JOIN Sportler as s on s.spId = w.SpId " +
                "JOIN Sportarten as sa ON sa.SportartId = w.SportartId " +
                "WHERE w.Name = ? AND w.Datum = ? " +
                "ORDER BY w.Platzierung ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, datum);
            ResultSet rs = ps.executeQuery();

            System.out.println("Liste der Sieger in den Wettbewerben " + name + " am " + datum + ": ");
            while (rs.next()) {
                String vorname = rs.getString("Vorname");
                String nachname = rs.getString("Nachname");
                String sportart = rs.getString("Sportart");
                String wettName = rs.getString("Name");
                String date = rs.getString("Datum");
                int platzierung = rs.getInt("Platzierung");

                System.out.println(vorname + " " + nachname + " hat " + platzierung + " Platz");
            }
        } catch (SQLException e) {
            System.out.println("Fehler bei showWettbewerbByNameAndDatum: " + e.getMessage());
        }
    }

//    public void changePlatzierungTransaction(int spIdVon, int spIdZu, int platzierung) {
//        int rowsAffected = 0;
//        try {
//            connection.setAutoCommit(false);
//        }
//        catch(SQLException ex){
//            System.out.println(ex.getMessage());
//        }
//
//        // language=SQLite
//        String updateSql = "UPDATE Wettbewerbe " +
//                "SET Platzierung = Wettbewerbe.Platzierung - ?" +
//                "WHERE SpId = ?;";
//        int minusErfolg = 0;
//        int plusErfolg = 0;
//        try (PreparedStatement pStmt = connection.prepareStatement(updateSql)) {
//            pStmt.setInt(1,platzierung);
//            pStmt.setInt(2,spIdVon);
//            abbuchErfolgreich = pStmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Fehler beim Ändern eines Mitarbeitendn: " + e.getMessage());
//        }
//
//        String updateGutbuchenSql = "UPDATE Mitarbeitende\n" +
//                "SET MitarbeiterBonuspunkte = MitarbeiterBonuspunkte + ?" +
//                "WHERE MAId = ?;";
//
//        try (PreparedStatement pStmt = conn.prepareStatement(updateAbbuchenSql)) {
//            pStmt.setInt(1,bonuspunkte);
//            pStmt.setInt(2,mitIdZu);
//            gutBuchenErfolgreich = pStmt.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Fehler beim Ändern eines Mitarbeitendn: " + e.getMessage());
//        }
//
//        try
//        {
//            if (abbuchErfolgreich==1 && gutBuchenErfolgreich==1){
//                conn.commit();
//            } else {
//                conn.rollback();
//            }
//        } catch (SQLException e) {
//            System.out.println("Fehler beim Ändern eines Mitarbeitendn: " + e.getMessage());
//        }
//        try {
//            conn.setAutoCommit(true);
//        }
//        catch(SQLException ex){
//
//        }
//
//    }


}
