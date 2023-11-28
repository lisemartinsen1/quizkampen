package Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesClass {
    Properties properties = new Properties();

    public void loadProperties() {
        try {
            properties.load(new FileInputStream("src/Server/Admin.properties"));
        } catch (IOException e) {
            System.err.println("Failed to read from Admin.properties");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unknown error with Admin.properties");
            e.printStackTrace();
        }
    }
    public int getAmountOfRounds() {
        return Integer.parseInt(properties.getProperty("amountOfRounds", "2"));
    }
    public int getAmountOfQuestions() {
        return Integer.parseInt(properties.getProperty("amountOfQuestions", "2"));
    }

}
