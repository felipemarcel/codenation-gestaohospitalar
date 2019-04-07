package gestao.api;

import gestao.exception.CheckoutNotValidException;
import gestao.model.Hospital;
import gestao.model.Internacao;
import gestao.model.Paciente;
import gestao.service.HospitalService;
import gestao.service.InternacaoService;
import gestao.service.PacienteService;
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
    // TODO Listar todos os pacientes internados, ou seja, não trazer pacientes que tiveram alta
    public ResponseEntity<List<Paciente>> listAllPatientAdmitted(@PathVariable("id") Long id) {
        return null;
    }

    @GetMapping("/{id}/historico-de-admissoes")
    // TODO Listar todos os pacientes que já foram internados, mesmo aqueles que já receberam alta
    public ResponseEntity<List<Paciente>> listAllPatient(@PathVariable("id") Long id) {
        return null;
    }

    @ResponseBody
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

    @ResponseBody
    @PutMapping("/{id}/pacientes/{paciente}/checkout")
    public ResponseEntity<?> checkout(@PathVariable("id") Long id, @PathVariable("paciente") Long idPaciente) {
        this.internacaoService.checkout(idPaciente, id);
        return ok().build();
    }


}