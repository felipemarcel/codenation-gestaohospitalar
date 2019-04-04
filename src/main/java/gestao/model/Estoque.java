package gestao.model;

import javax.persistence.*;

import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "estoques")
public class Estoque {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @ManyToMany(cascade = ALL)
    @JoinTable(name = "estoque_produto",
            joinColumns = @JoinColumn(name = "estoque_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "produto_id", referencedColumnName = "id"))
    private Set<Produto> produtos;

    @ManyToOne
    @MapsId("hospital_id")
    private Hospital hospital;

    private Integer quantidade;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(Set<Produto> produtos) {
        this.produtos = produtos;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
