package digital.erben.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.function.Consumer;

public class App {

  private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("digital.erben.batchsize");

  public static void main(String[] args) {
    createFiveAuthorsWithTenBooksEach();

    System.out.println("====================== Warm up completed =====================");

    readAllAuthorsAndBooks();

    entityManagerFactory.close();
  }

  public static void readAllAuthorsAndBooks() {
    transactional(entityManager -> {
      // depending on the BatchSize in the owning Entity, Hibernate will have to create less SELECTs to find the joined entities
      // Setting no batch size will result in the N+1 query problem
      List<Author> authors = entityManager.createQuery("from Author", Author.class).getResultList();
      for (Author author : authors) {
        author.getBooks().size(); // this causes the lazy loading of joined entities
      }
    });
  }

  public static void createFiveAuthorsWithTenBooksEach() {
    transactional(entityManager -> {
      for (int i = 0; i < 5; i++) {
        Author author = Author.getInstance("Foo-" + i);
        for (int j = 0; j < 10; j++) {
          author.addBook(Book.getInstance("Bar-" + i + "-" + j));
        }
        entityManager.persist(author);
      }
    });
  }

  public static void transactional(Consumer<EntityManager> consumer) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();

    consumer.accept(entityManager);

    entityManager.getTransaction().commit();
    entityManager.close();
  }

}
