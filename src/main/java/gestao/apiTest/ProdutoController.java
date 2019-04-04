package gestao.apiTest;

import gestao.model.Produto;
import gestao.serviceTest.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    /**
     * Salva o produto cadastrado
     * @param produto Salva todos os parametros da classe produto
     * @return Retorna o Id do produto
     * @throws URISyntaxException Se não foi possível salvar o produto
     */
    @ResponseBody
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Produto produto) throws URISyntaxException {
    	Produto saved = service.save(produto);
        return created(new URI(saved.getId().toString())).build();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable("id") Long id) {
        return ok(service.findBy(id));
    }

    @ResponseBody
    @GetMapping
    public ResponseEntity<List<Produto>> listAll() {
        return ok(service.listAll());
    }

    /**
     * Busca parcial pelo nome do produto
     * @param nome Nome parcial do produto
     * @return Lista dos produtos que foram encontrados
     */
    @GetMapping("/por-nome")
    public ResponseEntity<List<Produto>> pesquiar(@PathVariable("nome") String nome) {
        return ok(service.pesquisar(nome));
    }

    /**
     * Exclui o produto
     * @param id identificação do produto
     * @return Salvo com sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Produto> delete(@PathVariable("id") Long id) {
        service.remove(id);
        return ok().build();
    }

}
