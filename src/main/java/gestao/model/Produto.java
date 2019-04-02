package gestao.model;

import javax.persistence.*;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    private Long id;

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
    
}
