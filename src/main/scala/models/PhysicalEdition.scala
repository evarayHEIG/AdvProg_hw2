import Types.ISBN
import Types.ISBN13
import Types.Year

/**
 * Represents a physical edition of a book.
 * 
 * @param book the book this edition belongs to
 * @param isbn the ISBN identifier
 * @param isbn13 the ISBN-13 identifier
 * @param publisher the publisher name
 * @param format the physical format of the edition 
 * @param nbPages the number of pages 
 * @param publicationYear the year of publication
 * @author Eva Ray
 */
case class PhysicalEdition(
    book: Book,
    override val isbn: Option[ISBN] = None,
    override val isbn13: Option[ISBN13] = None,
    override val publisher: Option[String] = None,
    override val format: Option[PhysicalType] = None,
    override val nbPages: Option[Int] = None,
    override val publicationYear: Option[Year] = None
) extends Edition[PhysicalType]