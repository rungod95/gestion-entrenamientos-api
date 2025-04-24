package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.repository.TrainerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")

public class TrainerControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainerRepository trainerRepository;

    @MockBean
    private JwtTokenService jwtTokenService;


    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        trainerRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetTrainer() throws Exception {
        // Crear entrenador
        Trainer trainer = new Trainer();
        trainer.setNombre("Juan Pérez");
        trainer.setEspecialidad("Fuerza");
        trainer.setExperiencia(5);
        trainer.setFechaIngreso(LocalDate.now());

        String trainerJson = objectMapper.writeValueAsString(trainer);

        String response = mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(trainerJson))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Trainer createdTrainer = objectMapper.readValue(response, Trainer.class);
        assertNotNull(createdTrainer.getId());
        assertEquals("Juan Pérez", createdTrainer.getNombre());

        // Obtener entrenador
        mockMvc.perform(get("/trainers/" + createdTrainer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    public void testCreateTrainerWithInvalidData() throws Exception {
        Trainer invalidTrainer = new Trainer();
        // Sin nombre ni especialidad, que son campos requeridos

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTrainer)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistentTrainer() throws Exception {
        mockMvc.perform(get("/trainers/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTrainer() throws Exception {
        // Crear entrenador inicial
        Trainer trainer = new Trainer();
        trainer.setNombre("Entrenador Original");
        trainer.setEspecialidad("Cardio");
        trainer.setExperiencia(3);
        trainer.setFechaIngreso(LocalDate.now());

        String response = mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Trainer createdTrainer = objectMapper.readValue(response, Trainer.class);

        // Actualizar entrenador
        createdTrainer.setNombre("Entrenador Actualizado");
        createdTrainer.setExperiencia(5);

        mockMvc.perform(put("/trainers/" + createdTrainer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdTrainer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Entrenador Actualizado"))
                .andExpect(jsonPath("$.experiencia").value(5));
    }

    @Test
    public void testDeleteTrainer() throws Exception {
        // Crear entrenador
        Trainer trainer = new Trainer();
        trainer.setNombre("Entrenador a Eliminar");
        trainer.setEspecialidad("Fuerza");
        trainer.setExperiencia(4);
        trainer.setFechaIngreso(LocalDate.now());

        String response = mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Trainer createdTrainer = objectMapper.readValue(response, Trainer.class);

        // Eliminar entrenador
        mockMvc.perform(delete("/trainers/" + createdTrainer.getId()))
                .andExpect(status().isNoContent());

        // Verificar que el entrenador fue eliminado
        mockMvc.perform(get("/trainers/" + createdTrainer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterTrainers() throws Exception {
        // Crear entrenadores de prueba
        Trainer trainer1 = new Trainer();
        trainer1.setNombre("Juan Pérez");
        trainer1.setEspecialidad("Fuerza");
        trainer1.setExperiencia(5);
        trainer1.setFechaIngreso(LocalDate.now());

        Trainer trainer2 = new Trainer();
        trainer2.setNombre("María García");
        trainer2.setEspecialidad("Cardio");
        trainer2.setExperiencia(3);
        trainer2.setFechaIngreso(LocalDate.now());

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer2)))
                .andExpect(status().isCreated());

        // Filtrar entrenadores
        mockMvc.perform(get("/trainers/filter")
                .param("nombre", "Juan")
                .param("especialidad", "Fuerza")
                .param("experiencia", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    public void testGetTrainersByExperiencia() throws Exception {
        // Crear entrenadores con diferentes experiencias
        Trainer trainer1 = new Trainer();
        trainer1.setNombre("Juan Pérez");
        trainer1.setEspecialidad("Fuerza");
        trainer1.setExperiencia(5);
        trainer1.setFechaIngreso(LocalDate.now());

        Trainer trainer2 = new Trainer();
        trainer2.setNombre("María García");
        trainer2.setEspecialidad("Cardio");
        trainer2.setExperiencia(3);
        trainer2.setFechaIngreso(LocalDate.now());

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainer2)))
                .andExpect(status().isCreated());

        // Obtener entrenadores por experiencia
        mockMvc.perform(get("/trainers/filterByExperiencia")
                .param("experiencia", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }
}