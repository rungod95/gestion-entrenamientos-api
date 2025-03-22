package com.example.Apitrain.controller;

import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.domain.dto.AthleteInDto;
import com.example.Apitrain.exception.AthleteNotFoundException;
import com.example.Apitrain.service.AthleteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AthleteController.class)
public class AthleteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AthleteService athleteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllAthletesReturnOk() throws Exception {
        List<Athlete> athleteList = List.of(
                new Athlete(1L, "Carlos", "Junior", 25, 1.75f, LocalDate.now(), null),
                new Athlete(2L, "Lucía", "Pro", 30, 1.68f, LocalDate.now(), null)

        );
                when(athleteService.getAllAthletes()).thenReturn(athleteList);


        String response = mockMvc.perform(get("/athletes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Athlete> result = objectMapper.readValue(response, new TypeReference<>() {});
        assertEquals(2, result.size());
        assertEquals("Carlos", result.get(0).getNombre());
    }

    @Test
    public void testGetAthleteByIdReturnsNotFound() throws Exception {
        when(athleteService.getAthleteById(99L)).thenThrow(new AthleteNotFoundException("Athlete not found"));

        mockMvc.perform(get("/athletes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAthleteReturnsBadRequest() throws Exception {
        Athlete invalidAthlete = new Athlete();
        // Sin nombre ni categoría, ambos requeridos (@NotNull en DTO si se usa uno)

        String requestBody = objectMapper.writeValueAsString(invalidAthlete);

        mockMvc.perform(post("/athletes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateAthleteDtoReturnsCreated() throws Exception {
        // Datos de entrada (DTO)
        AthleteInDto athleteInDto = new AthleteInDto("Carlos", "Junior", 25, 1.75f);

        // Objeto que simula el retorno del servicio (modelo real)
        Athlete createdAthlete = new Athlete(1L, "Carlos", "Junior", 25, 1.75f, LocalDate.now(), null);

        when(athleteService.createAthlete(any(Athlete.class))).thenReturn(createdAthlete);

        String requestBody = objectMapper.writeValueAsString(athleteInDto);

        mockMvc.perform(post("/athletes/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.categoria").value("Junior"));
    }

    @Test
    public void testCreateAthleteDtoReturnsBadRequest() throws Exception {
        // Faltan campos requeridos (todos nulos)
        AthleteInDto invalidDto = new AthleteInDto(null, null, null, null);

        String requestBody = objectMapper.writeValueAsString(invalidDto);

        mockMvc.perform(post("/athletes/dto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


}