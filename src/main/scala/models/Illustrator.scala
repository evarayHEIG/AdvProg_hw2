/**
  * Represents an illustrator, which is a type of creator who creates books by providing illustrations.
  *
  * @param name the name of the illustrator
  * @param books the list of books illustrated by this illustrator
  * @author Eva Ray
  */
case class Illustrator(name: String, books: List[Book]) extends Creator:

  type Self = Illustrator

  /* Creates a copy of this illustrator with an updated list of books. This is used to maintain immutability while allowing 
   * modifications to the illustrator's details.
   *
   * @param newBooks the new list of books to associate with this illustrator
   * @return a new instance of Illustrator with the updated list of books
   */
  def makeCopy(newBooks: List[Book]): Self =
    this.copy(books = newBooks)

object Illustrator:
  /**
    * Parses an illustrator from CSV data.
    * 
    * @param raw the raw illustrator name from CSV
    * @return a new Illustrator with an empty book list
    */
  def fromString(raw: String): Illustrator =
    Illustrator(raw.trim, List())    // jpc: same, not necessarily a csv, it's a string
