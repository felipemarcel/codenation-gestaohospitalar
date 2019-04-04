package gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestao.exception.HospitalNotFoundException;
import gestao.model.Hospital;
import gestao.service.HospitalService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private Validator validator;

    private Map<Long, Hospital> fakeRepository = new HashMap<>();

    private Long idCount = 0L;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer((Answer) invocation -> {
            Hospital hospital = (Hospital) invocation.getArguments()[0];
            Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
            if (violations.isEmpty()) {
                hospital.setId(++idCount);
                fakeRepository.put(hospital.getId(), hospital);
                return hospital;
            } else {
                throw new HospitalNotFoundException(id);
            }
            return null;
        });
    }

    private Stubber getDoAnswerToUpdate() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            Hospital hospital = (Hospital) invocation.getArguments()[1];
            if (this.fakeRepository.containsKey(id)) {
                hospital.setId(id);
                this.fakeRepository.put(id, hospital);
            } else {
                throw new HospitalNotFoundException(id);
            }
            return null;
        });
    }

    private Stubber getDoAnswerToFindById() {
        return Mockito.doAnswer((Answer) invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Hospital hospital = this.fakeRepository.get(id);
                return hospital;
            } else {
                throw new HospitalNotFoundException(id);
            }
        });
    }

    private Stubber getDoAnswerToList() {
        return Mockito.doAnswer((Answer) invocation -> {
            return fakeRepository.values().stream().collect(Collectors.toList());
        });
    }

    @Before
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.service).save(Mockito.any(Hospital.class));
        this.getDoAnswerToList().when(this.service).listAll();
        this.getDoAnswerToUpdate().when(this.service).update(Mockito.anyLong(), Mockito.any(Hospital.class));
        this.getDoAnswerToFindById().when(this.service).findBy(Mockito.anyLong());
    }

    @Test
    public void shouldSaveHospital() throws Exception {
        Hospital hospital = this.buildValidHospital();
        this.mockMvc.perform(post("/hospitais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(hospital)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotSaveInvalidHospital() throws Exception {
        Hospital hospital = this.buildValidHospital();
        hospital.setNome(null);
        hospital.setEndereco(null);
        this.mockMvc.perform(post("/hospitais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(hospital)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldReturnAllHospital() throws Exception{
        this.mockMvc.perform(get("/hospitais"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldTrowsHospitalNotFoundExceptionWhenUpdateAndNotExists() throws Exception {
        Hospital hospital = this.buildValidHospital();
        this.mockMvc.perform(put("/hospitais/30000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(hospital)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotReturnHospitalWithInnexistentId() throws Exception {
        this.mockMvc.perform(get("/hospitais/30000"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
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
