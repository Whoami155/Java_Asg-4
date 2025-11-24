import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class LibraryManager {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Member> members = new HashMap<>();
    private Set<String> categories = new HashSet<>();
    private final String booksFile = "books.txt";
    private final String membersFile = "members.txt";
    private Scanner scanner = new Scanner(System.in);

    public LibraryManager() {
        loadFromFile();
    }

    private int nextBookId() {
        return books.keySet().stream().max(Integer::compareTo).orElse(100) + 1;
    }

    private int nextMemberId() {
        return members.keySet().stream().max(Integer::compareTo).orElse(200) + 1;
    }

    public void addBook() {
        try {
            System.out.print("Enter Book Title: ");
            String title = scanner.nextLine().trim();
            System.out.print("Enter Author: ");
            String author = scanner.nextLine().trim();
            System.out.print("Enter Category: ");
            String category = scanner.nextLine().trim();
            if (title.isEmpty() || author.isEmpty() || category.isEmpty()) {
                System.out.println("All fields are required.\n");
                return;
            }
            int id = nextBookId();
            Book b = new Book(id, title, author, category);
            books.put(id, b);
            categories.add(category);
            saveBooks();
            System.out.println("Book added successfully with ID: " + id + "\n");
        } catch (Exception e) {
            System.out.println("Error adding book: " + e.getMessage() + "\n");
        }
    }

    public void addMember() {
        try {
            System.out.print("Enter Member Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();
            if (name.isEmpty() || email.isEmpty()) {
                System.out.println("All fields are required.\n");
                return;
            }
            if (!isValidEmail(email)) {
                System.out.println("Invalid email format.\n");
                return;
            }
            int id = nextMemberId();
            Member m = new Member(id, name, email);
            members.put(id, m);
            saveMembers();
            System.out.println("Member added successfully with ID: " + id + "\n");
        } catch (Exception e) {
            System.out.println("Error adding member: " + e.getMessage() + "\n");
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$").matcher(email).matches();
    }

    public void issueBook() {
        try {
            System.out.print("Enter Member ID: ");
            int mid = Integer.parseInt(scanner.nextLine().trim());
            Member m = members.get(mid);
            if (m == null) {
                System.out.println("Member not found.\n");
                return;
            }
            System.out.print("Enter Book ID to issue: ");
            int bid = Integer.parseInt(scanner.nextLine().trim());
            Book b = books.get(bid);
            if (b == null) {
                System.out.println("Book not found.\n");
                return;
            }
            if (b.isIssued()) {
                System.out.println("Book is already issued.\n");
                return;
            }
            b.markAsIssued();
            m.addIssuedBook(bid);
            saveBooks();
            saveMembers();
            System.out.println("Book issued successfully.\n");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number input.\n");
        } catch (Exception e) {
            System.out.println("Error issuing book: " + e.getMessage() + "\n");
        }
    }

    public void returnBook() {
        try {
            System.out.print("Enter Member ID: ");
            int mid = Integer.parseInt(scanner.nextLine().trim());
            Member m = members.get(mid);
            if (m == null) {
                System.out.println("Member not found.\n");
                return;
            }
            System.out.print("Enter Book ID to return: ");
            int bid = Integer.parseInt(scanner.nextLine().trim());
            Book b = books.get(bid);
            if (b == null) {
                System.out.println("Book not found.\n");
                return;
            }
            boolean removed = m.returnIssuedBook(bid);
            if (!removed) {
                System.out.println("This member did not have that book issued.\n");
                return;
            }
            b.markAsReturned();
            saveBooks();
            saveMembers();
            System.out.println("Book returned successfully.\n");
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid number input.\n");
        } catch (Exception e) {
            System.out.println("Error returning book: " + e.getMessage() + "\n");
        }
    }

    public void searchBooks() {
        System.out.println("Search by: 1.Title 2.Author 3.Category");
        System.out.print("Enter choice: ");
        String c = scanner.nextLine().trim();
        System.out.print("Enter keyword: ");
        String kw = scanner.nextLine().trim().toLowerCase();
        List<Book> results = new ArrayList<>();
        switch (c) {
            case "1":
                for (Book b : books.values()) if (b.getTitle().toLowerCase().contains(kw)) results.add(b);
                break;
            case "2":
                for (Book b : books.values()) if (b.getAuthor().toLowerCase().contains(kw)) results.add(b);
                break;
            case "3":
                for (Book b : books.values()) if (b.getCategory().toLowerCase().contains(kw)) results.add(b);
                break;
            default:
                System.out.println("Invalid choice.\n"); return;
        }
        if (results.isEmpty()) System.out.println("No books found.");
        else {
            System.out.println("Results:"); results.forEach(Book::displayBookDetails);
        }
        System.out.println();
    }

    public void sortBooks() {
        System.out.println("Sort by: 1.Title 2.Author 3.Category");
        System.out.print("Enter choice: ");
        String c = scanner.nextLine().trim();
        List<Book> list = new ArrayList<>(books.values());
        switch (c) {
            case "1": Collections.sort(list); break; // Comparable by title
            case "2": Collections.sort(list, Comparator.comparing(Book::getAuthor, String.CASE_INSENSITIVE_ORDER)); break;
            case "3": Collections.sort(list, Comparator.comparing(Book::getCategory, String.CASE_INSENSITIVE_ORDER)); break;
            default: System.out.println("Invalid choice.\n"); return;
        }
        System.out.println("Sorted list:"); list.forEach(Book::displayBookDetails); System.out.println();
    }

    private void loadFromFile() {
        loadBooks();
        loadMembers();
    }

    private void loadBooks() {
        File f = new File(booksFile);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Book b = Book.fromString(line);
                if (b != null) {
                    books.put(b.getBookId(), b);
                    categories.add(b.getCategory());
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading books: " + e.getMessage());
        }
    }

    private void loadMembers() {
        File f = new File(membersFile);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                Member m = Member.fromString(line);
                if (m != null) members.put(m.getMemberId(), m);
            }
        } catch (IOException e) {
            System.out.println("Error loading members: " + e.getMessage());
        }
    }

    private void saveBooks() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(booksFile))) {
            for (Book b : books.values()) bw.write(b.toString() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    private void saveMembers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(membersFile))) {
            for (Member m : members.values()) bw.write(m.toString() + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error saving members: " + e.getMessage());
        }
    }

    public void mainMenu() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to City Library Digital Management System");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Books");
            System.out.println("6. Sort Books");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            String ch = scanner.nextLine().trim();
            switch (ch) {
                case "1": addBook(); break;
                case "2": addMember(); break;
                case "3": issueBook(); break;
                case "4": returnBook(); break;
                case "5": searchBooks(); break;
                case "6": sortBooks(); break;
                case "7": running = false; break;
                default: System.out.println("Invalid choice.\n");
            }
        }
        // on exit, save data
        saveBooks(); saveMembers();
        System.out.println("Exiting. Data saved. Thank you!");
        scanner.close();
    }

    public static void main(String[] args) {
        LibraryManager lm = new LibraryManager();
        lm.mainMenu();
    }
}
