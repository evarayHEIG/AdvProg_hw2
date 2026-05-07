/**
 * Represents a library entry for a physical edition of a book. It extends the LibraryEntry trait.
 * 
 * @param book the book this edition belongs to
 * @param condition the condition of the physical book 
 * @param isbn the ISBN identifier
 * @param isbn13 the ISBN-13 identifier
 * @param publisher the publisher name
 * @param format the physical format of the edition 
 * @param nbPages the number of pages 
 * @param publicationYear the year of publication
 * @author Eva Ray
 */
case class PhysicalLibraryEntry(
    edition: PhysicalEdition,
    condition: Condition,
    dateAdded: String,
    override val readingStatus: ReadingStatus = ReadingStatus.ToRead,
    override val dateRead: Option[String] = None,
    override val purchaseDate: Option[String] = None,
    override val privateNotes: Option[String] = None,
    override val readCount: Int = 0,
    override val bookshelves: List[ShelfPlacement] = List()
) extends LibraryEntry:

    type Self = PhysicalLibraryEntry

    override def toString(): String =
        super.toString() + s"- condition: ${condition.toString}\n"

    override def makeCopy(
        newBookshelves: List[ShelfPlacement] = bookshelves,
        newReadingStatus: ReadingStatus = readingStatus,
        newDateRead: Option[String] = dateRead,
        newReadCount: Int = readCount
    ): PhysicalLibraryEntry =
        this.copy(
            bookshelves = newBookshelves,
            readingStatus = newReadingStatus,
            dateRead = newDateRead,
            readCount = newReadCount
        )