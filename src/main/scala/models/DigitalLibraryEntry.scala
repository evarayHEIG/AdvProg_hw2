import Types.Date

/**
  * Represents a library entry for a digital edition of a book. It extends the LibraryEntry trait.
  *
  * @param book the book this edition belongs to
  * @param isbn the ISBN identifier
  * @param isbn13 the ISBN-13 identifier
  * @param publisher the publisher name
  * @param format the digital format of the edition 
  * @param nbPages the number of pages 
  * @param publicationYear the year of publication
  * @author Eva Ray
  */
case class DigitalLibraryEntry(
    edition: DigitalEdition,
    dateAdded: Date,
    override val readingStatus: ReadingStatus = ReadingStatus.ToRead,
    override val dateRead: Option[Date] = None,
    override val purchaseDate: Option[Date] = None,
    override val privateNotes: Option[String] = None,
    override val readCount: Int = 0,
    override val bookshelves: List[ShelfPlacement] = List()
) extends LibraryEntry:

    type Self = DigitalLibraryEntry

    override def makeCopy(
        newBookshelves: List[ShelfPlacement] = bookshelves,
        newReadingStatus: ReadingStatus = readingStatus,
        newDateRead: Option[Date] = dateRead,
        newReadCount: Int = readCount
    ): DigitalLibraryEntry =
        this.copy(
            bookshelves = newBookshelves,
            readingStatus = newReadingStatus,
            dateRead = newDateRead,
            readCount = newReadCount
        )