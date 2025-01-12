package com.example.Apitrain.service;

import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.exception.AthleteNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Apitrain.repository.EventRepository;
import com.example.Apitrain.domain.Event;
import com.example.Apitrain.exception.EventNotFoundException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


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
    public Event updatePartial(Long id, Map<String, Object> updates) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + id));

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Event.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, event, value);
            }
        });

        return eventRepository.save(event);
    }
}
