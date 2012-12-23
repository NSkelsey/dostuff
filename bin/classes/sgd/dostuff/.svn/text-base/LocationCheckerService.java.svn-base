package sgd.dostuff;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


//TODO IMPELMENT LOCATION LISTENER
public class LocationCheckerService extends Service {

	private static String TAG = "LocCheckService";
	public static final String BROADCAST_ACTION = "sgd.dostuff.locationchecker.eventget";
	private Timer timer = new Timer();
	private SpotDataSource datasource;
	private Date currentDate;
	private int radius = 100000;
	private Intent intent;
	//in mins
	private long timeThreshold = 30;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		
		
		checkLocation();

		/*
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				checkLocation();
				Log.v(TAG , "THREAD IS A GO. YEAH");
			}
		}, 0, 50000);
		//*/
	}
	
	public void checkLocation()
	{
		Log.v(TAG, "LocationCheckerService#checkLocation called");
		
		currentDate = new Date();
		
		Spot nextEvent = getNextUpcomingSpot();
		
		if(nextEvent != null)
		{
			
			//Checking time as well
			
			//checkTime(nextEvent);
			
			Calendar spotTime = Calendar.getInstance();
			Calendar currentTime = Calendar.getInstance();
			
			spotTime.setTime(nextEvent.getDate());
			
			currentTime.setTime(new Date());
			
			if( spotTime == currentTime) Log.v(TAG, "yep, singleton");
			Log.v(TAG, nextEvent.getDate() + " this is what we think the spot time is");
			Log.v(TAG, new Date() + " this is what we think current time is");
			
			long timeDiff = spotTime.getTimeInMillis() - currentTime.getTimeInMillis();
			
			Log.v(TAG, "Time diff: " + timeDiff + "   Time diff in mins: " + timeDiff/1000.0/60.0 + "  Thereshold(mins): " + timeThreshold);
			
			
			//make sure we have a valid current location--something is wrong if we don't
			Location currentLocation = BackgroundService.myLocation;
			if (currentLocation == null) {
				Log.v(TAG, "current location null--not checking arrival");
				return;
			}
			
			double distance = currentLocation.distanceTo(nextEvent.getLocation());
			boolean withinRadius = distance <= radius;
//			Log.v("boggle", "Next Event: "+nextEvent.getDate()+" , "+nextEvent.getLocation().getLatitude()+" : "+nextEvent.getLocation().getLongitude());
//			Log.v("boggle", "Stored: "+BackgroundService.myLocation.getLatitude()+" : "+BackgroundService.myLocation.getLongitude());
			Log.v(TAG, "Dist: "+ distance + "   Radius: " + radius);
			Log.v(TAG, "Name: " + nextEvent.getName());
//			Log.v("boggle", "Radius: "+radius);
//			Log.v("boggle", "In Radius: "+withinRadius);
			
			//converts timeDiff into minutes
			//This checks if the event being compared to now should be completed. 
			if(withinRadius && timeDiff/1000.0/60.0 < timeThreshold)
			{
				completeEvent(nextEvent);
			}
		}
	}
	
	private Spot getNextUpcomingSpot() {
		datasource = new SpotDataSource(getBaseContext());
		
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
		s.setPtsgot(s.getPtval());
		s.setDone(true);
		
		datasource.changeSpot(s);
		
		datasource.close();
		
		intent = new Intent(BROADCAST_ACTION);
		intent.putExtra("ID", s.getId());
		sendBroadcast(intent);
	}
	

}