package sgd.dostuff;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpotViewActivity extends Activity{

	private SpotDataSource datasource;
	private Spot currspot;
	private static final int UNDETERMINED_ICON = R.drawable.images;
	private static final int SUCCESS_ICON = R.drawable.green;
	private static final int FAILURE_ICON = R.drawable.rrod;
	private static final int ARROW_ICON = R.drawable.arrow;
	private ImageView status;
	
	
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.spotview);
		

	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		
		datasource = new SpotDataSource(this);
		
		datasource.open();
		Intent intent = getIntent();
		Bundle extra = intent.getExtras();
		long id = extra.getLong("ID", 1);
		currspot = datasource.getSpotByID(id);
		TextView tv = (TextView)findViewById(R.id.spotnamefield);
		String s = makeSexyText(currspot);
		tv.setText(s);
		
		
		datasource.close();
		
		status = (ImageView)findViewById(R.id.SpotIcon);
		
				
		int icon = 0;
		
		switch(currspot.getStatus()) {
		case UNDETERMINED:
			//color = Color.TRANSPARENT;
			icon = UNDETERMINED_ICON;
			SpotDataSource sd = new SpotDataSource(getBaseContext());
			sd.open();
			if (currspot.equals(sd.getNextSpot(new Date()))){
				icon = ARROW_ICON;
			}
			sd.close();
			break;
		case SUCCESS:
			icon = SUCCESS_ICON;
			break;
		case FAILURE:
			
			icon = FAILURE_ICON;
			break;
		}
		
		status.setImageResource(icon);
		
	}

	private String makeSexyText(Spot spot) {
		String s = "";
		s += "Name: " + spot.getName() + "\n";
		SimpleDateFormat sdf = new SimpleDateFormat("EEE hh:mm");
		String d = sdf.format(spot.getDate());
		s += "When: " + d + "\n";

		String w = "";
		Geocoder geoCoder = new Geocoder(
				getBaseContext(), Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(
					spot.getLat(), 
					spot.getLon(), 1);

			String add = "";
			if (addresses.size() > 0) 
			{
				for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
						i++)
					add += addresses.get(0).getAddressLine(i) + "\n";
			}

			w = add;
			
		}
		catch (IOException e) {                
			e.printStackTrace();
			Log.v("go", "I DIED GUYS LOLOLOLOOLLOLOLOLOLOLOLOOLOLOLOLOLOLOLO");
		}
		if (w == ""){
			w = "good job, you broke something";
			
			
		}
		
		s += "Where: " + w + "\n";
		s +=  "Points: " + spot.getPtsgot() + "/" + spot.getPtval() + "\n";
		return s;
	}
	
	
	
}
