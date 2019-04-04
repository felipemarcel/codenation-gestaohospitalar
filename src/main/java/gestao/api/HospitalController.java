package gestao.api;

import gestao.model.Hospital;
import gestao.model.Paciente;
import gestao.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/hospitais")
public class HospitalController {

    @Autowired
    private HospitalService service;

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Hospital>> listAll() {
        return ok(service.listAll());
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Hospital hospital) throws URISyntaxException {
        Hospital saved = service.save(hospital);
        return created(new URI(saved.getId().toString())).build();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Hospital> findById(@PathVariable("id") Long id){
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
    public ResponseEntity<?> update(@PathVariable("id") Long id, @RequestBody Hospital hospital){
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
    
    @GetMapping("/{id}/estoque")
    public ResponseEntity<Estoque> listEstoque(@PathVariable("id") Long id){
    	return service.getEstoqueBy(id);
    }
    
    @GetMapping("/{id}/estoque/{produto}")
    public ResponseEntity<List<Estoque>> listEstoque(@PathVariable("id") Long id, @PathVariable("produto") Long productId){
    	return service.getProdutoFromEstoque(id, productId);
    }
}
