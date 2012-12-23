package sgd.dostuff;


import java.util.Date;

import android.location.Location;

public class Spot extends Objective {

    private static final int UNDETERMINED_ICON = R.drawable.windmill_beard;
	private static final int SUCCESS_ICON = R.drawable.happycat;
	private static final int FAILURE_ICON = R.drawable.you_make_kitty_scared;

	private double lat;
	private double lon;
	private Location myLocation;
	private String name;

	
	public Spot()
	{
		super("Spotty the spot");
		myLocation = new Location("database");
	}
    
    
    public Spot(String name) {
		super(name);
        myLocation = new Location("database");
	}
    
    
    @Override
	public ObjectiveStatus evaluateStatus(ObjectiveInfo info) {
		
		//TODO within a certain margin for all of these
		if (info.date.equals(date)) {	//if at the right time
			
			//determine whether they're in the right place or not
			if (info.lat == lat &&
                info.lon == lon ) {
				setStatus(ObjectiveStatus.SUCCESS);
			}
			setStatus(ObjectiveStatus.FAILURE);
		}
		
		return mStatus;
	}
	


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Location getLocation()
	{
		return myLocation;
	}
	public double getLat() {
		return lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLat(double lat) {
		this.lat = lat;
		myLocation.setLatitude(lat);
	}
	public void setLon(double lon) {
		this.lon = lon;
		myLocation.setLongitude(lon);
	}
	
	public void determineStatus(){
		if (this.isDone){
			if (this.ptsgot > 0){
				this.setStatus(ObjectiveStatus.SUCCESS);
			} else {
				this.setStatus(ObjectiveStatus.FAILURE);
			}
			
			
		}
		else {
			this.setStatus(ObjectiveStatus.UNDETERMINED);
			
		}//*/
		
	}
	

	





	@Override
	public String toString(){
		String val = "ID: " + this.getId() + " \nName: " + this.getName() + " \nLocation:  \n\tlat: " + this.getLat() + " \n\tlon: " +this.getLon() 
				+ " \nTime: " + this.getDate() + " \nPTs Gotten: " + this.getPtsgot() + "\nisDone= " + this.isDone();
		return val;
	}
}
