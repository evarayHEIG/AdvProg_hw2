import Types.ISBN
import Types.ISBN13
import Types.Year
/**
 * Represents a specific edition of a book.
 * 
 * @param book the book this edition belongs to
 * @param isbn the ISBN identifier
 * @param isbn13 the ISBN-13 identifier
 * @param publisher the publisher name
 * @param format the format of the book edition (e.g., Hardcover, Paperback, Ebook, Audiobook)
 * @param nbPages the number of pages
 * @param publicationYear the year of publication
 * @author Eva Ray
 * @note This trait is parametrized by a format type [F] which allows us to represent different formats of editions.
 * @note This trait is covariant in the format type parameter [F] to allow a single edition type to represent different
 * formats (e.g., physical or digital) while maintaining type safety.
 */
trait Edition[+F <: Format]:
    val book: Book
    val isbn: Option[ISBN] = None
    val isbn13: Option[ISBN13] = None
    val publisher: Option[String] = None
    val format: Option[F] = None
    val nbPages: Option[Int] = None
    val publicationYear: Option[Year] = None

    /**
     * Returns a formatted string representation of the edition.
     * 
     * @return multi-line string with edition details
     */
    override def toString(): String =
        s"- book: ${book.title}\n" +
        s"- isbn: ${isbn.getOrElse("None")}\n" +
        s"- isbn13: ${isbn13.getOrElse("None")}\n" +
        s"- publisher: ${publisher.getOrElse("None")}\n" +
        s"- format: ${format.getOrElse("None")}\n" +
        s"- nbPages: ${nbPages.getOrElse("None")}\n" +
        s"- publicationYear: ${publicationYear.getOrElse("None")}\n"

object Edition:

    /**
     * Creates an edition from CSV data.
     * 
     * @param raw the parsed Goodreads CSV row
     * @return a new Edition instance
     */
    def fromCsv(raw: GoodreadsRow): Edition[Format] =

        val book = Book.fromCsv(raw)
        val isbn = raw.isbn.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty)
        val isbn13 = raw.isbn13.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty)
        val publisher = raw.publisher.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty)
        val format = Format.fromCsv(raw.binding)
        val nbPages = raw.numberOfPages.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty.toInt)
        val publicationYear = raw.yearPublished.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty.toInt)

        format match
            case Some(p: PhysicalType) => PhysicalEdition(book, isbn, isbn13, publisher, Some(p), nbPages, publicationYear)
            case Some(d: (EbookType | AudioType)) => DigitalEdition(book, isbn, isbn13, publisher, Some(d), nbPages, publicationYear)
            case _ => PhysicalEdition(book, isbn, isbn13, publisher, None, nbPages, publicationYear) // Default to physical


