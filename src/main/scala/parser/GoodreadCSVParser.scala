/**
  * Goodreads CSV Parser
  * 
  * @author Eva Ray
  */
object GoodreadCSVParser:

    /**
     * Parses a row of Goodreads CSV data into a structured format.
     * 
     * @param row the list of string values from a CSV row
     * @return a GoodreadsRow containing the parsed data
     */
    def parseRow(row: List[String]): GoodreadsRow =
        GoodreadsRow(
            bookId = row(0),
            title = row(1),
            author = row(2),
            authorLf = row(3),
            additionalAuthors = row(4),
            isbn = row(5),
            isbn13 = row(6),
            myRating = row(7),
            averageRating = row(8),
            publisher = row(9),
            binding = row(10),
            numberOfPages = row(11),
            yearPublished = row(12),
            originalPublicationYear = row(13),
            dateRead = row(14),
            dateAdded = row(15),
            bookshelves = row(16),
            bookshelvesWithPositions = row(17),
            exclusiveShelf = row(18),
            myReview = row(19),
            spoiler = row(20),
            privateNotes = row(21),
            readCount = row(22),
            recommendedFor = row(23),
            recommendedBy = row(24),
            ownedCopies = row(25),
            originalPurchaseDate = row(26),
            originalPurchaseLocation = row(27),
            condition = row(28),
            conditionDescription = row(29),
            bcid = row(30)
        )
