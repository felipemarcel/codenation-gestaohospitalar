package gestao.api;

import gestao.model.Leito;
import gestao.service.LeitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/leito")
public class LeitoController {

    @Autowired
    private LeitoService service;

    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Leito leito) throws URISyntaxException {

        return ResponseEntity.created(new URI(this.service.save(leito).getId().toString())).build();
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Leito>> listAll(){
        return ResponseEntity.ok(this.service.listAll());
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Leito> findById(@PathVariable("id") Long id){
        return ResponseEntity.ok(this.service.findBy(id));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ResponseEntity<Leito> update(@PathVariable("id") Long id, @Valid @RequestBody Leito leito) {
        this.service.update(id, leito);
        return ResponseEntity.ok().build();
    }


}
