/**
 * Represents a specific edition of a book.
 * 
 * @param book the book this edition belongs to
 * @param isbn the ISBN identifier
 * @param isbn13 the ISBN-13 identifier
 * @param publisher the publisher name
 * @param format the physical or digital format
 * @param nbPages the number of pages
 * @param publicationYear the year of publication
 * @author Eva Ray
 */
case class Edition (
    book: Book,
    isbn: Option[String] = None,
    isbn13: Option[String] = None,
    publisher: Option[String] = None,
    format: Option[Format] = None,
    nbPages: Option[Int] = None,
    publicationYear: Option[String] = None
):
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
    def fromCsv(raw: GoodreadsRow): Edition =
        Edition(
            book = Book.fromCsv(raw),
            isbn = raw.isbn.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty),
            isbn13 = raw.isbn13.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty),
            publisher = raw.publisher.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty),
            format = Format.fromCsv(raw.binding),
            nbPages = raw.numberOfPages.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty.toInt),
            publicationYear = raw.yearPublished.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty)
        )