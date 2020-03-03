/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcardsapi;

import datalogging.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author George.Giazitzis
 */
public class Game {

    private Map<String, String> deckOfCards;
    private Map<String, Integer> ratings;
    private Logger log;

    public Game() {
        deckOfCards = new LinkedHashMap();
        ratings = new LinkedHashMap();
        log = new Logger();
    }

    public void menu() {
        String action;
        do {
            log.println("Input the action (add, remove, import, export, ask, exit):");
            action = log.nextLine();
            switch (action) {
                case "add":
                    addCard();
                    break;
                case "remove":
                    removeCard();
                    break;
                case "import":
                    importCards("");
                    break;
                case "export":
                    exportCards("");
                    break;
                case "ask":
                    askCards();
                    break;
                case "log":
                    saveLog();
                    break;
                case "hardest card":
                    getHardestCard();
                    break;
                case "reset stats":
                    resetStats();
                    break;
                default:
                    log.println("Invalid command!");
            }
        } while (!"exit".equals(action));
        log.println("Bye bye!");
    }

    private void addCard() {
        log.println("The Card:");
        String card = log.nextLine();
        if (deckOfCards.containsKey(card)) {
            log.println("The card \"" + card + "\" already exists.");
        } else {
            log.println("The definition of the card");
            String definition = log.nextLine();
            if (deckOfCards.containsValue(definition)) {
                log.println("The definition \"" + definition + "\" already exists.");
            } else {
                deckOfCards.put(card, definition);
                log.println("The pair " + card + ":" + definition + " has been added.");
            }
        }
    }

    private void removeCard() {
        log.println("The Card:");
        String card = log.nextLine();
        if (deckOfCards.containsKey(card)) {
            deckOfCards.remove(card);
            ratings.remove(card);
            log.println("The card has been removed.");
        } else {
            log.println("Can't remove \"" + card + "\": there is no such card");
        }
    }

    public void importCards(String path) {
        if (path.isEmpty()) {
            log.println("File path:");
            path = log.nextLine();
        }
        int count = 0;
        try (Scanner sc = new Scanner(new File(path))) {
            while (sc.hasNext()) {
                count++;
                String card = sc.nextLine();
                String description = sc.nextLine();
                int cardRating = Integer.parseInt(sc.nextLine());
                deckOfCards.put(card, description);
                ratings.put(card, cardRating);
            }
            log.println(count + " cards have been loaded.");
        } catch (IOException e) {
            log.println("File not found");
        }
    }

    public void exportCards(String path) {
        if (path.isEmpty()) {
            log.println("File path:");
            path = log.nextLine();
        }
        try (PrintWriter printWriter = new PrintWriter(new File(path))) {
            for (Map.Entry<String, String> pair : deckOfCards.entrySet()) {
                printWriter.println(pair.getKey());
                printWriter.println(pair.getValue());
                if (ratings.containsKey(pair.getKey())) {
                    printWriter.println(ratings.get(pair.getKey()));
                } else {
                    printWriter.println(0);
                }
            }
            log.println(deckOfCards.size() + " cards have been saved.");
        } catch (FileNotFoundException e) {
            log.println("Invalid path directory " + e.getMessage());
        }
    }

    private void saveLog() {
        log.println("File name:");
        String path = log.nextLine();
        File file = new File(path);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String logLine : log.getLog()) {
                printWriter.println(logLine);
            }
            log.println("The log has been saved.");
        } catch (IOException e) {
            log.println("File not found");
        }
    }

    private void askCards() {
        Random r = new Random();
        log.println("How many times to ask?");
        int times = Integer.parseInt(log.nextLine());
        Object[] keys = deckOfCards.keySet().toArray();
        Object[] values = deckOfCards.values().toArray();
        while (times-- > 0) {
            int spot = r.nextInt(keys.length);
            String keyQuestion = keys[spot].toString();
            String valueQuestion = values[spot].toString();
            log.println("Print the definition of \"" + keyQuestion + "\"");
            String answer = log.nextLine();
            if (answer.equalsIgnoreCase(valueQuestion)) {
                log.println("Correct answer.");
            } else if (deckOfCards.containsValue(answer)) {
                log.println("Wrong answer. The correct one is \"" + valueQuestion + "\", you've just written the definition of \"" + returnValue(deckOfCards, answer) + "\".");
                addRating(keyQuestion);
            } else {
                log.println("Wrong answer. The correct one is \"" + valueQuestion + "\".");
                addRating(keyQuestion);
            }
        }
    }

    private void addRating(String card) {
        if (ratings.containsKey(card)) {
            ratings.replace(card, ratings.get(card) + 1);
        } else {
            ratings.put(card, 1);
        }
    }

    private void getHardestCard() {
        int max = 0;
        for (Integer value : ratings.values()) {
            if (value > max) {
                max = value;
            }
        }
        ArrayList<String> cards = new ArrayList<>();
        for (Map.Entry<String, Integer> pair : ratings.entrySet()) {
            if (pair.getValue().equals(max)) {
                cards.add(pair.getKey());
            }
        }
        if (cards.size() == 0) {
            log.println("There are no cards with errors.");
        } else if (cards.size() == 1) {
            log.println("The hardest card is \"" + cards.get(0) + "\". You have " + max + " errors answering it.");
        } else {
            log.println("The hardest cards are " + getCards(cards) + ". You have " + max + " errors answering them.");
        }
    }

    private String getCards(List<String> cards) {
        String s = "";
        for (int i = 0; i < cards.size() - 1; i++) {
            s += "\"" + cards.get(i) + "\", ";
        }
        s += "\"" + cards.get(cards.size() - 1) + "\"";
        return s;
    }

    private String returnValue(Map<String, String> map, String value) {
        for (String card : map.keySet()) {
            if (map.get(card).equalsIgnoreCase(value)) {
                return card;
            }
        }
        return "";
    }

    private void resetStats() {
        ratings.clear();
        log.println("Card statistics has been reset.");
    }
}
