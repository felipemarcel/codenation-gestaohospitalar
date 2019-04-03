package gestao.service;

import gestao.model.Hospital;
import gestao.repository.HospitalRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
public class HospitalServiceTest {

    @Autowired
    private HospitalService service;

    @MockBean
    private HospitalRepository repository;

    @TestConfiguration
    static class HospitalServiceTestContextConfiguration {

        @Bean
        public HospitalService hospitalService() {
            return new HospitalService();
        }
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(service).isNotNull();
    }

    @Test
    public void shouldReturnAllHospital() {
        List<Hospital> hospitals = service.listAll();
        assertThat(hospitals, hasSize(0));
    }
}
