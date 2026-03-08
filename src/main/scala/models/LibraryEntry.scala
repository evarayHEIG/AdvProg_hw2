/**
 * Represents an entry in a user's library.
 * 
 * @param edition the book edition
 * @param condition the physical condition
 * @param dateAdded when the book was added to the library
 * @param readingStatus current reading status
 * @param dateRead when the book was read
 * @param purchaseDate when the book was purchased
 * @param privateNotes user's private notes
 * @param readCount number of times read
 * @param bookshelves list of bookshelf placements
 * @author Eva Ray
 */
case class LibraryEntry(
    edition: Edition,
    condition: Condition,
    dateAdded: String,
    readingStatus: ReadingStatus = ReadingStatus.ToRead,
    dateRead: Option[String] = None,
    purchaseDate: Option[String] = None,
    privateNotes: Option[String] = None,
    readCount: Int = 0,
    bookshelves: List[ShelfPlacement] = List()
):

    /**
     * Returns a formatted string representation of the library entry.
     * 
     * @return multi-line string with entry details
     */
    override def toString(): String =
        s"- edition:\n${edition.toString()}" +
        s"- condition: ${condition.toString}\n" +
        s"- dateAdded: $dateAdded\n" +
        s"- readingStatus: ${readingStatus.toString}\n" +
        s"- dateRead: ${dateRead.getOrElse("None")}\n" +
        s"- purchaseDate: ${purchaseDate.getOrElse("None")}\n" +
        s"- privateNotes: ${privateNotes.getOrElse("None")}\n" +
        s"- readCount: $readCount\n" +
        s"- bookshelves:\n${bookshelves.map(b => b.bookshelf.toString()).mkString("\n")}"
    
    /**
     * Adds a bookshelf to this library entry.
     * 
     * @param bookshelf the bookshelf to add
     * @param position optional position on the bookshelf
     * @return a new LibraryEntry with the bookshelf added
     */
    def addBookshelf(bookshelf: Bookshelf, position: Option[Int] = None): LibraryEntry =
        this.copy(bookshelves = ShelfPlacement(bookshelf, position) :: this.bookshelves)

    /**
     * Removes a bookshelf from this library entry.
     * 
     * @param bookshelfName the name of the bookshelf to remove
     * @return a new LibraryEntry without the bookshelf
     */
    def removeBookshelf(bookshelfName: String): LibraryEntry =
        this.copy(bookshelves = this.bookshelves.filter(_.bookshelf.name != bookshelfName))

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
     * @return a new LibraryEntry instance
     */
    def fromCsv(raw: GoodreadsRow): LibraryEntry =
        LibraryEntry(
            edition = Edition.fromCsv(raw),
            condition =  Condition.fromCsv(raw.condition),
            readingStatus = ReadingStatus.fromCsv(raw.exclusiveShelf).getOrElse(ReadingStatus.ToRead),
            dateAdded = raw.dateAdded.trim match
                case "" => "Unknown"
                case nonEmpty => nonEmpty,
            dateRead = raw.dateRead.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty),
            purchaseDate = raw.originalPurchaseDate.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty),
            privateNotes = raw.privateNotes.trim match
                case "" => None
                case nonEmpty => Some(nonEmpty),
            readCount = raw.readCount.trim match
                case "" => 0
                case nonEmpty => nonEmpty.toInt,
            bookshelves = List()
        )
            
        