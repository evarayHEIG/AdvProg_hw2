import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Test suite for library exploration functionalities. This suite covers methods for managing library entries, 
 * bookshelves, and data exploration features like best-rated books and authors.
 * @author Eva Ray
 */
class LibraryExplorationTest extends AnyFlatSpec with Matchers:

  "aggregateLibraryStats" should "compute total, avgRating and unique authors" in {
    val author = Author("A Author", List())
    val book = Book(1, "Book A", author, None, Some(4.5), Some(2000))
    val edition = PhysicalEdition(book, None, None, None, None, Some(250), Some(2000))
    val entry = PhysicalLibraryEntry(edition, Condition.Unspecified, "2020-01-01", ReadingStatus.Read)
    val user = User(1, "Alice", "a@example.com").addLibraryEntry(entry)
    val review = Review(book, user.id, Some(5), "Nice")

    val stats = LibraryStats.aggregateLibraryStats(user, List(review))
    stats.total shouldBe 1
    stats.avgRating shouldBe Some(5.0)
    stats.uniqueAuthors shouldBe 1
  }

  "ifLibraryNotEmpty" should "return Some when user has books and None when empty" in {
    val author = Author("Author A", List())
    val book = Book(2, "Book A", author)
    val edition = PhysicalEdition(book)
    val entry = PhysicalLibraryEntry(edition, Condition.Unspecified, "2020-01-01")
    val userWith = User(10, "Has", "x@example.com").addLibraryEntry(entry)
    val userEmpty = User(11, "Empty", "y@example.com")

    val some = LibraryExploration.ifLibraryNotEmpty(userWith)(LibraryStats.aggregateLibraryStats(userWith, List()))
    some.isDefined shouldBe true

    val none = LibraryExploration.ifLibraryNotEmpty(userEmpty)(LibraryStats.aggregateLibraryStats(userEmpty, List()))
    none shouldBe None
  }

  "recommend" should "use the given EditionMatcher and return matching editions" in {
    given EditionMatcher[Edition[Format]] = EditionMatcher.generalMatcher

    val author = Author("Author A", List())
    val book = Book(3, "Book A", author, None, Some(4.8), Some(2005))
    val edition = PhysicalEdition(book, None, None, None, None, Some(300), Some(2005))
    val entry = PhysicalLibraryEntry(edition, Condition.Unspecified, "2021-01-01")
    val user = User(20, "Alice", "a@example.com").addLibraryEntry(entry)

    val recs = LibraryExploration.recommend(user, List(edition), 5)
    recs should contain (edition)
  }

  "userJaccardSimilarity" should "be 1.0 for identical single-book libraries" in {
    val author = Author("Author A", List())
    val book = Book(4, "Book A", author)
    val edition = PhysicalEdition(book)
    val entry = PhysicalLibraryEntry(edition, Condition.Unspecified, "2022-01-01")
    val u1 = User(30, "Alice", "a@example.com").addLibraryEntry(entry)
    val u2 = User(31, "Bob", "b@example.com").addLibraryEntry(entry)

    UserSimilarity.userJaccardSimilarity(u1, u2) shouldBe 1.0
  }