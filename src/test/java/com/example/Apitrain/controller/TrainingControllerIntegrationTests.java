package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Training;
import com.example.Apitrain.repository.TrainingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TrainingControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        trainingRepository.deleteAll();
    }

    @Test
    public void testCreateAndGetTraining() throws Exception {
        // Crear entrenamiento
        Training training = new Training();
        training.setTipo("Fuerza");
        training.setNivel("Principiante");
        training.setDuracion(60);
        training.setCompletado(false);
        training.setFecha(LocalDate.now());

        String trainingJson = objectMapper.writeValueAsString(training);

        String response = mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(trainingJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Training createdTraining = objectMapper.readValue(response, Training.class);
        assertNotNull(createdTraining.getId());
        assertEquals("Fuerza", createdTraining.getTipo());

        // Obtener entrenamiento
        mockMvc.perform(get("/trainings/" + createdTraining.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Fuerza"));
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
    public void testGetNonExistentTraining() throws Exception {
        mockMvc.perform(get("/trainings/999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateTraining() throws Exception {
        // Crear entrenamiento inicial
        Training training = new Training();
        training.setTipo("Fuerza");
        training.setNivel("Principiante");
        training.setDuracion(60);
        training.setCompletado(false);
        training.setFecha(LocalDate.now());

        String response = mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Training createdTraining = objectMapper.readValue(response, Training.class);

        // Actualizar entrenamiento
        createdTraining.setNivel("Avanzado");
        createdTraining.setDuracion(90);

        mockMvc.perform(put("/trainings/" + createdTraining.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createdTraining)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nivel").value("Avanzado"))
                .andExpect(jsonPath("$.duracion").value(90));
    }

    @Test
    public void testDeleteTraining() throws Exception {
        // Crear entrenamiento
        Training training = new Training();
        training.setTipo("Fuerza");
        training.setNivel("Principiante");
        training.setDuracion(60);
        training.setCompletado(false);
        training.setFecha(LocalDate.now());

        String response = mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Training createdTraining = objectMapper.readValue(response, Training.class);

        // Eliminar entrenamiento
        mockMvc.perform(delete("/trainings/" + createdTraining.getId()))
                .andExpect(status().isOk());

        // Verificar que el entrenamiento fue eliminado
        mockMvc.perform(get("/trainings/" + createdTraining.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFilterTrainings() throws Exception {
        // Crear entrenamientos de prueba
        Training training1 = new Training();
        training1.setTipo("Fuerza");
        training1.setNivel("Principiante");
        training1.setDuracion(60);
        training1.setCompletado(false);
        training1.setFecha(LocalDate.now());

        Training training2 = new Training();
        training2.setTipo("Cardio");
        training2.setNivel("Avanzado");
        training2.setDuracion(45);
        training2.setCompletado(false);
        training2.setFecha(LocalDate.now());

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training2)))
                .andExpect(status().isOk());

        // Filtrar entrenamientos
        mockMvc.perform(get("/trainings/filter")
                .param("tipo", "Fuerza")
                .param("nivel", "Principiante")
                .param("duracion", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Fuerza"));
    }

    @Test
    public void testGetTrainingsByNivelAndDuracion() throws Exception {
        // Crear entrenamientos con diferentes niveles y duraciones
        Training training1 = new Training();
        training1.setTipo("Fuerza");
        training1.setNivel("Principiante");
        training1.setDuracion(60);
        training1.setCompletado(false);
        training1.setFecha(LocalDate.now());

        Training training2 = new Training();
        training2.setTipo("Cardio");
        training2.setNivel("Avanzado");
        training2.setDuracion(45);
        training2.setCompletado(false);
        training2.setFecha(LocalDate.now());

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/trainings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(training2)))
                .andExpect(status().isOk());

        // Obtener entrenamientos por nivel y duración
        mockMvc.perform(get("/trainings/filterByNivelAndDuracion")
                .param("nivel", "Principiante")
                .param("duracion", "60")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("Fuerza"));
    }
} 