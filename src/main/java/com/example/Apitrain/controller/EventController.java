package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Athlete;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.service.EventService;
import com.example.Apitrain.domain.Event;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/events")
public class EventController {

        @Autowired
        private EventService eventService;

        @GetMapping
        public List<Event> getAllEvents() {
            return eventService.getAllEvents();
        }

        @GetMapping("/{id}")
        public Event getEventById(@PathVariable Long id) {
            return eventService.getEventById(id);
        }

        @PostMapping
        public Event createEvent(@RequestBody Event event) {
            return eventService.createEvent(event);
        }

        @PutMapping("/{id}")
        public Event updateEvent(@PathVariable Long id, @RequestBody Event event) {
            return eventService.updateEvent(id, event);
        }

        @DeleteMapping("/{id}")
        public void deleteEvent(@PathVariable Long id) {
            eventService.deleteEvent(id);
        }

        @GetMapping("/filter")
        public List<Event> filterEvents(
                @RequestParam(required = false, defaultValue = "") String nombre,
                @RequestParam(required = false, defaultValue = "") String ubicacion,
                @RequestParam(required = false, defaultValue = "0") Integer capacidad) {
            return eventService.filterEvents(nombre, ubicacion, capacidad);
        }
    @PatchMapping("/{id}")
    public ResponseEntity<Event> updatePartialEvent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        Event updatedEvent = eventService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedEvent);
    }


}
