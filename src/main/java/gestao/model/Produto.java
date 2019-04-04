package gestao.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @NotBlank(message = "{produto.nome.not.blank}")
    private String nome;

    private String descricao;

    @OneToMany(mappedBy = "produto", cascade = ALL, orphanRemoval = true)
    private List<Estoque> estoque;

    @OneToMany(mappedBy = "produto", cascade = ALL, orphanRemoval = true)
    private List<Tratamento> tratamento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Estoque> getEstoque() {
        return estoque;
    }

    public void setEstoque(List<Estoque> estoque) {
        this.estoque = estoque;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Produto(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
}
