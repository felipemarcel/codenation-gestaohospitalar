package gestao.model;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "tratamentos")
public class Tratamento {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private LocalDateTime data;

    @ManyToOne
    @MapsId("internacao_id")
    private Internacao internacao;

    @ManyToOne
    @MapsId("produto_id")
    private Produto produto;

    @ManyToOne
    @MapsId("procedimento_id")
    private Procedimento procedimento;


    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Internacao getInternacao() {
        return internacao;
    }

    public void setInternacao(Internacao internacao) {
        this.internacao = internacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Procedimento getProcedimento() {
        return procedimento;
    }

    public void setProcedimento(Procedimento procedimento) {
        this.procedimento = procedimento;
    }
}
