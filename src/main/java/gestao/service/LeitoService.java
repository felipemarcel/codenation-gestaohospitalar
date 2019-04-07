package gestao.service;

import gestao.exception.LeitoNotFoundException;
import gestao.model.Leito;
import gestao.repository.LeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;


public class LeitoService {

    @Autowired
    private LeitoRepository repository;

    public List<Leito> listAll() {
        return stream(repository.findAll().spliterator(), true).collect(toList());
    }

    public Leito save(Leito leito) {
        return repository.save(leito);
    }

    public Leito findBy(Long id) {
        Optional<Leito> optionalLeito = repository.findById(id);
        if (optionalLeito == null || !optionalLeito.isPresent()) {
            throw new LeitoNotFoundException(id);
        }
        return optionalLeito.get();
    }

    public void update(Long id, Leito leito) {
        Leito saved = this.findBy(id);
        saved.setTipoLeito(leito.getTipoLeito());
        this.save(saved);
    }

}
