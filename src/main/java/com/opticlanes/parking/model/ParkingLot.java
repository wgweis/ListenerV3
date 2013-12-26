package com.opticlanes.parking.model;

import java.util.ArrayList;
import java.util.UUID;

/**
 * ParkingLot entity bean
 * 
 * @author wgweis
 *
 */
public class ParkingLot 
{
	private UUID parkingID;
	private String name;
	private String streetNO;
	private String street;
	private String city;
	private String state;
	private Integer zip;
	private double lotLatitude;
	private double lotLongitude;
	
	private Integer capacity;
	private Long empty;
	private Long occupied;
	private Long version;
	private String[] amenities;
	
	/**
	 * @return the parkingID
	 */
	public UUID getParkingID() {
		return parkingID;
	}
	
	/**
	 * @param parkingID the parkingID to set
	 */
	public void setParkingID(UUID parkingID) {
		this.parkingID = parkingID;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the streetNO
	 */
	public String getStreetNO() {
		return streetNO;
	}
	
	/**
	 * @param streetNO the streetNO to set
	 */
	public void setStreetNO(String streetNO) {
		this.streetNO = streetNO;
	}
	
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * @return the zip
	 */
	public Integer getZip() {
		return zip;
	}
	
	/**
	 * @param zip the zip to set
	 */
	public void setZip(Integer zip) {
		this.zip = zip;
	}
	
	/**
	 * @return the lotLatitude
	 */
	public double getLotLatitude() {
		return lotLatitude;
	}
	
	/**
	 * @param lotLatitude the lotLatitude to set
	 */
	public void setLotLatitude(double lotLatitude) {
		this.lotLatitude = lotLatitude;
	}
	
	/**
	 * @return the lotLagntitude
	 */
	public double getLotLongitude() {
		return lotLongitude;
	}
	
	/**
	 * @param lotLagntitude the lotLagntitude to set
	 */
	public void setLotLongitude(double lotLongitude) {
		this.lotLongitude = lotLongitude;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public Long getEmpty() {
		return empty;
	}

	public void setEmpty(Long empty) {
		this.empty = empty;
	}

	public Long getOccupied() {
		return occupied;
	}

	public void setOccupied(Long occupied) {
		this.occupied = occupied;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public String[] getAmenities() {
		return amenities;
	}

	public void setAmenities(String[] amenities) {
		this.amenities = amenities;
	}
}
