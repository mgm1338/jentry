package collections.list;

import core.Const;
import core.array.ArrayUtil;
import core.array.GrowthStrategy;

/**
 * Copyright © 2012 Max Miller
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * -   Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * -   Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * -   Neither the name of Jentry nor the names of its contributors may be
 * used to endorse or promote products derived from this software without
 * specific prior written permission.
 * <p/>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
public class EntryMultiList
{
    private static GrowthStrategy growthStrategy = GrowthStrategy.doubleGrowth;

    protected int[] lists;
    protected int[] nexts;
    protected int freeListPtr;
    protected int nextUnusedEntry = 0;
    protected int maxHead = -1;

    public EntryMultiList( int initialListSize, int totalEntrySize )
    {
        this.lists = new int[ initialListSize ];
        this.nexts = new int[ totalEntrySize - initialListSize ];
    }

    /**
     * With the listHead provided, get an entry, insert it into the linked list
     * and return the entry
     *
     * @param listHead the head of the list
     * @return the entry
     */
    public int addEntryToList( int listHead )
    {
        if( listHead > maxHead ) //if we are greater, may have to grow lists
        {
            maxHead = listHead;
            ArrayUtil.ArrayProviderInt.ensureArrayCapacity( lists, listHead + 1,
                                                            Const.NO_ENTRY,
                                                            growthStrategy );
        }
        int entry;
        if( freeListPtr != -1 )
        {
            entry = freeListPtr;
            freeListPtr = nexts[ entry ];
        }
        else
        {
            entry = nextUnusedEntry++;
            ArrayUtil.ArrayProviderInt.ensureArrayCapacity( nexts, entry,
                                                            Const.NO_ENTRY,
                                                            growthStrategy );
        }

        if( lists[ listHead ] == Const.NO_ENTRY )
        {
            lists[ listHead ] = entry;
        }
        else
        {
            // we have pre-pended our entry before the old head entry,
            // when iterating, new entry will be first encountered
            nexts[ entry ] = lists[ listHead ];
            lists[ listHead ] = entry;
        }
        return entry;
    }

    //update the free list
    public void removeEntryFromList( int listHead, int entry )
    {

    }

    /**
     * For the list signified by <i>listHead</i>, return the next entry. If
     * the previous entry is Const.NoEntry, will return the head entry,
     * otherwise return the next of the previous entry.
     *
     * @param listHead  the listHead of the list
     * @param prevEntry the previous entry (Const.NO_ENTRY) if none
     * @return the next entry for the listHead
     */
    public int getNextEntryForList( int listHead, int prevEntry )
    {
        if( prevEntry == Const.NO_ENTRY )
        {
            return lists[ listHead ];
        }
        return nexts[ prevEntry ];
    }

}
