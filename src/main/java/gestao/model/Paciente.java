package gestao.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    private Long id;

    @Column(name = "nome_completo")
    private String nomeCompleto;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    // TODO Adicionar validação de cpf
    private String cpf;

    private String endereco;
    private Double latitude;
    private Double longitude;


}
