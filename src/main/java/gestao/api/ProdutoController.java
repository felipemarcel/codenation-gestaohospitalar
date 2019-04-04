package gestao.api;

import gestao.model.Produto;
import gestao.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @PostMapping
    //TODO Implementar metodo para salvar produto
    public ResponseEntity<?> save(@RequestBody Produto produto) throws URISyntaxException {
        return null;
    }

    @GetMapping("/{id}")
    // TODO Implementar método para obter detalhes de um produto
    public ResponseEntity<Produto> findById(@PathVariable("id") Long id) {
        return null;
    }
}
