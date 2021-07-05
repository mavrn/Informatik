import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;


public class PizzaKunde {

    //Statische Variablen
    static final String[][] produktNamen = {
            {"Salami", "Funghi", "Margharita", "Quattro Stagioni", "Speziale"},
            {"Wasser", "Cola", "Spezi", "Sprite", "Fanta"},
            {"Tomatensuppe", "Pizzabrot", "Pommes Frites", "Gemischter Salat", "Caprese"}
    }; //Namen der Produkte, angeordnet in 3 Kategorien(Pizzas, Getränke, Beilagen)
    static final double[][] produktPreise = {
            {8.99, 7.99, 6.99, 8.99, 9.99},
            {1.99, 2.50, 2.75, 2.25, 2.25},
            {3.99, 3.50, 2.99, 5.50, 4.99}
    }; //Preise der Produkte
    static String textEingabe = ""; //Platzhalter für eine Texteingabe
    static int zahlEingabe = -2; //Platzhalter für eine Zahleingabe
    static Scanner sc = new Scanner(System.in);
    static int kundenAnzahl = 0; //Gesamtanzahl der erstellten Kunden
    static int kundenNummer = 0; //Identifikationsnummer des bestellenden Kunden
    static DecimalFormat geldFormat = new DecimalFormat("#.##");

    static PizzaKunde[] PizzaKunden = new PizzaKunde[100]; //Liste von erstellten Kunden

    //Kundenabhängige Variablen
    double summe; //Summe(Geld) der Bestellung
    double geld;  //"Konto"stand des Kunden
    int[] favorite; //Favorit des Kunden in einer bestimmten Kategorie
    String name; //Name des Kunden
    boolean ordered; //Ist "true", wenn der Kunde etwas bestellt hat
    int[][] produktAnzahl = new int[3][5]; //Anzahl von bestellten Produkten, nach Produktart
    int[][] letzteBestellung = new int[3][5]; //Platzhalter für letzte Bestellung des Kunden
    double letzteSumme; //Platzhalter für letzte Summe des Kunden
    boolean istStammkunde; //Ist "true", wenn der Kunde schon einmal bestellt hat


    public PizzaKunde(String name, double summe, boolean ordered, double geld) {  //Konstruktor für das Objekt PizzaKunde
        this.name = name;
        this.summe = summe;
        this.ordered = ordered;
        this.geld = geld;
        this.favorite = new int[]{-1, -1, -1}; //Der Kunde hat bei Erstellung keine Favoriten!
        this.letzteSumme = 0.0;
        this.istStammkunde = false;
    }

    public static boolean istKunde(String s) {  //Test anhand des eingegeben Namen, ob der Kunde schon einmal da war
        for (int i = 0; i < kundenAnzahl; i++) {
            if (PizzaKunden[i].name.equals(s)) { //Wenn der Name registriert ist
                kundenNummer = i; //Die Nummer ist nun die ID des bestehenden Kunden
                return true;
            }
        }
        return false;
    }

