package ru.frostman.XML.dao;

import ru.frostman.XML.model.Author;

import java.util.Collection;

/**
 * AuthorDAO is the interface describing methods with authors
 */
public interface AuthorDAO {
    /**
     * addAuthor adds author to the authorMap
     *
     * @param name   is the name of the author to add
     * @param silent is the parameter saying about if it is possible to print information about
     * @return added author
     */
    Author addAuthor(String name, boolean silent);

    /**
     * findAuthor finds author in the authorMap
     *
     * @param name   is the name of the author to find
     * @param silent is the parameter saying about if it is possible to print information about
     * @return information about found author or "No results"
     */
    Author findAuthor(String name, boolean silent);

    /**
     * getAll returns all authors from authorMap
     *
     * @return collection of authors
     */
    Collection<Author> getAll();

    /**
     * addAll adds collection of authors to the authorMap
     *
     * @param authors is the collection to add
     */
    void addAll(Collection<Author> authors);
}
