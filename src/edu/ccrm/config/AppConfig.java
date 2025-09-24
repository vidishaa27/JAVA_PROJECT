package edu.ccrm.config;


public class AppConfig {
    private static final AppConfig INSTANCE = new AppConfig();

    private final String dataFolder = "data";
    private final int maxCredits = 24;

    private AppConfig() {}

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public String getDataFolder() {
        return dataFolder;
    }

    public int getMaxCredits() {
        return maxCredits;
    }
}
