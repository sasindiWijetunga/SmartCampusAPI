/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.smartcampusapi.resources;

import com.smartcampus.smartcampusapi.data.DataStore;
import com.smartcampus.smartcampusapi.model.Room;
import com.smartcampus.smartcampusapi.model.ErrorMessage;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import com.smartcampus.smartcampusapi.exception.RoomNotEmptyException;
/**
 *
 * @author sasin
 */
@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {
    //Returns all rooms in the system
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        return Response.ok(roomList).build();
    }

    //Returns a specific room by ID
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Room not found" + roomId,404,"NOT_FOUND"))
                    .build();
        }
        return Response.ok(room).build();
    }
    
    //Creates a new room
    @POST
    public Response createRoom(Room room) {
        //Validate room Id 
        if (room == null ||room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage("Room ID is required",400,"BAD_REQUEST"))
                    .build();
        }
        //Check room does not already exist
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorMessage("Room already exists",409,"CONFLICT"))
                    .build();
        }
        //Check sensor list if null
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        for (String sensorId : room.getSensorIds()) {
            if (!DataStore.sensors.containsKey(sensorId)) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorMessage("Sensor ID not found: " + sensorId, 400, "BAD_REQUEST"))
                        .build();
            }
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }
    
    //DELETE a room 
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        // Check room exists
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorMessage("Room not found" + roomId,404,"NOT_FOUND"))
                    .build();
        }
        // Cannot delete room if sensors are still assigned
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because it still has sensors assigned.");
        }
        DataStore.rooms.remove(roomId);
        return Response.ok(new ErrorMessage("Room deleted successfully", 200, "OK")).build();
    }
}