    public static int intIn() { //Methode zur Eingabe von Zahlen
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception ignored) { //Abfangen von Ausnahmen; z.B. der Kunde gibt Buchstaben ein
                System.out.println("Bitte tätigen Sie eine gültige Eingabe.");
            }
        }
    }

    public static String runden(double input) {
        geldFormat.setRoundingMode(RoundingMode.HALF_UP);
        return geldFormat.format(input);
    }

    public static int[][] deepCopy(int[][] original) {  //Methode zur unabhängigen Kopie von Arrays, src: https://stackoverflow.com/questions/1564832/how-do-i-do-a-deep-copy-of-a-2d-array-in-java
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public static void reset(PizzaKunde kunde) { //Setzt bestellungsabhängige Variablen am Ende der Bestellung zurück; behält aber Namen, Favoriten etc.
        kunde.summe = 0.0;
        kunde.ordered = false;
        for (int kategorie = 0; kategorie < 3; kategorie++) {
            for (int produkt = 0; produkt < 5; produkt++)
                kunde.produktAnzahl[kategorie][produkt] = 0;
        }
    }

    public static void einzahlen(PizzaKunde kunde) { //Methode zum Einzahlen von Geld
        System.out.println("Möchten Sie Geld einzahlen? [1-ja/2-nein]");
        zahlEingabe = intIn();
        if (zahlEingabe == 1) {
            System.out.println("Wie viel Geld möchten Sie einzahlen?");
            kunde.geld += intIn();
            System.out.println("Einzahlung erfolgreich!");
        } else if (zahlEingabe != 2) {
            System.out.println("Bitte tätigen Sie eine gültige Eingabe.");
        } else {
            reset(kunde); //Zurücksetzen, da der Kunde die Bestellung nicht bezahlen kann
        }
    }

    public static void bestellen(PizzaKunde kunde, int kategorie, int produktWahl) { //Ändern der Summe und der produktAnzahl anhand der Bestellung, Methode wegen sechsfacher Wiederholung
        kunde.summe += produktPreise[kategorie][produktWahl];  //Erhöhe Summe der Bestellung
        kunde.produktAnzahl[kategorie][produktWahl]++;  //Erhöhe Anzahl des Produkts
        kunde.ordered = true;
    }

    public static void pizzaBestellung(PizzaKunde kunde) { //Bestellung einer Pizza

        if (kunde.favorite[0] >= 0) System.out.println("[0] Das Übliche"); //Dieser Teil wird nur ausgegeben, wenn der Kunde einen Pizza-Favoriten hat
        System.out.println("[1] Pizza Salami - 8,99€ \n[2] Pizza Funghi - 7,99€ \n[3] Pizza Margharita - 6,99€ \n[4] Pizza Quattro Stagioni - 8,99€\n[5] Pizza Speziale - 9,99€ \n[6] Auswahl abschließen\n");

        do {
            zahlEingabe = intIn() - 1; //-1 zur Korrektur der Eingabe auf den Index des Produkts

            if ((zahlEingabe>= 0 && zahlEingabe <5) || (kunde.favorite[0] != -1 && zahlEingabe == -1)) {
                if(zahlEingabe == -1) zahlEingabe=kunde.favorite[0]; //Setze die Eingabe auf den Favoriten

                System.out.println("Sie haben eine " + produktNamen[0][zahlEingabe] + " bestellt. Noch etwas?");
                bestellen(kunde, 0, zahlEingabe); //0 entspricht der Kategorie "Pizzas"
            }
            else if (zahlEingabe!=5) { //Wenn der Kunde ein Produkt auswählt
                System.out.println("Bitte geben Sie eine gültige Bestellung auf.");
                }
        }
        while (zahlEingabe != 5); //Wiederhole, bis "Auswahl abschließen" gewählt wird
        System.out.println("Alles klar, ist das alles?");
        zahlEingabe = -2;
    }

    public static void drinkBestellung(PizzaKunde kunde) { //Bestellung eines Getränks
        if (kunde.favorite[1] >= 0) System.out.println("[0] Das Übliche"); //Dieser Teil wird nur ausgegeben, wenn der Kunde einen Getränk-Favoriten hat
        System.out.println("[1] Wasser - 1,99€ \n[2] Cola - 2,50€ \n[3] Spezi - 2,75€ \n[4] Sprite - 2,25€\n[5] Fanta - 2,25€ \n[6] Auswahl abschließen\n");

        do {
            zahlEingabe = intIn() - 1; //-1 zur Korrektur der Eingabe auf den Index des Produkts

            if ((zahlEingabe>= 0 && zahlEingabe <5) || (kunde.favorite[1] != -1 && zahlEingabe == -1)) {
                if(zahlEingabe == -1) zahlEingabe=kunde.favorite[1]; //Setze die Eingabe auf den Favoriten

                if (zahlEingabe == 0) System.out.print("Sie haben ein ");
                else System.out.print("Sie haben eine ");
                System.out.println(produktNamen[1][zahlEingabe] + " bestellt. Noch etwas?");

                bestellen(kunde, 1, zahlEingabe);
            }
            else if (zahlEingabe!=5) { //Wenn der Kunde ein Produkt auswählt
                System.out.println("Bitte geben Sie eine gültige Bestellung auf.");
            }
        }
        while (zahlEingabe != 5); //Wiederhole, bis "Auswahl abschließen" gewählt wird
        System.out.println("Alles klar, ist das alles?");
        zahlEingabe = -2;
    }

    public static void beilageBestellung(PizzaKunde kunde) { //Bestellung einer Beilage
        if (kunde.favorite[2] >= 0) System.out.println("[0] Das Übliche"); //Dieser Teil wird nur ausgegeben, wenn der Kunde einen Beilagen-Favoriten hat
        System.out.println("[1] Tomatensuppe - 3,99€ \n[2] Pizzabrot - 2,50€ \n[3] Pommes Frites - 2,99€ \n[4] Gemischter Salat - 5,50€\n[5] Caprese - 4,99€ \n[6] Auswahl abschließen\n");

        do {
            zahlEingabe = intIn() - 1; //-1 zur Korrektur der Eingabe auf den Index des Produkts

            if ((zahlEingabe>= 0 && zahlEingabe <5) || (kunde.favorite[2] != -1 && zahlEingabe == -1)) {
                if(zahlEingabe == -1) zahlEingabe=kunde.favorite[1]; //Setze die Eingabe auf den Favoriten

                if (zahlEingabe == 0) System.out.print("Sie haben eine Tomatensuppe bestellt. Noch etwas? ");
                else if (zahlEingabe == 1) System.out.print("Sie haben ein Pizzabrot bestellt. Noch etwas?");
                else if (zahlEingabe == 2) System.out.println("Sie haben Pommes Frites bestellt. Noch etwas?");
                else if (zahlEingabe == 3) System.out.println("Sie haben einen gemischten Salat bestellt. Noch etwas?");
                else System.out.println("Sie haben einen Caprese bestellt. Noch etwas?");
                bestellen(kunde, 2, zahlEingabe);
            }
            else if (zahlEingabe!=5) { //Wenn der Kunde ein Produkt auswählt
                System.out.println("Bitte geben Sie eine gültige Bestellung auf.");
            }
        }
        while (zahlEingabe != 5); //Wiederhole, bis "Auswahl abschließen" gewählt wird
        System.out.println("Alles klar, ist das alles?");
        zahlEingabe = -2;
    }

    public static void support(PizzaKunde kunde) {
        System.out.println("Welche Produkte möchten Sie als Favorit festlegen?");
        do{
            System.out.println("[1] Pizzas\n[2] Getränke\n[3] Beilagen & Vorspeisen\n[4] Festlegen");
            zahlEingabe = intIn();
            switch (zahlEingabe) {
                case 1 -> {
                    System.out.println("Welche Pizza ist Ihr Favorit?");
                    System.out.println("[1] Pizza Salami\n[2] Pizza Funghi\n[3] Pizza Margharita\n[4] Pizza Quattro Stagioni\n[5] Pizza Speziale");
                    favoritFestlegen(kunde, 0); //Favorit festlegen für Kategorie 0 (Pizzen)
                }

                case 2 -> {
                    System.out.println("Welches Getränk ist Ihr Favorit?");
                    System.out.println("[1] Wasser\n[2] Cola\n[3] Spezi\n[4] Sprite\n[5] Fanta");
                    favoritFestlegen(kunde, 1); //Favorit festlegen für Kategorie 1 (Getränke)
                }

                case 3 -> {
                    System.out.println("Welche Beilage ist Ihr Favorit?");
                    System.out.println("[1] Tomatensuppe\n[2] Pizzabrot\n[3] Pommes Frites\n[4] Gemischter Salat\n[5] Calprese");
                    favoritFestlegen(kunde, 2); //Favorit festlegen für Kategorie 2 (Beilagen)
                }
                case 4 -> System.out.println("Okay! Das werden wir uns merken.");
                default -> System.out.println("Bitte tätigen Sie eine gültige Auswahl.");
            }

        }
        while (zahlEingabe != 4);
        zahlEingabe = -2; //Zurücksetzen des Platzhalters
    }

    public static void favoritFestlegen(PizzaKunde kunde, int kategorie) { //Methode wegen dreifacher Wiederholung
        zahlEingabe = intIn() - 1; //Korrigieren auf Index
        if (zahlEingabe >= 0 && zahlEingabe <= 4) {
            kunde.favorite[kategorie] = zahlEingabe; //Favorit setzen
            System.out.println("Alles klar!");
        } else System.out.println("Das hat nicht geklappt.");
        zahlEingabe = 0;
    }

    public static void neuerKunde() {
        System.out.println("Herzlich willkommen bei Pizzaria Alfredo!");
        System.out.println("Wie ist ihr Name?");
        textEingabe = sc.nextLine();
        if (istKunde(textEingabe)) System.out.println("Willkommen zurück, " + PizzaKunden[kundenNummer].name + "!");
        else { //Wenn der Benutzer ein neuer Kunde ist
            kundenNummer = kundenAnzahl;  //Setze die kundenNummer ans Ende des Arrays
            PizzaKunden[kundenNummer] = new PizzaKunde(textEingabe, 0.0, false, 0.0); //Erstellung des Kunden
            kundenAnzahl++;
            System.out.println("Willkommen " + PizzaKunden[kundenNummer].name + "!");
            System.out.println("Wie viel Geld haben Sie zur Verfügung?");
            PizzaKunden[kundenNummer].geld = intIn();
        }
    }

    public static void bestellungEnde(PizzaKunde kunde) {
        System.out.println("Vielen Dank für ihre Bestellung, ihre Auswahl ist:");
        for (int x = 0; x < 3; x++) { //Erstelle einen "Beleg" aus den bestellten Produkten
            for (int y = 0; y < 5; y++) {
                if (kunde.produktAnzahl[x][y] > 0) {
                    System.out.println(kunde.produktAnzahl[x][y] + "x " + produktNamen[x][y] + " - " + (produktPreise[x][y] * kunde.produktAnzahl[x][y]) + "€");
                }
            }
        }
        System.out.println("Ihre Summe beträgt " + runden(kunde.summe) + "€. Drücken Sie Enter zum Bezahlen.");
        try {
            sc.nextLine();
        } catch (Exception ignored) {
        }
        kunde.geld -= kunde.summe;
        kunde.letzteBestellung = deepCopy(kunde.produktAnzahl); //Lagern der Bestellung
        kunde.letzteSumme = kunde.summe; //Lagern der Summe
        reset(kunde);
        kunde.istStammkunde = true;
        System.out.println("Ihr neuer Kontostand ist " + runden(kunde.geld) + "€. Vielen Dank und bis zum nächsten Mal, " + kunde.name + "!");
    }
    public static void bestellVorgang (PizzaKunde kunde) {

        System.out.println("Was hätten Sie denn gern?");
        do{
            if (kunde.istStammkunde) System.out.println("[0] Das von letztem Mal"); //Wird nur ausgegeben, wenn der Kunde bereits einmal bestellt hat
            System.out.println("[1] Pizzas\n[2] Getränke\n[3] Beilagen/Vorspeisen\n[4] Favorit festlegen\n[5] Bezahlen\n[6] Restaurant verlassen");
            zahlEingabe = intIn();
            switch (zahlEingabe) {
                case 0 -> { //Wenn der Kunde seine letzte Bestellung wiederholen will
                    if (kunde.istStammkunde) {  //Und schon einmal da war (wenn nein, default)
                        kunde.summe = kunde.letzteSumme; //Setze Summe auf letzte Summe
                        kunde.produktAnzahl = deepCopy(kunde.letzteBestellung); //Und kopiere die letzte Bestellung unabhängig
                        System.out.println("Okay " + kunde.name + ", noch etwas dazu?"); //Kunde kann nun bezahlen oder etwas dazubestellen
                    }
                }
                //Bestellungen oder Favoriten
                case 1 -> pizzaBestellung(kunde);
                case 2 -> drinkBestellung(kunde);
                case 3 -> beilageBestellung(kunde);
                case 4 -> support(kunde);
                //Bezahlen
                case 5 -> {
                    if (kunde.geld < kunde.summe) { //Wenn der Kunde nicht genug Geld hat
                        System.out.println("Für diese Bestellung haben Sie leider nicht genug Geld zur Verfügung.");
                        einzahlen(kunde);  }
                    else if (kunde.ordered) { //Wenn der Kunde etwas bestellt hat
                        bestellungEnde(kunde); }
                    else { //Wenn der Kunde nichts bestellt hat
                        System.out.println("Wollten Sie nicht zuerst etwas bestellen?");
                        zahlEingabe=-2;  }
                }
                //Gehen
                case 6 -> {
                    System.out.println("Schade, vielleicht beim nächsten Mal!");
                    reset(kunde);
                }

                default -> System.out.println("Bitte wählen Sie eine gültige Option.");
            }
        }
        while (zahlEingabe != 5 && zahlEingabe != 6 && zahlEingabe != 0); //Solange bis der Kunde bezahlt oder geht

        System.out.println("Warten auf nächsten Kunden...");
        try {
            sc.nextLine(); //Warten auf Enter
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        while (kundenAnzahl < 100) { //Programm läuft, bis 100 verschiedene Kunden da waren
            neuerKunde();
            bestellVorgang(PizzaKunden[kundenNummer]);
        }
    }
}

