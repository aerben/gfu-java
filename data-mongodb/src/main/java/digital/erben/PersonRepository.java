package digital.erben;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface PersonRepository extends MongoRepository<Person, String> {
    List<Person> findPersonsByBirthdayAfter(LocalDate cutoff);
}
