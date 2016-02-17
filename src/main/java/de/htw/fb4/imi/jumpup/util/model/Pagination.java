/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.util.model;

/**
 * <p>
 * General Pagination model
 * </p>
 * 
 * TODO: extend for pagination logic: add totalNumberResults and actPage fields
 * to calculate limit and offset to be handed to underlying database
 * automatically
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 17.02.2016
 *
 */
public class Pagination
{
    public static final int NO_OFFSET = 0;
    public static final int NO_LIMIT = Integer.MAX_VALUE;

    private int limit;
    private int offset;

    public static Pagination fromParameters(int limit, int offset)
    {
        Pagination pagination = new Pagination();
        pagination.setLimit(limit);
        pagination.setOffset(offset);

        return pagination;
    }

    public int getLimit()
    {
        return limit;
    }

    public void setLimit(int limit)
    {
        this.limit = limit;
    }

    public int getOffset()
    {
        return offset;
    }

    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + limit;
        result = prime * result + offset;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pagination other = (Pagination) obj;
        if (limit != other.limit)
            return false;
        if (offset != other.offset)
            return false;
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Pagination [limit=");
        builder.append(limit);
        builder.append(", offset=");
        builder.append(offset);
        builder.append("]");
        return builder.toString();
    }
}
