import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class PizzaKunde {

    //Statische Variablen
    static String textEingabe; //Platzhalter für eine Texteingabe
    static int zahlEingabe; //Platzhalter für eine Zahleingabe
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
    static Scanner sc = new Scanner(System.in);
    static int kundenAnzahl = 0; //Gesamtanzahl der erstellten Kunden
    static int kundenNummer = 0; //Identifikationsnummer des bestellenden Kunden

    static PizzaKunde[] PizzaKunden = new PizzaKunde[100]; //Liste von erstellten Kunden

    //Kundenabhängige Variablen
    double summe; //Summe der bestellten Produkte eines Kunden
    double geld;  //"Konto"stand eines Kunden
    int[] favorite; //Favorit eines Kunden in einer bestimmten Kategorie
    String name; //Name eines Kunden
    boolean ordered; //Ist "true", wenn ein Kunde etwas bestellt hat
    int[][] produktAnzahl = new int[3][5]; //Anzahl von bestellten Produkten, nach Produktart
    int[][] letzeBestellung = new int[3][5]; //Platzhalter für letzte Bestellung eines Kunden
    boolean istStammkunde; //Ist "true", wenn ein Kunde schon einmal bestellt hat


    public PizzaKunde(String name, double summe, boolean ordered, double geld) {  //Konstruktor für das Objekt PizzaKunde
        this.name = name;
        this.summe = summe;
        this.ordered = ordered;
        this.geld = geld;
        this.favorite = new int[]{-1, -1, -1}; //Der Kunde hat bei Erstellung keine Favoriten!
    }

    public static boolean kundenTest(String s) {  //Test anhand des eingegeben Namen, ob der Kunde schon einmal da war
        for (int i = 0; i < kundenAnzahl; i++) {
            if (PizzaKunden[i].name.equals(s)) {
                kundenNummer = i;
                return true;
            }
        }
        return false;
    }

    public static int intIn() { //Methode zur Eingabe von Zahlen
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) { //Abfangen von Ausnahmen; z.B. der Kunde gibt Buchstaben ein
                System.out.println("Bitte tätigen Sie eine gültige Eingabe.");
            }
        }
    }

    public static int[][] deepCopy(int[][] original) {  //Methode zur unabhängigen Kopie von Arrays, src: https://stackoverflow.com/questions/1564832/how-do-i-do-a-deep-copy-of-a-2d-array-in-java
        if (original == null) {
            return null;
        }
        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    public static void zurücksetzen(PizzaKunde kunde) { //Setzt bestellungsabhängige Variablen am Ende der Bestellung zurück
        kunde.summe = 0;
        kunde.ordered = false;
        for (int k = 0; k < 3; k++) {
            for (int o = 0; o < 5; o++)
                kunde.produktAnzahl[k][o] = 0;
        }
    }

    public static void einzahlen(PizzaKunde kunde) { //Methode zum Einzahlen von Geld
        System.out.println("Möchten Sie Geld einzahlen? [1-ja/2-nein]");
        zahlEingabe = intIn();
        if (zahlEingabe == 1) {
            System.out.println("Wieviel Geld müchten Sie einzahlen?");
            kunde.geld += intIn();
            System.out.println("Einzahlung erfolgreich!");
        } else if (zahlEingabe != 2) {
            System.out.println("Bitte tätigen Sie eine gültige Eingabe.");
        } else {
            zurücksetzen(PizzaKunden[kundenNummer]); //Zurücksetzen, da der Kunde die Bestellung nicht bezahlen kann
        }
    }

    public static void bestellen(PizzaKunde kunde, int kategorie, int produktWahl) {
        if (produktWahl == -1) {
            kunde.summe += produktPreise[kategorie][kunde.favorite[0]];
            kunde.produktAnzahl[kategorie][kunde.favorite[0]]++;
        } else {
            kunde.summe += produktPreise[kategorie][produktWahl];
            kunde.produktAnzahl[kategorie][produktWahl]++;
        }
        kunde.ordered = true;
    }

    public static void pizzaBestellung(PizzaKunde kunde) { //Bestellung einer Pizza

        if (kunde.favorite[0] >= 0) {
            System.out.println("[0] Das Übliche");
        } //Dieser Teil wird nur ausgegeben, wenn der Kunde einen Pizza-Favoriten hat
        System.out.println("[1] Pizza Salami - 8,99€ \n[2] Pizza Funghi - 7,99€ \n[3] Pizza Margharita - 6,99€ \n[4] Pizza Quattro Stagioni - 8,99€\n[5] Pizza Speziale - 9,99€ \n[6] Auswahl abschließen\n");

        do {

            zahlEingabe = intIn() - 1; //-1 zur Korrektur der Eingabe auf den Index des Produkts

            if (zahlEingabe >= 0 && zahlEingabe < 5) { //Wenn der Kunde ein Produkt auswählt
                System.out.println("Sie haben eine Pizza " + produktNamen[0][zahlEingabe] + " bestellt. Noch etwas?");
                bestellen(kunde, 0, zahlEingabe);
            } else if (zahlEingabe == -1 && kunde.favorite[0] != -1) { //Wenn der Kunde seinen Favoriten wählt
                System.out.println("Okay " + kunde.name + ", eine Pizza " + produktNamen[0][kunde.favorite[0]] + " ist bestellt. Noch etwas?");
                bestellen(kunde, 0, zahlEingabe);
            } else if (zahlEingabe != 5) {
                System.out.println("Bitte geben Sie eine gültige Bestellung auf.");
            }
        }
        while (zahlEingabe != 5); //Wiederhole, bis "Auswahl abschließen" gewählt wird
        System.out.println("Alles klar, ist das alles?");
        zahlEingabe = -2;
    }

    public static void drinkBestellung(PizzaKunde kunde) { //Bestellung eines Getränks
        if (kunde.favorite[1] >= 0) {
            System.out.println("[0] Das Übliche");
        } //Dieser Teil wird nur ausgegeben, wenn der Kunde einen Getränk-Favoriten hat
        System.out.println("[1] Wasser - 1,99€ \n[2] Cola - 2,50€ \n[3] Spezi - 2,75€ \n[4] Sprite - 2,25€\n[5] Fanta - 2,25€ \n[6] Auswahl abschließen\n");

        do {

            zahlEingabe = intIn() - 1; //-1 zur Korrektur der Eingabe auf den Index des Produkts

            if (zahlEingabe >= 0 && zahlEingabe < 5) { //Wenn der Kunde ein Produkt auswählt
                if (zahlEingabe == 0) System.out.print("Sie haben ein ");
                else System.out.print("Sie haben eine ");
                System.out.println(produktNamen[1][zahlEingabe] + " bestellt. Noch etwas?");
                bestellen(kunde, 1, zahlEingabe);
            } else if (zahlEingabe == -1 && kunde.favorite[1] != -1) { //Wenn der Kunde seinen Favoriten wählt
                if (kunde.favorite[1] == 0) System.out.print("Okay " + kunde.name + ", ein ");
                else System.out.print("Okay " + kunde.name + ", eine ");
                System.out.println(produktNamen[1][kunde.favorite[1]] + " ist bestellt. Noch etwas?");
                bestellen(kunde, 1, zahlEingabe);
            } else if (zahlEingabe != 5) {
                System.out.println("Bitte geben Sie eine gültige Bestellung auf.");
            }
        }
        while (zahlEingabe != 5); //Wiederhole, bis "Auswahl abschließen" gewählt wird
        System.out.println("Alles klar, ist das alles?");
        zahlEingabe = -2;
    }

    public static void beilageBestellung(PizzaKunde kunde) { //Bestellung einer Beilage
        if (kunde.favorite[2] >= 0) {
            System.out.println("[0] Das Übliche");
        } //Dieser Teil wird nur ausgegeben, wenn der Kunde einen Beilagen-Favoriten hat
        System.out.println("[1] Tomatensuppe - 3,99€ \n[2] Pizzabrot - 2,50€ \n[3] Pommes Frites - 2,99€ \n[4] Gemischter Salat - 5,50€\n[5] Caprese - 4,99€ \n[6] Auswahl abschließen\n");

        do {

            zahlEingabe = intIn() - 1; //-1 zur Korrektur der Eingabe auf den Index des Produkts

            if (zahlEingabe >= 0 && zahlEingabe < 5) { //Wenn der Kunde ein Produkt auswählt
                if (zahlEingabe == 0) System.out.print("Sie haben eine Tomatensuppe bestellt. Noch etwas? ");
                else if (zahlEingabe == 1) System.out.print("Sie haben ein Pizzabrot bestellt. Noch etwas?");
                else if (zahlEingabe == 2) System.out.println("Sie haben Pommes Frites bestellt. Noch etwas?");
                else if (zahlEingabe == 3) System.out.println("Sie haben einen gemischten Salat bestellt. Noch etwas?");
                else System.out.println("Sie haben einen Caprese bestellt. Noch etwas?");
                bestellen(kunde, 1, zahlEingabe);
            } else if (zahlEingabe == -1 && kunde.favorite[0] != -1) //Wenn der Kunde seinen Favoriten wählt
            {
                if (kunde.favorite[2] == 0)
                    System.out.print("Okay " + kunde.name + ", eine Tomatensuppe ist bestellt. Noch etwas? ");
                else if (kunde.favorite[2] == 1)
                    System.out.print("Okay " + kunde.name + ", ein Pizzabrot ist bestellt. Noch etwas?");
                else if (kunde.favorite[2] == 2)
                    System.out.println("Okay " + kunde.name + ", Pommes Frites sind bestellt. Noch etwas?");
                else if (kunde.favorite[2] == 3)
                    System.out.println("Okay " + kunde.name + ", ein gemischter Salat ist bestellt. Noch etwas?");
                else System.out.println("Okay " + kunde.name + ", ein Caprese ist bestellt. Noch etwas?");
                bestellen(kunde, 1, zahlEingabe);
            } else if (zahlEingabe != 5) {
                System.out.println("Bitte geben Sie eine gültige Bestellung auf.");
            }
        }
        while (zahlEingabe != 5); //Wiederhole, bis "Auswahl abschließen" gewählt wird
        System.out.println("Alles klar, ist das alles?");
        zahlEingabe = -2;
    }

    public static void support(PizzaKunde kunde) {
        zahlEingabe = 0;
        System.out.println("Welche Produkte möchten Sie als Favorit festlegen?");
        System.out.println();
        while (zahlEingabe != 4) {
            System.out.println("[1] Pizzas\n[2] Getränke\n[3] Beilagen & Vorspeisen\n[4] Festlegen");
            zahlEingabe = intIn();
            switch (zahlEingabe) {
                case 1 -> {
                    System.out.println("Welche Pizza ist Ihr Favorit?");
                    System.out.println("[1] Pizza Salami\n[2] Pizza Funghi\n[3] Pizza Margharita\n[4] Pizza Quattro Stagioni\n[5] Pizza Speziale");
                    zahlEingabe = intIn() - 1;
                    if (zahlEingabe >= 0 && zahlEingabe <= 4) {
                        kunde.favorite[0] = zahlEingabe;
                        System.out.println("Alles klar!");
                    } else System.out.println("Das hat nicht geklappt.");
                    zahlEingabe = 0;
                }

                case 2 -> {
                    System.out.println("Welches Getränk ist Ihr Favorit?");
                    System.out.println("[1] Wasser\n[2] Cola\n[3] Spezi\n[4] Sprite\n[5] Fanta");
                    zahlEingabe = intIn() - 1;
                    if (zahlEingabe >= 0 && zahlEingabe <= 4) {
                        kunde.favorite[1] = zahlEingabe;
                        System.out.println("Alles klar!");
                    } else System.out.println("Das hat nicht geklappt.");
                    zahlEingabe = 0;
                }

                case 3 -> {
                    System.out.println("Welche Beilage ist Ihr Favorit?");
                    System.out.println("[1] Tomatensuppe\n[2] Pizzabrot\n[3] Pommes Frites\n[4] Gemischter Salat\n[5] Calprese");
                    zahlEingabe = intIn() - 1;
                    if (zahlEingabe >= 0 && zahlEingabe <= 4) {
                        kunde.favorite[2] = zahlEingabe;
                        System.out.println("Alles klar!");
                    } else System.out.println("Das hat nicht geklappt.");
                    zahlEingabe = 0;
                }

                case 4 -> System.out.println("Okay! Das werden wir uns merken.");
                default -> System.out.println("Bitte tätigen Sie eine gültige Auswahl.");
            }

        }
        zahlEingabe = -2;
    }

    public static void neuerKunde() {
        System.out.println("Herzlich willkommen bei Pizzaria X!");
        System.out.println("Ihr Name ist?");
        textEingabe = sc.nextLine();
        if (!kundenTest(textEingabe)) {
            kundenNummer = kundenAnzahl;
            PizzaKunden[kundenNummer] = new PizzaKunde(textEingabe, 0, false, 0);
            kundenAnzahl++;
            System.out.println("Willkommen " + PizzaKunden[kundenNummer].name + "!");
            System.out.println("Wie viel Geld haben Sie zur Verfügung?");
            PizzaKunden[kundenNummer].geld = intIn();
        } else System.out.println("Willkommen zurück, " + PizzaKunden[kundenNummer].name + "!");
    }

    public static void bestellungEnde(PizzaKunde kunde) {
        System.out.println("Vielen Dank für ihre Bestellung, ihre Auswahl ist:");
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 5; y++) {
                if (kunde.produktAnzahl[x][y] > 0) {
                    System.out.println(kunde.produktAnzahl[x][y] + "x " + produktNamen[x][y] + " - " + (produktPreise[x][y] * kunde.produktAnzahl[x][y]) + "€");
                }
            }

        }
        System.out.println("Ihre Summe beträgt " + kunde.summe + "€. Drücken Sie Enter zum Bezahlen.");
        try {
            System.in.read();
        } catch (Exception e) {
        }
        kunde.geld -= kunde.summe;
        kunde.letzeBestellung = deepCopy(kunde.produktAnzahl);
        zurücksetzen(kunde);
        kunde.istStammkunde = true;
        System.out.println("Ihr neuer Kontostand ist " + kunde.geld + ". Vielen Dank und bis zum nächsten Mal, " + kunde.name + "!");

    }

    public static void main(String[] args) {
        while (kundenAnzahl < 100) {

            neuerKunde();
            System.out.println("Was hätten Sie denn gern?");
            zahlEingabe = -2;
            while (zahlEingabe != 4 && zahlEingabe != 6 && zahlEingabe != 0) {
                if (PizzaKunden[kundenNummer].istStammkunde) System.out.println("[0] Das von letztem Mal");
                System.out.println("[1] Pizzas\n[2] Getränke\n[3] Beilagen/Vorspeisen\n[4] Bezahlen\n[5] Support\n[6] Restaurant verlassen");
                zahlEingabe = intIn();
                switch (zahlEingabe) {
                    case 0 -> {
                        if (PizzaKunden[kundenNummer].istStammkunde) {
                            PizzaKunden[kundenNummer].produktAnzahl = deepCopy(PizzaKunden[kundenNummer].letzeBestellung);
                            if (PizzaKunden[kundenNummer].geld < PizzaKunden[kundenNummer].summe) {
                                System.out.println("Für diese Bestellung haben Sie leider nicht genug Geld zur Verfügung.");
                                einzahlen(PizzaKunden[kundenNummer]);
                            } else {
                                System.out.println("Alles klar!");
                                bestellungEnde(PizzaKunden[kundenNummer]);
                            }
                        } else System.out.println("Bitte wählen Sie eine gültige Option.");
                    }
                    case 1 -> pizzaBestellung(PizzaKunden[kundenNummer]);
                    case 2 -> drinkBestellung(PizzaKunden[kundenNummer]);
                    case 3 -> beilageBestellung(PizzaKunden[kundenNummer]);
                    case 4 -> {
                        if (PizzaKunden[kundenNummer].geld < PizzaKunden[kundenNummer].summe) {
                            System.out.println("Für diese Bestellung haben Sie leider nicht genug Geld zur Verfügung.");
                            einzahlen(PizzaKunden[kundenNummer]);
                        } else if (PizzaKunden[kundenNummer].ordered) {
                            bestellungEnde(PizzaKunden[kundenNummer]);
                        } else System.out.println("Wollten Sie nicht zuerst etwas bestellen?");
                    }
                    case 5 -> support(PizzaKunden[kundenNummer]);
                    case 6 -> System.out.println("Schade, vielleicht beim nächsten Mal!");
                    default -> System.out.println("Bitte wählen Sie eine gültige Option.");
                }
            }
            try {
                System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

