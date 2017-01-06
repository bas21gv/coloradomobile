package com.cts.coloradomobile.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.cts.coloradomobile.pojo.Notification;

@Transactional
public interface NotificationDao extends CrudRepository<Notification,Integer>{

}
