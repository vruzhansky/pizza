package org.blotter;

import com.google.common.base.Stopwatch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PizzaSlicer {

    public static void main(String[] args) throws Exception {

        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        String filename;
        if (0 < args.length) {
            filename = args[0];
        } else {
            filename = getFilenameFromSystemIn();
        }

        Pizza pizza = readFileToPizza(Paths.get("data/" + filename).toAbsolutePath().toFile());
//        System.out.println(pizza);


        Stopwatch stopwatch = Stopwatch.createStarted();

        List<Slice> solution = new ArrayList<>();

        Set<PointOnPizza> allStartingPoints = new LinkedHashSet<>(pizza.freeSpots());
        List<PointOnPizza> currentStartingPoints = new ArrayList<>(allStartingPoints);
        while (!currentStartingPoints.isEmpty()) {
            PointOnPizza startingPoint = currentStartingPoints.get(0);
            currentStartingPoints = currentStartingPoints.subList(1, currentStartingPoints.size());
            Slice possibleSlice = new Slice(startingPoint, startingPoint);
            while (!pizza.isNiceSlice(possibleSlice) && pizza.isNotTooBig(possibleSlice)) {
                Optional<Slice> biggerSlice = tryToIncreaseSliceRight(possibleSlice, pizza);
                if (biggerSlice.isPresent()) {
                    possibleSlice = biggerSlice.get();
                } else {
                    possibleSlice = new Slice(startingPoint, startingPoint.withRow(possibleSlice.bottomRight().row()));
                    biggerSlice = tryToIncreaseSliceBottom(possibleSlice, pizza);
                    if (biggerSlice.isPresent()) {
                        possibleSlice = biggerSlice.get();
                    } else {
                        //cannot do more with this starting point
                        break;
                    }
                }
            }
            if (pizza.isNiceSlice(possibleSlice)) {
//                System.out.println("Slicing away " + possibleSlice);
//                System.out.println(pizza.printSlice(possibleSlice));
                pizza.sliceAway(possibleSlice);
                solution.add(possibleSlice);
                for (int row = possibleSlice.topLeft().row(); row <= possibleSlice.bottomRight().row(); row++) {
                    for (int col = possibleSlice.topLeft().col(); col <= possibleSlice.bottomRight().col(); col++) {
                        allStartingPoints.remove(new PointOnPizza(row, col));
                    }
                }
//                currentStartingPoints = new ArrayList<>(allStartingPoints);
            }

        }


        System.out.println("SOLUTION");
        System.out.println("SCORE = " + solution.stream().mapToInt(Slice::size).sum() + "/" + pizza.size());
//        solution.forEach(System.out::println);

        solution = solution.stream().map(slice -> tryToExpandSlice(slice, pizza)).collect(Collectors.toList());

        System.out.println("SOLUTION AFTER EXPANDING");
        System.out.println("SCORE = " + solution.stream().mapToInt(Slice::size).sum() + "/" + pizza.size());
//        solution.forEach(System.out::println);

        System.out.println("TIME = "+ stopwatch.elapsed(TimeUnit.SECONDS)+ "s");
    }

    private static Slice tryToExpandSlice(Slice originalSlice, Pizza pizza) {
        //try right
        boolean wentRight;
        do {
            Slice newPart = new Slice(originalSlice.topLeft().withCol(originalSlice.bottomRight().col() + 1), originalSlice.bottomRight().nextOnRight());
            Slice merged = new Slice(originalSlice.topLeft(), newPart.bottomRight());
            if (pizza.isInsidePizza(newPart) && pizza.isNotSlicedAway(newPart) && pizza.isNiceSliceOverlaping(merged)) {
                originalSlice = merged;
                wentRight = true;
            } else {
                wentRight = false;
            }
        } while (wentRight);

        //try bottom
        boolean wentBottom;
        do {
            Slice newPart = new Slice(originalSlice.topLeft().withRow(originalSlice.bottomRight().row() + 1), originalSlice.bottomRight().nextUnder());
            Slice merged = new Slice(originalSlice.topLeft(), newPart.bottomRight());
            if (pizza.isInsidePizza(newPart) && pizza.isNotSlicedAway(newPart) && pizza.isNiceSliceOverlaping(merged)) {
                originalSlice = merged;
                wentBottom = true;
            } else {
                wentBottom = false;
            }
        } while (wentBottom);

        return originalSlice;
    }

    private static Optional<Slice> tryToIncreaseSliceRight(Slice possibleSlice, Pizza pizza) {
        return Optional.of(possibleSlice.withIncreasedRight()).filter(pizza::isInsidePizza);
    }

    private static Optional<Slice> tryToIncreaseSliceBottom(Slice possibleSlice, Pizza pizza) {
        return Optional.of(possibleSlice.withIncreasedBottom()).filter(pizza::isInsidePizza);
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
            String[] s = br.readLine().split("\\s+");
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
