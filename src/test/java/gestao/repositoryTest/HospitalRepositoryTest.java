package gestao.repositoryTest;

import gestao.model.Hospital;
import gestao.repository.HospitalRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HospitalRepositoryTest {

    @Autowired
    private HospitalRepository repository;

    @Test
    public void contextLoads() {
        Assertions.assertThat(repository).isNotNull();
    }

    @Test
    public void shouldReturnAllHospital(){
        List<Hospital> hospitals = (List<Hospital>) repository.findAll();
        assertThat(hospitals, hasSize(0));
    }
}
