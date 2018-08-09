package org.ithot.android.transmit.cache;

/**
 * This is the LRU cache used for the RAM layer when configured to used references.
 * @param <T> is the class of object stored in the cache.
 */
public class ReferenceLruCache<T> extends RamLruCache<String, T> {

    private SizeOf<T> mHandlerSizeOf;

    /**
     * @param maxSize for caches that do not override {@link #sizeOf}, this is
     *                the maximum number of entries in the cache. For all other caches,
     *                this is the maximum sum of the sizes of the entries in this cache.
     *
     * @param handler computes the size of each object stored in the RAM cache layer.
     */
    public ReferenceLruCache(int maxSize, SizeOf<T> handler) {
        super(maxSize);
        mHandlerSizeOf = handler;
    }

    @Override
    protected int sizeOf(String key, T value) {
        return mHandlerSizeOf.sizeOf(value);
    }
}
