package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Event;
import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.repository.EventRepository;
import com.example.Apitrain.repository.FacilityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc

@ActiveProfiles("test")
public class EventControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Facility testFacility;

    @BeforeEach
    public void setUp() {
        eventRepository.deleteAll();
        facilityRepository.deleteAll();

        testFacility = new Facility();
        testFacility.setNombre("Instalación Principal");
        testFacility.setTipo("Gimnasio");
        testFacility.setCapacidad(200);
        testFacility = facilityRepository.save(testFacility);
    }


    @Test
    public void testCreateAndGetEvent() throws Exception {
        Event event = new Event();
        event.setNombre("Maratón de Madrid");
        event.setUbicacion("Madrid");
        event.setCapacidad(100);
        event.setFechaEvento(LocalDate.now());
        event.setFacility(testFacility);

        String eventJson = objectMapper.writeValueAsString(event);

        String response = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Event createdEvent = objectMapper.readValue(response, Event.class);
        assertNotNull(createdEvent.getId());
        assertEquals("Maratón de Madrid", createdEvent.getNombre());

        mockMvc.perform(get("/events/" + createdEvent.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Maratón de Madrid"));
    }

    @Test
    public void testCreateEventWithInvalidData() throws Exception {
        Event invalidEvent = new Event(); // sin nombre ni ubicación

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEvent)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistentEvent() throws Exception {
        mockMvc.perform(get("/events/999")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        Event event = new Event();
        event.setNombre("Evento Original");
        event.setUbicacion("Madrid");
        event.setCapacidad(100);
        event.setFechaEvento(LocalDate.now());
        event.setFacility(testFacility);

        String response = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Event createdEvent = objectMapper.readValue(response, Event.class);

        createdEvent.setNombre("Evento Actualizado");
        createdEvent.setCapacidad(200);

        mockMvc.perform(put("/events/" + createdEvent.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Evento Actualizado"))
                .andExpect(jsonPath("$.capacidad").value(200));
    }

    @Test
    public void testDeleteEvent() throws Exception {
        Event event = new Event();
        event.setNombre("Evento a Eliminar");
        event.setUbicacion("Madrid");
        event.setCapacidad(100);
        event.setFechaEvento(LocalDate.now());
        event.setFacility(testFacility);

        String response = mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Event createdEvent = objectMapper.readValue(response, Event.class);

        mockMvc.perform(delete("/events/" + createdEvent.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/events/" + createdEvent.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterEvents() throws Exception {
        Event event1 = new Event();
        event1.setNombre("Maratón Madrid");
        event1.setUbicacion("Madrid");
        event1.setCapacidad(100);
        event1.setFechaEvento(LocalDate.now());
        event1.setFacility(testFacility);

        Event event2 = new Event();
        event2.setNombre("Maratón Barcelona");
        event2.setUbicacion("Barcelona");
        event2.setCapacidad(50);
        event2.setFechaEvento(LocalDate.now());
        event2.setFacility(testFacility);

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(event2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/events/filter")
                        .param("nombre", "Maratón")
                        .param("ubicacion", "Madrid")
                        .param("capacidad", "100")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Maratón Madrid"));
    }
}
