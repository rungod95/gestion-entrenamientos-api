package com.example.Apitrain.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.repository.EventRepository;
import com.example.Apitrain.domain.Event;
import com.example.Apitrain.exception.EventNotFoundException;

import java.util.List;


@Service
public class EventService {

        @Autowired
        private EventRepository eventRepository;

        public List<Event> getAllEvents() {
            return eventRepository.findAll();
        }

        public Event getEventById(Long id) {
            return eventRepository.findById(id)
                    .orElseThrow(() -> new EventNotFoundException("Event not found with id " + id));
        }

        public Event createEvent(Event event) {
            return eventRepository.save(event);
        }

        public Event updateEvent(Long id, Event updatedEvent) {
            Event event = getEventById(id);
            event.setNombre(updatedEvent.getNombre());
            event.setFechaEvento(updatedEvent.getFechaEvento());
            event.setUbicacion(updatedEvent.getUbicacion());
            event.setCapacidad(updatedEvent.getCapacidad());
            return eventRepository.save(event);
        }

        public void deleteEvent(Long id) {
            Event event = getEventById(id);
            eventRepository.deleteById(id);
        }

        public List<Event> filterEvents(String nombre, String ubicacion, Integer capacidad) {
            return eventRepository.findByNombreContainingAndUbicacionContainingAndCapacidadGreaterThanEqual(nombre, ubicacion, capacidad);
        }
}
