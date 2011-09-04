package com.unitedcaterers.db;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents an record in the database file provided.
 * 
 * @author eradkal
 * 
 */
public class Room implements Serializable {
	
	/**
	 * For the serialization process, a ID is necessary to avoid confusion in
	 * the class versions.
	 */
	private static final long	serialVersionUID	= -4784015794996561325L;
	private Integer				recordNumber;
	private Boolean				isValid;
	private String				name;
	private String				location;
	private String				size;
	private String				smoking;
	private String				rate;
	private Date				date;
	private String				customerId;
	
	/**
	 * Default constructor.
	 */
	public Room() {
		
	}
	
	/**
	 * Returns the record number (reservation number) of the room.
	 * 
	 * @return an Integer representing the record number
	 */
	public Integer getRecordNumber() {
		return recordNumber;
	}
	
	/**
	 * Sets the record number of the room.
	 * 
	 * @param recordNumber
	 *            an Integer representing the record number.
	 */
	public void setRecordNumber(Integer recordNumber) {
		this.recordNumber = recordNumber;
	}
	
	/**
	 * Returns true if the record is valid.
	 * 
	 * @return true if the record is valid, else returns false.
	 */
	public Boolean isValid() {
		return isValid;
	}
	
	/**
	 * Sets the validity of the record.
	 * 
	 * @param isValid
	 *            a Boolean representing the validity of the record.
	 */
	public void setValid(Boolean isValid) {
		this.isValid = isValid;
	}
	
	/**
	 * Returns a <code>String</code> containing the name of the hotel.
	 * 
	 * @return the hotel name as a String.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the hotel.
	 * 
	 * @param name
	 *            the hotel name as a String.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Returns a <code>String</code> containing the location of the hotel. The
	 * location is the city name.
	 * 
	 * @return The location is the city name as a String.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the location of the hotel. This is the city name.
	 * 
	 * @param location
	 *            String which represents the location of the hotel.
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Returns the number of persons allowed for the room.
	 * 
	 * @return the number of persons for the room as a String.
	 */
	public String getSize() {
		return size;
	}
	
	/**
	 * Sets the number of persons allowed for the room.
	 * 
	 * @param size
	 *            the number of persons for the room as a String.
	 */
	public void setSize(String size) {
		this.size = size;
	}
	
	/**
	 * Returns a Boolean which represents if smoking is allowed in the room. A
	 * null value means unknown.
	 * 
	 * @return Boolean which indicates if the room is a smoking room.
	 */
	public String getSmoking() {
		return smoking;
	}
	
	/**
	 * Sets if smoking is allowed in the Room.
	 * 
	 * @param smoking
	 *            a Boolean. true if smoking is allowed, false if it is not.
	 */
	public void setSmoking(String smoking) {
		this.smoking = smoking;
	}
	
	/**
	 * Returns the rate of the Room. The currency may be specified. Ex: 100$.
	 * 
	 * @return the rate of the Room as a String.
	 */
	public String getRate() {
		return rate;
	}
	
	/**
	 * Sets the rate of the Room. The currency may be specified. Ex: 100$.
	 * 
	 * @param rate
	 *            the rate of the Room as a String.
	 */
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	/**
	 * Returns the date of the availability of the room.
	 * 
	 * @return the date of the availability of the room as a Date.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Sets the date of the availability of the room.
	 * 
	 * @param date
	 *            the date of the availability of the room as a Date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	/**
	 * Returns the customer ID (a 8 digits number) of the Room. If null, it
	 * means the Room is not booked and is therefore available for a new
	 * booking.
	 * 
	 * @return the customer ID (a 8 digits number) of the Room as a String.
	 */
	public String getCustomerId() {
		return customerId;
	}
	
	/**
	 * Sets the customer ID (a 8 digits number) of the Room. If null, it means
	 * the Room is not booked and is therefore available for a new booking.
	 * 
	 * @param customerId
	 *            the customer ID (a 8 digits number) of the Room as a String.
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * Checks if the specified virtual room matches the current one. A null
	 * criteria in the room specified in parameter is ignored. Two rooms match
	 * if : 1) They have the exact same "valid" and "smoking" criterias. 2) The
	 * "name", "location", "size", "rate", "date" and "customerId" criteria of
	 * the current room starts with the corresponding criteria of the Room in
	 * parameter. Ie: The "Me" location matches "Mexico" and "Metropolis".
	 * 
	 * @param room
	 *            The criteria represented as a Room
	 * @return true if the room specified in parameter matches the current room.
	 *         false if at least one criteria does not match.
	 */
	
	/**
	 * Checks if the specified virtual room exactly matches the current one. A
	 * null criteria in the room specified in parameter is ignored. Two rooms
	 * exactly match if : They have the exact same "valid", "smoking", "name",
	 * "location", "size", "rate", "date" and "customerId" criterias.
	 * 
	 * @param room
	 *            The criteria represented as a Room
	 * @return true if the room specified in parameter exactly matches the
	 *         current room. false if at least one criteria does not match.
	 */
	public boolean exactlyMatches(Room room) {
		boolean matches = true;
		if (room.isValid != null && this.isValid != null) {
			matches &= (room.isValid.equals(this.isValid));
		}
		matches &= equalsIfNotNull(room.name, this.name);
		matches &= equalsIfNotNull(room.location, this.location);
		matches &= equalsIfNotNull(room.size, this.size);
		if (room.smoking != null && this.smoking != null) {
			matches &= (room.smoking.equals(this.smoking));
		}
		matches &= equalsIfNotNull(room.rate, this.rate);
		if (room.date != null && this.date != null) {
			matches &= this.date.equals(room.date);
		}
		matches &= equalsIfNotNull(room.customerId, this.customerId);
		return matches;
	}
	
	/**
	 * Compares both String in parameter for equality.
	 * 
	 * @param string1
	 *            one of the String to compare
	 * @param string2
	 *            the other String to compare
	 * @return true if one of the String is null. If both String are non null,
	 *         return string1.equals(string2).
	 * 
	 */
	private boolean equalsIfNotNull(String string1, String string2) {
		if (string1 != null && string2 != null) {
			return string1.equals(string2);
		} else {
			return true;
		}
	}
	
	/**
	 * Compares both String to check if string1 starts with string2.
	 * 
	 * @param string1
	 *            one of the String to compare
	 * @param string2
	 *            the other String to compare
	 * @return true if one of the String is null. If both String are non null,
	 *         return string1.startsWith(string2).
	 * 
	 */
	private boolean startsWithIfNotNull(String string1, String string2) {
		if (string1 != null && string2 != null) {
			return string1.startsWith(string2);
		} else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		return "Room [recordNumber=" + this.recordNumber + ", isValid=" + this.isValid + ", name=" + this.name
				+ ", location=" + this.location + ", size=" + this.size + ", smoking=" + this.smoking + ", rate="
				+ this.rate + ", date=" + this.date + ", customerId=" + this.customerId + "]";
	}
	
}
