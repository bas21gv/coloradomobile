package com.cts.coloradomobile.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.coloradomobile.pojo.Notification;

public interface NotificationDao extends JpaRepository<Notification, Integer>{

}
