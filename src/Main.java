import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("Sportwettbewerbe Verwaltung!");

        DBHelper dbHelper = new DBHelper();
        dbHelper.connectToDatabase();
//        dbHelper.displaySportler();

        //Aufgabe 3 - Create Table in Java
//        dbHelper.createTableWettbewerbe();

//        System.out.println(dbHelper.getSportler(2));

//        Sportart tanzen = dbHelper.addSportart(new Sportart(-1, "Tz", "Tanzen"));
//        dbHelper.addSportler(new Sportler(-1, "Zum", "Beispiel", 2025, Land.valueOf("AUSTRIA"), tanzen));
//        dbHelper.deleteSportler(11);
//        dbHelper.deleteSportart("Tanzen");

        //Aufgabe 5 - Constraints UNIQUE, FOREIGN_KEY)
//        dbHelper.addSportart(new Sportart(-1, "RG", "Rhythmische Gymnastik"));
//        dbHelper.addSportler(new Sportler(-1, "Zum", "Beispiel", 2025, Land.valueOf("AUSTRIA"), new Sportart(10, "A", "B")));

        //Aufgabe 4 - insert, delete, update
//                dbHelper.deleteSportler(11);
        //        dbHelper.updateWettbewerb(10, "Graz Cup", "2025-07-07");

        //Aufgabe 6
//        ArrayList<Sportler> allSportlers = dbHelper.getAllSportlerOrderedBySportart();
//        for (Sportler s: allSportlers) {
//            System.out.println(s);
//        }


        //Aufgabe 6 und 7 - Map und SUM
        Map<String, Integer> sportlersByLand = dbHelper.getSportlerCountByLand();

        for (Map.Entry<String, Integer> entry : sportlersByLand.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        //Aufgabe 6
        System.out.println("Aus der Ukraine gibt es " +
                dbHelper.getCountForCountry(sportlersByLand, "UKRAINE") + " Sportler");

        ArrayList<String> sportlerMitMeistenSiegen = dbHelper.getSportlerListMitMeistenSiegen();
        dbHelper.printSportlerListMitMeistenSiegen(sportlerMitMeistenSiegen);

        dbHelper.showWettbewerbByNameAndDatum("Wien Drachenboot Cup", "2025-06-10");

        System.out.println("Ups! Es ist ein Fehler passiert – die Schiedsrichter haben die Punkte neu gezählt! " +
                "Die Siegerliste sieht jetzt so aus:");

        dbHelper.showWettbewerbByNameAndDatum("Wien Drachenboot Cup", "2025-06-10");

        dbHelper.closeDatabaseConnection();
    }




}