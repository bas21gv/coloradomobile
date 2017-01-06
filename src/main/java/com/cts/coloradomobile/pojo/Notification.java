package com.cts.coloradomobile.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="notification")
public class Notification implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5350509038298233112L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column
	private String deviceId;
	
	@Column
	private String message;
	
	public Notification() {
	}

	public Notification(Integer id, String deviceId, String message) {
		this.id = id;
		this.deviceId = deviceId;
		this.message = message;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Notification [id=" + id + ", deviceId=" + deviceId + ", message=" + message + "]";
	}

}
