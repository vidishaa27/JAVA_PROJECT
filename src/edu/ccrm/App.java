package edu.ccrm;

import edu.ccrm.cli.MainMenu;
import edu.ccrm.config.AppConfig;

public class App {
    public static void main(String[] args) {
        AppConfig.getInstance();
        new MainMenu().start();
    }
}
