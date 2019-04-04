package gestao.service;

import gestao.exception.EstoqueNotFoundException;
import gestao.model.Estoque;
import gestao.model.Produto;
import gestao.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository repository;

    @Autowired
    private ProdutoService service;

    public Estoque findBy(Long id) {
        return repository.findById(id).orElseThrow(() -> new EstoqueNotFoundException(id));
    }

    public List<Produto> findAllProducts(Long id) {
        return null;
    }
}
