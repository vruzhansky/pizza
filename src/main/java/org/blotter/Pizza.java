package org.blotter;

import java.util.Arrays;

public class Pizza {
    enum Ingredient {T, M}

    final Ingredient[][] ingredients;
    final int minIngredientsPerSlice;
    final int maxCellsPerSlice;

    public Pizza(int rows, int cols, int minIngredientsPerSlice, int maxCellsPerSlice) {
        this.ingredients = new Ingredient[rows][cols];
        this.minIngredientsPerSlice = minIngredientsPerSlice;
        this.maxCellsPerSlice = maxCellsPerSlice;
    }

    @Override
    public String toString() {
        return "Pizza{" +
                "ingredients=" + Arrays.deepToString(ingredients) +
                ", minIngredientsPerSlice=" + minIngredientsPerSlice +
                ", maxCellsPerSlice=" + maxCellsPerSlice +
                '}';
    }
}
