package store.table.change;

/**
 * Copyright 4/24/13
 * All rights reserved.
 * <p/>
 * User: Max Miller
 * Created: 4/24/13
 */
public interface ChangeQueue
{
    public boolean isChanged(int rowId, int colId);
}
