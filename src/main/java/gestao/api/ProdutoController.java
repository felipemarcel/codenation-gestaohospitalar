package gestao.api;

import gestao.model.Produto;
import gestao.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @ResponseBody
    @PostMapping
    //TODO adicionar anotação @valid em produto
    public ResponseEntity<?> save(@RequestBody Produto produto) throws URISyntaxException {
    	Produto saved = service.save(produto);
        return created(new URI(saved.getId().toString())).build();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable("id") Long id) {
        return ok(service.findBy(id));
    }
}
