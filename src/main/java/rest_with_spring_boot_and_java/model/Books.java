package rest_with_spring_boot_and_java.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class Books implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String author;
    @Column(name = "launch_date", nullable = false, length = 80)
    private Date launchDate;
    @Column(nullable = false, length = 100)
    private BigDecimal price;
    @Column(nullable = false, length = 6)
    private String title;

}
