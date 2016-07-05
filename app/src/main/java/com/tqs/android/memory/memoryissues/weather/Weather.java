package com.tqs.android.memory.memoryissues.weather;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author qisen (woaitqs@gmail.com)
 */
public class Weather implements Parcelable {

  private int temperature;
  private String status;
  private int weedSpeed;
  private int airPressure;
  private String city;
  private String backgroundUrl;

  public Weather(
      int temperature, String status, int weedSpeed, int airPressure,
      String city, String backgroundUrl) {
    this.temperature = temperature;
    this.status = status;
    this.weedSpeed = weedSpeed;
    this.airPressure = airPressure;
    this.city = city;
    this.backgroundUrl = backgroundUrl;
  }

  protected Weather(Parcel in) {
    temperature = in.readInt();
    status = in.readString();
    weedSpeed = in.readInt();
    airPressure = in.readInt();
    city = in.readString();
    backgroundUrl = in.readString();
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(temperature);
    dest.writeString(status);
    dest.writeInt(weedSpeed);
    dest.writeInt(airPressure);
    dest.writeString(city);
    dest.writeString(backgroundUrl);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Creator<Weather> CREATOR = new Creator<Weather>() {
    @Override
    public Weather createFromParcel(Parcel in) {
      return new Weather(in);
    }

    @Override
    public Weather[] newArray(int size) {
      return new Weather[size];
    }
  };

  public int getTemperature() {
    return temperature;
  }

  public String getStatus() {
    return status;
  }

  public int getWeedSpeed() {
    return weedSpeed;
  }

  public int getAirPressure() {
    return airPressure;
  }

  public String getCity() {
    return city;
  }

  public String getBackgroundUrl() {
    return backgroundUrl;
  }
}
