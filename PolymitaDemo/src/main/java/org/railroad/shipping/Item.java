package org.railroad.shipping;

import java.io.Serializable;
import java.util.*;

public class Item implements Serializable {
	private static final long serialVersionUID = 2L;
	private static final Set<String> TYPES = new HashSet<String>(Arrays.asList(new String[] { "non-hazardous",
			"hazardous-approved", "Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6", "Class 7",
			"Class 8", "Class 9" }));
	public static final int MaxWeightLb = 100;
	private Container containerParent = new Container();
	private String type;
	private String description;
	private int weightLb;
	private int maxAllowableTemp;
	private int minAllowableTemp;
	private float priceDollars;
	private String state;
	private static final Set<String> STATES = new HashSet<String>(Arrays.asList(new String[] {
			"waiting-on-customer-information", "start-process", "in-process", "accepted", "rejected", "waiting-on-human-task" }));

	public Item() {
		priceDollars = 0;
		type = "non-hazardous";
		state = "waiting-on-customer-information";
	}

	public Container getContainerParent() {
		return containerParent;
	}

	public void setContainerParent(Container containerParent) {
		containerParent.addToItems(this);
		this.containerParent = containerParent;
	}

	/**
	 * Returns the hazard type of the item.
	 * 
	 * @return The hazard type or class of each item.
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (TYPES.contains(type)) {
			this.type = type;
		} else {
			throw new IllegalArgumentException("Item type must be:" + TYPES.toString());
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWeightLb() {
		return weightLb;
	}

	public void setWeightLb(int weightLb) {
		this.weightLb = weightLb;
	}

	public int getMaxAllowableTemp() {
		return maxAllowableTemp;
	}

	public void setMaxAllowableTemp(int maxAllowableTemp) {
		this.maxAllowableTemp = maxAllowableTemp;
	}

	public int getMinAllowableTemp() {
		return minAllowableTemp;
	}

	public void setMinAllowableTemp(int minAllowableTemp) {
		this.minAllowableTemp = minAllowableTemp;
	}

	public float getPriceDollars() {
		return priceDollars;
	}

	public void setPriceDollars(float priceDollars) {
		this.priceDollars = priceDollars;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		if (STATES.contains(state)) {
			this.state = state;
		} else {
			throw new IllegalArgumentException("Item state must be:" + STATES.toString());
		}
	}
}
