# Concurrent Library Management System with RESTful API

This API Implements a thread-safe `Library` class that manages a collection of books concurrently. It has the following methods 
- `addBook(Book book)`: Adds a new book to the library
- `removeBook(String isbn)`: Removes a book from the library by ISBN
- `findBookByISBN(String isbn)`: Returns a book by its ISBN
- `findBooksByAuthor(String author)`: Returns a list of books by a given author
- `borrowBook(String isbn)`: Decreases the available copies of a book by 1
- `returnBook(String isbn)`: Increases the available copies of a book by 1


# **To start the application, please follow these steps**

1. Clone the project from GIT: [https://github.com/jvangara/lmsapp.git](https://github.com/jvangara/lmsapp.git)
2. In IntelliJ IDEA, import the project as Maven project.
3. Open the Terminal and go inside the **project lmsapp/**.
4. Execute the following commands 
    mvn clean install 
    mvn spring-boot:run 
5. The above commands can also be run from the maven window on IDE
    choosing lmsapp -> Lifecycle -> clean, followed by install 
    Plugins -> spring-boot -> spring-boot:run


# **This Library RESTful API consists of the following HTTP methods**

These can be run from a REST client like Postman, or ARC (Advanced REST Client)

To add a new book to the library, use 
POST  http://localhost:8080/api/books
Request body:
{
    "isbn":"1234",
    "title":"Title1",
    "author":"Author1",
    "pubYear":"2014",
    "avlCopies":"10"
}

Sample success response (HTTP Status 201), 
{
    "isbn": "1234",
    "title": "Title1",
    "author": "Author1",
    "pubYear": 2014,
    "avlCopies": 10
}


To return a book by its ISBN, use 
GET  http://localhost:8080/api/books/{isbn}
Sample success response:
{
    "isbn":"1234",
    "title":"Title1",
    "author":"Author1",
    "pubYear":"2014",
    "avlCopies":"10"
}
Sample failure response: Book not found for ISBN: 1234


To return a list of books by a given author, use 
GET  http://localhost:8080/api/books?author={author}
Sample success response:
[
    {
        "isbn":"1234",
        "title":"Title1",
        "author":"Author1",
        "pubYear":"2014",
        "avlCopies":"10"
    }, 
    {
        "isbn":"12345",
        "title":"Title2",
        "author":"Author1",
        "pubYear":"2015",
        "avlCopies":"5"
    }
]


To removes a book from the library by ISBN, use  
DELETE  http://localhost:8080/api/books/{isbn}
Success response has no body, simply returns HTTP Status of 204, NO CONTENT


To return a book, use 
PUT  http://localhost:8080/api/books/return/{isbn}
Success response has no body, simply returns HTTP Status of 200, OK


To borrow a book, use 
PUT  http://localhost:8080/api/books/borrow/{isbn}
Success response has no body, simply returns HTTP Status of 200, OK  
