import Types.Date

/**
 * Represents an entry in a user's library.
 * 
 * @param edition the book edition
 * @param dateAdded when the book was added to the library
 * @param readingStatus current reading status
 * @param dateRead when the book was read
 * @param purchaseDate when the book was purchased
 * @param privateNotes user's private notes
 * @param readCount number of times read
 * @param bookshelves list of bookshelf placements
 * @author Eva Ray
 */
trait LibraryEntry:
    val edition: Edition[Format]
    val dateAdded: Date
    val readingStatus: ReadingStatus = ReadingStatus.ToRead
    val dateRead: Option[Date] = None
    val purchaseDate: Option[Date] = None
    val privateNotes: Option[String] = None
    val readCount: Int = 0
    val bookshelves: List[ShelfPlacement] = List()

    /**
      * A member type representing the concrete type of the library entry (physical or digital). 
      * This allows methods in the trait to return the correct type when creating modified copies of the entry. Without
      * this type member, we could not implement methods like `addBookshelf` and `removeBookshelf` because they need to
      * return a new instance of the same type of library entry, and the trait itself does not know whether it is a 
      * physical or digital entry. Furthermore, we couldn't return a LibraryEntry directly from these methods because
      * it is an abstract type.
      */
    type Self <: LibraryEntry

    /**
     * Returns a formatted string representation of the library entry.
     * 
     * @return multi-line string with entry details
     */
    override def toString(): String =
        s"- edition:\n${edition.toString()}" +
        s"- dateAdded: $dateAdded\n" +
        s"- readingStatus: ${readingStatus.toString}\n" +
        s"- dateRead: ${dateRead.getOrElse("None")}\n" +
        s"- purchaseDate: ${purchaseDate.getOrElse("None")}\n" +
        s"- privateNotes: ${privateNotes.getOrElse("None")}\n" +
        s"- readCount: $readCount\n" +
        s"- bookshelves:\n${bookshelves.map(b => b.bookshelf.toString()).mkString("\n")}"

    /**
      * Creates a copy of this library entry with updated fields. This is used to maintain immutability while allowing 
      * modifications to the entry's details.
      *
      * @param newBookshelves the new list of bookshelves to associate with this entry
      * @param newReadingStatus the new reading status to set for this entry
      * @param newDateRead the new date read to set for this entry
      * @param newReadCount the new read count to set for this entry
      * @return a new instance of the library of the same type with the updated fields
      */
    def makeCopy(
        newBookshelves: List[ShelfPlacement] = bookshelves,
        newReadingStatus: ReadingStatus = readingStatus,
        newDateRead: Option[Date] = dateRead,
        newReadCount: Int = readCount
    ): Self

    /**
     * Adds a bookshelf to this library entry.
     * 
     * @param bookshelf the bookshelf to add
     * @param position optional position on the bookshelf
     * @return a new LibraryEntry with the bookshelf added
     */
    def addBookshelf(bookshelf: Bookshelf, position: Option[Int] = None): Self =
        this.makeCopy(newBookshelves = ShelfPlacement(bookshelf, position) :: this.bookshelves)

    /**
     * Removes a bookshelf from this library entry.
     * 
     * @param bookshelfName the name of the bookshelf to remove
     * @return a new LibraryEntry without the bookshelf
     */
    def removeBookshelf(bookshelfName: String): Self =
        this.makeCopy(newBookshelves = this.bookshelves.filter(_.bookshelf.name != bookshelfName))

    /**
     * Gets the position of a bookshelf in this library entry.
     * 
     * @param bookshelfName the name of the bookshelf
     * @return the position of the bookshelf, or None if not found
     */
    def getBookshelfPosition(bookshelfName: String): Option[Int] =
        this.bookshelves.find(_.bookshelf.name == bookshelfName).flatMap(_.position)

object LibraryEntry:

    /**
     * Creates a library entry from CSV data.
     * 
     * @param raw the parsed Goodreads CSV row
     * @return a new LibraryEntry instance of the appropriate type
     */
    def fromCsv(raw: GoodreadsRow): LibraryEntry =
        val edition = Edition.fromCsv(raw)
        val readingStatus = ReadingStatus.fromCsv(raw.exclusiveShelf).getOrElse(ReadingStatus.ToRead)
        val dateAdded = raw.dateAdded.trim match
            case "" => "Unknown"
            case nonEmpty => nonEmpty
        val dateRead = raw.dateRead.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty)
        val purchaseDate = raw.originalPurchaseDate.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty)
        val privateNotes = raw.privateNotes.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty)
        val readCount = raw.readCount.trim match
            case "" => 0
            case nonEmpty => nonEmpty.toInt
        val bookshelves = List()

        edition match
            case e: PhysicalEdition => PhysicalLibraryEntry(e, Condition.fromCsv(raw.condition), dateAdded, readingStatus, dateRead, purchaseDate, privateNotes, readCount, bookshelves)
            case e: DigitalEdition => DigitalLibraryEntry(e, dateAdded, readingStatus, dateRead, purchaseDate, privateNotes, readCount, bookshelves)

