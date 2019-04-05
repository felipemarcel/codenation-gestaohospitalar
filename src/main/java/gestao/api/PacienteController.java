package gestao.api;

import gestao.model.Hospital;
import gestao.model.Paciente;
import gestao.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Paciente paciente) throws URISyntaxException {
        return ResponseEntity.created(new URI(this.service.save(paciente).getId().toString())).build();
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Paciente>> listAll(){
        return ResponseEntity.ok(this.service.listAll());
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.findById(id));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> update(@PathVariable("id") Long id, @Valid @RequestBody Paciente paciente) {
        this.service.update(id, paciente);
        return ResponseEntity.ok().build();
    }

    @ResponseBody
    @GetMapping("/{id}/hospitais-proximos")
    // TODO Listar todos os hospitais próximos do paciente
    public ResponseEntity<List<Hospital>> listAllNearby(@PathVariable("id") Long id) {
        return null;
    }
}
