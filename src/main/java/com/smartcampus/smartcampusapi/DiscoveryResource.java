/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author sasin
 */
@Path("/")
public class DiscoveryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {
        Map<String, Object> info = new HashMap<>();

        // Version info
        info.put("name", "Smart Campus API");
        info.put("description", "API for managing campus rooms and sensors");
        // Admin contact
        info.put("contact", "supportTeam@smartcampus.ac.uk");

       

        // Room endpoints
        Map<String, String> roomLinks = new HashMap<>();
        roomLinks.put("GET all rooms", "/api/v1/rooms");
        roomLinks.put("GET one room", "/api/v1/rooms/{roomId}");
        roomLinks.put("POST create room", "/api/v1/rooms");
        roomLinks.put("DELETE room", "/api/v1/rooms/{roomId}");

        // Sensor endpoints
        Map<String, String> sensorLinks = new HashMap<>();
        sensorLinks.put("GET all sensors", "/api/v1/sensors");
        sensorLinks.put("GET filter by type", "/api/v1/sensors?type={type}");
        sensorLinks.put("GET one sensor", "/api/v1/sensors/{sensorId}");
        sensorLinks.put("POST create sensor", "/api/v1/sensors");

        // Reading endpoints
        Map<String, String> readingLinks = new HashMap<>();
        readingLinks.put("GET all readings", "/api/v1/sensors/{sensorId}/readings");
        readingLinks.put("POST add reading", "/api/v1/sensors/{sensorId}/readings");

        // Add all to resources
        Map<String, Object> resources = new HashMap<>();
        resources.put("rooms", roomLinks);
        resources.put("sensors", sensorLinks);
        resources.put("readings", readingLinks);

        info.put("resources", resources);

        return Response.ok(info).build();
    }
}
