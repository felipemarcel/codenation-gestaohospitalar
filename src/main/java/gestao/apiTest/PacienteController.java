package gestao.apiTest;

import gestao.model.Hospital;
import gestao.model.Paciente;
import gestao.serviceTest.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @PostMapping
    // TODO Implementar método para salvar paciente
    public ResponseEntity<?> save(@RequestBody Paciente paciente) throws URISyntaxException {
        return null;
    }

    @GetMapping
    // TODO Implementar método para retornar a lista de pacientes
    public ResponseEntity<List<Paciente>> listAll(){
        return null;
    }

    @GetMapping("/{id}")
    // TODO Implementar método para buscar por id, mostrar detalhes
    public ResponseEntity<Paciente> findById(@PathVariable("id") Long id){
        return null;
    }

    @GetMapping("/{id}/hospitais-proximos")
    // TODO Listar todos os hospitais próximos do paciente
    public ResponseEntity<List<Hospital>> listAllNearby(@PathVariable("id") Long id) {
        return null;
    }
}
