package ru.frostman.XML;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.frostman.XML.dao.AuthorDAO;
import ru.frostman.XML.dao.BookDAO;
import ru.frostman.XML.dao.DAOFactory;
import ru.frostman.XML.model.Author;
import ru.frostman.XML.model.Book;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * PersistenceManager is the class containing managing methods for application, such as save, load, reset
 */
public class PersistenceManager {
    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;

    /**
     * Constructor
     */
    public PersistenceManager() {
    }

    /**
     * method save saves all information about current books and authors to xml
     *
     * @return true, if saving was successful and false, if not
     */
    public boolean save() {
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return false;
        }
        BookDAO bookDAO = DAOFactory.getBookDAO();
        Collection<Book> books = bookDAO.getAll();
        Document document = documentBuilder.newDocument();
        Element booksElement = document.createElement("books");
        document.appendChild(booksElement);

        for (Book book : books) {
            Element bookElement = document.createElement("book");
            booksElement.appendChild(bookElement);
            Element authorsElement = document.createElement("authors");
            bookElement.appendChild(authorsElement);

            for (Author author : book.getAuthors()) {
                Element authorElement = document.createElement("author");
                authorsElement.appendChild(authorElement);
                Text nameElement = document.createTextNode(author.getName());
                authorElement.appendChild(nameElement);
            }
            Element titleElement = document.createElement("title");
            bookElement.appendChild(titleElement);
            Text titleText = document.createTextNode(book.getTitle());
            titleElement.appendChild(titleText);
        }

        StreamResult result = new StreamResult(new File("storage.xml"));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            return false;
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            transformer.transform(new DOMSource(document), result);
        } catch (TransformerException e) {
            return false;
        }
        return true;
    }

    /**
     * method load loads information about books and authors from the file storage.xml
     *
     * @return true if successful and false, if not
     */
    public boolean load() {
        BookDAO bookDAO = DAOFactory.getBookDAO();
        AuthorDAO authorDAO = DAOFactory.getAuthorDAO();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        final List<Book> bookList = new ArrayList<Book>();
        final List<Author> authorList = new ArrayList<Author>();
        final Map<String, Author> authorMap = new HashMap<String, Author>();

        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            return false;
        } catch (SAXException e) {
            return false;
        }
        try {
            parser.parse(new FileInputStream("storage.xml"), new DefaultHandler() {

                String currentTag;
                String data;
                Book book;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    currentTag = qName;
                    if ("book".equals(qName)) {
                        book = new Book();
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if ("book".equals(qName)) {
                        bookList.add(book);
                        book = null;
                    } else if ("author".equals(qName)) {
                        Author author;
                        if (authorMap.containsKey(data)) {
                            author = authorMap.get(data);
                        } else {
                            author = new Author(data);
                            authorMap.put(data, author);
                        }
                        author.getBooks().add(book);
                        book.getAuthors().add(author);
                    } else if ("title".equals(qName)) {
                        book.setTitle(data);
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length) throws SAXException {
                    data = new String(ch, start, length);
                }
            });
        } catch (SAXException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        for (Author author : authorMap.values()) {
            authorList.add(author);
        }

        authorDAO.addAll(authorList);
        bookDAO.addAll(bookList);
        return true;
    }

    /**
     * method reset exits the application without saving
     */
    public void reset() {
        BookDAO bookDAO = DAOFactory.getBookDAO();
        AuthorDAO authorDAO = DAOFactory.getAuthorDAO();
        bookDAO.getAll().clear();
        authorDAO.getAll().clear();
        System.out.println("Reset was complete successfully");
    }
}
