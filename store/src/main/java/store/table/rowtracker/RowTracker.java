package store.table.rowtracker;

/**
 * Copyright 6/13/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 6/13/13
 */
public interface RowTracker
{
    int getRowCount();

    void addRow( int rowId );

    void removeRow( int rowId );

    boolean containsRow( int rowId );

    int getMaxRow();
}
