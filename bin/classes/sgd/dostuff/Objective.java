package sgd.dostuff;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;




/**
 * A class encapsulating all the data and behavior common to all objectives for
 * which a user might be rewarded (currently just reaching specific locations).
 */
public abstract class Objective {

	//consts for ids that must be present in ListView item layouts for
	//our ObjectiveAdapter to work
	public static final int TEXT_ID = R.id.label;
	public static final int ICON_ID = R.id.icon;

	/**
	 * objectives can only have a finite number of statuses we can check for + change
	 */
	public enum ObjectiveStatus {
		UNDETERMINED, SUCCESS, FAILURE;
	}

	/**
	 * A standardized data structure we can pass to all our Objectives
	 * to let them evaluate whether they've been completed or not. Currently
	 * just has GPS and date info, but could also have, say, whether and/or
	 * how long the phone has been moving (for exercise or nap detection), or
	 * whatever else we come up with. Basically, allows us to have a single
	 * {@link Objective#evaluateStatus(ObjectiveInfo)} method common to all
	 * objectives.
	 */
	public class ObjectiveInfo {
		public long id;
		public double lat;
		public double lon;
		public Date date;
	}

	ObjectiveStatus mStatus = ObjectiveStatus.UNDETERMINED;
	public long id;
	String mName;
	protected Date date;
	protected long ptval = 1000;
	protected long ptsgot = 0;
	protected boolean isDone = false;


	public Objective(String name) {
		mName = name;
	}

	/**
	 * Using a standardized set of data common to all objectives, evaluates
	 * whether this particular objective has been completed, failed, or is still
	 * undetermined. Basically forcing a common interface for extensibility.
	 * @param info The set of data available to Objectives
	 * @return The status of this Objective after evaluation.
	 */
	public abstract ObjectiveStatus evaluateStatus(ObjectiveInfo info);


	public ObjectiveStatus getStatus() {
		return mStatus;
	}
	public void setStatus(ObjectiveStatus s) {
		mStatus = s;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}

	public long getPtsgot() {
		return ptsgot;
	}

	public void setPtsgot(long ptsgot) {
		this.ptsgot = ptsgot;
	}
	public boolean isDone() {
		return isDone;
	}
	public void setDone(boolean done) {
		this.isDone = done;
	}
	public long getPtval() {
		return ptval;
	}

	public void setPtval(long ptval) {
		this.ptval = ptval;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getDate(){
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object o){
		if (o instanceof Objective){
			Objective obj = (Objective)o;
			return obj.getId() == this.getId();

		} else {
			return false;
		}


	}
}

