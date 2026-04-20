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

    }
}
