import Types.UserId
import Types.Rating

/**
 * Represents a book review written by a user.
 * 
 * @param book the book being reviewed
 * @param user the ID of the user who wrote the review
 * @param rating optional rating from 1 to 5
 * @param text the review text content
 * @author Eva Ray
 */
case class Review (
    book: Book,
    user: UserId,
    rating: Option[Rating] = None,
    text: String,
):
    /**
     * Returns a formatted string representation of the review.
     * 
     * @return multi-line string with review details
     */
    override def toString(): String =
        s"- book: ${book.title}\n" +
        s"- user: $user\n" +
        s"- rating: ${rating.getOrElse("None")}\n" +
        s"- text: $text\n"

object Review:

    /**
     * Validates and wraps a rating value.
     * 
     * @param rating the rating value to validate
     * @return Some(rating) if between 1-5, None otherwise
     */
    def validatedRating(rating: Int): Option[Rating] =
        rating match
            case r if r >= 1 && r <= 5 => Some(r)
            case _ => None

    /**
     * Checks if a rating is valid.
     * 
     * @param rating the rating value to check
     * @return true if between 1-5, false otherwise
     */
    def isValidRating(rating: Int): Boolean =
        rating match
            case r if r >= 1 && r <= 5 => true
            case _ => false