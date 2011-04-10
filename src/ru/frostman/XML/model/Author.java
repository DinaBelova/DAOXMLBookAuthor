package ru.frostman.XML.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Author is the class describing author
 */
public class Author {

    private String name;
    private List<Book> books = new ArrayList<Book>();

    public Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
