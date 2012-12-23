package sgd.dostuff;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;



public class DevActivity extends Activity {

	private boolean serviceOn = false;

	private Intent backIntent, locationChecker;
	
	public static boolean showAll = false;


	@Override
	public void onCreate(Bundle bundle) {
			super.onCreate(bundle);



	}
	
	@Override
	public void onResume(){
		super.onResume();	
		setContentView(R.layout.devactivity);

		backIntent = new Intent(this, BackgroundService.class);
		locationChecker = new Intent(this, LocationCheckerService.class);
		
	}

	public void onClick(View view){
		switch(view.getId()){
		case R.id.servicetoggle:
			if (serviceOn == false) {
				startService(backIntent);
				startService(locationChecker);
				Toast.makeText(getApplicationContext(),
						"Background service started", Toast.LENGTH_SHORT)
						.show();
				serviceOn = true;
			} else {
				stopService(locationChecker);
				stopService(backIntent);
				Toast.makeText(getApplicationContext(),
						"Background service stopped", Toast.LENGTH_SHORT)
						.show();
				serviceOn = false;
			}
			break;
			//*/
		case R.id.queryByIDButton:
			EditText ed = (EditText)findViewById(R.id.SpotQueryNum);
			int i = new Integer(ed.getText().toString());
			
			Intent intent = new Intent(this, SpotViewActivity.class);
			intent.putExtra("ID", i);
			
			startActivity(intent);
			break;
		case R.id.checkBox1:
			CheckBox cb = (CheckBox)findViewById(R.id.checkBox1);
			showAll = cb.isChecked();
			break;
		}


	}


}
