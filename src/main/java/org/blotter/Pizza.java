package org.blotter;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

public class Pizza {

    public static void main(String[] args) {
        String filename;
        if (0 < args.length) {
            filename = args[0];
        } else {
            filename = getFilenameFromSystemIn();
        }

        readFileByLine(Paths.get("data/" + filename).toAbsolutePath().toFile());
    }

    private static String getFilenameFromSystemIn() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a file name: ");
        System.out.flush();
        return scanner.nextLine();
    }

    private static void readFileByLine(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                System.out.println(scanner.next());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
