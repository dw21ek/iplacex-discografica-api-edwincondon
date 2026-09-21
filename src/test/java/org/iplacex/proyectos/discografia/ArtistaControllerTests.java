package org.iplacex.proyectos.discografia;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.iplacex.proyectos.discografia.artistas.Artista;
import org.iplacex.proyectos.discografia.artistas.ArtistaController;
import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ArtistaController.class)
public class ArtistaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IArtistaRepository artistaRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Artista mockArtista;

    @BeforeEach
    void setUp() {
        mockArtista = new Artista("art1", "Los Prisioneros", Arrays.asList("Rock", "New Wave"), 1983, true);
    }

    @Test
    void testHandleInsertArtistaRequest() throws Exception {
        when(artistaRepo.save(any(Artista.class))).thenReturn(mockArtista);

        mockMvc.perform(post("/api/artista")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mockArtista)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._id").value("art1"))
                .andExpect(jsonPath("$.nombre").value("Los Prisioneros"));
    }

    @Test
    void testHandleGetAristasRequest() throws Exception {
        List<Artista> list = Arrays.asList(mockArtista);
        when(artistaRepo.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/artistas")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]._id").value("art1"))
                .andExpect(jsonPath("$[0].nombre").value("Los Prisioneros"));
    }

    @Test
    void testHandleGetArtistaRequest_Found() throws Exception {
        when(artistaRepo.findById("art1")).thenReturn(Optional.of(mockArtista));

        mockMvc.perform(get("/api/artista/art1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value("art1"))
                .andExpect(jsonPath("$.nombre").value("Los Prisioneros"));
    }

    @Test
    void testHandleGetArtistaRequest_NotFound() throws Exception {
        when(artistaRepo.findById("art999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artista/art999")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHandleUpdateArtistaRequest_Found() throws Exception {
        when(artistaRepo.existsById("art1")).thenReturn(true);
        when(artistaRepo.save(any(Artista.class))).thenReturn(mockArtista);

        mockMvc.perform(put("/api/artista/art1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mockArtista)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value("art1"));
    }

    @Test
    void testHandleUpdateArtistaRequest_NotFound() throws Exception {
        when(artistaRepo.existsById("art999")).thenReturn(false);

        mockMvc.perform(put("/api/artista/art999")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mockArtista)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHandleDeleteArtistaRequest_Found() throws Exception {
        when(artistaRepo.existsById("art1")).thenReturn(true);
        doNothing().when(artistaRepo).deleteById("art1");

        mockMvc.perform(delete("/api/artista/art1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void testHandleDeleteArtistaRequest_NotFound() throws Exception {
        when(artistaRepo.existsById("art999")).thenReturn(false);

        mockMvc.perform(delete("/api/artista/art999")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

}
