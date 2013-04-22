package core.array;

/**
 * millemax
 * <p/>
 * A strategy for allocation. Will not perform the allocation, simply provide a number of items
 * that a collection should use when growing. Can be used to strictly control memory allocation
 * (or closely monitor growth requests when desired).
 */
public abstract class GrowthStrategy
{
    /**
     * Request a new size in order to grow a collection.
     *
     * @param currentSize       the current size
     * @param minimumAcceptable the minimum elements that the collection needs
     * @return the size to grow to
     */
    public abstract int growthRequest( int currentSize, int minimumAcceptable );


    public static final GrowthStrategy doubleGrowth = new DoubleGrowth();

    public static final GrowthStrategy toExactSize = new ExactSizeGrowth();

    protected static final class DoubleGrowth extends GrowthStrategy
    {
        @Override
        public int growthRequest( int currentSize, int minimumAcceptable )
        {
            if( minimumAcceptable <= 0 || currentSize >= minimumAcceptable )
                return currentSize;
            if( currentSize == 0 ) currentSize = 1;
            int size = currentSize * 2;
            while( size < minimumAcceptable ) size *= 2;
            return size;
        }
    }

    protected static final class ExactSizeGrowth extends GrowthStrategy
    {
        @Override
        public int growthRequest( int currentSize, int minimumAcceptable )
        {
            return minimumAcceptable;
        }
    }


}
