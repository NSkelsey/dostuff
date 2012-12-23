package sgd.dostuff;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import sgd.dostuff.Objective.ObjectiveStatus;

public class BackgroundService extends Service implements LocationListener {

	private static String TAG = "BgService";
	private static final int CHECK_INTERVAL_MS = 5000;
	private Timer timer = new Timer();
	public static LocationManager locationManager;
	public static String bestProvider;
	public static Location myLocation;
	
	//stuff from LocationCheckerService
	public static final String BROADCAST_ACTION = "sgd.dostuff.locationchecker.eventget";
	private SpotDataSource datasource;
	private int radius = 1000;
	private long timeThreshold = 1;	//1 minute
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		 SpotDataSource datasource = new SpotDataSource(getBaseContext());
		 datasource.open();
		 Spot s = datasource.getNextSpot(new Date());
		 datasource.close();
		 
		 if (s != null) {
			// get a Calendar object with current time
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(s.getDate());
			 // add 5 minutes to the calendar object
			 cal.add(Calendar.MINUTE, -10);
			 Intent intent = new Intent(getBaseContext(), AlarmBroadcastReceiver.class);
			 intent.putExtra("alarm_message", "no");
			 // In reality, you would want to have a static variable for the request code instead of 192837
			 PendingIntent sender = PendingIntent.getBroadcast(this, 192837, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			 
			 // Get the AlarmManager service
			 AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			 am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		 }
		
		 
		//periodically check if the user has reached an objective
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				checkLocation(myLocation);
				Log.v(TAG , "checking current location");
			}
		}, 0, CHECK_INTERVAL_MS);
		
		
		

		// Get the location manager
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		// List all providers:
		List<String> providers = locationManager.getAllProviders();

	//	Log.v("base", "Num Providers: "+providers.size() + "\n\n");
		for (String provider : providers)
		{
			printProvider(provider);
		}

		/*myLocation = new Location("fake");
		myLocation.setLatitude(38.05);
		myLocation.setLongitude(-78.5);*/
		
		Criteria criteria = new Criteria();

		//bestProvider = locationManager.getBestProvider(criteria, false);
		bestProvider = LocationManager.GPS_PROVIDER;

		
		
		//locationManager.addTestProvider(bestProvider, false, false, false, false, false, false, false, 0, 5);
		
	//	Log.v("base","\n\nBEST Provider:\n");
		printProvider(bestProvider);
		
		//locationManager.setTestProviderEnabled(bestProvider, true);
		
		
		//locationManager.setTestProviderLocation(bestProvider, loc);
		
		/*bestProvider = "gps";
		
		locationManager.setTestProviderEnabled(bestProvider, true);
		Location loc = new Location(bestProvider);
		loc.setLatitude(50);
		loc.setLongitude(80);
		locationManager.setTestProviderLocation(bestProvider, loc);
		*/

		// output.append("\n\nLocations (starting with last known):");
		Location location = locationManager.getLastKnownLocation(bestProvider);
		setLocation(location);
		//printLocation(location);
		locationManager.requestLocationUpdates(bestProvider, 0, 0, this);
	}


	
	public void onLocationChanged(Location location)
	{
		setLocation(location);
		Log.v(TAG, "location changed");
		//hasBeenSet = true;
	//	Log.v(TAG, "loc changed");
		
		//updateDB(location);

	}

	
	public void onProviderDisabled(String provider)
	{
		//hasBeenSet = false;
//		Log.v("base", "Provider "+provider+" Disabled");
	}

	
	public void onProviderEnabled(String provider)
	{
		// TODO Auto-generated method stub
//		Log.v("base", "Provider "+provider+" Enabled");
	}

	
	public void onStatusChanged(String provider, int status, Bundle extras)
	{
		// TODO Auto-generated method stub
//		Log.v(TAG, "CHanGEd");

	}

	private void printProvider(String provider)
	{
		LocationProvider info = locationManager.getProvider(provider);
	//	Log.v("base", provider+": "+info.toString() + "\n\n");
	}

	private void printLocation(Location location)
	{
		if (location == null)
			Log.v("base", "\nLocation[unknown]\n\n");
		else
			Log.v("base", "\n\n" + location.toString());
	}
	
	public void setLocation(Location loc)
	{
		if (loc != null)
		{
			//myLatitude = loc.getLatitude();
			//myLongitude = loc.getLongitude();
			//hasBeenSet = true;
			myLocation = loc;
			printLocation(loc);
			
			checkLocation(loc);
		}
		else
		{
			//hasBeenSet = false;
	//		Log.v("base", "Location null");
		}
	}
	
	@Override
	public void onDestroy(){
	//	Log.v(TAG , "DESTROYED.");
		locationManager.removeUpdates(this);
		timer.cancel();
	}
	
	
	/* ----------------------------------------------------------------------------
	 * Temporary addition for MSE; copied methods from LocationCheckerService
	 * so that we can get everything to update when the location changes
	 * --------------------------------------------------------------------------- */
	
	public void checkLocation(Location currentLocation)
	{
		//make sure we have a valid current location--something is wrong if we don't
		if (currentLocation == null) {
			Log.i(TAG, "current location null--not checking arrival");
			return;
		}
						
		//get the set of objectives whose completion we should check
		//currently just the next spot in the list
		Spot nextEvent = getNextUpcomingSpot();
		
		//if there are any objectives to check, do so
		if(nextEvent != null)
		{
			//initialize current time and objective time so we can compare them
			Calendar spotTime = Calendar.getInstance();
			Calendar currentTime = Calendar.getInstance();
			spotTime.setTime(nextEvent.getDate());
			currentTime.setTime(new Date());
			
			//get difference in between current time and objective time
			long timeDiff = spotTime.getTimeInMillis() - currentTime.getTimeInMillis();
			
			//debug output
			Log.v(TAG, nextEvent.getDate() + " this is what we think the spot time is");
			Log.v(TAG, new Date() + " this is what we think current time is");
			Log.v(TAG, "Time diff: " + timeDiff + "   Time diff in mins: " + timeDiff/1000.0/60.0 + "  Thereshold(mins): " + timeThreshold);
			

			//determine if the event should be completed
			double distance = currentLocation.distanceTo(nextEvent.getLocation());
			boolean withinRadius = distance <= radius;
			Log.v(TAG, "Dist: "+ distance + "   Radius: " + radius);
			Log.v(TAG, "Name: " + nextEvent.getName());
			
			//converts timeDiff into minutes
			//This checks if the event being compared to now should be completed. 
			if(timeDiff/1000.0/60.0 < timeThreshold)
			{
				if (withinRadius) {
					nextEvent.setStatus(ObjectiveStatus.SUCCESS);
				} else {
					nextEvent.setStatus(ObjectiveStatus.FAILURE);
				}
				completeEvent(nextEvent);
				
			} 
			
			//test TODO
			//completeEvent(nextEvent);
		} else {
			Log.v(TAG, "no upcoming events");
		}
	}
	
	private Spot getNextUpcomingSpot() {
		if (datasource == null) {
			datasource = new SpotDataSource(getBaseContext());
		}
		
		datasource.open();
		
		Date currDate = new Date();
		//getnextspot makes dangerous assumptions careful.
		Spot nextSpot = datasource.getNextSpot(currDate);
		
		datasource.close();
		return nextSpot;
	}

	public void completeEvent(Spot s)
	{
//		Log.v("boggle", "START EVENT COMPLETED");
		Log.v("boggle", "Event Completed!" + s.getLocation() + "");
		datasource.open();
		
		//all or nothing pts for now
		long pts = s.getStatus() == ObjectiveStatus.SUCCESS ? s.getPtval() : 0;
		s.setPtsgot(pts);
		s.setDone(true);
		
		Log.v(TAG, "spot ptsgot before change = " + datasource.getSpotByID(s.getId()).getPtsgot());
		datasource.changeSpot(s);
		Log.v(TAG, "spot ptsgot after change = " + datasource.getSpotByID(s.getId()).getPtsgot());
		
		datasource.close();
		
		Intent intent = new Intent(BROADCAST_ACTION);
		intent.putExtra("ID", s.getId());	//TODO array of ids, in case multiple objectives
		sendBroadcast(intent);
	}
}