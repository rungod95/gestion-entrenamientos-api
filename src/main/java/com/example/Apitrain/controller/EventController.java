package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Event;
import com.example.Apitrain.exception.EventNotFoundException;
import com.example.Apitrain.service.EventService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getAllEvents() {
        logger.info("Iniciando operación para obtener todos los eventos");
        List<Event> events = eventService.getAllEvents();
        logger.info("Operación completada: Se obtuvieron {} eventos", events.size());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(events);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        logger.info("Iniciando operación para obtener el evento con ID {}", id);
        Event event = eventService.getEventById(id);
        logger.info("Operación completada: Evento obtenido con ID {}", id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(event);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) throws BadRequestException {
        logger.info("Petición para crear evento: {}", event);
        Event createdEvent = eventService.createEvent(event);
        logger.info("Evento creado correctamente con ID {}. Devolviendo status 201 Created", createdEvent.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdEvent);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        logger.info("Iniciando operación para actualizar el evento con ID {}", id);
        Event updatedEvent = eventService.updateEvent(id, event);
        logger.info("Operación completada: Evento actualizado con ID {}", id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        logger.info("Iniciando operación para eliminar el evento con ID {}", id);
        eventService.deleteEvent(id);
        logger.info("Evento eliminado. Devolviendo status 204 No Content");
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        System.out.println(">>> ENDPOINT REACHED");
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body("Hola!");
    }

    @GetMapping(value = "/filter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> filterEvents(
            @RequestParam(required = false, defaultValue = "") String nombre,
            @RequestParam(required = false, defaultValue = "") String ubicacion,
            @RequestParam(required = false, defaultValue = "0") Integer capacidad) {
        logger.info("Petición para filtrar eventos: nombre={}, ubicación={}, capacidad={}", nombre, ubicacion, capacidad);
        List<Event> filteredEvents = eventService.filterEvents(nombre, ubicacion, capacidad);
        logger.info("Filtrado completado. Se encontraron {} eventos", filteredEvents.size());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(filteredEvents);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Event> updatePartialEvent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        logger.info("Iniciando operación PATCH para el evento con ID {}. Cambios: {}", id, updates);
        Event updatedEvent = eventService.updatePartial(id, updates);
        logger.info("Actualización parcial completada. Devolviendo evento: {}", updatedEvent);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(updatedEvent);
    }

    @GetMapping(value = "/filterByUbicacionAndCapacidad", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getByUbicacionAndCapacidad(
            @RequestParam String ubicacion,
            @RequestParam Integer capacidad) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(eventService.getByUbicacionandCapacidad(ubicacion, capacidad));
    }

    @GetMapping(value = "/filterByCapacidadAndUbicacion", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Event>> getEventsByCapacidadAndUbicacion(
            @RequestParam Integer capacidad,
            @RequestParam String ubicacion) {
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                .body(eventService.getEventsByCapacidadAndUbicacion(capacidad, ubicacion));
    }
}
