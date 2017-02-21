package org.blotter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PizzaSlicer {

    public static void main(String[] args) throws Exception {
        String filename;
        if (0 < args.length) {
            filename = args[0];
        } else {
            filename = getFilenameFromSystemIn();
        }

        Pizza pizza = readFileToPizza(Paths.get("data/" + filename).toAbsolutePath().toFile());

        System.out.println(pizza);
    }

    private static String getFilenameFromSystemIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a file name: ");
        System.out.flush();
        return scanner.nextLine();
    }

    private static Pizza readFileToPizza(File file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String s1 = br.readLine();
            String[] s = s1.split("\\s+");
            Pizza pizza = new Pizza(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));

            int i = 0;
            while ((line = br.readLine()) != null) {
                List<Pizza.Ingredient> row = Arrays.stream(line.split(""))
                        .map(Pizza.Ingredient::valueOf).collect(Collectors.toList());

                pizza.ingredients[i++] = row.toArray(new Pizza.Ingredient[0]);
            }
            return pizza;
        }
    }
}
