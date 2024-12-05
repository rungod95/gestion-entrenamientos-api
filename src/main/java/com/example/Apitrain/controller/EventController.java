package com.example.Apitrain.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.service.EventService;
import com.example.Apitrain.domain.Event;

import java.util.List;

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
}
