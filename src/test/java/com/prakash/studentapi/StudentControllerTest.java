package com.prakash.studentapi;

import com.prakash.studentapi.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    private static final String ASHA =
            "{\"name\":\"Asha Rao\",\"email\":\"asha@example.com\",\"course\":\"CSE\",\"year\":3}";

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    void clean() {
        repository.deleteAll();
    }

    private String createAsha() throws Exception {
        String body = mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(ASHA))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return body.replaceAll(".*\"id\":(\\d+).*", "$1");
    }

    @Test
    void createsAndFetchesStudent() throws Exception {
        String id = createAsha();
        mvc.perform(get("/api/students/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Asha Rao"));
    }

    @Test
    void filtersByCourse() throws Exception {
        createAsha();
        mvc.perform(get("/api/students").param("course", "cse"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void rejectsInvalidStudent() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"bad\",\"course\":\"CSE\",\"year\":9}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.email").exists())
                .andExpect(jsonPath("$.details.year").exists());
    }

    @Test
    void rejectsDuplicateEmail() throws Exception {
        createAsha();
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON).content(ASHA))
                .andExpect(status().isConflict());
    }

    @Test
    void updatesAndDeletesStudent() throws Exception {
        String id = createAsha();
        mvc.perform(put("/api/students/" + id).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Asha R\",\"email\":\"asha@example.com\",\"course\":\"ISE\",\"year\":4}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course").value("ISE"));

        mvc.perform(delete("/api/students/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/students/" + id)).andExpect(status().isNotFound());
    }
}
