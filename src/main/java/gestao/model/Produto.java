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
}
