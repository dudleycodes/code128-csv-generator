import encoders.Code128;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Csv128Generator {
    public static void main(String[] args) {
        System.out.println("[Code 128 CSV generator]\n");

        String currentWorkingDirectory = System.getProperty("user.dir");
        System.out.printf("Current working directory is: %s\n", currentWorkingDirectory);

        File targetFile = new File(currentWorkingDirectory + File.separator + "128codes.csv");

        Scanner scanner = new Scanner(System.in);

        // Verify file is safe to overwrite
        if (targetFile.exists()) {
            while (true) {
                System.out.printf("File '%s' already exists. Do you want to overwrite it (default 'y')? y/n: ", targetFile.getName());
                String input = scanner.nextLine();

                if (input == null || input.trim().isBlank() || input.trim().equalsIgnoreCase("y")) {
                    break;
                }

                if (!input.trim().equalsIgnoreCase("n")) {
                    System.out.println("Exiting...");
                    System.exit(1);
                }

                break;
            }
        }

        // Get number of values to generate
        int numberOfValuesToGenerate;
        while (true) {
            System.out.print("How many entries do you want to generate (default '300'): ");
            String input = scanner.nextLine();

            if (input == null || input.isBlank()) {
                numberOfValuesToGenerate = 300;
                break;
            }

            try {
                numberOfValuesToGenerate = Integer.parseInt(input);
            } catch (NumberFormatException  e) {
                continue;
            }

            break;
        }

        // Prefix
        String prefix = null;
        while (true) {
            System.out.print("What prefix do you want to use? Up to four characters are allowed (default ''): ");
            String input = scanner.nextLine();

            if (input == null || input.trim().isBlank()) {
                break;
            }

            if (input.length() > 4) {
                continue;
            }

            prefix = input.toUpperCase();
            break;
        }

        // Length
        int valueLength = 8;
        while (true) {
            System.out.print("How many characters should each value be? Enter for default (8): ");
            String input = scanner.nextLine();

            if (input == null || input.trim().isBlank()) {
                break;
            }

            try {
                valueLength = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                continue;
            }

            break;
        }

        generateCSV(targetFile, numberOfValuesToGenerate, prefix, valueLength);
    }

    public static void generateCSV(File targetFile, int numberOfEntriesToGenerate, String prefix, int valueLength) {
        if (targetFile == null) {
            throw new IllegalArgumentException("Target file cannot be null.");
        }

        if (numberOfEntriesToGenerate < 1) {
            throw new IllegalArgumentException("Number of values must be greater than 0.");
        }

        if (valueLength < 1) {
            throw new IllegalArgumentException("Value length must be greater than 0.");
        }

        List<String> seen = new ArrayList<>(numberOfEntriesToGenerate);

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(targetFile), StandardCharsets.UTF_8)) {
            writer.append("\"Raw Value\",\"Font-Encoded Value\"");

            while (numberOfEntriesToGenerate > 0) {
                String rawValue = Generators.generateRandomHex(valueLength);
                rawValue = (prefix != null && !prefix.isBlank()) ? prefix.trim() + rawValue : rawValue;
                String encodedValue;

                try {
                    encodedValue = Escapers.csvQuotes(Code128.Encode(rawValue));

                } catch (Exception ex) {
                    System.err.printf("Failed to encode value '%s': %s\n", rawValue, ex.getMessage());
                    continue;
                }

                if (seen.contains(rawValue)) {
                    continue;
                }

                seen.add(rawValue);
                System.out.println("Generated: " + rawValue + " -> " + encodedValue);
                writer.append('\n');

                writer.append("\"").append(rawValue).append("\"").append(',');
                writer.append("\"").append(encodedValue).append("\"");

                numberOfEntriesToGenerate--;
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the CSV file.");
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("CSV file created successfully!");
        System.exit(0);
    }
}
