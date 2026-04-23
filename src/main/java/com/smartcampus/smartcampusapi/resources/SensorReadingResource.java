/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.resources;

import com.smartcampus.smartcampusapi.data.DataStore;
import com.smartcampus.smartcampusapi.model.Sensor;
import com.smartcampus.smartcampusapi.model.SensorReading;
import com.smartcampus.smartcampusapi.model.ErrorMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.smartcampus.smartcampusapi.exception.SensorUnavailableException;

/**
 *
 * @author sasin
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {
        private String sensorId;

    //Constructor
    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // Returns all readings for a sensor
    @GET
    public Response getAllReadings() {
        Sensor sensor = DataStore.sensors.get(sensorId);
        // Check sensor exists
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Sensor not found"+ sensorId,404,"NOT_FOUND"))
                    .build();
        }

        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
        }

        return Response.ok(sensorReadings).build();
    }

    //Adds a new reading to a sensor
    @POST
    public Response addReading(SensorReading reading) {
        // Check sensor exists
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity (new ErrorMessage("Sensor not found " + sensorId, 404, "NOT_FOUND"))
                    .build();
        }

        // Block readings if sensor is in MAINTENANCE status
        if (sensor.getStatus() != null && sensor.getStatus().equalsIgnoreCase("MAINTENANCE")) {
            throw new SensorUnavailableException("Sensor is under maintenance and cannot accept readings.");
        }
         // Validate reading body is provided
        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Reading body is required", 400, "BAD_REQUEST"))
                    .build();
        }
        // Auto generate UUID if no ID provided
        if (reading.getId() == null || reading.getId().trim().isEmpty()) {
            reading.setId(UUID.randomUUID().toString());
        }

        // Auto generate timestamp if not provided
        if (reading.getTimestamp() == 0) {
            reading.setTimestamp(System.currentTimeMillis());
        }
        // Save reading to datastore
        List<SensorReading> sensorReadings = DataStore.readings.get(sensorId);

        if (sensorReadings == null) {
            sensorReadings = new ArrayList<>();
            DataStore.readings.put(sensorId, sensorReadings);
        }

        sensorReadings.add(reading);
        // Update sensor currentValue with latest reading
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}