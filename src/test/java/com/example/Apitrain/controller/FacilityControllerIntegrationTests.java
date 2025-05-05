package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Facility;
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
public class FacilityControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenService jwtTokenService;


    @Autowired
    private FacilityRepository facilityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        facilityRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetFacility() throws Exception {
        // Crear instalación
        Facility facility = new Facility();
        facility.setNombre("Gimnasio Central");
        facility.setTipo("Gimnasio");
        facility.setCapacidad(200);
        facility.setHorario("8:00-22:00");
        facility.setFechaApertura(LocalDate.now());

        String facilityJson = objectMapper.writeValueAsString(facility);

        String response = mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(facilityJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Facility createdFacility = objectMapper.readValue(response, Facility.class);
        assertNotNull(createdFacility.getId());
        assertEquals("Gimnasio Central", createdFacility.getNombre());

        // Obtener instalación
        mockMvc.perform(get("/facilities/" + createdFacility.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Gimnasio Central"));
    }

    @Test
    public void testCreateFacilityWithInvalidData() throws Exception {
        Facility invalidFacility = new Facility();
        // Sin nombre ni tipo, que son campos requeridos

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidFacility)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistentFacility() throws Exception {
        mockMvc.perform(get("/facilities/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateFacility() throws Exception {
        // Crear instalación inicial
        Facility facility = new Facility();
        facility.setNombre("Instalación Original");
        facility.setTipo("Gimnasio");
        facility.setCapacidad(100);
        facility.setHorario("8:00-22:00");
        facility.setFechaApertura(LocalDate.now());

        String response = mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Facility createdFacility = objectMapper.readValue(response, Facility.class);

        // Actualizar instalación
        createdFacility.setNombre("Instalación Actualizada");
        createdFacility.setCapacidad(200);

        mockMvc.perform(put("/facilities/" + createdFacility.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdFacility)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Instalación Actualizada"))
                .andExpect(jsonPath("$.capacidad").value(200));
    }

    @Test
    public void testDeleteFacility() throws Exception {
        // Crear instalación
        Facility facility = new Facility();
        facility.setNombre("Instalación a Eliminar");
        facility.setTipo("Gimnasio");
        facility.setCapacidad(100);
        facility.setHorario("8:00-22:00");
        facility.setFechaApertura(LocalDate.now());

        String response = mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Facility createdFacility = objectMapper.readValue(response, Facility.class);

        // Eliminar instalación
        mockMvc.perform(delete("/facilities/" + createdFacility.getId()))
                .andExpect(status().isNoContent());

        // Verificar que la instalación fue eliminada
        mockMvc.perform(get("/facilities/" + createdFacility.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterFacilities() throws Exception {
        // Crear instalaciones de prueba
        Facility facility1 = new Facility();
        facility1.setNombre("Gimnasio Madrid");
        facility1.setTipo("Gimnasio");
        facility1.setCapacidad(100);
        facility1.setHorario("8:00-22:00");
        facility1.setFechaApertura(LocalDate.now());

        Facility facility2 = new Facility();
        facility2.setNombre("Piscina Barcelona");
        facility2.setTipo("Piscina");
        facility2.setCapacidad(50);
        facility2.setHorario("7:00-23:00");
        facility2.setFechaApertura(LocalDate.now());

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility2)))
                .andExpect(status().isCreated());

        // Filtrar instalaciones
        mockMvc.perform(get("/facilities/filter")
                .param("nombre", "Gimnasio")
                .param("tipo", "Gimnasio")
                .param("capacidad", "100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gimnasio Madrid"));
    }

    @Test
    public void testGetFacilitiesByFechaApertura() throws Exception {
        // Crear instalaciones con diferentes fechas de apertura
        Facility facility1 = new Facility();
        facility1.setNombre("Gimnasio Central");
        facility1.setTipo("Gimnasio");
        facility1.setCapacidad(100);
        facility1.setFechaApertura(LocalDate.of(2025, 3, 21));

        Facility facility2 = new Facility();
        facility2.setNombre("Piscina Olímpica");
        facility2.setTipo("Piscina");
        facility2.setCapacidad(50);
        facility2.setFechaApertura(LocalDate.of(2025, 3, 22));

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/facilities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(facility2)))
                .andExpect(status().isCreated());

        // Obtener instalaciones por fecha de apertura
        mockMvc.perform(get("/facilities/filterByFechaApertura")
                        .param("fecha", "2025-03-20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Gimnasio Central"))
                .andExpect(jsonPath("$[1].nombre").value("Piscina Olímpica"));
    }
} 