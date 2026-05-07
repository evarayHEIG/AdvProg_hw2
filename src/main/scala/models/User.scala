import Types.UserId
import Types.Rating
import utils.Similarity

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
     * Returns the user's top-rated books (data exploration). This method demonstrates functional programming, avoiding 
     * mutable state
     * 
     * @param reviews all reviews to filter from
     * @param n number of top books to return
     * @return list of top n reviews by this user, sorted by rating
     */
    def bestRatedBooks(reviews: List[Review], n: Int): List[(String, Rating)] =
        reviews
            .filter(r => r.user == this.id && r.rating.isDefined)
            .map(r => (r.book.title, r.rating.get))
            .sortBy(-_._2)
            .take(n)

    /**
      * Returns the user's most shelved authors based on the books in their library (data exploration). This method
      * showcases functional programming, avoiding mutable state.
      *
      * @param n number of top authors to return
      * @return list of most shelved authors, sorted by number of books shelved
      */
    def mostShelvedAuthors(n: Int): List[String] =
        libraryEntries
            .groupBy(_.edition.book.author.name)
            .map{case (author, entries) => (author, entries.size)}
            .toList
            .sortBy(-_._2) 
            .take(n)
            .map(_._1)

    /**
      * Returns the user's best-rated authors based on their reviews (data exploration). This method demonstrates 
      * functional programming by using higher-order functions and avoiding mutable state.
      *
      * @param reviews all reviews to filter from
      * @param n number of best-rated authors to return
      * @return list of best-rated authors with their average rating
      */
    def bestRatedAuthors(reviews: List[Review], n: Int): List[(String, Double)] =
        reviews
            .filter(r => r.user == this.id && r.rating.isDefined)
            .groupBy(_.book.author.name)
            .map{case (author, reviews) => (author, reviews.flatMap(_.rating).sum.toDouble / reviews.size)}
            .toList
            .sortBy(-_._2)
            .take(n)

    /**
     * Prints the user's favorite books to the console.
     * 
     * @param reviews all reviews to filter from
     * @param n number of top books to print
     */
    def printFavoriteBooks(reviews: List[Review], n: Int): Unit =
        println(s"========== ${this.name.toUpperCase()}'s TOP $n FAVORITE BOOKS ==========")
        bestRatedBooks(reviews, n) match
            case Nil => println("No rated books found.")
            case favs => favs.foreach(r => println(s"- ${r._1} (${r._2} stars)"))

    /**
     * Prints the user's best-rated authors to the console.
     * 
     * @param reviews all reviews to filter from
     * @param n number of top authors to print
     */
    def printBestRatedAuthors(reviews: List[Review], n: Int): Unit =
        println(s"========== ${this.name.toUpperCase()}'S TOP $n BEST-RATED AUTHORS ==========")
        bestRatedAuthors(reviews, n) match
            case Nil => println("No rated authors found.")
            case favs => favs.foreach(r => println(s"- ${r._1} (average rating: ${r._2} stars)"))

    /**
     * Prints the user's most shelved authors to the console.
     * 
     * @param n number of top authors to print
     */
    def printMostShelvedAuthors(n: Int): Unit =
        println(s"========== ${this.name.toUpperCase()}'S TOP $n MOST SHELVED AUTHORS ==========")
        mostShelvedAuthors(n) match
            case Nil => println("No shelved authors found.")
            case favs => favs.foreach(a => println(s"- ${a}"))

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

object UserSimilarity:

    /**
      * Calculates the similarity between two users based on a provided similarity function (higher-order function). This
      * allows for flexible similarity calculations using different criteria without changing the method's implementation.
      *
      * @param user1 the first user to compare
      * @param user2 the second user to compare
      * @param similarityFunction a function that takes two users and returns a similarity score as a Double
      * @return the similarity score between the two users
      */
    def userSimilarity(user1: User, user2: User, similarityFunction: (User, User) => Double): Double =
        similarityFunction(user1, user2)

    /**
      * A specific similarity function that calculates the Jaccard similarity between two users based on the books in 
      * their libraries (showcases anonymous function).
      *
      * @param user1 the first user to compare
      * @param user2 the second user to compare
      * @return the Jaccard similarity score between the two users
      */
    def userJaccardSimilarity(user1: User, user2: User): Double =
        val userJaccard = Similarity.jaccard[User, Int](
            _.libraryEntries.map(_.edition.book.id).toSet
        )
        userJaccard.similarity(user1, user2)

/* Object for exploring the user's library */
object LibraryExploration:

    /**
      * Executes a code block if the user's library is not empty (call-by-name). This allows for safe execution of code 
      * that relies on the presence of library entries without having to check for emptiness every time. The evaluation 
      * of the code block is delayed until it's confirmed that the library is not empty.
      *
      * @param user the user whose library to check
      * @param codeBlock the code block to execute if the library is not empty
      * @return an Option containing the result of the code block if the library is not empty, or None if it is empty
      */
    def ifLibraryNotEmpty[A](user: User)(codeBlock: => A): Option[A] =
        if user.libraryEntries.nonEmpty then Some(codeBlock) else None

    /**
      * Recommends editions to the user based on their preferences and a provided edition matcher (context abstraction).
      * This allows the matcher logic to be defined separately and easy to swap out for different recommendation 
      * strategies without modifying the recommendation method itself.
      *
      * @param user the user for whom to recommend editions
      * @param editions the list of editions to consider for recommendation
      * @param n the number of recommendations to return
      * @param matcher the edition matcher to use for evaluating recommendations
      * @return a list of recommended editions
      */
    def recommend[E <: Edition[Format]](user: User, editions: List[E], n: Int)
        (using matcher: EditionMatcher[E]): List[E] =
        editions
        .filter(edition => matcher.isGoodFor(user.preference, edition))
        .take(n)