package fr.um2.user;

public class Friend extends AbstractUser {
	boolean tracable= false;
	boolean visibleInMap = true;
	
	
	public void setTracable(boolean tracable) {
		this.tracable = tracable;
	}
	
	public void setVisibleInMap(boolean visibleInMap) {
		this.visibleInMap = visibleInMap;
	}
	public boolean isTracable() {
		return tracable;
	}
	public boolean isVisibleInMap() {
		return visibleInMap;
	}
}
