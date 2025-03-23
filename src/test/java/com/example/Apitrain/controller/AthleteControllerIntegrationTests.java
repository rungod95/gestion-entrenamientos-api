package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.domain.dto.AthleteInDto;
import com.example.Apitrain.repository.AthleteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AthleteControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private AthleteRepository athleteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        athleteRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetAthlete() throws Exception {
        // Crear atleta
        Athlete athlete = new Athlete();
        athlete.setNombre("Carlos García");
        athlete.setCategoria("Junior");
        athlete.setEdad(25);
        athlete.setAltura(1.75f);
        athlete.setFechaRegistro(LocalDate.now());

        String athleteJson = objectMapper.writeValueAsString(athlete);

        String response = mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(athleteJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Athlete createdAthlete = objectMapper.readValue(response, Athlete.class);
        assertNotNull(createdAthlete.getId());
        assertEquals("Carlos García", createdAthlete.getNombre());

        // Obtener atleta
        mockMvc.perform(get("/athletes/" + createdAthlete.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos García"));
    }

    @Test
    public void testCreateAthleteWithDto() throws Exception {
        // Crear atleta usando DTO
        AthleteInDto athleteDto = new AthleteInDto();
        athleteDto.setNombre("María López");
        athleteDto.setCategoria("Pro");
        athleteDto.setEdad(30);
        athleteDto.setAltura(1.68f);

        String response = mockMvc.perform(post("/athletes/dto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athleteDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Athlete createdAthlete = objectMapper.readValue(response, Athlete.class);
        assertNotNull(createdAthlete.getId());
        assertEquals("María López", createdAthlete.getNombre());
    }

    @Test
    public void testCreateAthleteWithInvalidData() throws Exception {
        Athlete invalidAthlete = new Athlete();
        // Sin nombre ni categoría

        mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidAthlete)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistentAthlete() throws Exception {
        mockMvc.perform(get("/athletes/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateAthlete() throws Exception {
        // Crear atleta inicial
        Athlete athlete = new Athlete();
        athlete.setNombre("Atleta Original");
        athlete.setCategoria("Junior");
        athlete.setEdad(25);
        athlete.setAltura(1.75f);
        athlete.setFechaRegistro(LocalDate.now());

        String response = mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athlete)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Athlete createdAthlete = objectMapper.readValue(response, Athlete.class);

        // Actualizar atleta
        createdAthlete.setNombre("Atleta Actualizado");
        createdAthlete.setCategoria("Pro");

        mockMvc.perform(put("/athletes/" + createdAthlete.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdAthlete)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Atleta Actualizado"))
                .andExpect(jsonPath("$.categoria").value("Pro"));
    }

    @Test
    public void testDeleteAthlete() throws Exception {
        // Crear atleta
        Athlete athlete = new Athlete();
        athlete.setNombre("Atleta a Eliminar");
        athlete.setCategoria("Junior");
        athlete.setEdad(25);
        athlete.setAltura(1.75f);
        athlete.setFechaRegistro(LocalDate.now());

        String response = mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athlete)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Athlete createdAthlete = objectMapper.readValue(response, Athlete.class);

        // Eliminar atleta
        mockMvc.perform(delete("/athletes/" + createdAthlete.getId()))
                .andExpect(status().isOk());

        // Verificar que el atleta fue eliminado
        mockMvc.perform(get("/athletes/" + createdAthlete.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterAthletes() throws Exception {
        // Crear atletas de prueba
        Athlete athlete1 = new Athlete();
        athlete1.setNombre("Carlos García");
        athlete1.setCategoria("Junior");
        athlete1.setEdad(25);
        athlete1.setAltura(1.75f);
        athlete1.setFechaRegistro(LocalDate.now());

        Athlete athlete2 = new Athlete();
        athlete2.setNombre("María López");
        athlete2.setCategoria("Pro");
        athlete2.setEdad(30);
        athlete2.setAltura(1.68f);
        athlete2.setFechaRegistro(LocalDate.now());

        mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athlete1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athlete2)))
                .andExpect(status().isOk());

        // Filtrar atletas
        mockMvc.perform(get("/athletes/filter")
                .param("nombre", "Carlos")
                .param("categoria", "Junior")
                .param("edad", "25")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Carlos García"));
    }
} 