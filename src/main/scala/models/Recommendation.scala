/**
 * Represents a book recommendation made by one user to one or more other users.
 * 
 * @param book the book being recommended
 * @param recommendedBy the user ID of the person making the recommendation
 * @param recommendedTo a list of user IDs of the people receiving the recommendation, comma-separated in the CSV
 * @author Eva Ray
 */
case class Recommendation(
    book: Book,
    recommendedBy: Option[UserId],
    recommendedTo: Option[List[UserId]]
):
    /**
     * Returns a formatted string representation of the recommendation.
     * 
     * @return multi-line string with recommendation details
     */
    override def toString(): String =
        s"- book: ${book.title}\n" +
        s"- recommendedBy: ${recommendedBy.map(_.toString).getOrElse("None")}\n" +
        s"- recommendedTo: ${recommendedTo.map(_.mkString(", ")).getOrElse("None")}\n"

object Recommendation:

    /**
     * Creates a recommendation from CSV data.
     * 
     * @param raw the parsed Goodreads CSV row
     * @return a new Recommendation instance
     */
    def parseRecommendedFor(raw: String): Option[List[UserId]] =
        raw.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty.split(",").toList.map(_.trim.toInt))

    def parseRecommendedBy(raw: String): Option[UserId] =
        raw.trim match
            case "" => None
            case nonEmpty => Some(nonEmpty.toInt)