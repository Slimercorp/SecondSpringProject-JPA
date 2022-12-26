package ru.slimercorp.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.slimercorp.springcourse.models.Book;
import ru.slimercorp.springcourse.models.Person;
import ru.slimercorp.springcourse.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public void release(int id) {
        Optional<Book> foundBook = booksRepository.findById(id);
        foundBook.get().setOwner(null);
    }

    @Transactional
    public void setPerson(int id, Person person) {
        Optional<Book> foundBook = booksRepository.findById(id);
        foundBook.get().setOwner(person);
    }

    @Transactional
    public Optional<Person> getOwner(int id){
        Book book = booksRepository.findById(id).get();
        Hibernate.initialize(book.getOwner());
        return Optional.ofNullable(book.getOwner());
    }
}
