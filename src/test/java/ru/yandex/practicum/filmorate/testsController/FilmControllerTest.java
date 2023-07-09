package ru.yandex.practicum.filmorate.testsController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.FilmorateApplicationTests;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addFilm_ValidData() throws Exception {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film)))
                .andExpect(status().isOk());
    }

    @Test
    public void addFilm_EmptyName() throws Exception {
        Film film = new Film();
        film.setName("");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addFilm_LongDescription() throws Exception {
        Film film = new Film();
        film.setName("");
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce vel urna elementum, gravida turpis sit amet, lobortis turpis. Suspendisse sit amet sapien est. Nunc a finibus lacus. Donec aliquam aliquam nunc, ac feugiat nisl tincidunt nec. Proin ut enim ligula. Sed vestibulum ullamcorper neque, at eleifend ligula varius id. Sed feugiat, erat nec faucibus aliquet, risus risus varius metus, a varius nibh velit a elit. Nam rutrum metus quis lorem lacinia hendrerit.");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(120);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFilm_InvalidReleaseDate() throws Exception {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(120);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(film)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFilm_NegativeDuration() throws Exception {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2022, 1, 1));
        film.setDuration(-120);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(film)))
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
}