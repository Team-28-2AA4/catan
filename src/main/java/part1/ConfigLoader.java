package part1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConfigLoader {

    public static int loadTurns(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.startsWith("turns:")) {
                    int turns = Integer.parseInt(line.split(":")[1].trim());

                    if (turns < 1 || turns > 8192) {
                        throw new IllegalArgumentException("Turns must be between 1 and 8192.");
                    }

                    return turns;
                }
            }

            // If we finished scanning and didn't find turns:
            throw new IllegalArgumentException("Missing 'turns:' in config file.");

        } catch (IllegalArgumentException e) {
            // Re throw validation errors
            throw e;

        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file not found: " + filename, e);

        } catch (Exception e) {
            throw new RuntimeException("Error reading config file: " + e.getMessage(), e);
        }
    }
}
