package org.blotter;

import com.google.common.util.concurrent.AtomicLongMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Pizza {


    enum Ingredient {T, M}

    final Ingredient[][] ingredients;
    final boolean[][] slicedAway;
    final int minIngredientsPerSlice;
    final int maxCellsPerSlice;
    private final int size;

    public Pizza(int rows, int cols, int minIngredientsPerSlice, int maxCellsPerSlice) {
        this.ingredients = new Ingredient[rows][cols];
        this.slicedAway = new boolean[rows][cols];
        this.minIngredientsPerSlice = minIngredientsPerSlice;
        this.maxCellsPerSlice = maxCellsPerSlice;
        this.size = rows * cols;
    }

    public List<PointOnPizza> freeSpots() {
        List<PointOnPizza> freeSpots = new ArrayList<>();
        for (int row = 0; row < slicedAway.length; row++) {
            for (int col = 0; col < slicedAway[row].length; col++) {
                if (!slicedAway(row, col)) {
                    freeSpots.add(new PointOnPizza(row, col));
                }
            }
        }
        return Collections.unmodifiableList(freeSpots);
    }

    public boolean isNiceSlice(Slice slice) {
        if (isNotSlicedAway(slice)) {
            if (isNotTooBig(slice)) {
                if (hasCorrectAmountOfIngredients(slice)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNiceSliceOverlaping(Slice slice) {
        if (isNotTooBig(slice)) {
            if (hasCorrectAmountOfIngredients(slice)) {
                return true;
            }
        }
        return false;
    }

    public void sliceAway(Slice slice) {
        for (int row = slice.topLeft().row(); row <= slice.bottomRight().row(); row++) {
            for (int col = slice.topLeft().col(); col <= slice.bottomRight().col(); col++) {
                slicedAway[row][col] = true;
            }
        }
    }

    private boolean hasCorrectAmountOfIngredients(Slice slice) {
        int m = 0;
        int t = 0;
        for (int row = slice.topLeft().row(); row <= slice.bottomRight().row(); row++) {
            Ingredient[] ingRow = ingredients[row];
            for (int col = slice.topLeft().col(); col <= slice.bottomRight().col(); col++) {
                if (ingRow[col] == Ingredient.M) {
                    ++m;
                } else {
                    ++t;
                }
            }
        }
        if (m < minIngredientsPerSlice || t < minIngredientsPerSlice) {
            return false;
        }
        return true;
    }

    public boolean isNotTooBig(Slice slice) {
        return slice.size() <= maxCellsPerSlice;
    }

    public boolean isInsidePizza(Slice slice) {
        int bottomRow = slice.bottomRight().row();
        return bottomRow < ingredients.length && slice.bottomRight().col() < ingredients[bottomRow].length;
    }

    public boolean isNotSlicedAway(Slice slice) {
        for (int row = slice.topLeft().row(); row <= slice.bottomRight().row(); row++) {
            boolean[] dataRow = slicedAway[row];
            for (int col = slice.topLeft().col(); col <= slice.bottomRight().col(); col++) {
                if (dataRow[col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String printSlice(Slice slice) {
        StringBuilder sb = new StringBuilder();
        for (int row = slice.topLeft().row(); row <= slice.bottomRight().row(); row++) {
            for (int col = slice.topLeft().col(); col <= slice.bottomRight().col(); col++) {
                sb.append(slicedAway(row, col) ? "X" : ingredientAt(row, col).name());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public Ingredient ingredientAt(int row, int col) {
        return ingredients[row][col];
    }

    public boolean slicedAway(PointOnPizza pointOnPizza) {
        return slicedAway[pointOnPizza.row()][pointOnPizza.col()];
    }

    public boolean slicedAway(int row, int col) {
        return slicedAway[row][col];
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "ingredients=" + Arrays.deepToString(ingredients) +
                ", slicedAway=" + Arrays.deepToString(slicedAway) +
                ", minIngredientsPerSlice=" + minIngredientsPerSlice +
                ", maxCellsPerSlice=" + maxCellsPerSlice +
                '}';
    }
}
