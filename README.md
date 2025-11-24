# Java_Asg-4
City Library Digital Management System

Short description
Simple Java console application to manage books and members, issue/return books, and persist data to files. Built for a college assignment to demonstrate file I/O and Java Collections.

Files

Book.java — Book model (Comparable by title; CSV toString() / fromString() for file I/O).

Member.java — Member model (stores issued book IDs as List<Integer>; CSV toString() / fromString()).

LibraryManager.java — Main program: menu, uses Map for records, Set for categories, file load/save, search/sort/issue/return.

Features

Add Book / Add Member

Issue Book / Return Book (updates both book & member records)

Search books by title, author, or category

Sort books by title (Comparable), author or category (Comparator)

Persistent storage in books.txt and members.txt using buffered character I/O

Basic input validation (empty fields, email format, numeric IDs) and exception handling

Data format

books.txt: bookId,title,author,category,isIssued (one per line)

members.txt: memberId,name,email,book1;book2;... (one per line)

Files are created automatically if missing.

How to compile & run
javac *.java
java LibraryManager

