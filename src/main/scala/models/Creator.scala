/**
  * Defines the Creator trait, which represents a person who creates books (either an author or an illustrator).
  * 
  * @trait Creator
  * @param name the name of the creator
  * @param books the list of books created by this creator
  * @author Eva Ray
  */
trait Creator:
    val name: String
    val books: List[Book]

    // The type member Self allows us to define methods in the Creator trait that return the correct concrete type 
    // when creating modified copies of the creator. This is necessary because the Creator trait itself does not know 
    // whether it is an Author or an Illustrator, and we want to maintain immutability while allowing modifications 
    // to the creator's details. (Similar as in LibraryEntry with the Self type member)
    type Self <: Creator

    /* Creates a copy of this creator with an updated list of books. This is used to maintain immutability while allowing 
     * modifications to the creator's details.
     *
     * @param newBooks the new list of books to associate with this creator
     * @return a new instance of Creator with the updated list of books
     */
    def makeCopy(newBooks: List[Book] = books): Self

    /**
      * Returns a string representation of the creator and their books.
      *
      * @return a formatted string with the creator's name and their books
      */
    override def toString(): String =
        s"$name: ${books.map(_.title).mkString(", ")}"

    /**
      * Adds a book to the creator's list of books. 
      *
      * @param book The book to be added to the creator's list of books.
      * @return the updated creator instance with the new book added to their list of books.
      */
    def addBook(book: Book): Self =
        this.makeCopy(newBooks = book :: this.books) 