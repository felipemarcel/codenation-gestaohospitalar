package gestao.api;

import gestao.model.Hospital;
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
}
