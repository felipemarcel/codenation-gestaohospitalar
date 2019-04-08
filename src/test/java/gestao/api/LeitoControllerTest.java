package gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestao.exception.LeitoNotFoundException;
import gestao.model.Hospital;
import gestao.model.Leito;
import gestao.model.TipoLeito;
import gestao.service.LeitoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Stubber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LeitoController.class)
public class LeitoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LeitoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;

    private Map<Long, Leito> fakeRepository = new HashMap<>();

    private Long idCount = 0L;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Leito leito = (Leito) invocation.getArguments()[0];
            Set<ConstraintViolation<Leito>> violations = validator.validate(leito);
            if (violations.isEmpty()) {
                leito.setId(++idCount);
                fakeRepository.put(leito.getId(), leito);
                return leito;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToUpdate() {
        return Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            Leito leito = (Leito) invocation.getArguments()[1];
            if (this.fakeRepository.containsKey(id)) {
                leito.setId(id);
                this.fakeRepository.put(id, leito);
            } else {
                throw new LeitoNotFoundException(id);
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                return this.fakeRepository.get(id);
            } else {
                throw new LeitoNotFoundException(id);
            }
        });
    }

    private Stubber getDoAnswerToList() {
        return Mockito.doAnswer(invocation -> fakeRepository.values().stream().collect(Collectors.toList()));
    }

    @Before
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.service).save(Mockito.any(Leito.class));
        this.getDoAnswerToList().when(this.service).listAll();
        this.getDoAnswerToUpdate().when(this.service).update(Mockito.anyLong(), Mockito.any(Leito.class));
        this.getDoAnswerToFindById().when(this.service).findBy(Mockito.anyLong());
    }

    @Test
    public void shouldSaveHospital() throws Exception {
        Leito leito = this.buildValidLeito();
        this.mockMvc.perform(post("/leito")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(leito)))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    public void shouldReturnAllLeito() throws Exception {
        this.mockMvc.perform(get("/leito"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldTrowsLeitoNotFoundExceptionWhenUpdateAndNotExists() throws Exception {
        Leito leito = this.buildValidLeito();
        this.mockMvc.perform(put("/leito/30000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(leito)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
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
