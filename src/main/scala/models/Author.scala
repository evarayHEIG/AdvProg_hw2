/**
 * Represents a book author.
 * 
 * @param name the author's name
 * @param books the list of books written by this author
 * @author Eva Ray
 */
case class Author(name: String, books: List[Book]) extends Creator

object Author:

  /**
   * Parses an author from CSV data.
   * 
   * @param raw the raw author name from CSV
   * @return a new Author with an empty book list
   */
  def fromCsv(raw: String): Author =
    Author(raw.trim, List())