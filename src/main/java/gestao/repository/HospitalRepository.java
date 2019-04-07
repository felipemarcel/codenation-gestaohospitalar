package gestao.repository;

import gestao.model.Hospital;
import gestao.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("from estoque where hospital_id = :idHospital and produto_id = :idProduto")
    Produto find(Long idHospital, Long idProduto);
}
