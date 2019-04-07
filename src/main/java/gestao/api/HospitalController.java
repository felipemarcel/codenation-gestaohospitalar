package gestao.api;

import gestao.model.Hospital;
import gestao.model.Internacao;
import gestao.model.Paciente;
import gestao.model.Tratamento;
import gestao.service.HospitalService;
import gestao.service.InternacaoService;
import gestao.service.PacienteService;
import gestao.service.TratamentoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/hospitais")
public class HospitalController {

    @Autowired
    private HospitalService service;

    @Autowired
    private InternacaoService internacaoService;

    @Autowired
    private TratamentoService tratamentoService;

    @Autowired
    private PacienteService pacienteService;

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Hospital>> listAll() {
        return ok(service.listAll());
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Hospital hospital) throws URISyntaxException {
        Hospital saved = service.save(hospital);
        return created(new URI(saved.getId().toString())).build();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> findById(@PathVariable("id") Long id) {
        return ok(service.findBy(id));
    }

    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity<?> remove(@PathVariable("id") Long id) {
        service.remove(id);
        return ok().build();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, @Valid @RequestBody Hospital hospital) {
        service.update(id, hospital);
        return ok().build();
    }

    @GetMapping("/{id}/pacientes")
    public ResponseEntity<List<Paciente>> listAllPatientAdmitted(@PathVariable("id") Long id) {
        return ok(this.pacienteService.listPacientesInternadosSemAlta(id));
    }

    @GetMapping("/{id}/historico-de-admissoes")
    public ResponseEntity<List<Paciente>> listAllPatient(@PathVariable("id") Long id) {
        return ok(this.pacienteService.listPacientesInternados(id));
    }

    @ResponseBody
    @PutMapping("/{id}/pacientes/{paciente}/checkout")
    public ResponseEntity<?> checkout(@PathVariable("id") Long id, @PathVariable("paciente") Long idPaciente) {
        this.internacaoService.checkout(idPaciente, id);
        return ok().build();
    }

    @ResponseBody
    @PostMapping("/internacoes/{id}/tratamentos")
    public ResponseEntity<?> addTratamento(@PathVariable("id") Long id, @RequestBody List<@Valid Tratamento> tratamentos) {
        Internacao internacao = this.internacaoService.findById(id);
        for (Tratamento tratamento : tratamentos) {
            tratamento.setInternacao(internacao);
            tratamento = this.tratamentoService.save(tratamento);
        }
        return ok(tratamentos);
    }

    @ResponseBody
    @GetMapping("/{id}/pacientes/{paciente}/internados/tratamentos")
    public ResponseEntity<List<Tratamento>> listTratamentosDePacientesInternados(@PathVariable("id") Long id, @PathVariable("paciente") Long paciente) {
        Internacao internacao = this.internacaoService.findInternacaoAbertaByPaciente(paciente);
        return ok(this.tratamentoService.listTratamentosPacientesInternados(internacao, paciente, id));
    }

    @GetMapping("/{id}/estoque")
    public ResponseEntity<?> listEstoque(@PathVariable("id") Long id) {
        return ok(service.getEstoqueBy(id));
    }

    @GetMapping("/{id}/estoque/{produto}")
    public ResponseEntity<?> findProdutoFromEstoque(@PathVariable("id") Long id, @PathVariable("produto") Long idProduto) {
        return ok(service.getProdutoFromEstoque(id, idProduto));
    }

    @PostMapping("/{id}/pacientes/{paciente}/checkin")
    public ResponseEntity<?> checkin(@PathVariable("id") Long id, @PathVariable("paciente") Long idPaciente) {
        Paciente paciente = pacienteService.findById(idPaciente);
        Hospital hospital = service.findBy(id);
        Internacao internacao = new Internacao();
        internacao.setHospital(hospital);
        internacao.setPaciente(paciente);
        internacao.setDataEntrada(LocalDateTime.now());
        internacaoService.save(internacao);
        return ok().build();
    }
}