package gestao.model;

import gestao.tipo.FatorRH;
import gestao.tipo.Sangue;
import gestao.tipo.Tipo;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    /**
     * Identifica se é um remedio ou sangue
     */
    private Tipo tipo;

    /**
     * Identifica o Tipo de Sangue.
     * p.e.: A, B, O e AB
     */
    private Sangue sangue;

    /**
     * Identifica o Fator RH
     * Positivo ou Negativo
     */
    private FatorRH fatorrh;

    /**
     * A data que foi cadastrado o produto
     */
    private LocalDate entradadoProduto;

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

    public List<Tratamento> getTratamento() {
        return tratamento;
    }

    public void setTratamento(List<Tratamento> tratamento) {
        this.tratamento = tratamento;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Sangue getSangue() {
        return sangue;
    }

    public void setSangue(Sangue sangue) {
        this.sangue = sangue;
    }

    public FatorRH getFatorrh() {
        return fatorrh;
    }

    public void setFatorrh(FatorRH fatorrh) {
        this.fatorrh = fatorrh;
    }

    public LocalDate getEntradadoProduto() {
        return entradadoProduto;
    }

    public void setEntradadoProduto(LocalDate entradadoProduto) {
        this.entradadoProduto = entradadoProduto;
    }

    public Produto(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Produto(String nome, String descricao, Tipo tipo, LocalDate entradadoProduto, List<Estoque> estoque, List<Tratamento> tratamento) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.entradadoProduto = entradadoProduto;
        this.estoque = estoque;
        this.tratamento = tratamento;
    }

    public Produto(String nome, String descricao, Tipo tipo, Sangue sangue, FatorRH fatorrh, Long quantidade, LocalDate entradadoProduto, List<Estoque> estoque, List<Tratamento> tratamento) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.sangue = sangue;
        this.fatorrh = fatorrh;
        this.entradadoProduto = entradadoProduto;
        this.estoque = estoque;
        this.tratamento = tratamento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return id.equals(produto.id) &&
                nome.equals(produto.nome) &&
                descricao.equals(produto.descricao) &&
                tipo == produto.tipo &&
                sangue == produto.sangue &&
                fatorrh == produto.fatorrh &&
                Objects.equals(entradadoProduto, produto.entradadoProduto) &&
                Objects.equals(estoque, produto.estoque) &&
                Objects.equals(tratamento, produto.tratamento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, descricao, tipo, sangue, fatorrh, entradadoProduto, estoque, tratamento);
    }
}
