package com.tqs.android.memory.memoryissues;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.tqs.android.memory.memoryissues.bitmap.BitmapManager;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class MemoryApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    LeakCanary.install(this);
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    Log.d(MemoryApplication.class.getSimpleName(), "onLowMemory and clear cache.");
    BitmapManager.getInstance().clear();
  }

  @Override
  public void onTrimMemory(int level) {
    Log.d(MemoryApplication.class.getSimpleName(), "onTrimMemory and level is " + level);
    if (level >= ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
      BitmapManager.getInstance().clear();
    }
    super.onTrimMemory(level);
  }
}