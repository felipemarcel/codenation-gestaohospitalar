package src.main.java.gestao.serviceTest;


import gestao.model.Estoque;
import gestao.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstoqueService {
	
	@Autowired
	private EstoqueRepository repository;
	
	public Estoque findBy(Long id) {
		return repository.findById(id);
	}

}
