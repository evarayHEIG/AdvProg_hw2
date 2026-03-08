/**
 * Class that represents a row in the Goodreads CSV export.
 * 
 * @author Eva Ray
 */
case class GoodreadsRow(
  bookId: String,
  title: String,
  author: String,
  authorLf: String,
  additionalAuthors: String,
  isbn: String,
  isbn13: String,
  myRating: String,
  averageRating: String,
  publisher: String,
  binding: String,
  numberOfPages: String,
  yearPublished: String,
  originalPublicationYear: String,
  dateRead: String,
  dateAdded: String,
  bookshelves: String,
  bookshelvesWithPositions: String,
  exclusiveShelf: String,
  myReview: String,
  spoiler: String,
  privateNotes: String,
  readCount: String,
  recommendedFor: String,
  recommendedBy: String,
  ownedCopies: String,
  originalPurchaseDate: String,
  originalPurchaseLocation: String,
  condition: String,
  conditionDescription: String,
  bcid: String
)
