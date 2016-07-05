package com.tqs.android.memory.memoryissues.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class BitmapManager {

  private final BitmapMemoryCache memoryCache;
  private final BitmapInBitmapCache inBitmapCache;

  private static BitmapManager instance;

  private BitmapManager() {
    this.memoryCache = new BitmapMemoryCache();
    this.inBitmapCache = new BitmapInBitmapCache();

    memoryCache.initBitmapCache(inBitmapCache);
  }

  static {
    instance = new BitmapManager();
  }

  public static BitmapManager getInstance() {
    return instance;
  }

  public Bitmap getBitmap(Resources resources, int resId, int width, int height) {
    String key = String.valueOf(resId);

    Bitmap memoryDrawable = memoryCache.getBitmap(key);
    if (memoryDrawable != null) {
      Log.d(BitmapManager.class.getSimpleName(), "hit memory cache");
      return memoryDrawable;
    }

    Bitmap bitmap = BitmapDecoder.generate(resources, resId, width, height, inBitmapCache);
    memoryCache.addBitmap(key, bitmap);

    return bitmap;
  }

  public void clear() {
    memoryCache.clear();
  }
}
