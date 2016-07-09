package com.tqs.android.memory.memoryissues.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tqs.android.memory.memoryissues.R;
import com.tqs.android.memory.memoryissues.bitmap.BitmapManager;
import com.tqs.android.memory.memoryissues.weather.Weather;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class CityWeatherFragment extends Fragment {

  public static final String WEATHER_ARGUMENT = "WEATHER_ARGUMENT";

  private Weather weather;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    weather = getArguments().getParcelable(WEATHER_ARGUMENT);
  }

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_weather_detail, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    TextView temperature = (TextView) view.findViewById(R.id.temperature);
    TextView weedSpeed = (TextView) view.findViewById(R.id.weedSpeed);
    TextView status = (TextView) view.findViewById(R.id.status);
    TextView airPressure = (TextView) view.findViewById(R.id.airPressure);
    View bgView = view.findViewById(R.id.bg_view);
    temperature.setText(weather.getTemperature() + "℃");
    weedSpeed.setText("风速：" + weather.getWeedSpeed());
    status.setText(weather.getStatus());
    airPressure.setText("气压：" + weather.getAirPressure());
    new FetchBgTask(getResFromUrl(weather.getBackgroundUrl()), bgView).execute();

    costTime();
  }

  private void costTime() {
    long timeCost = 0;
    for (int i = 0 ; i < 10000000; i ++) {
      timeCost += i;
    }
    Log.d(CityWeatherFragment.class.getSimpleName(), "timeCost is " + timeCost);
  }

  private int getResFromUrl(String url) {
    if ("a".equalsIgnoreCase(url)) {
      return R.drawable.bg_beijing;
    } else if ("b".equalsIgnoreCase(url)) {
      return R.drawable.bg_shanghai;
    } else if ("c".equalsIgnoreCase(url)) {
      return R.drawable.bg_guangzhou;
    } else {
      return R.drawable.bg_shenzhen;
    }
  }

  private class FetchBgTask extends AsyncTask<Void, Void, Bitmap> {

    private final int resId;
    private final View view;

    private FetchBgTask(int resId, View view) {
      this.resId = resId;
      this.view = view;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
      return BitmapManager.getInstance().getBitmap(
          getResources(), resId, getScreenWidth(), getScreenHeight());
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
      if (bitmap == null) {
        return;
      }
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        view.setBackground(new BitmapDrawable(bitmap));
      } else {
        view.setBackgroundDrawable(new BitmapDrawable(bitmap));
      }
    }

    private int getScreenWidth() {
      return getResources().getDisplayMetrics().widthPixels / 4;
    }

    private int getScreenHeight() {
      return getResources().getDisplayMetrics().heightPixels / 4;
    }
  }

}
