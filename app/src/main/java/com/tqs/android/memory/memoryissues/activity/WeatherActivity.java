package com.tqs.android.memory.memoryissues.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tqs.android.memory.memoryissues.R;
import com.tqs.android.memory.memoryissues.fragment.CityWeatherFragment;
import com.tqs.android.memory.memoryissues.weather.Weather;
import com.tqs.android.memory.memoryissues.weather.WeatherManager;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements WeatherManager.WeatherListener {

  private ViewPager viewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    viewPager = (ViewPager) findViewById(R.id.viewpager);
    initPager();
    WeatherManager.getInstance().addWeatcherListener(this);
  }

  @Override
  protected void onDestroy() {
     WeatherManager.getInstance().removeWeatherListener(this);
    super.onDestroy();
  }

  private List<Weather> mockWeathers() {

    ArrayMap<String, String> mockUrls = new ArrayMap<>();
    mockUrls.put("http://a.image", "a");
    mockUrls.put("http://b.image", "b");
    mockUrls.put("http://c.image", "c");
    mockUrls.put("http://d.image", "d");

    List<Weather> weathers = new ArrayList<>();
    weathers.add(new Weather(20, "晴天", 40, 273, "北京", mockUrls.get("http://a.image")));
    weathers.add(new Weather(25, "晴天", 30, 263, "上海", mockUrls.get("http://b.image")));
    weathers.add(new Weather(30, "晴天", 50, 283, "广州", mockUrls.get("http://c.image")));
    weathers.add(new Weather(31, "晴天", 51, 236, "深圳", mockUrls.get("http://d.image")));
    return weathers;
  }

  private void initPager() {
    WeatherAdapter weatherAdapter = new WeatherAdapter(getSupportFragmentManager());
    viewPager.setAdapter(weatherAdapter);
    weatherAdapter.setWeathers(mockWeathers());
  }

  @Override
  public void onWeatcherUpdate(String city, Weather weather) {
    // do nothings.
  }

  private static class WeatherAdapter extends FragmentPagerAdapter {

    private List<Weather> weathers;

    public WeatherAdapter(FragmentManager fm) {
      super(fm);
    }

    public void setWeathers(List<Weather> weathers) {
      this.weathers = weathers;
      notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
      CityWeatherFragment weatherFragment = new CityWeatherFragment();
      Bundle bundle = new Bundle();
      bundle.putParcelable(CityWeatherFragment.WEATHER_ARGUMENT, weathers.get(position));
      weatherFragment.setArguments(bundle);
      return weatherFragment;
    }

    @Override
    public int getCount() {
      return weathers == null ? 0 : weathers.size();
    }
  }
}