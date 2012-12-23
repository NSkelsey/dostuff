package sgd.dostuff;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class DostuffActivity extends MapActivity {

	private static final String TAG = "DS";
	private MapController mapController;
	private MapView mapView;
	private LocationManager locationManager;

	List<Overlay> mapOverlays;
	Drawable drawable;
	TempItemizedOverlay itemizedOverlay;

	private DateFormat df;
	private Date date;
	private boolean makeSelection = false;
	private String name;
	private int ptval;
	
	private SpotDataSource datasource;

	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.maps); // bind the layout to the activity

		
		datasource = new SpotDataSource(this);
		
		df = DateFormat.getDateTimeInstance(0, 0, Locale.US);

		Bundle extras = getIntent().getExtras();
		String dstring = extras.getString("DATE");
		name = extras.getString("NAME");
		ptval = extras.getInt("PTVAL");

		try {
			date = df.parse(dstring);
			Toast.makeText(getBaseContext(), dstring, Toast.LENGTH_LONG).show();
			Log.v(TAG , "date parse worked:   " + date.toString());
		} catch (ParseException e) {
			Log.v(TAG , "date parse failed");
		}

		// create a map view
		RelativeLayout linearLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapView.setStreetView(true);
		mapController = mapView.getController();
		mapController.setZoom(16); // Zoon 1 is world view
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new GeoUpdateHandler());

		mapOverlays = mapView.getOverlays();
		drawable = this.getResources().getDrawable(R.drawable.androidmarker);
		itemizedOverlay = new TempItemizedOverlay(drawable);
		GeoPoint point = new GeoPoint((int)(38.03 * 1E6), (int)(-78.478889 * 1E6));
		OverlayItem overlayitem = new OverlayItem(point, "", "");

		mapController.animateTo(point);
		itemizedOverlay.addOverlay(overlayitem);
		mapOverlays.add(itemizedOverlay);

		TouchOverlay touchSensor = new TouchOverlay();
		mapOverlays.add(touchSensor);

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	public class GeoUpdateHandler implements LocationListener {

		public void onLocationChanged(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			OverlayItem overlayitem = new OverlayItem(point, "", "");
			itemizedOverlay.addOverlay(overlayitem);
			mapController.animateTo(point); //	mapController.setCenter(point);

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public class TouchOverlay extends Overlay {


		@Override
		public boolean onTouchEvent(MotionEvent event, MapView mapView){
			if (!makeSelection && event.getAction() == 1) {
				
				GeoPoint p = mapView.getProjection().fromPixels(
						(int) event.getX(),
						(int) event.getY());



				Geocoder geoCoder = new Geocoder(
						getBaseContext(), Locale.getDefault());
				try {
					List<Address> addresses = geoCoder.getFromLocation(
							p.getLatitudeE6()  / 1E6, 
							p.getLongitudeE6() / 1E6, 1);

					String add = "";
					if (addresses.size() > 0) 
					{
						for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
								i++)
							add += addresses.get(0).getAddressLine(i) + "\n";
					}

					Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
					
				}
				catch (IOException e) {                
					e.printStackTrace();
					Log.v("go", "I DIED GUYS LOLOLOLOOLLOLOLOLOLOLOLOOLOLOLOLOLOLOLO");
				}   
				return true;
				//*/
			}
			if (makeSelection && event.getAction() == 1){
				GeoPoint p = mapView.getProjection().fromPixels(
						(int) event.getX(),
						(int) event.getY());
				Toast.makeText(getBaseContext(), 
						p.getLatitudeE6() / 1E6 + "," + 
								p.getLongitudeE6() /1E6 , 
								Toast.LENGTH_SHORT).show();

				makeNewSpot(name, ptval, date, p);
	
				/*
				Context c = getApplicationContext();
				Intent intent = new Intent(c, Main.class);
				startActivity(intent);
				*/
				finish();
			}

			return false;
		}
	}
	public void onClick(View view){
		switch(view.getId()){
		case R.id.mappickerbutton:
			makeSelection  = true;
			break;
		}

	}

	public void makeNewSpot(String name, int ptval, Date date, GeoPoint p) {
		datasource.open();
		Spot s = datasource.createSpot(p.getLatitudeE6()  / 1E6, p.getLongitudeE6()  / 1E6, date, ptval, name);
		datasource.close();
		
	}



}