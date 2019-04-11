package gestao.service;

import gestao.exception.EstoqueNotEnoughException;
import gestao.exception.HospitalNotFoundException;
import gestao.exception.ProdutoNotFoundException;
import gestao.model.Estoque;
import gestao.model.Hospital;
import gestao.model.Produto;
import gestao.repository.EstoqueRepository;
import gestao.repository.HospitalRepository;
import gestao.repository.LeitoRepository;
import gestao.repository.ProdutoRepository;
import gestao.tipo.Tipo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class HospitalService {

    @Autowired
    private HospitalRepository repository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    final private static Integer QTD_MIN_PRODUTOS_PARA_TRANSFERENCIA  = 4;
    final private static Integer QTD_MIN_SANGUE_PARA_TRANSFERENCIA  = 8;

    public List<Hospital> listAll() {
        return stream(repository.findAll().spliterator(), true).collect(toList());
    }

    public Hospital save(Hospital hospital) {
        return repository.save(hospital);
    }

    public Hospital findBy(Long id) {
        Optional<Hospital> optionalHospital = repository.findById(id);
        if (optionalHospital == null || !optionalHospital.isPresent()) {
            throw new HospitalNotFoundException(id);
        }
        return optionalHospital.get();
    }

    public void update(Long id, Hospital hospital) {
        Optional<Hospital> optionalHospital = repository.findById(id);
        if (optionalHospital != null && optionalHospital.isPresent()) {
            Hospital savedHospital = optionalHospital.get();
            savedHospital.setNome(hospital.getNome());
            savedHospital.setLongitude(hospital.getLongitude());
            savedHospital.setLatitude(hospital.getLatitude());
            savedHospital.setEndereco(hospital.getEndereco());

            this.repository.save(savedHospital);
        } else {
            throw new HospitalNotFoundException(id);
        }
    }

    public void remove(Long id) {
        repository.deleteById(id);
    }

    public List<Estoque> getEstoqueBy(Long id) {
        Hospital hospital = this.findBy(id);
        return this.estoqueRepository.listByHospitalAndProduto(hospital.getId());
    }

    public Estoque getProdutoFromEstoque(Long idHospital, Long idProduto) {
        return this.estoqueRepository.findByHospitalAndProduto(idHospital, idProduto);
    }

    public void removeProdutoNoEstoque(Long idHospital, Long idProduto, int quantidade, boolean isToOtherHospital) {
        Hospital hospital = this.findBy(idHospital);
        Estoque estoque = this.estoqueRepository.findByHospitalAndProduto(idHospital, idProduto);
        Optional<Produto> optionalProduto = this.produtoRepository.findById(idProduto);
        if (optionalProduto == null || !optionalProduto.isPresent()) {
            throw new ProdutoNotFoundException(idProduto);
        }
        if (estoque == null) {
            throw new EstoqueNotEnoughException(0);
        } else if (estoque.getQuantidade() < quantidade) {
            throw new EstoqueNotEnoughException(estoque.getQuantidade());
        } else if (isToOtherHospital && (estoque.getQuantidade() <= QTD_MIN_PRODUTOS_PARA_TRANSFERENCIA ||
                (estoque.getProduto().getTipo().equals(Tipo.SANGUE) && estoque.getQuantidade() <= QTD_MIN_SANGUE_PARA_TRANSFERENCIA))) {
            throw new EstoqueNotEnoughException("Não é possível tranferir. Quantidade mínima para uso interno do hospital.");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        this.estoqueRepository.save(estoque);
    }

    public Estoque addProdutoNoEstoque(Long idHospital, Long idProduto, int quantidade) {
        Hospital hospital = this.findBy(idHospital);
        Estoque estoque = this.estoqueRepository.findByHospitalAndProduto(idHospital, idProduto);
        Optional<Produto> optionalProduto = this.produtoRepository.findById(idProduto);
        if (optionalProduto == null || !optionalProduto.isPresent()) {
            throw new ProdutoNotFoundException(idProduto);
        }
        if (estoque == null) {
            estoque = new Estoque();
            estoque.setHospital(hospital);
            estoque.setProduto(optionalProduto.get());
            estoque.setQuantidade(quantidade);
        } else {
            estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        }
        this.estoqueRepository.save(estoque);
        return estoque;
    }

}
