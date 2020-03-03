/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datalogging;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author George.Giazitzis
 */
public class Logger {

    ArrayList<String> log;
    Scanner sc;

    public Logger() {
        sc = new Scanner(System.in);
        log = new ArrayList();
    }

    public void println(String text) {
        System.out.println(text);
        log.add(text);
    }

    public String nextLine() {
        String text = sc.nextLine();
        log.add(text);
        return text;
    }

    public String[] getLog() {
        return log.toArray(new String[log.size()]);
    }
}
