import Types.UserId

/**
 * Represents a user with their library and bookshelves.
 * 
 * @param id unique user identifier
 * @param name user's display name
 * @param email user's email address
 * @param libraryEntries collection of books in the user's library
 * @param bookshelves collection of user's bookshelves
 * @author Eva Ray
 */
case class User(
    id: UserId,
    name: String,
    email: String,
    libraryEntries: List[LibraryEntry] = List.empty,
    bookshelves: List[Bookshelf] = List.empty,
    preference: UserPreference = UserPreference.casualReader
):
    /**
     * Returns a formatted string representation of the user.
     * 
     * @return multi-line string with user details
     */
    override def toString(): String =
        s"- id: $id\n" +
        s"- name: $name\n" +
        s"- email: $email\n" +
        s"- libraryEntries:\n${libraryEntries.map(e => e.edition.book.title).mkString("\n")}\n" +
        s"- bookshelves:\n${bookshelves.map(b => b.name).mkString("\n")}\n"

    /**
     * Adds a library entry to the user's collection.
     * 
     * @param entry the library entry to add
     * @return a new User with the entry added
     */
    def addLibraryEntry(entry: LibraryEntry): User =
        this.copy(libraryEntries = entry :: this.libraryEntries)

    /**
     * Removes a library entry from the user's collection.
     * 
     * @param entry the library entry to remove
     * @return a new User without the entry
     */
    def removeLibraryEntry(entry: LibraryEntry): User =
        this.copy(libraryEntries = this.libraryEntries.filter(_ != entry))

    /**
     * Adds a bookshelf to the user's collection.
     * Prevents duplicate bookshelves.
     * 
     * @param bookshelf the bookshelf to add
     * @return a new User with the bookshelf added, or unchanged if it already exists
     */
    def addBookshelf(bookshelf: Bookshelf): User =
        if (this.bookshelves.exists(_.name == bookshelf.name)) this
        else this.copy(bookshelves = bookshelf :: this.bookshelves)

    /**
     * Removes a bookshelf from the user's collection.
     * System bookshelves cannot be removed.
     * 
     * @param bookshelf the bookshelf to remove
     * @return a new User without the bookshelf, or unchanged if it's a system bookshelf
     */
    def removeBookshelf(bookshelf: Bookshelf): User =
        if (this.bookshelves.exists(_.name == bookshelf.name) && !Bookshelf.isSystemBookshelf(bookshelf))
            this.copy(bookshelves = this.bookshelves.filter(_ != bookshelf))
        else this

    /**
     * Adds an entry to a bookshelf with an optional position.
     * 
     * @param entry the library entry to place
     * @param bookshelf the target bookshelf
     * @param position optional position on the bookshelf
     * @return a new User with the updated entry
     */
    def addEntryToBookshelf(entry: LibraryEntry, bookshelf: Bookshelf, position: Option[Int] = None): User =
        val updatedEntry = entry.addBookshelf(bookshelf, position)
        this.copy(libraryEntries = updatedEntry :: this.libraryEntries.filter(_ != entry))

    /**
     * Prints the user's library to the console.
     */
    def printLibrary(n: Int): Unit =
        println(s"========== ${n} BOOKS FROM ${this.name.toUpperCase()}'s LIBRARY ==========")
        for entry <- libraryEntries.take(n) do
            println(s"- ${entry.edition.book.title} by ${entry.edition.book.author} (status: ${entry.readingStatus}, format: ${entry.edition.format.getOrElse("Unknown")}, bookshelves: ${entry.bookshelves.map(_.toString()).mkString(", ")})")

    /**
     * Returns the user's top-rated books.
     * 
     * @param reviews all reviews to filter from
     * @param n number of top books to return
     * @return list of top n reviews by this user, sorted by rating
     */
    def favoriteBooks(reviews: List[Review], n: Int): List[Review] =
        reviews.filter(r => r.user == this.id && r.rating.isDefined).sortBy(-_.rating.get).take(n)

    /**
     * Prints the user's favorite books to the console.
     * 
     * @param reviews all reviews to filter from
     * @param n number of top books to print
     */
    def printFavoriteBooks(reviews: List[Review], n: Int): Unit =
        println(s"========== ${this.name.toUpperCase()}'s TOP $n FAVORITE BOOKS ==========")
        favoriteBooks(reviews, n) match
            case Nil => println("No rated books found.")
            case favs => favs.foreach(review => println(s"- ${review.book.title} (${review.rating.get} stars)"))

    /**
     * Retrieves a bookshelf by name.
     * 
     * @param bookshelfName the name of the bookshelf to retrieve
     * @return an Option containing the Bookshelf if found, or None if not found
     */
    def getBookshelf(bookshelfName: String): Option[Bookshelf] =
        this.bookshelves.find(_.name == bookshelfName)

    /**
     * Retrieves bookshelves of a specific subtype.
     * Demonstrates covariance with type parameter [B <: Bookshelf] and use of ClassTag
     * to overcome type erasure.
     * 
     * @return list of bookshelves matching the specified type
     */
    def getBookshelvesByType[B <: Bookshelf](implicit tag: reflect.ClassTag[B]): List[B] =
        bookshelves.collect { case b: B => b }

    /**
     * Determines if a given edition is a good match for the user's preferences using the general edition matcher.
     * 
     * @param edition the edition to evaluate
     * @return true if the edition is a good match, false otherwise
     */
    def isEditionAGoodMatch(edition: Edition[Format]): Boolean =
        EditionMatcher.generalMatcher.isGoodFor(this.preference, edition)

object User:

    /**
     * Creates a new user with system bookshelves initialized.
     * 
     * @param id unique user identifier
     * @param name user's display name
     * @param email user's email address
     * @return a new User with default system bookshelves
     */
    def apply(id: UserId, name: String, email: String): User =
        new User(
            id = id,
            name = name,
            email = email,
            bookshelves = Bookshelf.systemBookshelves
        )