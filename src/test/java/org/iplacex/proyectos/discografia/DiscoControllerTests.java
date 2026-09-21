package org.iplacex.proyectos.discografia;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.iplacex.proyectos.discografia.artistas.IArtistaRepository;
import org.iplacex.proyectos.discografia.discos.Disco;
import org.iplacex.proyectos.discografia.discos.DiscoController;
import org.iplacex.proyectos.discografia.discos.IDiscoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DiscoController.class)
public class DiscoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDiscoRepository discoRepo;

    @MockBean
    private IArtistaRepository artistaRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private Disco mockDisco;

    @BeforeEach
    void setUp() {
        mockDisco = new Disco("disc1", "art1", "La voz de los '80", 1984, Arrays.asList("La voz de los '80", "Sexo", "Paramar"));
    }

    @Test
    void testHandlePostDiscoRequest_ArtistExists() throws Exception {
        when(artistaRepo.existsById("art1")).thenReturn(true);
        when(discoRepo.save(any(Disco.class))).thenReturn(mockDisco);

        mockMvc.perform(post("/api/disco")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(mockDisco)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._id").value("disc1"))
                .andExpect(jsonPath("$.idArtista").value("art1"))
                .andExpect(jsonPath("$.nombre").value("La voz de los '80"));
    }

    @Test
    void testHandlePostDiscoRequest_ArtistNotFound() throws Exception {
        when(artistaRepo.existsById("art999")).thenReturn(false);
        Disco discoInvalido = new Disco("disc2", "art999", "Disco Fantasma", 2020, Arrays.asList("Track 1"));

        mockMvc.perform(post("/api/disco")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(discoInvalido)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHandleGetDiscosRequest() throws Exception {
        List<Disco> list = Arrays.asList(mockDisco);
        when(discoRepo.findAll()).thenReturn(list);

        mockMvc.perform(get("/api/discos")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]._id").value("disc1"))
                .andExpect(jsonPath("$[0].nombre").value("La voz de los '80"));
    }

    @Test
    void testHandleGetDiscoRequest_Found() throws Exception {
        when(discoRepo.findById("disc1")).thenReturn(Optional.of(mockDisco));

        mockMvc.perform(get("/api/disco/disc1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value("disc1"))
                .andExpect(jsonPath("$.nombre").value("La voz de los '80"));
    }

    @Test
    void testHandleGetDiscoRequest_NotFound() throws Exception {
        when(discoRepo.findById("disc999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/disco/disc999")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void testHandleGetDiscosByArtistaRequest() throws Exception {
        List<Disco> list = Arrays.asList(mockDisco);
        when(discoRepo.findDiscosByIdArtista("art1")).thenReturn(list);

        mockMvc.perform(get("/api/artista/art1/discos")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]._id").value("disc1"))
                .andExpect(jsonPath("$[0].idArtista").value("art1"));
    }

}
