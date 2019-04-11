package gestao.repository;

import gestao.model.Estoque;
import gestao.model.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    @Query("SELECT e from Estoque e WHERE e.hospital.id = :idHospital and e.produto.id = :idProduto")
    public Estoque findByHospitalAndProduto(Long idHospital, Long idProduto);

    @Query("SELECT e from Estoque e WHERE e.hospital.id = :idHospital")
    public List<Estoque> listByHospitalAndProduto(Long idHospital);
}
