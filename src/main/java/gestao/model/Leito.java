package gestao.model;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "leitos")
public class Leito {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoLeito tipoLeito;

    @ManyToOne
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    public TipoLeito getTipoLeito() {
        return tipoLeito;
    }

    public void setTipoLeito(TipoLeito tipoLeito) {
        this.tipoLeito = tipoLeito;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
}
