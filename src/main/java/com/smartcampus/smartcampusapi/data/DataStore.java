/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.data;

import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.model.SensorReading;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @author sasin
 */
public class DataStore {
    // In-memory storage for rooms, sensors and readings
    public static Map<String, Room> rooms = new HashMap<>();
    public static Map<String, Sensor> sensors = new HashMap<>();
    public static Map<String, List<SensorReading>> readings = new HashMap<>();
    static {
    // Sample rooms
    Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
    rooms.put(r1.getId(), r1);

    // Sample sensors
    Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
    sensors.put(s1.getId(), s1);

    // Link sensors to rooms
    r1.getSensorIds().add(s1.getId());

    // Empty reading lists
    readings.put(s1.getId(), new ArrayList<>());
    
     // Sample rooms
    Room r2 = new Room("CLASS-302", "CLASSROOM ", 40);
    rooms.put(r2.getId(), r2);

    // Sample sensors
    Sensor s2 = new Sensor("HUM-001", "Humidity", "ACTIVE", 60.0, "CLASS-302");
    sensors.put(s2.getId(), s2);

    // Link sensors to rooms
    r2.getSensorIds().add(s2.getId());

    // Empty reading lists
    readings.put(s2.getId(), new ArrayList<>());
    
    }
}
