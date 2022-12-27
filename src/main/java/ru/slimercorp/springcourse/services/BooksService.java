package ru.slimercorp.springcourse.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public List<Book> findAll(String page, String booksPerPage, String sortByYear) {
        int pageInt;
        int booksPerPageInt;
        boolean paginationIsEnabled;

        try {
            pageInt = Integer.parseInt(page);
            booksPerPageInt = Integer.parseInt(booksPerPage);
            paginationIsEnabled = booksPerPageInt > 0;
        } catch (NumberFormatException e) {
            pageInt = 0;
            booksPerPageInt = 0;
            paginationIsEnabled = false;
        }

        boolean sortByYearBool = Boolean.parseBoolean(sortByYear);

        if (paginationIsEnabled && sortByYearBool) {
            return booksRepository.findAll(PageRequest.of(pageInt, booksPerPageInt, Sort.by("year"))).getContent();
        } else if (paginationIsEnabled) {
            return booksRepository.findAll(PageRequest.of(pageInt, booksPerPageInt)).getContent();
        } else if (sortByYearBool) {
            return booksRepository.findAll(Sort.by("year"));
        } else {
            return booksRepository.findAll();
        }
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

    public List<Book> search(String query) {
        List<Book> books = booksRepository.findByNameStartingWith(query);
        for (Book book : books) {
            Hibernate.initialize(book.getOwner());
        }

        return books;
    }
}
