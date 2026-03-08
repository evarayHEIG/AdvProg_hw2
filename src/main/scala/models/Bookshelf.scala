/**
 * Represents a bookshelf that can contain books in a user's library.
 * 
 * @param name the name of the bookshelf
 * @author Eva Ray
 */
abstract class Bookshelf (
  val name: String
):
  /**
    * Returns a string representation of the bookshelf details.
    *
    * @return a formatted string with the bookshelf's information
    */
  override def toString(): String =
    s"- name: $name\n"

object Bookshelf:

  // Predefined system bookshelves based on reading status. Every user has them by default.
  val systemBookshelves: List[Bookshelf] = List(
    ReadingStatusBookshelf("to-read", ReadingStatus.ToRead),
    ReadingStatusBookshelf("currently-reading", ReadingStatus.CurrentlyReading),
    ReadingStatusBookshelf("read", ReadingStatus.Read),
    ReadingStatusBookshelf("dnf", ReadingStatus.Dnf)
  )

  /**
    * Checks if a given bookshelf is one of the predefined system bookshelves.
    *
    * @param bookshelf the bookshelf to check
    * @return true if the bookshelf is a system bookshelf, false otherwise
    */
  def isSystemBookshelf(bookshelf: Bookshelf): Boolean =
    bookshelf.name match
      case "to-read" | "currently-reading" | "read" | "dnf" => true
      case _ => false


  /**
   * Parses a list of bookshelf names from CSV data, associating them with the reading status if necessary.
   * 
   * @param bookshelves the raw bookshelf string from CSV (comma-separated)
   * @param readingStatus the raw reading status string from CSV
   * @return a sequence of ShelfPlacement instances representing the parsed bookshelves
   */
  def parseBookshelves(bookshelves: String, readingStatus: String): Seq[ShelfPlacement] =
    // If the bookshelf field is empty, we assign the book to the bookshelf corresponding to its reading status
    bookshelves.trim match
      case "" => Seq(ShelfPlacement(ReadingStatusBookshelf(readingStatus, ReadingStatus.fromCsv(readingStatus).get), None))
      case nonEmpty => 
        val parsedBookshelves = nonEmpty.split(",").map(_.trim)
        for (bookshelf <- parsedBookshelves) yield
          val (shelfName, position) = ShelfPlacement.parseBookshelfWithPosition(bookshelf)
          shelfName match
            case "to-read" => ShelfPlacement(ReadingStatusBookshelf(shelfName, ReadingStatus.ToRead), position)
            case "currently-reading" => ShelfPlacement(ReadingStatusBookshelf(shelfName, ReadingStatus.CurrentlyReading), position)
            case "read" => ShelfPlacement(ReadingStatusBookshelf(shelfName, ReadingStatus.Read), position)
            case "dnf" => ShelfPlacement(ReadingStatusBookshelf(shelfName, ReadingStatus.Dnf), position)
            case _ => ShelfPlacement(CustomBookshelf(shelfName), position)