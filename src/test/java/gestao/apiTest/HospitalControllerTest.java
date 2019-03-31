package gestao.apiTest;

import gestao.serviceTest.HospitalService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(HospitalController.class)
public class HospitalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HospitalService service;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
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
}
