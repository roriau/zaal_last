package mn.zaal.zaal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

import mn.zaal.zaal.directionModules.DirectionFinder;
import mn.zaal.zaal.directionModules.DirectionFinderListener;
import mn.zaal.zaal.directionModules.Route;
import mumayank.com.airlocationlibrary.AirLocation;

import static mn.zaal.zaal.tuslah.GoogleMapHelper.buildCameraUpdate;
import static mn.zaal.zaal.tuslah.GoogleMapHelper.defaultMapSettings;
import static mn.zaal.zaal.tuslah.GoogleMapHelper.getDefaultPolyLines;
import static mn.zaal.zaal.tuslah.GoogleMapHelper.getDottedPolylines;
import static mn.zaal.zaal.tuslah.UiHelper.showAlwaysCircularProgressDialog;

public class MapsActivity extends AppCompatActivity implements DirectionFinderListener {

    private enum PolylineStyle {
        DOTTED,
        PLAIN
    }

    private static final String[] POLYLINE_STYLE_OPTIONS = new String[]{
            "PLAIN",
            "DOTTED"
    };
    private PolylineStyle polylineStyle = PolylineStyle.PLAIN;
    private AirLocation airLocation;
    private MapView mMapView;
    private GoogleMap googleMap;
    private Polyline polyline;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
      /*  mMapView = findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); */

        AppCompatSpinner polylineStyleSpinner = findViewById(R.id.polylineStyleSpinner);
        ArrayAdapter adapter = new ArrayAdapter(MapsActivity.this, android.R.layout.simple_spinner_item, POLYLINE_STYLE_OPTIONS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> MapsActivity.this.googleMap = googleMap);
        }
        polylineStyleSpinner.setAdapter(adapter);
        polylineStyleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    polylineStyle = PolylineStyle.PLAIN;
                else if (position == 1)
                    polylineStyle = PolylineStyle.DOTTED;
                if (polyline == null || !polyline.isVisible())
                    return;
                List<LatLng> points = polyline.getPoints();
                polyline.remove();
                if (position == 0)
                    polyline = googleMap.addPolyline(getDefaultPolyLines(points));
                else if (position == 1)
                    polyline = googleMap.addPolyline(getDottedPolylines(points));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        airLocation = new AirLocation(Objects.requireNonNull(MapsActivity.this), true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(@NotNull Location location) {
                Intent intent = getIntent();
                String lat = String.valueOf(location.getLatitude());
                String lng = String.valueOf(location.getLongitude());
                String latDest = intent.getStringExtra("lat");
                String lngDest = intent.getStringExtra("lng");
                String origin = lat + "," + lng;
                String destination = latDest + "," + lngDest;

                if (origin.isEmpty() || destination.isEmpty()) {
                    Toast.makeText(MapsActivity.this, "Өгөгдөл олдсонгүй!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!origin.contains(",") || !destination.contains(",")) {
                    Toast.makeText(MapsActivity.this, "Өгөгдөл буруу байна!", Toast.LENGTH_SHORT).show();
                    return;
                }
                fetchDirections(origin, destination);
            }
            @Override
            public void onFailed(@NotNull AirLocation.LocationFailedEnum locationFailedEnum) {
                // do something
            }
        });

    }

    private void fetchDirections(String origin, String destination) {
        try {
            new DirectionFinder(MapsActivity.this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

  /*  @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    } */

    @Override
    public void onDirectionFinderStart() {
        if (materialDialog == null)
            materialDialog = showAlwaysCircularProgressDialog(this, "Fetching Directions...");
        else materialDialog.show();
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        if (materialDialog != null && materialDialog.isShowing())
            materialDialog.dismiss();
        if (!routes.isEmpty() && polyline != null) polyline.remove();
        try {
            for (Route route : routes) {
                PolylineOptions polylineOptions = getDefaultPolyLines(route.points);
                if (polylineStyle == PolylineStyle.DOTTED)
                    polylineOptions = getDottedPolylines(route.points);
                polyline = googleMap.addPolyline(polylineOptions);
            }
        } catch (Exception e) {
            Toast.makeText(MapsActivity.this, "Error occurred on finding the directions...", Toast.LENGTH_SHORT).show();
        }
        googleMap.animateCamera(buildCameraUpdate(routes.get(0).endLocation),5,null);
    }
}
