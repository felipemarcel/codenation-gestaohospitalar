package gestao.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gestao.exception.CheckinNotValidException;
import gestao.exception.CheckoutNotValidException;
import gestao.exception.HospitalNotFoundException;
import gestao.model.*;
import gestao.service.HospitalService;
import gestao.service.InternacaoService;
import gestao.service.PacienteService;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private InternacaoService internacaoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Validator validator;

    private Map<Long, Hospital> fakeRepository = new HashMap<>();

    private Map<Long, Internacao> fakeInternacaoRepository = new HashMap<>();

    private Long idInternacaoCount = 0L;

    private Long idCount = 0L;

    @Test
    public void contextLoads() {
        assertThat(service).isNotNull();
        assertThat(mockMvc).isNotNull();
    }

    private Stubber getDoAnswerToSave() {
        return Mockito.doAnswer(invocation -> {
            Hospital hospital = (Hospital) invocation.getArguments()[0];
            Set<ConstraintViolation<Hospital>> violations = validator.validate(hospital);
            if (violations.isEmpty()) {
                hospital.setId(++idCount);
                fakeRepository.put(hospital.getId(), hospital);
                return hospital;
            }
            return null;
        });
    }

    private Stubber getDoAnswerToInternacaoSave() {
        return Mockito.doAnswer(invocation -> {
            Internacao internacao = (Internacao) invocation.getArguments()[0];
            if (getInternacaoAberta(internacao.getPaciente().getId()) == null) {
                Set<ConstraintViolation<Internacao>> violations = validator.validate(internacao);
                if (violations.isEmpty()) {
                    internacao.setId(++idInternacaoCount);
                    fakeInternacaoRepository.put(internacao.getId(), internacao);
                    return internacao;
                }
            } else {
                throw new CheckinNotValidException();
            }
            return null;
        });
    }

    private Internacao getInternacaoAberta(Long idPaciente) {
        Optional<Internacao> optional = this.fakeInternacaoRepository.values().stream()
                .filter(item -> (item.getPaciente().getId().equals(idPaciente)
                        && item.getDataSaida() == null))
                .findAny();
        if (!optional.isPresent()) {
            return null;
        }
        return optional.get();
    }

    private Stubber getDoAnswerToInternacaoCheckout() {
        return Mockito.doAnswer(invocation -> {
            Long idPaciente = (Long) invocation.getArguments()[0];
            Long idHospital = (Long) invocation.getArguments()[1];
            Internacao internacao = getInternacaoAberta(idPaciente);

            if (internacao == null || !internacao.getHospital().getId().equals(idHospital)) {
                throw new CheckoutNotValidException("O paciente não está internado neste hospital.");
            }

            internacao.setDataSaida(LocalDateTime.now());
            fakeInternacaoRepository.put(internacao.getId(), internacao);
            return internacao;
        });
    }

    private Stubber getDoAnswerToUpdate() {
        return Mockito.doAnswer(invocation -> {
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
        return Mockito.doAnswer(invocation -> {
            Long id = (Long) invocation.getArguments()[0];
            if (this.fakeRepository.containsKey(id)) {
                Hospital hospital = this.fakeRepository.get(id);
                return hospital;
            } else {
                throw new HospitalNotFoundException(id);
            }
        });
    }

    private Stubber getDoAnswerToPacienteFindById() {
        // Será considerado que existe o paciente
        return Mockito.doAnswer(invocation -> {
            Paciente paciente = this.buildValidPaciente();
            paciente.setId((Long) invocation.getArguments()[0]);
            return paciente;
        });
    }

    private Stubber getDoAnswerToList() {
        return Mockito.doAnswer(invocation -> fakeRepository.values().stream().collect(Collectors.toList()));
    }

    @Before
    public void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
        this.getDoAnswerToSave().when(this.service).save(Mockito.any(Hospital.class));
        this.getDoAnswerToList().when(this.service).listAll();
        this.getDoAnswerToUpdate().when(this.service).update(Mockito.anyLong(), Mockito.any(Hospital.class));
        this.getDoAnswerToFindById().when(this.service).findBy(Mockito.anyLong());
        this.getDoAnswerToPacienteFindById().when(this.pacienteService).findById(Mockito.anyLong());
        this.getDoAnswerToInternacaoSave().when(this.internacaoService).save(Mockito.any(Internacao.class));
        this.getDoAnswerToInternacaoCheckout().when(this.internacaoService).checkout(Mockito.anyLong(), Mockito.anyLong());
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
    public void shouldReturnAllHospital() throws Exception {
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

    @Test
    public void shouldCheckinPaciente() throws Exception {
        Hospital hospital = this.buildValidHospital();
        service.save(hospital);

        this.mockMvc.perform(post("/hospitais/1/pacientes/1/checkin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private void checkinPacienteId1() {
        Hospital hospital = this.buildValidHospital();
        Paciente paciente = this.buildValidPaciente();
        Internacao internacao = new Internacao();
        hospital = service.save(hospital);
        paciente.setId(1L);
        internacao.setPaciente(paciente);
        internacao.setHospital(hospital);
        internacao.setDataEntrada(LocalDateTime.now());
        internacao.setId(1L);
        this.fakeInternacaoRepository.put(1L, internacao);
    }

    @Test
    public void shouldNotCheckinPacienteInternado() throws Exception {
        this.checkinPacienteId1();
        this.mockMvc.perform(post("/hospitais/1/pacientes/1/checkin"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldCheckoutPacienteInternado() throws Exception {
        this.checkinPacienteId1();
        this.mockMvc.perform(put("/hospitais/1/pacientes/1/checkout"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotCheckoutPacienteNaoInternado() throws Exception {
        this.checkinPacienteId1();
        this.mockMvc.perform(put("/hospitais/1/pacientes/1/checkout"))
                .andDo(print())
                .andExpect(status().isOk());

        this.mockMvc.perform(put("/hospitais/1/pacientes/1/checkout"))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    private Hospital buildValidHospital() {
        Hospital hospital = new Hospital();
        hospital.setNome("Hospital de Teste");
        hospital.setEndereco("Av. Brasil, 902");
        hospital.setLatitude(new BigDecimal("10.058"));
        hospital.setLongitude(new BigDecimal("-52.12"));
        Estoque estoque = new Estoque();
        estoque.setId(1L);
        Set<Estoque> estoques = new HashSet<>();
        estoques.add(estoque);
        hospital.setEstoques(estoques);
        return hospital;
    }

    private Paciente buildValidPaciente() {
        Paciente paciente = new Paciente();
        paciente.setNomeCompleto("Maria da Silva");
        paciente.setCpf("371.310.164-70");
        paciente.setSexo(Sexo.F);
        paciente.setDataNascimento(LocalDate.of(1992, 10, 20));
        paciente.setEndereco("R. Gen. Lima e Silva, 606 - Cidade Baixa - Porto Alegre - RS, 90050-102");
        paciente.setLatitude(new BigDecimal("-30.038260"));
        paciente.setLongitude(new BigDecimal(-51.221581));
        return paciente;
    }
}
