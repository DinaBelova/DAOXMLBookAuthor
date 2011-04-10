package ru.frostman.XML.dao.impl.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import ru.frostman.XML.dao.AuthorDAO;
import ru.frostman.XML.dao.BookDAO;
import ru.frostman.XML.dao.DAOFactory;
import ru.frostman.XML.model.Author;
import ru.frostman.XML.model.Book;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * BookDAOXMLImpl is the implementation of the interface BookDAO
 */
public class BookDAOXMLImpl implements BookDAO {

    Map<String, Book> bookMap = new HashMap<String, Book>();

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer;

    /**
     * Constructor
     */
    public BookDAOXMLImpl() {
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public Book addBook(String title, String[] authors, boolean silent) {

        AuthorDAO authorDAO = DAOFactory.getAuthorDAO();
        Book bookAdd = new Book();
        bookAdd.setTitle(title);
        for (String authorName : authors) {
            Author a = authorDAO.findAuthor(authorName, true);
            if (a == null) {
                a = authorDAO.addAuthor(authorName, true);
            }
            bookAdd.getAuthors().add(a);
            a.getBooks().add(bookAdd);
        }
        bookMap.put(title, bookAdd);
        if (!silent) {
            printBook(bookAdd);
        }
        return bookAdd;
    }

    public Book findBook(String title, boolean silent) {
        Book b = bookMap.get(title);
        if (b != null) {
            if (!silent) {
                printBook(b);
            }
        } else {
            if (!silent) {
                System.out.println("No results");
            }
        }
        return b;
    }

    public Collection<Book> getAll() {
        return bookMap.values();
    }

    public void addAll(Collection<Book> books) {
        for (Book book : books) {
            bookMap.put(book.getTitle(), book);
        }
    }

    /**
     * printBook prints information about book
     *
     * @param book is the book the information to print about
     */
    private void printBook(Book book) {
        try {
            Document document = documentBuilder.newDocument();
            Element bookElement = document.createElement("book");
            document.appendChild(bookElement);
            Element authorsElement = document.createElement("authors");
            bookElement.appendChild(authorsElement);

            for (Author author : book.getAuthors()) {
                Element authorElement = document.createElement("author");
                authorsElement.appendChild(authorElement);

                Text authorText = document.createTextNode(author.getName());
                authorElement.appendChild(authorText);
            }

            Element titleElement = document.createElement("title");
            bookElement.appendChild(titleElement);
            Text titleText = document.createTextNode(book.getTitle());
            titleElement.appendChild(titleText);

            StreamResult result = new StreamResult(System.out);
            transformer.transform(new DOMSource(document), result);
        } catch (TransformerException e) {
            System.out.println("It is impossible to print a book");
        }
    }
}
