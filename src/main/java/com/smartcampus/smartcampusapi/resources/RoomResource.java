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
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(DataStore.rooms.values());
        for (Room room : roomList) {
        if (room.getSensorIds() != null) {
            for (String sensorId : room.getSensorIds()) {
                if (!DataStore.sensors.containsKey(sensorId)) {
                    ErrorMessage error = new ErrorMessage(
                        "Sensor ID not found: " + sensorId,
                        400,
                        "BAD_REQUEST"
                    );
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity(error)
                            .build();
                }
            }
        }
    }
        return Response.ok(roomList).build();
    }


    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("error:Room not found" + roomId)
                    .build();
        }
        return Response.ok(room).build();
    }
    @POST
    public Response createRoom(Room room) {
        if (room == null ||room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("error:Room ID is required")
                    .build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("error:Room already exists")
                    .build();
        }
        if (room.getSensorIds() == null) {
            room.setSensorIds(new ArrayList<>());
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("error:Room not found")
                    .build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room cannot be deleted because it still has sensors assigned.");
        }
        DataStore.rooms.remove(roomId);
        return Response.ok("message:Room deleted successfully").build();
    }
}
