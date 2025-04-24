package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Training;
import com.example.Apitrain.service.TrainingService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.exception.TrainingNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class TrainingControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TrainingService trainingService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private ObjectMapper objectMapper;

    private Training training;

    @BeforeEach
    public void setUp() {
        training = new Training();
        training.setTipo("Fuerza");
        training.setNivel("Principiante");
        training.setDuracion(60);
        training.setCompletado(false);
        training.setFecha(LocalDate.now());
    }

    @Test
    public void testGetAllTrainings() throws Exception {
        when(trainingService.getAllTrainings()).thenReturn(Arrays.asList(training));
        mockMvc.perform(get("/trainings")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Fuerza"))
                .andExpect(jsonPath("$[0].nivel").value("Principiante"));
    }

    @Test
    public void testGetTrainingById() throws Exception {
        when(trainingService.getTrainingById(1L)).thenReturn(training);

        mockMvc.perform(get("/trainings/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Fuerza"))
                .andExpect(jsonPath("$.nivel").value("Principiante"));
    }

    @Test
    public void testGetTrainingByIdNotFound() throws Exception {
        when(trainingService.getTrainingById(1L)).thenThrow(new TrainingNotFoundException("Training not found"));

        mockMvc.perform(get("/trainings/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTraining() throws Exception {
        when(trainingService.createTraining(any(Training.class))).thenReturn(training);

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Fuerza"))
                .andExpect(jsonPath("$.nivel").value("Principiante"));
    }

    @Test
    public void testCreateTrainingWithInvalidData() throws Exception {
        Training invalidTraining = new Training();
        // Sin nivel, que es un campo requerido

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTraining)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateTraining() throws Exception {
        when(trainingService.updateTraining(eq(1L), any(Training.class))).thenReturn(training);

        mockMvc.perform(put("/trainings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Fuerza"))
                .andExpect(jsonPath("$.nivel").value("Principiante"));
    }

    @Test
    public void testUpdateTrainingNotFound() throws Exception {
        when(trainingService.updateTraining(eq(1L), any(Training.class)))
                .thenThrow(new TrainingNotFoundException("Training not found"));

        mockMvc.perform(put("/trainings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTraining() throws Exception {
        doNothing().when(trainingService).deleteTraining(1L);

        mockMvc.perform(delete("/trainings/1"))
                .andExpect(status().isOk());

        verify(trainingService).deleteTraining(1L);
    }

    @Test
    public void testDeleteTrainingNotFound() throws Exception {
        doThrow(new TrainingNotFoundException("Training not found")).when(trainingService).deleteTraining(1L);

        mockMvc.perform(delete("/trainings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterTrainings() throws Exception {
        List<Training> trainings = Arrays.asList(training);
        when(trainingService.filterTrainings("Fuerza", "Principiante", 60)).thenReturn(trainings);

        mockMvc.perform(get("/trainings/filter")
                .param("tipo", "Fuerza")
                .param("nivel", "Principiante")
                .param("duracion", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Fuerza"))
                .andExpect(jsonPath("$[0].nivel").value("Principiante"));
    }

    @Test
    public void testGetTrainingsByNivelAndDuracion() throws Exception {
        List<Training> trainings = Arrays.asList(training);
        when(trainingService.getByNivelAndDuracion("Principiante", 60)).thenReturn(trainings);

        mockMvc.perform(get("/trainings/filterByNivelAndDuracion")
                .param("nivel", "Principiante")
                .param("duracion", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Fuerza"))
                .andExpect(jsonPath("$[0].nivel").value("Principiante"));
    }
} 