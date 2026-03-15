import Types.ISBN
import Types.ISBN13
import Types.Year

/**
  * Represents a digital edition of a book, which can be either an ebook or an audiobook.
  *
  * @param book the book this edition belongs to
  * @param isbn the ISBN identifier
  * @param isbn13 the ISBN-13 identifier
  * @param publisher the publisher name
  * @param format the digital format of the edition 
  * @param nbPages the number of pages 
  * @param publicationYear the year of publication
  * @author Eva Ray
  * @note This class extends the Edition trait with a specific union type of `EbookType | AudioType` to represent 
  * digital editions. The use of a union type here is a concise way to express that the format can be one of two 
  * specific types without needing a more complex class hierarchy.
  */
case class DigitalEdition(
    book: Book,
    override val isbn: Option[ISBN] = None,
    override val isbn13: Option[ISBN13] = None,
    override val publisher: Option[String] = None,
    override val format: Option[EbookType | AudioType] = None,
    override val nbPages: Option[Int] = None,
    override val publicationYear: Option[Year] = None
) extends Edition[EbookType | AudioType]