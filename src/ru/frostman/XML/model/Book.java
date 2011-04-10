package ru.frostman.XML.model;

import java.util.ArrayList;
import java.util.List;

/**
 * class Book is the class describing book
 */
public class Book {

    private int id;
    private String title;
    private List<Author> authors = new ArrayList<Author>();

    public Book() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
