package com.github.ayan.gpsprojectnexis;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    TextView latlot;
    TextView address;
    TextView distanceTextView;
    TextView title;
    ConstraintLayout layout;
    ImageView image;

    List<Address> currentAddress;
    List<Address> addresses;
    ArrayList<LocationAndTime> allAddresses;
    double latitude;
    double longitude;

    LocationManager locationManager;
    Geocoder geocoder;

    int count;
    float distance;
    float distanceInMiles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latlot = findViewById(R.id.id_textView_textView);
        address = findViewById(R.id.id_textView_address);
        distanceTextView = findViewById(R.id.id_textView_distance);
        title = findViewById(R.id.id_textView_title);
        layout = findViewById(R.id.id_constraintLayout);
        image = findViewById(R.id.id_imageView);

        latlot.setTextColor(Color.BLACK);
        address.setTextColor(Color.BLACK);
        distanceTextView.setTextColor(Color.BLACK);
        title.setTextColor(Color.BLACK);
        layout.setBackgroundColor(Color.CYAN);
        image.setImageResource(R.drawable.gpsmarker);

        allAddresses = new ArrayList<>();


        Locale unitedStates = new Locale("en","US");
         geocoder = new Geocoder(this,Locale.US );

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Inside permission missing");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,this);
        //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //  .GPS_PROVIDER);

        onLocationChanged(location);



    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            latlot.setText("Latitude: " + latitude + "\nLongitude: " + longitude);

            try {
                System.out.print("Checking Address");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                boolean isPresent = Geocoder.isPresent();
                currentAddress = geocoder.getFromLocation(latitude, longitude, 2);

                LocationAndTime currentLocationAndTime = new LocationAndTime(currentAddress.get(0).getLongitude(), currentAddress.get(0).getLatitude(), new Date(), 0);
                long elapsedTime = 0;
                if(allAddresses.size() > 0){
                    Location currentLocation = new Location("CurrentLocation");
                    Location previousLocation = new Location("Previous Location");
                    currentLocation.setLatitude(currentAddress.get(0).getLatitude());
                    currentLocation.setLongitude(currentAddress.get(0).getLongitude());

                    LocationAndTime previousLocationAndTime = allAddresses.get(allAddresses.size()-1);
                    previousLocation.setLatitude(previousLocationAndTime.getLatitude());
                    previousLocation.setLongitude(previousLocationAndTime.getLongitude());

                    distance += previousLocation.distanceTo(currentLocation);
                    distanceInMiles = distance;
                    distanceInMiles/=1000;
                    distanceInMiles*=0.621371;

                    elapsedTime = currentLocationAndTime.getDate().getTime()-previousLocationAndTime.getDate().getTime();
                    currentLocationAndTime.setElapsedTime(elapsedTime);

                }

                if(currentAddress.size() > 0){
                    address.setText(""+currentAddress.get(0).getAddressLine(0));
                }

                distanceTextView.setText("Distance Traveled: "+distanceInMiles+" miles");

                allAddresses.add(currentLocationAndTime);
                addresses = geocoder.getFromLocation(latitude,longitude,2);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
