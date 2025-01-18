package com.example.Apitrain.service;

import com.example.Apitrain.domain.Event;
import com.example.Apitrain.exception.EventNotFoundException;
import com.example.Apitrain.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        logger.info("Obteniendo todos los eventos de la base de datos");
        List<Event> events = eventRepository.findAll();
        logger.info("Se obtuvieron {} eventos", events.size());
        return events;
    }

    public Event getEventById(Long id) {
        logger.info("Buscando evento con ID {}", id);
        return eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Evento no encontrado con ID {}", id);
                    return new EventNotFoundException("Event not found with id " + id);
                });
    }

    public Event createEvent(Event event) {
        logger.info("Guardando un nuevo evento en la base de datos");
        Event createdEvent = eventRepository.save(event);
        logger.info("Evento creado con éxito: ID {}", createdEvent.getId());
        return createdEvent;
    }

    public Event updateEvent(Long id, Event updatedEvent) {
        logger.info("Actualizando el evento con ID {}", id);
        Event event = getEventById(id);
        event.setNombre(updatedEvent.getNombre());
        event.setFechaEvento(updatedEvent.getFechaEvento());
        event.setUbicacion(updatedEvent.getUbicacion());
        event.setCapacidad(updatedEvent.getCapacidad());
        Event savedEvent = eventRepository.save(event);
        logger.info("Evento actualizado con éxito: ID {}", id);
        return savedEvent;
    }

    public void deleteEvent(Long id) {
        logger.info("Eliminando el evento con ID {}", id);
        getEventById(id);
        eventRepository.deleteById(id);
        logger.info("Evento eliminado con éxito: ID {}", id);
    }

    public List<Event> filterEvents(String nombre, String ubicacion, Integer capacidad) {
        logger.info("Filtrando eventos con parámetros: nombre={}, ubicación={}, capacidad>={}", nombre, ubicacion, capacidad);
        List<Event> events = eventRepository.findByNombreContainingAndUbicacionContainingAndCapacidadGreaterThanEqual(nombre, ubicacion, capacidad);
        logger.info("Operación completada: Se obtuvieron {} eventos filtrados", events.size());
        return events;
    }

    public Event updatePartial(Long id, Map<String, Object> updates) {
        logger.info("Actualizando parcialmente el evento con ID {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Evento no encontrado con ID {}", id);
                    return new EventNotFoundException("Event not found with id: " + id);
                });

        updates.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Event.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, event, value);
                logger.debug("Campo actualizado: {} con valor {}", key, value);
            }
        });

        Event savedEvent = eventRepository.save(event);
        logger.info("Evento actualizado parcialmente con éxito: ID {}", id);
        return savedEvent;
    }

    public List<Event> getByUbicacionandCapacidad(String ubicacion, Integer capacidad) {
        logger.info("Obteniendo eventos en la ubicación {} con capacidad mayor o igual a {}", ubicacion, capacidad);
        return eventRepository.findEventsByUbicacionAndCapacidad(ubicacion, capacidad);
    }
    public List <Event> getEventsByCapacidadAndUbicacion(Integer capacidad, String ubicacion){
        logger.info("Obteniendo eventos con capacidad mayor o igual a {} y ubicación {}", capacidad, ubicacion);
        return eventRepository.findEventsByCapacidadAndUbicacion(capacidad, ubicacion);

    }
}
