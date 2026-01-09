package rest_with_spring_boot_and_java.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@Table
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false, length = 80)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 80)
    private String lastName;
    @Column(nullable = false, length = 100)
    private String address;
    @Column(nullable = false, length = 6)
    private String gender;
    @Column(nullable = false)
    private Boolean enabled;
    @Column(name = "wikipedia_profile_url")
    private String wikipediaProfileUrl;
    @Column(name = "photo_url")
    private String photoUrl;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "person_books",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private List<Books> books;

}
