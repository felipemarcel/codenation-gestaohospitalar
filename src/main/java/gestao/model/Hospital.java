package gestao.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
public class Hospital {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    private String name;
}
