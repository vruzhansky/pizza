package org.blotter;

/**
 * Created by molok on 22.02.2017.
 */
public class Slice {


    private final PointOnPizza topLeft;
    private final PointOnPizza bottomRight;

    public Slice(PointOnPizza topLeft, PointOnPizza bottomRight) {

        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public PointOnPizza topLeft() {
        return topLeft;
    }

    public PointOnPizza bottomRight() {
        return bottomRight;
    }

    public Slice withIncreasedRight() {
        return new Slice(topLeft, bottomRight.nextOnRight());
    }

    public Slice withIncreasedBottom() {
        return new Slice(topLeft, bottomRight.nextUnder());
    }

    public int size() {
        return (bottomRight.row() - topLeft.row() + 1) * (bottomRight.col() - topLeft.col() + 1);
    }

    @Override
    public String toString() {
        return "Slice{" +
                "topLeft=" + topLeft +
                ", bottomRight=" + bottomRight +
                ", size=" + size() +
                '}';
    }
}
