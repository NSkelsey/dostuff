package sgd.dostuff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.Log;
import android.widget.Toast;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 
		 Intent serviceStarter = new Intent(context, LocationCheckerService.class);
		 
		 context.startService(serviceStarter);
		 
		 Log.d("dostuff", "I actually ran wut");
		 
		 
		 context.stopService(serviceStarter);
		 /*
	   try {
	     Bundle bundle = intent.getExtras();
	     String message = bundle.getString("alarm_message");
	     Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	    } catch (Exception e) {
	     Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
	     e.printStackTrace();
	 
	    }//*/
	 }
	 
	}