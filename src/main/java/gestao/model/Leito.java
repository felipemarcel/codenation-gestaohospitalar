package gestao.model;

import javax.persistence.*;

import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Leito leito = (Leito) o;
        return id.equals(leito.id) &&
                tipoLeito == leito.tipoLeito &&
                hospital.equals(leito.hospital);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tipoLeito, hospital);
    }

    @Override
    public String toString() {
        return "Leito{" +
                "id=" + id +
                ", tipoLeito=" + tipoLeito +
                ", hospital=" + hospital +
                '}';
    }
}
