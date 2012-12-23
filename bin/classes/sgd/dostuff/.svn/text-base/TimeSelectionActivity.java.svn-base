package sgd.dostuff;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.android.maps.GeoPoint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimeSelectionActivity extends Activity {


	private static final String TAG = "DS";
	private DateFormat df;
	private EditText nameField;
	private String defaultName = "";
	
	private SpotDataSource datasource;
	
	private SeekBar pointBar;
	private int MAX_PTS = 1000;
	private int INTIAL_VALUE = 250;
	private TextView ptSliderText;
	private CheckBox mUseCurrentLocChk;
	

	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.timeselect);
		
		datasource = new SpotDataSource(this);
		
		df = DateFormat.getDateTimeInstance(0, 0, Locale.US);
		
		mUseCurrentLocChk = (CheckBox) findViewById(R.id.use_current_loc_chk);
		
		nameField = (EditText)findViewById(R.id.nameEditText);
		nameField.setText(defaultName);
		
		pointBar = (SeekBar)findViewById(R.id.PointBar);
		pointBar.setMax(MAX_PTS);
		pointBar.setProgress(INTIAL_VALUE);
		//For constanly changing pt score updating
		pointBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			//Methods must be implmented for the interface....
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				//This is a hack so the correct setText method gets called
				ptSliderText.setText("" + progress);

			}
		});
		
		ptSliderText = (TextView)findViewById(R.id.PointSliderText);
		ptSliderText.setText("" + INTIAL_VALUE);

	}
	
	@Override
	public void onResume(){
		super.onResume();

	}

	public void onClick(View view){
		switch (view.getId()){
		case R.id.button1:
			String d = dealwithTimeSelection();
			String name = nameField.getText().toString();
			int ptval = pointBar.getProgress();
			
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			
			if(loc == null)
			{
				loc = new Location("test");
			}
			if (mUseCurrentLocChk.isChecked()) {
				datasource.open();
				Spot s = datasource.createSpot(loc.getLatitude(), 
						loc.getLongitude(), getTimeSelection(), ptval, name);
				datasource.close();
				
				finish();
			} else {
				Intent intent = new Intent(this, DostuffActivity.class);
				intent.putExtra("DATE", d);
				intent.putExtra("NAME", name);
				intent.putExtra("PTVAL", ptval);
				
				startActivity(intent);
			}
						
			break;
		case R.id.morebutton:
			//Code to simply test if the update is working feel free to delete
			// just wanted to commit this if I ever need it again
//			Spot s = new Spot();
//			s.id = 5;
//			s.setName("HURRR DURR DERP FLURR DERR DERP DUM BE DERP");
//			s.setPtval(99999);
//			s.setPtsgot(4500);
//			SpotDataSource ds = new SpotDataSource(this);
//			ds.open();
//			ds.changeSpot(s);
//			ds.close();
		}

	}
	
	public String dealwithTimeSelection(){
		return df.format(getTimeSelection());
	}
	
	
	private Date getTimeSelection() {
		DatePicker dp = (DatePicker)findViewById(R.id.datePicker1);
		TimePicker tp = (TimePicker)findViewById(R.id.timePicker1);
		
		int year = dp.getYear();
		int month = dp.getMonth();
		int day = dp.getDayOfMonth();
		
		int hour = tp.getCurrentHour();
		int min = tp.getCurrentMinute();
		
		//Why year needs a - 1900 is java's fault
		return new Date(year - 1900, month, day, hour, min);
	}

}
