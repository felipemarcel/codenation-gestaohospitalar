package gestao.serviceTest;

import gestao.repository.PacienteRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PacienteServiceTest {

    @Autowired
    private PacienteService service;

    @MockBean
    private PacienteRepository repository;

    @TestConfiguration
    static class PacienteServiceTestConfiguration {

        @Bean
        public PacienteService pacienteService() {
            return new PacienteService();
        }
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(service).isNotNull();
    }
}
