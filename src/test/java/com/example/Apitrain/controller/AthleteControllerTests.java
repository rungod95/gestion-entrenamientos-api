package com.example.Apitrain.controller;

import com.example.Apitrain.Security.JwtTokenService;
import com.example.Apitrain.config.TestSecurityConfig;
import com.example.Apitrain.domain.Athlete;
import com.example.Apitrain.domain.dto.AthleteInDto;
import com.example.Apitrain.exception.AthleteNotFoundException;
import com.example.Apitrain.service.AthleteService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
public class AthleteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenService jwtTokenService;


    @MockBean
    private AthleteService athleteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test

    public void testGetAllAthletesReturnsOk() throws Exception {
        Athlete athlete1 = new Athlete();
        athlete1.setId(1L);
        athlete1.setNombre("Juan Pérez");
        athlete1.setCategoria("Principiante");
        athlete1.setEdad(25);
        athlete1.setAltura(1.75f);
        athlete1.setFechaRegistro(LocalDate.now());

        Athlete athlete2 = new Athlete();
        athlete2.setId(2L);
        athlete2.setNombre("María García");
        athlete2.setCategoria("Avanzado");
        athlete2.setEdad(30);
        athlete2.setAltura(1.65f);
        athlete2.setFechaRegistro(LocalDate.now());

        List<Athlete> athleteList = List.of(athlete1, athlete2);
        when(athleteService.getAllAthletes()).thenReturn(athleteList);

        String response = mockMvc.perform(get("/athletes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Athlete> result = objectMapper.readValue(response, new TypeReference<>() {});
        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getNombre());
    }

    @Test
    public void testGetAthleteByIdReturnsOk() throws Exception {
        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setNombre("Juan Pérez");
        athlete.setCategoria("Principiante");
        athlete.setEdad(25);
        athlete.setAltura(1.75f);
        athlete.setFechaRegistro(LocalDate.now());
        
        when(athleteService.getAthleteById(1L)).thenReturn(athlete);

        mockMvc.perform(get("/athletes/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    public void testGetAthleteByIdReturnsNotFound() throws Exception {
        when(athleteService.getAthleteById(99L)).thenThrow(new AthleteNotFoundException("Athlete not found"));

        mockMvc.perform(get("/athletes/99")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateAthleteReturnsCreated() throws Exception {
        AthleteInDto athleteDto = new AthleteInDto();
        athleteDto.setNombre("Nuevo Atleta");
        athleteDto.setCategoria("Intermedio");
        athleteDto.setEdad(28);
        athleteDto.setAltura(1.80f);

        Athlete createdAthlete = new Athlete();
        createdAthlete.setId(1L);
        createdAthlete.setNombre("Nuevo Atleta");
        createdAthlete.setCategoria("Intermedio");
        createdAthlete.setEdad(28);
        createdAthlete.setAltura(1.80f);
        createdAthlete.setFechaRegistro(LocalDate.now());
        
        when(athleteService.createAthlete(any(Athlete.class))).thenReturn(createdAthlete);

        mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athleteDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Nuevo Atleta"));
    }

    @Test
    public void testCreateAthleteReturnsBadRequest() throws Exception {
        AthleteInDto invalidDto = new AthleteInDto();
        // Sin nombre ni categoría, ambos requeridos

        mockMvc.perform(post("/athletes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateAthleteReturnsOk() throws Exception {
        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setNombre("Atleta Actualizado");
        athlete.setCategoria("Avanzado");
        athlete.setEdad(30);
        athlete.setAltura(1.85f);
        athlete.setFechaRegistro(LocalDate.now());
        
        when(athleteService.updateAthlete(eq(1L), any(Athlete.class))).thenReturn(athlete);

        mockMvc.perform(put("/athletes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(athlete)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Atleta Actualizado"));
    }

    @Test
    public void testDeleteAthleteReturnsNoContent() throws Exception {
        doNothing().when(athleteService).deleteAthlete(1L);

        mockMvc.perform(delete("/athletes/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFilterAthletesReturnsOk() throws Exception {
        Athlete athlete = new Athlete();
        athlete.setId(1L);
        athlete.setNombre("Juan Pérez");
        athlete.setCategoria("Principiante");
        athlete.setEdad(25);
        athlete.setAltura(1.75f);
        athlete.setFechaRegistro(LocalDate.now());

        List<Athlete> filteredAthletes = List.of(athlete);
        when(athleteService.filterAthletes("Juan", "Principiante", 25)).thenReturn(filteredAthletes);

        mockMvc.perform(get("/athletes/filter")
                .param("nombre", "Juan")
                .param("categoria", "Principiante")
                .param("edad", "25")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    public void testUpdatePartialAthleteReturnsOk() throws Exception {
        Athlete updatedAthlete = new Athlete();
        updatedAthlete.setId(1L);
        updatedAthlete.setNombre("Atleta Actualizado");
        updatedAthlete.setCategoria("Avanzado");
        updatedAthlete.setEdad(30);
        updatedAthlete.setAltura(1.85f);
        updatedAthlete.setFechaRegistro(LocalDate.now());
        
        Map<String, Object> updates = Map.of("nombre", "Atleta Actualizado");
        
        when(athleteService.updatePartial(eq(1L), any(Map.class))).thenReturn(updatedAthlete);

        mockMvc.perform(patch("/athletes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Atleta Actualizado"));
    }

    @Test
    public void testGetAthletesByEdadGreaterThanReturnsOk() throws Exception {
        Athlete athlete1 = new Athlete();
        athlete1.setId(1L);
        athlete1.setNombre("Juan Pérez");
        athlete1.setCategoria("Principiante");
        athlete1.setEdad(25);
        athlete1.setAltura(1.75f);
        athlete1.setFechaRegistro(LocalDate.now());

        Athlete athlete2 = new Athlete();
        athlete2.setId(2L);
        athlete2.setNombre("María García");
        athlete2.setCategoria("Avanzado");
        athlete2.setEdad(30);
        athlete2.setAltura(1.65f);
        athlete2.setFechaRegistro(LocalDate.now());

        List<Athlete> athletes = List.of(athlete1, athlete2);
        when(athleteService.getByEdadGreaterThan(25)).thenReturn(athletes);

        mockMvc.perform(get("/athletes/filterByEdad")
                .param("edadMin", "25")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"))
                .andExpect(jsonPath("$[1].nombre").value("María García"));
    }
}