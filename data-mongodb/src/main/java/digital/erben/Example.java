package digital.erben;

import java.util.List;

record User(String email, List<String> nicknames) {}

public class Example {

    public static void main(String[] args) {
        List<User> users = List.of(
            new User("info@it-erben.com", List.of("alex", "aer")),
            new User("tobias@it-erben.com", List.of("tobi", "ter"))
        );
        users
            .stream()
            .flatMap(user -> user.nicknames().stream())
            .forEach(System.out::println);
        // alex
        // aer
        // tobi
        // ter
    }
}
