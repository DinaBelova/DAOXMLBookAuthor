package ru.frostman.XML.dao;

import ru.frostman.XML.dao.impl.xml.AuthorDAOXMLImpl;
import ru.frostman.XML.dao.impl.xml.BookDAOXMLImpl;

/**
 * DAOFactory provides exemplars of BookDAO and AuthorDAO
 */
public class DAOFactory {

    private static final BookDAO bookDAO = new BookDAOXMLImpl();
    private static final AuthorDAO authorDAO = new AuthorDAOXMLImpl();

    /**
     * getBookDAO returns exemplar of BookDAO
     *
     * @return exemplar of implementation of BookDAO
     */
    public static BookDAO getBookDAO() {
        return bookDAO;
    }

    /**
     * getAuthorDAO returns exemplar of AuthorDAO
     *
     * @return exemplar of implementation of AuthorDAO
     */
    public static AuthorDAO getAuthorDAO() {
        return authorDAO;
    }
}
