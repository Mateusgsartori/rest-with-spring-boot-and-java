package rest_with_spring_boot_and_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rest_with_spring_boot_and_java.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person e SET e.enabled = false WHERE e.id = :id")
    void disablePerson(@Param("id") Long id);
}
