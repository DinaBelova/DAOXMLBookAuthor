package ru.frostman.XML;

import ru.frostman.XML.dao.AuthorDAO;
import ru.frostman.XML.dao.BookDAO;
import ru.frostman.XML.dao.DAOFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class Main is responsible for the exchanging information with console line - it receives messages and respond with reaction needed.
 */
public class Main {

    private static final String ADD_BOOK = "add book";
    private static final String FIND_BOOK = "find book";
    private static final String ADD_AUTHOR = "add author";
    private static final String FIND_AUTHOR = "find author";

    public static void main(String[] args) throws Exception {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream("test.xml")));
        String command;
        while ((command = in.readLine().trim()) != null && analysis(command)) ;
    }

    /**
     * This method is analysing what command has been added to the console line
     *
     * @param command is the line user writes in the console
     * @return true if the work of the application continues and false if it stops
     */
    public static boolean analysis(String command) {

        BookDAO bookDAO = DAOFactory.getBookDAO();
        AuthorDAO authorDAO = DAOFactory.getAuthorDAO();
        PersistenceManager persistenceManager = new PersistenceManager();

        if (command.startsWith(ADD_BOOK)) {
            List<String> authors = new ArrayList<String>();
            bookDAO.addBook(parserAddBook(command, authors), authors.toArray(new String[1]), false);
        } else if (command.startsWith(ADD_AUTHOR)) {
            authorDAO.addAuthor(parserAddAuthor(command), false);
        } else if (command.startsWith(FIND_AUTHOR)) {
            authorDAO.findAuthor(parserFindAuthor(command), false);
        } else if (command.startsWith(FIND_BOOK)) {
            bookDAO.findBook(parserFindBook(command), false);
        } else if (command.startsWith("save")) {
            if (persistenceManager.save()) {
                System.out.println("Information was saved successfully");
            } else {
                System.out.println("It is impossible to save information");
            }
        } else if (command.startsWith("load")) {
            if (persistenceManager.load()) {
                System.out.println("Information was loaded successfully");
            } else {
                System.out.println("It is impossible to load information");
            }
        } else if (command.startsWith("exit")) {
            return false;
        } else if (command.startsWith("reset")) {
            persistenceManager.reset();
            return true;
        }
        return true;
    }

    /**
     * parserAddBook is parsing command to find out the title and the authors
     *
     * @param command is the line from console starting with "add book"
     * @param authors is the list of the book's authors
     * @return title of the current book
     */
    public static String parserAddBook(String command, List<String> authors) {
        String bookInformation = command.substring(ADD_BOOK.length() + 1);
        String[] authorsArray;
        if (bookInformation.contains(";")) {
            authorsArray = bookInformation.split("; ");
        } else {
            authorsArray = new String[1];
        }

        int position = authorsArray[authorsArray.length - 1].indexOf('\"');
        String title = authorsArray[authorsArray.length - 1].substring(position + 1, authorsArray[authorsArray.length - 1].length() - 1);
        authorsArray[authorsArray.length - 1] = authorsArray[authorsArray.length - 1].substring(0, position - 1);
        authors.addAll(Arrays.asList(authorsArray));

        return title;

    }

    /**
     * parserFindBook is parsing command to find out the title of the book
     *
     * @param command is the line from console starting with "find book"
     * @return title of the book
     */
    public static String parserFindBook(String command) {
        return command.substring(FIND_BOOK.length() + 2, command.length() - 1);
    }

    /**
     * parserAddAuthor is parsing the command to find out the name of the author
     *
     * @param command is the line from console starting with "add author"
     * @return name of the author
     */
    public static String parserAddAuthor(String command) {
        return command.substring(ADD_AUTHOR.length() + 1);
    }

    /**
     * parserFindAuthor is parsing the command to find out the name of the author
     *
     * @param command is the line from console starting with "find author"
     * @return name of  the author
     */
    public static String parserFindAuthor(String command) {
        return command.substring(FIND_AUTHOR.length() + 1);
    }
}
