package com.example.proyectofinal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Vector;

public class MapaFragment extends Fragment implements LocationListener,OnMapReadyCallback {
    Vector<Double> alertaCoordenadaX;
    Vector<Double> alertaCoordenadaY;
    Vector<String> nombreAlerta;
    Vector<String> tipoAlerta;
    LocationManager locationManager;
    double longitudeNetwork, latitudeNetwork;
    GoogleMap mMap;
    MarkerOptions mMaker;
    boolean posicion_actual_encontrada;
    public MapaFragment(Vector<Double> x, Vector<Double> y, Vector<String> z, Vector<String> t) {
        alertaCoordenadaX = x;
        alertaCoordenadaY = y;
        nombreAlerta = z;
        tipoAlerta = t;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        latitudeNetwork = -37.0976102;
        longitudeNetwork = -72.5615041;
        posicion_actual_encontrada = false;
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!checkLocation())
            return;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        locationManager.removeUpdates(this);

        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 10 * 1000, 3, this);
        Toast.makeText(getContext(), "Estableciendo ubicación actual", Toast.LENGTH_LONG).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
        LocationManager myLocManager;
        SupportMapFragment MapaFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);  //use SuppoprtMapaFragment for using in fragment instead of activity  MapaFragment = activity   SupportMapaFragment = fragment
        MapaFragment.getMapAsync(this);

        return rootView;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void onLocationChanged(Location location) {
        longitudeNetwork = location.getLongitude();
        latitudeNetwork = location.getLatitude();
        if(posicion_actual_encontrada == false) {
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(latitudeNetwork, longitudeNetwork))
                    .zoom(12)
                    .bearing(0)
                    .tilt(45)
                    .build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
            posicion_actual_encontrada = true;
            mMaker = new MarkerOptions().position(new LatLng(latitudeNetwork,longitudeNetwork))
                    .title("Mi ubicación actual")
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            mMap.addMarker(mMaker);
        }

        mMaker.position(new LatLng(latitudeNetwork,longitudeNetwork))
                .title("Mi ubicación actual")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        Toast.makeText(getContext(), "Actualizando ubicación "+String.valueOf(latitudeNetwork), Toast.LENGTH_SHORT).show();

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

    public double getLongitudeNetwork() {
        return longitudeNetwork;
    }

    public void setLongitudeNetwork(double longitudeNetwork) {
        this.longitudeNetwork = longitudeNetwork;
    }

    public double getLatitudeNetwork() {
        return latitudeNetwork;
    }

    public void setLatitudeNetwork(double latitudeNetwork) {
        this.latitudeNetwork = latitudeNetwork;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            mMap.clear(); //clear old markers
            CameraPosition googlePlex = CameraPosition.builder()
                    .target(new LatLng(latitudeNetwork,longitudeNetwork))
                    .zoom(12)
                    .bearing(0)
                    .tilt(45)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(-37.0976102,-72.5615041))
                    .title("Cuerpo De Bomberos 1ra Compañía Arturo Prat De Yumbel")
                    .snippet("Punto de encuentro")
                    .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.logo_bomberos_mapa)));
            for(int i = alertaCoordenadaX.size()-1; i >= 0; i--) {
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(alertaCoordenadaX.elementAt(i), alertaCoordenadaY.elementAt(i)))
                        .title(nombreAlerta.elementAt(i))
                        .snippet(tipoAlerta.elementAt(i)));
                Log.d("EN FRAGMENT: ", nombreAlerta.elementAt(i));
            }
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(-37.0776102,-72.5415041))
                    .title("Bombero 1")
                    .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.bombero)));

    }
}