package br.com.espacoconstruir.tutoring_backend.controller;

import br.com.espacoconstruir.tutoring_backend.dto.TeacherDTO;
import br.com.espacoconstruir.tutoring_backend.model.Teacher;
import br.com.espacoconstruir.tutoring_backend.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TeacherController.class)
@WithMockUser
public class TeacherControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TeacherService teacherService;

  @Test
  public void testRegisterTeacher() throws Exception {
    // Create test data
    TeacherDTO teacherDTO = new TeacherDTO();
    teacherDTO.setName("John Doe");
    teacherDTO.setEmail("john@example.com");
    teacherDTO.setPassword("password123");
    teacherDTO.setPhone("+5511999999999");
    teacherDTO.setCnpj("12345678901234");

    Teacher teacher = new Teacher();
    teacher.setId(1L);
    teacher.setName(teacherDTO.getName());
    teacher.setEmail(teacherDTO.getEmail());
    // Set other fields as needed

    // Mock the service response
    when(teacherService.registerTeacher(any(TeacherDTO.class))).thenReturn(teacher);

    // Perform the test
    mockMvc.perform(post("/api/teachers/register")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(teacherDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("John Doe"))
        .andExpect(jsonPath("$.email").value("john@example.com"));
  }
}