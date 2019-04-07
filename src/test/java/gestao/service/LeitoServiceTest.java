package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.exception.LeitoNotFoundException;
import gestao.model.Hospital;
import gestao.model.Leito;
import gestao.model.TipoLeito;
import gestao.repository.LeitoRepository;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
public class LeitoServiceTest {

    private Validator validator;
    private Map<Long, Leito> fakeRepository = new HashMap<>();
    private Long idCount = 0L;

    @Autowired
    private LeitoService service;

    @MockBean
    private LeitoRepository repository;

    @TestConfiguration
    static class LeitoServiceTestContextConfiguration {

        @Bean
        public LeitoService leitoService() {
            return new LeitoService();
        }
    }

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.repository).save(Mockito.any(Leito.class));
        this.getDoAnswerToList().when(this.repository).findAll();
        this.getDoAnswerToFindById().when(this.repository).findById(Mockito.anyLong());
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Leito leito = (Leito) invocation.getArguments()[0];
            Set<ConstraintViolation<Leito>> violations = validator.validate(leito);
            if (violations.isEmpty()) {
                if (leito.getId() == null) {
                    leito.setId(++idCount);
                }
                fakeRepository.put(leito.getId(), leito);
                return leito;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Leito leito = this.fakeRepository.get(id);
                return Optional.of(leito);
            } else {
                return null;
            }
        });
    }

    private Stubber getDoAnswerToList() {
        return Mockito.doAnswer((Answer) invocation -> {
            return fakeRepository.values().stream().collect(Collectors.toList());
        });
    }

    @Test
    public void contextLoads() {
        Assertions.assertThat(service).isNotNull();
    }

    @Test
    public void shouldSaveLeito() {
        Leito leito = this.buildValidLeito();
        Leito saved = this.service.save(leito);
        assertThat(saved.getId(), is(notNullValue()));
    }


    @Test
    public void shouldReturnAllHospital() {
        List<Leito> leito = service.listAll();
        assertThat(leito, hasSize(0));
    }

    @Test(expected = LeitoNotFoundException.class)
    public void shouldThrowsLeitoNotFoundExceptionWhenNotExists() {
        Leito leito = this.service.findBy(999L);
    }

    private Leito buildValidLeito() {
        Hospital hospital = new Hospital();
        hospital.setNome("Hospital de Teste");
        hospital.setEndereco("Av. Brasil, 902");
        hospital.setLatitude(new BigDecimal("10.058"));
        hospital.setLongitude(new BigDecimal("-52.12"));

        Leito leito = new Leito();
        leito.setHospital(hospital);
        leito.setTipoLeito(TipoLeito.CLINICO);
        return leito;
    }
}
