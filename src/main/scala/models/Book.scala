import Types.Year

/**
 * Represents a book with its details.
 * 
 * @param id the unique identifier of the book
 * @param title the title of the book
 * @param author the main author of the book
 * @param additionalAuthors optional list of additional authors
 * @param averageRating optional average rating of the book
 * @author Eva Ray
 */
case class Book(
    id: Int,
    title: String,
    author: Creator,
    additionalAuthors: Option[List[Creator]] = None,
    averageRating: Option[Double] = None,
    originalPublicationYear: Option[Year] = None 
):
    /**
      * Returns a string representation of the book details.
      *
      * @return a formatted string with the book's information
      */
    override def toString(): String =
        s"- id: $id\n" +
        s"- title: $title\n" +
        s"- author: ${author.name}\n" +
        s"- additionalAuthors: ${additionalAuthors.getOrElse("None")}\n" +
        s"- averageRating: ${averageRating.getOrElse("None")}\n" +
        s"- originalPublicationYear: ${originalPublicationYear.getOrElse("None")}\n"

object Book:

    /**
    * Parses a book from CSV data.
    *
    * @param raw the raw GoodreadsRow from CSV
    * @return a new Book instance with parsed data
    */
    def fromCsv(raw: GoodreadsRow): Book =
        Book(
            id = raw.bookId.toInt,
            title = raw.title,
            author = Author.fromString(raw.author),
            additionalAuthors = raw.additionalAuthors.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty.split(",").toList.map(Author.fromString)),
            averageRating = raw.averageRating.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty.toDouble),
            originalPublicationYear = raw.originalPublicationYear.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty.toInt)
        )
