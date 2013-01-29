package fr.um2.user;

import java.io.Serializable;

public class Friend extends AbstractUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	boolean tracable= false;
	boolean visibleInMap = false;
	
	
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
