/**
  * Model representing aggregated statistics about a user's library, including total number of books, average rating, 
  * and unique authors.
  *
  * @param total the total number of books in the user's library
  * @param avgRating the average rating of the books in the user's library, if available
  * @param uniqueAuthors the number of unique authors represented in the user's library
  * @author Eva Ray
  */
case class LibraryStats(
    total: Int,
    avgRating: Option[Double],
    uniqueAuthors: Int
):
    /* Override toString for better readability when printing library stats */
    override def toString(): String =
        s"======= LIBRARY STATS =======\nTotal Books: $total\nAverage Rating: ${avgRating.map(r => f"$r%.2f").getOrElse("N/A")}\nUnique Authors: $uniqueAuthors"


object LibraryStats:

    /**
    * Aggregates statistics about a user's library based on their library entries and reviews (showcasing foldLeft). 
    * FoldLeft allows us to traverse the user's library entries while maintaining an accumulator that keeps track of the
    * total number of books, the sum of ratings, the count of ratings, and the set of unique authors. All these 
    * statistics are computed in a single pass through the library entries.
    *
    * @param user the user whose library stats to aggregate
    * @param reviews the list of reviews to consider for calculating average ratings
    * @return a LibraryStats instance containing the aggregated statistics
    */
    def aggregateLibraryStats(user: User, reviews: List[Review]): LibraryStats =
        val init = (0, 0, 0, Set.empty[String]) // initialize (total, ratingSum, ratingCount, authors)
        val (total, ratingSum, ratingCount, authors) =
            user.libraryEntries.foldLeft(init){ (acc, entry) =>
                acc match 
                    case (tot, rSum, rCnt, auths) =>
                        val bookRatings = reviews
                            .filter(_.book.id == entry.edition.book.id)
                            .flatMap(_.rating)
                        (tot + 1, rSum + bookRatings.sum, rCnt + bookRatings.size, auths + entry.edition.book.author.name)
            }
        val avg = if ratingCount > 0 then Some(ratingSum.toDouble / ratingCount) else None
        LibraryStats(total, avg, authors.size)