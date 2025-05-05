package com.example.Apitrain.controller;

import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Facility;
import com.example.Apitrain.domain.Event;
import com.example.Apitrain.exception.FacilityNotFoundException;
import com.example.Apitrain.service.FacilityService;
import com.example.Apitrain.repository.UserRepository;
import com.example.Apitrain.Security.JwtTokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(FacilityController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class FacilityControllerTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacilityService facilityService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenService jwtTokenService;

    private Facility facility;

    @BeforeEach
    public void setUp() {
        facility = new Facility();
        facility.setId(1L);
        facility.setNombre("Gimnasio Madrid");
        facility.setTipo("Gimnasio");
        facility.setCapacidad(200);
        facility.setHorario("8:00-22:00");
        facility.setFechaApertura(LocalDate.now());
    }

    @Test
    public void testGetAllFacilitiesReturnsOk() throws Exception {
        Facility facility2 = new Facility();
        facility2.setId(2L);
        facility2.setNombre("Piscina Barcelona");
        facility2.setTipo("Piscina");
        facility2.setCapacidad(150);
        facility2.setHorario("7:00-23:00");
        facility2.setFechaApertura(LocalDate.now());

        List<Facility> facilityList = List.of(facility, facility2);
        when(facilityService.getAllFacilities()).thenReturn(facilityList);

        String response = mockMvc.perform(get("/facilities")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Facility> result = objectMapper.readValue(response, new TypeReference<>() {});
        assertEquals(2, result.size());
        assertEquals("Gimnasio Madrid", result.get(0).getNombre());
    }

    @Test
    public void testGetFacilityByIdReturnsOk() throws Exception {
        when(facilityService.getFacilityById(1L)).thenReturn(facility);

        mockMvc.perform(get("/facilities/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Gimnasio Madrid"));
    }

    @Test
    public void testGetFacilityByIdReturnsNotFound() throws Exception {
        when(facilityService.getFacilityById(99L)).thenThrow(new FacilityNotFoundException("Facility not found with id 99"));

        mockMvc.perform(get("/facilities/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Facility not found with id 99"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void testCreateFacilityReturnsCreated() throws Exception {
        Facility newFacility = new Facility();
        newFacility.setNombre("Nueva Instalación");
        newFacility.setTipo("Gimnasio");
        newFacility.setCapacidad(200);
        newFacility.setHorario("8:00-22:00");
        newFacility.setFechaApertura(LocalDate.now());

        when(facilityService.createFacility(any(Facility.class))).thenReturn(facility);

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFacility)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Gimnasio Madrid"));
    }

    @Test
    public void testCreateFacilityReturnsBadRequest() throws Exception {
        Facility invalidFacility = new Facility();
        // Sin nombre ni tipo, que son campos requeridos
        invalidFacility.setCapacidad(200);
        invalidFacility.setHorario("8:00-22:00");
        invalidFacility.setFechaApertura(LocalDate.now());

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidFacility)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre es obligatorio"))
                .andExpect(jsonPath("$.tipo").value("El tipo es obligatorio"));


    }

    @Test
    public void testUpdateFacilityReturnsOk() throws Exception {
        facility.setNombre("Instalación Actualizada");
        when(facilityService.updateFacility(eq(1L), any(Facility.class))).thenReturn(facility);

        mockMvc.perform(put("/facilities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Instalación Actualizada"));
    }

    @Test
    public void testDeleteFacilityReturnsNoContent() throws Exception {
        doNothing().when(facilityService).deleteFacility(1L);

        mockMvc.perform(delete("/facilities/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testFilterFacilitiesReturnsOk() throws Exception {
        List<Facility> filteredFacilities = List.of(facility);
        when(facilityService.filterFacilities("Gimnasio", "Gimnasio", 200)).thenReturn(filteredFacilities);

        mockMvc.perform(get("/facilities/filter")
                .param("nombre", "Gimnasio")
                .param("tipo", "Gimnasio")
                .param("capacidad", "200")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gimnasio Madrid"));
    }

    @Test
    public void testUpdatePartialFacilityReturnsOk() throws Exception {
        facility.setNombre("Instalación Actualizada");
        Map<String, Object> updates = Map.of("nombre", "Instalación Actualizada");
        
        when(facilityService.updatePartial(eq(1L), any(Map.class))).thenReturn(facility);

        mockMvc.perform(patch("/facilities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Instalación Actualizada"))
                .andExpect(jsonPath("$.tipo").value("Gimnasio"))
                .andExpect(jsonPath("$.capacidad").value(200));
    }

    @Test
    public void testGetFacilitiesByFechaAperturaReturnsOk() throws Exception {
        Facility facility2 = new Facility();
        facility2.setId(2L);
        facility2.setNombre("Piscina Barcelona");
        facility2.setTipo("Piscina");
        facility2.setCapacidad(150);
        facility2.setHorario("7:00-23:00");
        facility2.setFechaApertura(LocalDate.now());

        List<Facility> facilities = List.of(facility, facility2);
        when(facilityService.getFacilitiesByFechaApertura(any(LocalDate.class))).thenReturn(facilities);

        mockMvc.perform(get("/facilities/filterByFechaApertura")
                .param("fecha", LocalDate.now().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gimnasio Madrid"))
                .andExpect(jsonPath("$[1].nombre").value("Piscina Barcelona"));
    }

    @Test
    public void testCreateFacilityWithEventsReturnsCreated() throws Exception {
        Facility newFacility = new Facility();
        newFacility.setNombre("Nueva Instalación");
        newFacility.setTipo("Gimnasio");
        newFacility.setCapacidad(200);
        newFacility.setHorario("8:00-22:00");
        newFacility.setFechaApertura(LocalDate.now());

        Event event1 = new Event();
        event1.setNombre("Maratón");
        event1.setUbicacion("Madrid");
        event1.setCapacidad(100);
        event1.setFechaEvento(LocalDate.now());

        Event event2 = new Event();
        event2.setNombre("Triatlón");
        event2.setUbicacion("Barcelona");
        event2.setCapacidad(50);
        event2.setFechaEvento(LocalDate.now());

        newFacility.setEvents(List.of(event1, event2));

        when(facilityService.createFacility(any(Facility.class))).thenReturn(facility);

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFacility)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Gimnasio Madrid"));
    }

    @Test
    public void testGetFacilityWithEventsReturnsOk() throws Exception {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setNombre("Maratón");
        event1.setUbicacion("Madrid");
        event1.setCapacidad(100);
        event1.setFechaEvento(LocalDate.now());
        event1.setFacility(facility);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setNombre("Triatlón");
        event2.setUbicacion("Barcelona");
        event2.setCapacidad(50);
        event2.setFechaEvento(LocalDate.now());
        event2.setFacility(facility);

        facility.setEvents(List.of(event1, event2));
        when(facilityService.getFacilityById(1L)).thenReturn(facility);

        mockMvc.perform(get("/facilities/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Gimnasio Madrid"))
                .andExpect(jsonPath("$.events").isArray())
                .andExpect(jsonPath("$.events[0].nombre").value("Maratón"))
                .andExpect(jsonPath("$.events[1].nombre").value("Triatlón"));
    }
} 