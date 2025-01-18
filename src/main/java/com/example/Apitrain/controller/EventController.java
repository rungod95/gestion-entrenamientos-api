package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Event;
import com.example.Apitrain.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        logger.info("Iniciando operación para obtener todos los eventos");
        List<Event> events = eventService.getAllEvents();
        logger.info("Operación completada: Se obtuvieron {} eventos", events.size());
        return events;
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        logger.info("Iniciando operación para obtener el evento con ID {}", id);
        Event event = eventService.getEventById(id);
        logger.info("Operación completada: Evento obtenido con ID {}", id);
        return event;
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        logger.info("Iniciando operación para crear un evento");
        Event createdEvent = eventService.createEvent(event);
        logger.info("Evento creado con éxito: ID {}", createdEvent.getId());
        return createdEvent;
    }

    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
        logger.info("Iniciando operación para actualizar el evento con ID {}", id);
        Event updatedEvent = eventService.updateEvent(id, event);
        logger.info("Operación completada: Evento actualizado con ID {}", id);
        return updatedEvent;
    }

    @DeleteMapping("/{id}")
    public void deleteEvent(@PathVariable Long id) {
        logger.info("Iniciando operación para eliminar el evento con ID {}", id);
        eventService.deleteEvent(id);
        logger.info("Operación completada: Evento eliminado con ID {}", id);
    }

    @GetMapping("/filter")
    public List<Event> filterEvents(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(required = false, defaultValue = "") String ubicacion,
            @RequestParam(required = false, defaultValue = "0") Integer capacidad) {
        logger.info("Iniciando operación para filtrar eventos con parámetros: nombre={}, ubicación={}, capacidad>={}", nombre, ubicacion, capacidad);
        List<Event> filteredEvents = eventService.filterEvents(nombre, ubicacion, capacidad);
        logger.info("Operación completada: Se obtuvieron {} eventos filtrados", filteredEvents.size());
        return filteredEvents;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Event> updatePartialEvent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        logger.info("Iniciando operación para actualización parcial del evento con ID {}", id);
        Event updatedEvent = eventService.updatePartial(id, updates);
        logger.info("Operación completada: Evento actualizado parcialmente con ID {}", id);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/filterByUbicacionAndCapacidad")
    public List<Event> getByUbicacionAndCapacidad(
            @RequestParam String ubicacion,
            @RequestParam Integer capacidad) {
            return eventService.getByUbicacionandCapacidad(ubicacion, capacidad);
    }
    @GetMapping("/filterByCapacidadAndUbicacion")
    public List <Event> getEventsByCapacidadAndUbicacion(
            @RequestParam Integer capacidad,
            @RequestParam String ubicacion) {
        return eventService.getEventsByCapacidadAndUbicacion(capacidad, ubicacion);
    }

}
