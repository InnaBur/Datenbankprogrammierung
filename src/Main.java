import java.util.ArrayList;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("Sportwettbewerbe Verwaltung!");

        DBHelper dbHelper = new DBHelper();

        //Aufgabe 1,2
        dbHelper.connectToDatabase();
        dbHelper.displaySportler();

        // Aufgabe 3 - Create Table in Java
        dbHelper.createTableWettbewerbe();

        System.out.println(dbHelper.getSportler(2));

        //Aufgabe 4 - insert
        Sportart tanzen = dbHelper.addSportart(new Sportart(-1, "Tz", "Tanzen"));
        dbHelper.addSportler(new Sportler(-1, "Zum", "Beispiel", 2025, Land.valueOf("AUSTRIA"), tanzen));
//        dbHelper.deleteSportler(11);
//        dbHelper.deleteSportart("Tanzen");

        // Aufgabe 5 - Constraints UNIQUE, FOREIGN_KEY)
        dbHelper.addSportart(new Sportart(-1, "RG", "Rhythmische Gymnastik"));
        dbHelper.addSportler(new Sportler(-1, "Zum", "Beispiel", 2025, Land.valueOf("AUSTRIA"), new Sportart(10, "A", "B")));

        //Aufgabe 4 - insert, delete, update
        dbHelper.updateWettbewerb(10, "Graz Cup3", "2025-07-07");

        //Aufgabe 6
        ArrayList<Sportler> allSportlers = dbHelper.getAllSportlerOrderedBySportart();
        for (Sportler s : allSportlers) {
            System.out.println(s);
        }

        //Aufgabe 6 und 7 - Map und SUM
        Map<String, Integer> sportlersByLand = dbHelper.getSportlerCountByLand();
        System.out.println("Anzahl der Sportler by Land: ");
        for (Map.Entry<String, Integer> entry : sportlersByLand.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        // Aufgabe 6
        System.out.println("Aus der Ukraine gibt es " +
                dbHelper.getCountForCountry(sportlersByLand, "UKRAINE") + " Sportler");

        ArrayList<String> sportlerSiegen = dbHelper.getSiegerSportlerList();
        dbHelper.printSportlerListMitMeistenSiegen(sportlerSiegen);

        //Aufgabe 7
        String date = "2025-06-10";
        String wettName = "Wien Drachenboot Cup";
        System.out.println();
        System.out.println("-----Durchschnittliche Punkte der Sportler im " + wettName + " am " + date + ": " + dbHelper.getDurchschnittlichePunkte(date));

        dbHelper.showWettbewerbByNameAndDatum("Wien Drachenboot Cup", "2025-06-10");

        //Aufgabe 8
//        System.out.println("Ups! Es ist ein Fehler passiert – die Schiedsrichter haben die Punkte neu gezählt! " +
//                "Die Siegerliste sieht jetzt so aus: ");
//        dbHelper.changePunkteTransaction(8, 5, wettName, date, 75);
//        dbHelper.showWettbewerbByNameAndDatum("Wien Drachenboot Cup", "2025-06-10");

        //Transaktion zurückgerollt, weil es keine 7 Sportler am diesen Datum gibt
        dbHelper.changePunkteTransaction(5, 7, wettName, date, 75);

        //Aufgabe 9
        dbHelper.printAllTables();
        dbHelper.infoWettbewerbeTable();

        dbHelper.closeDatabaseConnection();
    }


}