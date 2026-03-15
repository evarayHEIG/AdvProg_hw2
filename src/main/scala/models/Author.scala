/**
 * Represents a book author.
 * 
 * @param name the author's name
 * @param books the list of books written by this author
 * @author Eva Ray
 */
case class Author(name: String, books: List[Book]) extends Creator:

  type Self = Author

  /* Creates a copy of this author with an updated list of books. This is used to maintain immutability while allowing 
   * modifications to the author's details.
   *
   * @param newBooks the new list of books to associate with this author
   * @return a new instance of Author with the updated list of books
   */
  def makeCopy(newBooks: List[Book]): Self =
    this.copy(books = newBooks)

object Author:

  /**
   * Parses an author from CSV data.
   * 
   * @param raw the raw author name from CSV
   * @return a new Author with an empty book list
   */
  def fromString(raw: String): Author =    //jpc: not necessarily Csv, it's just a string
    Author(raw.trim, List())