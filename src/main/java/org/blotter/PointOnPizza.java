package org.blotter;

import com.google.common.base.Preconditions;

/**
 * Created by molok on 22.02.2017.
 */
public class PointOnPizza {

    private final int row;
    private final int column;

    public PointOnPizza(int row, int column) {
        this.row = row;
        this.column = column;
        Preconditions.checkArgument(row >= 0);
        Preconditions.checkArgument(column >= 0);
    }

    public int row() {
        return row;
    }

    public int col() {
        return column;
    }

    public PointOnPizza withRow(int newRow) {
        return new PointOnPizza(newRow, col());
    }

    public PointOnPizza withCol(int newCol) {
        return new PointOnPizza(row(), newCol);
    }

    public PointOnPizza nextOnRight() {
        return this.withCol(col()+1);
    }

    public PointOnPizza nextUnder() {
        return this.withRow(row()+1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PointOnPizza that = (PointOnPizza) o;

        if (row != that.row) return false;
        return column == that.column;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        return "{" + row + ", " + column + '}';
    }
}
