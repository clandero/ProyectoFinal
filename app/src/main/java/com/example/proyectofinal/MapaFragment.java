package com.example.proyectofinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class MapaFragment extends Fragment {
    double fuegoCoordenadaX;
    double fuegoCoordenadaY;
    public MapaFragment(double x, double y) {
        fuegoCoordenadaX = x;
        fuegoCoordenadaY = y;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mapa, container, false);
        LocationManager myLocManager;

        SupportMapFragment MapaFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapa);  //use SuppoprtMapaFragment for using in fragment instead of activity  MapaFragment = activity   SupportMapaFragment = fragment
        MapaFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(-37.0976102,-72.5615041))
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

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(fuegoCoordenadaX,fuegoCoordenadaY))
                        .title("Incendio")
                        .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.fuego)));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(-37.0776102,-72.5415041))
                        .title("Bombero 1")
                        .icon(bitmapDescriptorFromVector(getActivity(),R.drawable.bombero)));
            }
        });


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

}