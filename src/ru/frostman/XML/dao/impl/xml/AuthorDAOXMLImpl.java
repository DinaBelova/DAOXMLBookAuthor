package ru.frostman.XML.dao.impl.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import ru.frostman.XML.dao.AuthorDAO;
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
 * AuthorDAOXMLImpl is the implementation of the interface AuthorDAO
 */
public class AuthorDAOXMLImpl implements AuthorDAO {

    Map<String, Author> authorMap = new HashMap<String, Author>();

    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder;

    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer;

    /**
     * constructor
     */
    public AuthorDAOXMLImpl() {
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    public Author addAuthor(String name, boolean silent) {
        if (!authorMap.containsKey(name)) {
            Author newAuthor = new Author();
            newAuthor.setName(name);
            if (!silent) {
                printAuthor(newAuthor);
            }
            return newAuthor;
        } else {
            if (!silent) {
                System.out.println("Duplicate name");
            }
            return null;
        }
    }

    public void addAll(Collection<Author> authors) {
        for (Author author : authors) {
            authorMap.put(author.getName(), author);
        }
    }

    public Collection<Author> getAll() {
        return authorMap.values();
    }

    public Author findAuthor(String name, boolean silent) {
        Author a = authorMap.get(name);
        if (a != null) {
            if (!silent) {
                printAuthor(a);
            }
        } else {
            if (!silent) {
                System.out.println("No results");
            }
        }
        return a;
    }

    /**
     * printAuthor prints information about author
     *
     * @param author is the Author to print information about
     */
    private void printAuthor(Author author) {
        Document document = documentBuilder.newDocument();
        Element authorElement = document.createElement("author");
        document.appendChild(authorElement);

        Element nameElement = document.createElement("name");
        authorElement.appendChild(nameElement);

        Text name = document.createTextNode(author.getName());
        nameElement.appendChild(name);

        Element booksElement = document.createElement("books");
        authorElement.appendChild(booksElement);

        for (Book currentBook : author.getBooks()) {
            Element bookElement = document.createElement("book");
            booksElement.appendChild(bookElement);

            Element titleElement = document.createElement("title");
            bookElement.appendChild(titleElement);

            Text title = document.createTextNode(currentBook.getTitle());
            titleElement.appendChild(title);

            if (currentBook.getAuthors().size() > 1) {

                Element coauthorsElement = document.createElement("coauthors");
                bookElement.appendChild(coauthorsElement);

                for (Author coauthor : currentBook.getAuthors()) {
                    if (!coauthor.equals(author)) {
                        Element coauthorElement = document.createElement("coauthor");
                        coauthorsElement.appendChild(coauthorElement);

                        Text coauthorName = document.createTextNode(coauthor.getName());
                        coauthorElement.appendChild(coauthorName);
                    }
                }
            }
        }

        StreamResult result = new StreamResult(System.out);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            transformer.transform(new DOMSource(document), result);
        } catch (TransformerException e) {
            System.out.println("It is impossible to print an author");
        }
    }
}
