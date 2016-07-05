package com.tqs.android.memory.memoryissues.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class BitmapInBitmapCache {

  // 使用SoftReference来缓存不再memory cache里面的bitmap.
  private final Set<SoftReference<Bitmap>> reusableBitmaps = new HashSet<>();

  protected void putBitmapToResuableSet(Bitmap bitmap) {
    // reusableBitmaps.add(new SoftReference<>(bitmap));
  }

  protected Bitmap getBitmapFromReusableSet(BitmapFactory.Options options) {
    Bitmap bitmap = null;

    if (!reusableBitmaps.isEmpty()) {
      synchronized (reusableBitmaps) {
        final Iterator<SoftReference<Bitmap>> iterator = reusableBitmaps.iterator();
        Bitmap item;

        while (iterator.hasNext()) {
          item = iterator.next().get();

          if (null != item && item.isMutable()) {
            // Check to see it the item can be used for inBitmap
            if (canUseForInBitmap(item, options)) {
              bitmap = item;

              // Remove from reusable set so it can't be used again
              iterator.remove();
              break;
            }
          } else {
            // Remove from the set if the reference has been cleared.
            iterator.remove();
          }
        }
      }
    }

    return bitmap;
  }

  /**
   * @param candidate     - Bitmap to check
   * @param targetOptions - Options that have the out* value populated
   * @return true if <code>candidate</code> can be used for inBitmap re-use with
   * <code>targetOptions</code>
   */
  @TargetApi(Build.VERSION_CODES.KITKAT)
  private static boolean canUseForInBitmap(
      Bitmap candidate, BitmapFactory.Options targetOptions) {
    //BEGIN_INCLUDE(can_use_for_inbitmap)
    if (!hasKitKat()) {
      // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
      return candidate.getWidth() == targetOptions.outWidth
          && candidate.getHeight() == targetOptions.outHeight
          && targetOptions.inSampleSize == 1;
    }

    // From Android 4.4 (KitKat) onward we can re-use if the byte size of the new bitmap
    // is smaller than the reusable bitmap candidate allocation byte count.
    int width = targetOptions.outWidth / targetOptions.inSampleSize;
    int height = targetOptions.outHeight / targetOptions.inSampleSize;
    int byteCount = width * height * getBytesPerPixel(candidate.getConfig());
    return byteCount <= candidate.getAllocationByteCount();
    //END_INCLUDE(can_use_for_inbitmap)
  }

  /**
   * Return the byte usage per pixel of a bitmap based on its configuration.
   *
   * @param config The bitmap configuration.
   * @return The byte usage per pixel.
   */
  private static int getBytesPerPixel(Bitmap.Config config) {
    if (config == Bitmap.Config.ARGB_8888) {
      return 4;
    } else if (config == Bitmap.Config.RGB_565) {
      return 2;
    } else if (config == Bitmap.Config.ARGB_4444) {
      return 2;
    } else if (config == Bitmap.Config.ALPHA_8) {
      return 1;
    }
    return 1;
  }

  public static boolean hasKitKat() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
  }

}
