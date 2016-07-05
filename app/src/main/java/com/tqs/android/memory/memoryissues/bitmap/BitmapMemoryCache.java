package com.tqs.android.memory.memoryissues.bitmap;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class BitmapMemoryCache {

  private LruCache<String, Bitmap> bitmapLruCache;

  protected void initBitmapCache(final BitmapInBitmapCache bitmapInBitmapCache) {
    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;

    bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {

      @Override
      protected void entryRemoved(boolean evicted, String key,
                                  Bitmap oldValue, Bitmap newValue) {
        if (RecyclingBitmapDrawable.class.isInstance(oldValue)) {
          // The removed entry is a recycling drawable, so notify it
          // that it has been removed from the memory cache
          // TODO
        } else {
          // The removed entry is a standard BitmapDrawable

          if (hasHoneycomb()) {
            // We're running on Honeycomb or later, so add the bitmap
            // to a SoftReference set for possible use with inBitmap later
            bitmapInBitmapCache.putBitmapToResuableSet(oldValue);
          }
        }
      }


      @Override
      protected int sizeOf(String key, Bitmap bitmap) {
        // The cache size will be measured in kilobytes rather than
        // number of items.
        final int bitmapSize =  bitmap.getByteCount() / 1024;
        return bitmapSize == 0 ? 1 : bitmapSize;
      }
    };
  }

  public void addBitmap(String key, Bitmap bitmap) {
    if (getBitmap(key) == null) {
      bitmapLruCache.put(key, bitmap);
    }
  }

  public Bitmap getBitmap(String key) {
    return bitmapLruCache.get(key);
  }

  public static boolean hasHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }

  public void clear() {
    if (bitmapLruCache != null) {
      bitmapLruCache.evictAll();
    }
  }

}
