package com.tqs.android.memory.memoryissues.weather;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class WeatherManager {

  /**
   * 天气变化监听接口.
   */
  public interface WeatherListener {

    /**
     * 天气发生变化的更新回调
     *
     * @param city city.
     * @param weather weather.
     */
    void onWeatcherUpdate(String city, Weather weather);
  }

  private List<WeatherListener> weatherListeners;
  private static WeatherManager instance;

  private WeatherManager() {
    weatherListeners = new ArrayList<>();
  }

  static {
    instance = new WeatherManager();
  }

  public static WeatherManager getInstance() {
    return instance;
  }

  public void addWeatcherListener(WeatherListener listener) {
    if (listener == null) {
      return;
    }
    weatherListeners.add(listener);
  }

  public void removeWeatherListener(WeatherListener listener) {
    if (listener == null) {
      return;
    }
    weatherListeners.remove(listener);
  }

}
