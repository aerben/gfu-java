package digital.erben;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.List;

public record Person(
    @Id String id,
    String firstname,
    String lastname,
    LocalDate birthday
) {}
