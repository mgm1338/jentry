package store.table.rowtracker;

/**
 * Copyright 6/13/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/13/13
 *
 * A class used to track how many and which rows are full in an operator.
 *
 */
public interface RowTracker
{
    /**
     * Return the total number of active rows.
     *
     * @return
     */
    int getRowCount();

    /**
     * Add a row to the tracker, by id. The id must only be unique for this tracker. Therefore many times
     * a row can be the index it takes in a column.
     *
     * @param rowId the id of the row
     */
    void addRow( int rowId );

    /**
     * Remove a row from the tracker, by row id.
     *
     * @param rowId the id of the row
     */
    void removeRow( int rowId );

    /**
     * Returns true if the row is in the row tracker, false otherwise
     *
     * @param rowId the row id
     * @return true if the row is being tracked, false otherwise
     */
    boolean containsRow( int rowId );

    /**
     * Get the maximum row id, or high watermark. If the Operator is completely compact that will be
     * the number of rows -1.
     *
     * @return the maximum row id.
     */
    int getMaxRowId();
}
