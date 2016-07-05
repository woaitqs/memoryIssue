package com.tqs.android.memory.memoryissues.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class BitmapDecoder {

  /**
   * Time cast operation, don't call on ui thread.
   *
   * @param resources resources.
   * @param resId resource image id.
   * @param width desired width.
   * @param height desired height.
   * @return bitmap.
   */
  public static Bitmap generate(
      Resources resources, int resId, int width, int height, BitmapInBitmapCache inBitmapCache) {
    if (width == 0 || height == 0) {
      return BitmapFactory.decodeResource(resources, resId);
    }
    BitmapFactory.Options decodeOptions = new BitmapFactory.Options();

    // 获取图片大小，以进行相应的策略处理.
    decodeOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeResource(resources, resId, decodeOptions);

    // 根据具体的大小值，来进行缩放.
    decodeOptions.inSampleSize = calculateInSampleSize(decodeOptions, width, height);

    if (hasHoneycomb()) {
      addInBitmapOptions(decodeOptions, inBitmapCache);
    }

    // 重置图片，实际来获取图片.
    decodeOptions.inJustDecodeBounds = false;
    return BitmapFactory.decodeResource(resources, resId, decodeOptions);
  }

  private static void addInBitmapOptions(BitmapFactory.Options options,
                                         BitmapInBitmapCache inBitmapCache) {
    // inBitmap only works with mutable bitmaps, so force the decoder to
    // return mutable bitmaps.
    options.inMutable = true;

    if (inBitmapCache != null) {
      // Try to find a bitmap to use for inBitmap.
      Bitmap inBitmap = inBitmapCache.getBitmapFromReusableSet(options);

      if (inBitmap != null) {
        // If a suitable bitmap has been found, set it as the value of
        // inBitmap.
        options.inBitmap = inBitmap;
      }
    }
  }

  public static int calculateInSampleSize(
      BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight
          && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }

    return inSampleSize;
  }

  private static boolean hasHoneycomb() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
  }
}