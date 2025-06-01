package rest_with_spring_boot_and_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rest_with_spring_boot_and_java.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
