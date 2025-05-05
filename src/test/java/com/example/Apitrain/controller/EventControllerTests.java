package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Event;
import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.exception.EventNotFoundException;
import com.example.Apitrain.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@WebMvcTest(EventController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ApplicationContext context;

    @MockBean
    private JwtTokenService jwtTokenService;


    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private Facility testFacility;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testFacility = new Facility();
        testFacility.setId(1L);
        testFacility.setNombre("Instalación Test");
        testFacility.setTipo("Pista");
        testFacility.setCapacidad(200);
        testFacility.setHorario("9:00 - 20:00");
        testFacility.setFechaApertura(LocalDate.now());

        testEvent = new Event();
        testEvent.setId(1L);
        testEvent.setNombre("Maratón");
        testEvent.setUbicacion("Madrid");
        testEvent.setCapacidad(100);
        testEvent.setFechaEvento(LocalDate.now());
        testEvent.setFacility(testFacility);
    }
    @BeforeEach
    void checkControllerLoaded()
    {
        System.out.println("¿EventController cargado? " + context.containsBean("eventController"));
    }
    @Test
    public void testGetAllEventsReturnsOk() throws Exception {
        List<Event> eventList = List.of(testEvent);
        when(eventService.getAllEvents()).thenReturn(eventList);

        mockMvc.perform(get("/events")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Maratón"))
                .andExpect(jsonPath("$[0].facility.id").value(1));
    }

    @Test
    public void testGetEventByIdReturnsOk() throws Exception {
        when(eventService.getEventById(1L)).thenReturn(testEvent);

        mockMvc.perform(get("/events/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Maratón"))
                .andExpect(jsonPath("$.facility.id").value(1));
    }

    @Test
    void testTestEndpointReturnsTeapot() throws Exception {
        System.out.println(">>> Seguridad activa: " + context.getEnvironment().getActiveProfiles()[0]);

        mockMvc.perform(get("/events/test"))
                .andDo(result -> {
                    System.out.println("Status: " + result.getResponse().getStatus());
                    System.out.println("Body: " + result.getResponse().getContentAsString());
                })
                .andExpect(status().isIAmATeapot())
                .andExpect(content().string("Hola!"));
    }


    @Test
    public void testGetEventByIdReturnsNotFound() throws Exception {
        when(eventService.getEventById(99L)).thenThrow(new EventNotFoundException("Evento no encontrado con id 99"));

        mockMvc.perform(get("/events/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Evento no encontrado con id 99"))
                .andExpect(jsonPath("$.code").value(404));

    }

    @Test
    public void testCreateEventReturnsCreated() throws Exception {
        Event createdEvent = new Event();
        createdEvent.setId(1L);
        createdEvent.setNombre("Nuevo Evento");
        createdEvent.setUbicacion("Valencia");
        createdEvent.setCapacidad(75);
        createdEvent.setFechaEvento(LocalDate.now());
        createdEvent.setFacility(testFacility);
        
        when(eventService.createEvent(any(Event.class))).thenReturn(createdEvent);

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEvent)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nuevo Evento"))
                .andExpect(jsonPath("$.facility.id").value(1));
    }

    @Test
    public void testCreateEventReturnsBadRequest() throws Exception {
        Event invalidEvent = new Event();
        // Sin facility, que es requerido
        invalidEvent.setNombre("Evento Inválido");
        invalidEvent.setUbicacion("Madrid");

        mockMvc.perform(post("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEvent)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(eventService, never()).createEvent(any(Event.class));
    }

    @Test
    public void testUpdateEventReturnsOk() throws Exception {
        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setNombre("Evento Actualizado");
        updatedEvent.setUbicacion("Madrid");
        updatedEvent.setCapacidad(100);
        updatedEvent.setFechaEvento(LocalDate.now());
        updatedEvent.setFacility(testFacility);
        
        when(eventService.updateEvent(eq(1L), any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEvent)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Evento Actualizado"))
                .andExpect(jsonPath("$.facility.id").value(1));
    }

    @Test
    public void testDeleteEventReturnsNoContent() throws Exception {
        doNothing().when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());

        verify(eventService).deleteEvent(1L);
    }

    @Test
    public void testFilterEventsReturnsOk() throws Exception {
        List<Event> filteredEvents = List.of(testEvent);
        when(eventService.filterEvents("Maratón", "Madrid", 100)).thenReturn(filteredEvents);

        mockMvc.perform(get("/events/filter")
                .param("nombre", "Maratón")
                .param("ubicacion", "Madrid")
                .param("capacidad", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Maratón"))
                .andExpect(jsonPath("$[0].facility.id").value(1));
    }

    @Test
    public void testUpdatePartialEventReturnsOk() throws Exception {
        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setNombre("Evento Actualizado");
        updatedEvent.setUbicacion("Madrid");
        updatedEvent.setCapacidad(100);
        updatedEvent.setFechaEvento(LocalDate.now());
        updatedEvent.setFacility(testFacility);
        
        Map<String, Object> updates = Map.of(
            "nombre", "Evento Actualizado",
            "capacidad", 100
        );
        
        when(eventService.updatePartial(eq(1L), any(Map.class))).thenReturn(updatedEvent);

        mockMvc.perform(patch("/events/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Evento Actualizado"))
                .andExpect(jsonPath("$.capacidad").value(100))
                .andExpect(jsonPath("$.facility.id").value(1))
                .andExpect(jsonPath("$.facility.nombre").value("Instalación Test"));

        verify(eventService).updatePartial(eq(1L), any(Map.class));
    }

    @Test
    public void testUpdatePartialEventReturnsNotFound() throws Exception {
        Map<String, Object> updates = Map.of("nombre", "Evento Actualizado");
        
        when(eventService.updatePartial(eq(99L), any(Map.class)))
            .thenThrow(new EventNotFoundException("Evento no encontrado con id 99"));

        mockMvc.perform(patch("/events/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Evento no encontrado con id 99"));

    }

    @Test
    public void testDeleteEventReturnsNotFound() throws Exception {
        doThrow(new EventNotFoundException("Evento no encontrado con id 99"))
            .when(eventService).deleteEvent(99L);

        mockMvc.perform(delete("/events/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Evento no encontrado con id 99"))
                .andExpect(jsonPath("$.code").value(404));

    }

    @Test
    public void testFilterEventsWithInvalidCapacityReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/events/filter")
                .param("capacidad", "invalid")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tipo de parámetro inválido: capacidad"))
                .andExpect(jsonPath("$.code").value(400));


        verify(eventService, never()).filterEvents(any(), any(), any());
    }
} 