package ru.slimercorp.springcourse.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.slimercorp.springcourse.models.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

}
