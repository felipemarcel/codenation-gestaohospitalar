package gestao.model;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "hospitais")
public class Hospital {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private String nome;

    private String endereco;

    private Double latitude;

    private Double longitude;

    @OneToMany(mappedBy = "hospital", cascade = ALL, orphanRemoval = true)
    private List<Estoque> estoque;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String name) {
        this.nome = name;
    }
}
