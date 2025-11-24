import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Member {
    private int memberId;
    private String name;
    private String email;
    private List<Integer> issuedBooks;

    public Member(int memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.issuedBooks = new ArrayList<>();
    }

    public int getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<Integer> getIssuedBooks() { return issuedBooks; }

    public void displayMemberDetails() {
        System.out.printf("ID: %d | %s | %s%n", memberId, name, email);
        if (issuedBooks.isEmpty()) {
            System.out.println("Issued Books: None");
        } else {
            System.out.print("Issued Books: ");
            for (int id : issuedBooks) System.out.print(id + " ");
            System.out.println();
        }
    }

    public void addIssuedBook(int bookId) {
        if (!issuedBooks.contains(bookId)) issuedBooks.add(bookId);
    }

    public boolean returnIssuedBook(int bookId) {
        return issuedBooks.remove((Integer) bookId);
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(";"); // book ids separated by ;
        for (Integer id : issuedBooks) sj.add(String.valueOf(id));
        return memberId + "," + name + "," + email + "," + sj.toString();
    }

    public static Member fromString(String line) {
        // format: id,name,email,book1;book2;...
        String[] parts = line.split(",", -1);
        if (parts.length < 4) return null;
        try {
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String email = parts[2];
            Member m = new Member(id, name, email);
            String booksPart = parts[3];
            if (!booksPart.isEmpty()) {
                String[] bids = booksPart.split(";");
                for (String b : bids) {
                    if (b.trim().isEmpty()) continue;
                    m.addIssuedBook(Integer.parseInt(b.trim()));
                }
            }
            return m;
        } catch (Exception e) {
            return null;
        }
    }
}
