/**
 * Represents a bookshelf with a name and a reading status. These are the default bookshelves that Goodreads creates for each user.
 * 
 * @param name the name of the bookshelf
 * @param readingStatus the reading status associated with this bookshelf
 * @author Eva Ray
 */
case class ReadingStatusBookshelf(override val name: String, readingStatus: ReadingStatus) extends Bookshelf(name: String)
