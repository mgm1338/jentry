package core.array;

/**
 * millemax
 * <p/>
 * A strategy for growing collections, in elements.
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
    protected static final class DoubleGrowth extends GrowthStrategy
    {
        @Override
        public int growthRequest( int currentSize, int minimumAcceptable )
        {
            if (currentSize==0) currentSize=1;
            int size = currentSize * 2;
            while( size < minimumAcceptable ) size *= 2;
            return size;
        }
    }
}
