/**
  * Represents an illustrator, which is a type of creator who creates books by providing illustrations.
  *
  * @param name the name of the illustrator
  * @param books the list of books illustrated by this illustrator
  * @author Eva Ray
  */
case class Illustrator(name: String, books: List[Book]) extends Creator

object Illustrator:
  /**
    * Parses an illustrator from CSV data.
    * 
    * @param raw the raw illustrator name from CSV
    * @return a new Illustrator with an empty book list
    */
  def fromCsv(raw: String): Illustrator =
    Illustrator(raw.trim, List())
