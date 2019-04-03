package src.main.java.gestao.repository;

import gestao.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public class EstoqueRepository extends JpaRepository<Estoque, Long> {

}
