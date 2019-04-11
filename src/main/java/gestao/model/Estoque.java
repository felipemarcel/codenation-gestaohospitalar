package gestao.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "estoques")
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class Estoque {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private Integer quantidade;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
    @JsonManagedReference
    private Produto produto;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "hospital_id", referencedColumnName = "id")
    @JsonManagedReference
    private Hospital hospital;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
