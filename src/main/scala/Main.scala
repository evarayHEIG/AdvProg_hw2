import com.github.tototoshi.csv._
import java.io.File

object Main extends App {

  // Parser form the scala-csv library to read the CSV file
  val parser = GoodreadCSVParser

  val filename = "03-GoodreadsLibraryExport.csv"
  val reader = CSVReader.open(new File(filename))
  val rows = reader.all() // retrieve all rows from the CSV file as a list of lists of strings
  reader.close()
  val parsedRows = for row <- rows.tail yield parser.parseRow(row) // skip headers

  // Example users
  val users = List(User(1, "Alice", "alice@example.com"), User(2, "Bob", "bob@example.com"), User(3, "Charlie", "charlie@example.com"))
  // Collections to hold all parsed data
  var books = List.empty[Book]
  var reviews = List.empty[Review]
  var authors = List.empty[Author]
  var recommendations = List.empty[Recommendation]

  // For the example CSV, let's say it is Alice's library
  var alice = users.head

  // Iterate over all parsed rows
  for row <- parsedRows do
    val book = Book.fromCsv(row)
    var entry = LibraryEntry.fromCsv(row)
    val bookshelves = Bookshelf.parseBookshelves(row.bookshelvesWithPositions, row.exclusiveShelf) 
    var author = Author.fromCsv(row.author)
    // Process recommendations - only add if there is a non-empty recommendation field
    if row.recommendedFor.trim.nonEmpty || row.recommendedBy.trim.nonEmpty then
      val recommendation = Recommendation(
        book = book,
        recommendedBy = Recommendation.parseRecommendedBy(row.recommendedBy),
        recommendedTo = Recommendation.parseRecommendedFor(row.recommendedFor)
      )
      recommendations = recommendation :: recommendations

    // Process bookshelves 
    for bookshelf <- bookshelves do
      
      // If Alice doesn't own the bookshelf, add it to her collection and to the entry. Otherwise, add the existing bookshelf to the entry.
      alice.getBookshelf(bookshelf.bookshelf.name) match
        case None =>
          alice = alice.addBookshelf(bookshelf.bookshelf)
          entry = entry.addBookshelf(bookshelf.bookshelf, bookshelf.position)
        case Some(existingShelf) =>
          entry = entry.addBookshelf(existingShelf, bookshelf.position)

    // Process reviews - only add if there is at least the review text or a valid rating
    if row.myReview.trim.nonEmpty || Review.isValidRating(row.myRating.toInt) then
      val review = Review(
        book = book,
        user = alice.id,
        rating = Review.validatedRating(row.myRating.toInt),
        text = row.myReview.trim
      )
      reviews = review :: reviews

    // Add the book to Alice's library and to the global book collection. 
    alice = alice.addLibraryEntry(entry)
    books = book :: books

    // Update author with the new book. If the author already exists, update their book list. Otherwise, add the new author to the collection.
    authors.find(_.name == author.name) match
      case None =>
        author = author.addBook(book)
        authors = author :: authors
      case Some(existingAuthor) =>
        val updatedAuthor = existingAuthor.addBook(book)
        authors = updatedAuthor :: authors.filterNot(_.name == author.name)    

  // Print summary of parsed data, Alice's library, her favorite books, and all authors in the system.
  println(s"Parsed ${books.length} books and ${reviews.length} reviews for user ${alice.name}")
  alice.printLibrary(100)
  alice.printFavoriteBooks(reviews, 3)
  println("========== ALL AUTHORS ==========")
  for author <- authors do
    println(author)
}