package com.gymtracker.gym_api.controller.exercise;

import com.gymtracker.gym_api.application.dto.response.exercise.EjercicioResponse;
import com.gymtracker.gym_api.application.usecase.exercise.CreateEjercicioUseCase;
import com.gymtracker.gym_api.application.usecase.exercise.GetEjerciciosPorGrupoMuscularUseCase;
import com.gymtracker.gym_api.application.usecase.exercise.UpdateEjercicioUseCase;
import com.gymtracker.gym_api.domain.enums.GrupoMuscular;
import com.gymtracker.gym_api.infrastructure.config.securityConfig.SecurityConfig;
import com.gymtracker.gym_api.infrastructure.security.JwtAuthenticationFilter;
import com.gymtracker.gym_api.infrastructure.security.RestAccessDeniedHandler;
import com.gymtracker.gym_api.infrastructure.security.RestAuthenticationEntryPoint;
import com.gymtracker.gym_api.infrastructure.security.SecurityErrorWriter;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(EjercicioController.class)
@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class,
        RestAccessDeniedHandler.class, SecurityErrorWriter.class})
class EjercicioControllerSecurityTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private CreateEjercicioUseCase createEjercicioUseCase;
    @MockBean private GetEjerciciosPorGrupoMuscularUseCase getEjerciciosPorGrupoMuscularUseCase;
    @MockBean private UpdateEjercicioUseCase updateEjercicioUseCase;
    @MockBean private JwtAuthenticationFilter jwtAuthenticationFilter;

    private final UUID ejercicioId = UUID.randomUUID();

    @BeforeEach
    void permitirQueLaCadenaContinue() throws Exception {
        doAnswer(invocation -> {
            FilterChain chain = invocation.getArgument(2);
            chain.doFilter(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(jwtAuthenticationFilter).doFilter(any(), any(), any());
    }

    @Test
    void crearSinAutenticacionDevuelveUnauthorized() throws Exception {
        mockMvc.perform(post("/ejercicio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ejercicioJson()))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/ejercicio"));
        verify(createEjercicioUseCase, never()).guardar(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioNoPuedeCrearEjercicio() throws Exception {
        mockMvc.perform(post("/ejercicio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ejercicioJson()))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").isNotEmpty())
                .andExpect(jsonPath("$.path").value("/ejercicio"));
        verify(createEjercicioUseCase, never()).guardar(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void administradorPuedeCrearEjercicio() throws Exception {
        when(createEjercicioUseCase.guardar(any())).thenReturn(ejercicioResponse());
        mockMvc.perform(post("/ejercicio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ejercicioJson()))
                .andExpect(status().isCreated());
        verify(createEjercicioUseCase).guardar(any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioNoPuedeActualizarEjercicio() throws Exception {
        mockMvc.perform(patch("/ejercicio/{id}", ejercicioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Descripción actualizada\"}"))
                .andExpect(status().isForbidden());
        verify(updateEjercicioUseCase, never()).actualiazarEjercicio(any(), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void administradorPuedeActualizarEjercicio() throws Exception {
        when(updateEjercicioUseCase.actualiazarEjercicio(any(), any())).thenReturn(ejercicioResponse());
        mockMvc.perform(patch("/ejercicio/{id}", ejercicioId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"Descripción actualizada\"}"))
                .andExpect(status().isOk());
        verify(updateEjercicioUseCase).actualiazarEjercicio(any(), any());
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioPuedeConsultarEjercicios() throws Exception {
        when(getEjerciciosPorGrupoMuscularUseCase
                .obtnerEjerciciosPorGrupoMuscular(GrupoMuscular.PECHO))
                .thenReturn(List.of(ejercicioResponse()));
        mockMvc.perform(get("/ejercicio/{grupoMuscular}", GrupoMuscular.PECHO))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void jsonMalformadoDevuelve400() throws Exception {
        mockMvc.perform(post("/ejercicio").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void uuidInvalidoDevuelve400() throws Exception {
        mockMvc.perform(patch("/ejercicio/no-es-uuid")
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(roles = "USER")
    void enumInvalidoDevuelve400() throws Exception {
        mockMvc.perform(get("/ejercicio/DESCONOCIDO"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void dtoInvalidoDevuelve422() throws Exception {
        mockMvc.perform(post("/ejercicio").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\" \",\"grupoMuscular\":\"PECHO\",\"descripcion\":\" \"}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void metodoIncorrectoDevuelve405() throws Exception {
        mockMvc.perform(put("/ejercicio/" + ejercicioId)
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405));
    }

    private String ejercicioJson() {
        return "{\"nombre\":\"Press de banca\",\"grupoMuscular\":\"PECHO\",\"descripcion\":\"Press con barra\"}";
    }

    private EjercicioResponse ejercicioResponse() {
        return EjercicioResponse.builder()
                .id(ejercicioId)
                .nombre("Press de banca")
                .grupoMuscular(GrupoMuscular.PECHO)
                .descripcion("Press con barra")
                .build();
    }
}
