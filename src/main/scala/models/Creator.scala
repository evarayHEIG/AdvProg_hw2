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
    def addBook(book: Book): this.type =
        (this match
            case author: Author => author.copy(books = book :: author.books)
            case illustrator: Illustrator => illustrator.copy(books = book :: illustrator.books)
            case _ => this
        ).asInstanceOf[this.type]