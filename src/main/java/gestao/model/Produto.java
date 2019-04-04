package gestao.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    private Long id;

    private String nome;

    private String descricao;

    @ManyToMany(mappedBy = "produtos")
    private Set<Estoque> estoques;

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

    public Set<Estoque> getEstoques() {
        return estoques;
    }

    public void setEstoque(Set<Estoque> estoque) {
        this.estoques = estoque;
    }
}
