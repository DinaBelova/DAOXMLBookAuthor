package ru.frostman.XML.dao;

import ru.frostman.XML.model.Book;

import java.util.Collection;

/**
 * BookDAO is the interface describing methods with books
 */
public interface BookDAO {

    /**
     * addBook adds book to the bookMap
     *
     * @param title   is the title of the book to add
     * @param authors are the authors of this book
     * @param silent  is the parameter saying about if it is possible to print information about book or not
     * @return book added to the bookMap
     */
    Book addBook(String title, String[] authors, boolean silent);

    /**
     * findBook find and prints the information about asked book
     *
     * @param title  is the title of the book to find
     * @param silent is the parameter saying about is it possible to print information about book or not
     * @return book with specified title
     */
    Book findBook(String title, boolean silent);

    /**
     * getAll returns all books from the bookMap
     *
     * @return collection of books
     */
    Collection<Book> getAll();

    /**
     * addAll adds collection of the books to the bookMap
     *
     * @param books collection to add
     */
    void addAll(Collection<Book> books);

}
