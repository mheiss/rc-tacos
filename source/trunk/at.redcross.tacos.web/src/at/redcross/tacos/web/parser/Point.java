package at.redcross.tacos.web.parser;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Contains the <tt>x</tt> and <tt>y</tt> coordinates of a given cell.
 */
public class Point {

    /** the column (=x) position */
    private final int column;

    /** the row (=y) position */
    private final int row;

    /**
     * Creates a new point using the given coordinates
     * 
     * @param x
     *            the x position
     * @param y
     *            the y position
     */
    public Point(int column, int row) {
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("x", column).append("y", row).toString();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Returns the X coordinates of this point
     * 
     * @return the x coordinates
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns the Y coordinates of this point
     * 
     * @return the y coordinates
     */
    public int getRow() {
        return row;
    }
}
