package gestao.service;

import gestao.model.Hospital;
import gestao.repository.HospitalRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class HospitalServiceTest {

    private Validator validator;
    private Map<Long, Hospital> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

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

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer((Answer) invocation -> {
            Hospital hospital = (Hospital) invocation.getArguments()[0];
            Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
            if (violations.isEmpty()) {
                hospital.setId(++idCount);
                return hospital;
            }
            return null;
        });
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(service).isNotNull();
    }

    @Test
    public void shouldSaveHospital() {
        Hospital hospital = this.buildValidHospital();
        this.getDoAnswerToSave().when(this.repository).save(hospital);
        Hospital saved = this.service.save(hospital);
        assertThat(saved.getId(), is(notNullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithoutName() {
        Hospital hospital = this.buildValidHospital();
        hospital.setNome(null);
        this.getDoAnswerToSave().when(this.repository).save(hospital);
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithoutEndereco() {
        Hospital hospital = this.buildValidHospital();
        hospital.setEndereco(null);
        this.getDoAnswerToSave().when(this.repository).save(hospital);
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithInvalidLatitude() {
        Hospital hospital = this.buildValidHospital();
        hospital.setLatitude(new BigDecimal("90.1"));
        this.getDoAnswerToSave().when(this.repository).save(hospital);
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldNotSaveHospitalWithInvalidLongitude() {
        Hospital hospital = this.buildValidHospital();
        hospital.setLongitude(new BigDecimal("-190"));
        this.getDoAnswerToSave().when(this.repository).save(hospital);
        Hospital saved = this.service.save(hospital);
        assertThat(saved, is(nullValue()));
    }

    @Test
    public void shouldReturnAllHospital() {
        List<Hospital> hospitals = service.listAll();
        assertThat(hospitals, hasSize(0));
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
