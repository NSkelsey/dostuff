package sgd.dostuff;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sgd.dostuff.Objective.ObjectiveStatus;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom class for binding a <code>ListView</code> to our 
 * <code>Objective</code> class. Layout given must incorporate an
 * ImageView with id <code>R.id.label</code>, and 
 * <code>ImageView</code> with id R.id.icon.
 * @author Davis Blalock
 *
 * @see Objective
 */
public class ObjectiveAdapter extends ArrayAdapter<Objective> {
	
	private static final int NO_ICON = -1;
	private static final int UNDETERMINED_ICON = R.drawable.images;
	private static final int SUCCESS_ICON = R.drawable.green;
	private static final int FAILURE_ICON = R.drawable.rrod;
	private static final int ARROW_ICON = R.drawable.arrow;
	
	//resource for how we should display our Objective
	private int mLayoutId;
	
	//layout elements we need to have
	private ImageView mOptionIcon;
	private TextView mOptionName;
	private TextView mCountdownText;
	
	//formatter for how we should display time until objectives evaluated
	private DateFormat mDateFormat;
	private Calendar mCalendar = Calendar.getInstance();

	//our actual items
	private List<Objective> mItems = new ArrayList<Objective>();
	
	//store whether we want icons and text or just text
	private boolean mTextOnly = false;

	
	public ObjectiveAdapter(Context context, int layoutId,
			List<Objective> items) {
		super(context, layoutId);
		mLayoutId = layoutId;
		mItems = items;
		mTextOnly = false;
		mDateFormat = new SimpleDateFormat("EEE hh:mm");
	}
	
	public ObjectiveAdapter(Context context, int layoutId,
			List<Objective> items, boolean textOnly) {
		super(context, layoutId);
		mLayoutId = layoutId;
		mItems = items;
		mTextOnly = textOnly;
		mDateFormat = new SimpleDateFormat("EEE hh:mm");
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Objective getItem(int index) {
		return mItems.get(index);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		//recycle convertView if possible
		View row = convertView;
		
		//otherwise, make a new view
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(mLayoutId, parent, false);
		}

		// Get item
		Objective item = mItems.get(position);

		// Have the menu item show its associated text
		mOptionName = (TextView) row.findViewById(Objective.TEXT_ID);
		String name = item.getName() != null ? item.getName() : "an objective!" ;
		mOptionName.setText(name);
		
		View background = (View) row.findViewById(R.id.row_linear_layout);
		int color = Color.TRANSPARENT;
		int icon = NO_ICON;
		
		//hack to set the status TODO
		if (item.isDone() && (item.getStatus() == ObjectiveStatus.UNDETERMINED)) {
			if (item.getPtsgot() != 0) {
				item.setStatus(ObjectiveStatus.SUCCESS);	//if you got any points, call it a win
			} else {
				item.setStatus(ObjectiveStatus.FAILURE);	//otherwise, you lose
			}
		}
		
		switch(item.getStatus()) {
		case UNDETERMINED:
			//color = Color.TRANSPARENT;
			mOptionName.setTextColor(Color.WHITE);
			icon = UNDETERMINED_ICON;
			SpotDataSource sd = new SpotDataSource(getContext());
			sd.open();
			if (item.equals(sd.getNextSpot(new Date()))){
				icon = ARROW_ICON;
			}
			sd.close();
			break;
		case SUCCESS:
			mOptionName.setTextColor(Color.GREEN);
			icon = SUCCESS_ICON;
			break;
		case FAILURE:
			mOptionName.setTextColor(Color.GRAY);
			icon = FAILURE_ICON;
			break;
		}
		//set the background color to reflect the status of the objective
		//background.setBackgroundColor(color);
		
		// If we're using icons, set it to use the appropriate icon
		if (! mTextOnly) {
			mOptionIcon = (ImageView) row.findViewById(Objective.ICON_ID);

			if (mOptionIcon != null && icon != NO_ICON) {
				mOptionIcon.setImageResource(icon);
			}
		}
		
		mCountdownText = (TextView) row.findViewById(R.id.countdown);
		if (mCountdownText != null) {
			mCountdownText.setText(mDateFormat.format(item.getDate()));
			
			/*
			//calculate time between now and objective
			long diff = item.getDate().getTime() - (new Date()).getTime();
			long minutes = diff / 1000 / 60;
			long hours = minutes / 60;
			long days = hours / 24;
			minutes = minutes % 60;
			hours = hours % 24;
			
			String countdownStr;
			if (days <= 0) {
				StringBuilder sb = new StringBuilder();
				sb.append(days);
				sb.append(" - ");
				sb.append(hours);
				sb.append(":");
				sb.append(minutes);
				countdownStr = sb.toString();
			}
		
			//objective has yet to happen, so show time until it occurs
			if (diff > 0) {
				mCountdownText.setText(mDateFormat.format(item.getDate()));
				
			//objective already happened, so just show when it happened
			} else {
				mCountdownText.setText(mDateFormat.format(item.getDate()));
			}
			*/
		}
		
		return row;
	}
}