package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Trainer;
import com.example.Apitrain.exception.TrainerNotFoundException;
import com.example.Apitrain.service.TrainerService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(TrainerController.class)
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class TrainerControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtTokenService jwtTokenService;
    @MockBean
    private TrainerService trainerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Trainer testTrainer;

    @BeforeEach
    void setUp() {
        testTrainer = new Trainer();
        testTrainer.setId(1L);
        testTrainer.setNombre("Juan Pérez");
        testTrainer.setEspecialidad("Fitness");
        testTrainer.setExperiencia(5);
        testTrainer.setFechaIngreso(LocalDate.now());
        testTrainer.setActivo(true);
    }

    @Test
    public void testGetAllTrainersReturnsOk() throws Exception {
        List<Trainer> trainerList = List.of(testTrainer);
        when(trainerService.getAllTrainers()).thenReturn(trainerList);


        mockMvc.perform(get("/trainers")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].especialidad").value("Fitness"));
    }

    @Test
    public void testGetTrainerByIdReturnsOk() throws Exception {
        when(trainerService.getTrainerById(1L)).thenReturn(testTrainer);

        mockMvc.perform(get("/trainers/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.especialidad").value("Fitness"));
    }

    @Test
    public void testGetTrainerByIdReturnsNotFound() throws Exception {
        when(trainerService.getTrainerById(99L)).thenThrow(new TrainerNotFoundException("Entrenador no encontrado con id 99"));

        mockMvc.perform(get("/trainers/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Entrenador no encontrado con id 99"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void testCreateTrainerReturnsCreated() throws Exception {
        Trainer createdTrainer = new Trainer();
        createdTrainer.setId(1L);
        createdTrainer.setNombre("María García");
        createdTrainer.setEspecialidad("Yoga");
        createdTrainer.setExperiencia(3);
        createdTrainer.setFechaIngreso(LocalDate.now());
        createdTrainer.setActivo(true);
        
        when(trainerService.createTrainer(any(Trainer.class))).thenReturn(createdTrainer);

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrainer)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("María García"))
                .andExpect(jsonPath("$.especialidad").value("Yoga"));
    }

    @Test
    public void testCreateTrainerReturnsBadRequest() throws Exception {
        Trainer invalidTrainer = new Trainer();
        // Sin campos requeridos
        invalidTrainer.setExperiencia(5);

        mockMvc.perform(post("/trainers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTrainer)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        verify(trainerService, never()).createTrainer(any(Trainer.class));
    }

    @Test
    public void testUpdateTrainerReturnsOk() throws Exception {
        Trainer updatedTrainer = new Trainer();
        updatedTrainer.setId(1L);
        updatedTrainer.setNombre("Juan Pérez Actualizado");
        updatedTrainer.setEspecialidad("Fitness Avanzado");
        updatedTrainer.setExperiencia(6);
        updatedTrainer.setFechaIngreso(LocalDate.now());
        updatedTrainer.setActivo(true);
        
        when(trainerService.updateTrainer(eq(1L), any(Trainer.class))).thenReturn(updatedTrainer);

        mockMvc.perform(put("/trainers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTrainer)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez Actualizado"))
                .andExpect(jsonPath("$.especialidad").value("Fitness Avanzado"));
    }

    @Test
    public void testDeleteTrainerReturnsNoContent() throws Exception {
        doNothing().when(trainerService).deleteTrainer(1L);

        mockMvc.perform(delete("/trainers/1"))
                .andExpect(status().isNoContent());

        verify(trainerService).deleteTrainer(1L);
    }

    @Test
    public void testDeleteTrainerReturnsNotFound() throws Exception {
        doThrow(new TrainerNotFoundException("Entrenador no encontrado con id 99"))
            .when(trainerService).deleteTrainer(99L);

        mockMvc.perform(delete("/trainers/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Entrenador no encontrado con id 99"))
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    public void testFilterTrainersReturnsOk() throws Exception {
        List<Trainer> filteredTrainers = List.of(testTrainer);
        when(trainerService.filterTrainers("Juan", "Fitness", 5)).thenReturn(filteredTrainers);

        mockMvc.perform(get("/trainers/filter")
                .param("nombre", "Juan")
                .param("especialidad", "Fitness")
                .param("experiencia", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].especialidad").value("Fitness"));
    }

    @Test
    public void testFilterTrainersWithInvalidExperienceReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/trainers/filter")
                .param("experiencia", "invalid")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Tipo de parámetro inválido: experiencia"))
                .andExpect(jsonPath("$.code").value(400));

        verify(trainerService, never()).filterTrainers(any(), any(), any());
    }
} 