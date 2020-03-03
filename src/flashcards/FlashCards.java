/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package flashcards;

import flashcardsapi.Game;

/**
 *
 * @author George.Giazitzis
 */
public class FlashCards {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        boolean isImport = false;
        String importPath = "";
        boolean isExport = false;
        String exportPath = "";
        for (int i = 0; i < args.length; i++) {
            if ("-import".equals(args[i]) && i + 1 < args.length) {
                isImport = true;
                importPath = args[i + 1];
            }
            if ("-export".equals(args[i]) && i + 1 < args.length) {
                isExport = true;
                exportPath = args[i + 1];
            }
        }
        Game game = new Game();
        if (isImport) {
            game.importCards(importPath);
        }
        game.menu();
        if (isExport) {
            game.exportCards(exportPath);
        }
    }

}
