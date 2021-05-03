package mobile.sharif.hw2;

import android.location.Location;

public interface GPSCallback
{
    public abstract void onGPSUpdate(Location location);
}