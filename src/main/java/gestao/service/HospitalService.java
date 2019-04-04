package gestao.service;

import gestao.exception.HospitalNotFoundException;
import gestao.model.Estoque;
import gestao.model.Hospital;
import gestao.model.Produto;
import gestao.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository repository;

    @Autowired
    private EstoqueService estoqueService;

    public List<Hospital> listAll() {
        return stream(repository.findAll().spliterator(), true).collect(toList());
    }

    public Hospital save(Hospital hospital) {
        return repository.save(hospital);
    }

    public Hospital findBy(Long id) {
        return repository.findById(id).orElseThrow(() -> new HospitalNotFoundException(id));
    }

    public void update(Long id, Hospital hospital) {
        repository.findById(id).ifPresent(recoveredHospital -> {
            hospital.setNome(hospital.getNome());
            repository.save(hospital);
        });
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }

    public Set<Produto> getEstoqueBy(Long id) {
        return estoqueService.findBy(id).getProdutos();
    }

    public ResponseEntity<List<Estoque>> getProdutoFromEstoque(Long id, Long productId) {
        return null;
    }
}
