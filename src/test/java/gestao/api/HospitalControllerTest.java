package gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestao.model.Hospital;
import gestao.service.HospitalService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HospitalController.class)
public class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HospitalService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    @Test
    public void shouldSaveHospital() throws Exception {
        Hospital hospital = this.buildValidHospital();
        Mockito.doAnswer(invocationOnMock -> {
            Hospital hospital1 = (Hospital) invocationOnMock.getArguments()[0];
            hospital1.setId(1L);
            return hospital1;
        }).when(this.service).save(hospital);

        this.mockMvc.perform(post("/hospitais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(hospital)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnAllHospital() throws Exception{
        this.mockMvc.perform(get("/hospitais"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Ignore
    // TODO Corrigir Teste de retornar exceção HospitalNotFoundException quando for passado um hopistal com id inexistente
    public void shouldNotReturnHospitalWithInnexistentId() throws Exception {
        this.mockMvc.perform(get("/hospitais/30000"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private Hospital buildValidHospital() {
        Hospital hospital = new Hospital();
        hospital.setNome("Hospital de Teste");
        hospital.setEndereco("Av. Brasil, 902");
        hospital.setLatitude(new BigDecimal("10.058"));
        hospital.setLongitude(new BigDecimal("-52.12"));
        return hospital;
    }
}
