import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Test suite for the User class and its related functionalities. This suite covers methods for managing library entries, 
 * bookshelves, and data exploration features like best-rated books and authors.
 * @author Eva Ray
 */
class UserTest extends AnyFlatSpec with Matchers:

    /**
      * Helper method to create a LibraryEntry for testing purposes. This method abstracts the creation of entries, 
      * allowing tests to focus on the functionality being tested rather than setup details.
      * 
      * @param bookId the ID of the book
      * @param title the title of the book
      * @param authorName the name of the author
      * @return a LibraryEntry with the specified book details
      */ 
	private def makeEntry(bookId: Int, title: String, authorName: String): LibraryEntry =
		val author = Author(authorName, List.empty)
		val book = Book(bookId, title, author)
		val edition = PhysicalEdition(book)
		PhysicalLibraryEntry(edition, Condition.Unspecified, "2024-01-01")

	"addLibraryEntry" should "add an entry to the user library" in {
		val user = User(1, "Alice", "a@example.com")
		val entry = makeEntry(1, "Book A", "Author A")

		val updated = user.addLibraryEntry(entry)

		updated.libraryEntries should contain(entry)
	}

	"removeLibraryEntry" should "remove an entry from the user library" in {
		val entry = makeEntry(2, "Book B", "Author B")
		val user = User(2, "Alice", "a@example.com").addLibraryEntry(entry)

		val updated = user.removeLibraryEntry(entry)

		updated.libraryEntries should not contain entry
	}

	"addBookshelf" should "add a custom bookshelf to the user" in {
		val user = User(3, "Alice", "a@example.com")
		val shelf = CustomBookshelf("favorites")

		val updated = user.addBookshelf(shelf)

		updated.bookshelves.map(_.name) should contain("favorites")
	}

	"removeBookshelf" should "remove a non-system bookshelf" in {
		val shelf = CustomBookshelf("wishlist")
		val user = User(4, "Alice", "a@example.com").addBookshelf(shelf)

		val updated = user.removeBookshelf(shelf)

		updated.bookshelves.map(_.name) should not contain "wishlist"
	}

	"addEntryToBookshelf" should "attach a bookshelf to an existing entry" in {
		val shelf = CustomBookshelf("favorites")
		val entry = makeEntry(5, "Book A", "Author A")
		val user = User(5, "Alice", "a@example.com").addLibraryEntry(entry)

		val updated = user.addEntryToBookshelf(entry, shelf, Some(1))
		val updatedEntry = updated.libraryEntries.find(_.edition.book.id == 5).get

		updatedEntry.bookshelves.map(_.bookshelf.name) should contain("favorites")
	}

	"bestRatedBooks" should "return top rated books for a user" in {
		val user = User(6, "Alice", "a@example.com")
		val author = Author("Author A", List.empty)
		val book1 = Book(10, "Book A1", author)
		val book2 = Book(11, "Book A2", author)
		val reviews = List(
			Review(book1, user.id, Some(3), "ok"),
			Review(book2, user.id, Some(5), "great")
		)

		val top = user.bestRatedBooks(reviews, 1)

		top shouldBe List(("Book A2", 5))
	}

	"mostShelvedAuthors" should "return authors ordered by shelf count" in {
		val user = User(7, "Alice", "a@example.com")
			.addLibraryEntry(makeEntry(20, "Book A1", "Author A"))
			.addLibraryEntry(makeEntry(21, "Book A2", "Author A"))
			.addLibraryEntry(makeEntry(22, "Book B3", "Author B"))

		val top = user.mostShelvedAuthors(1)

		top shouldBe List("Author A")
	}

	"bestRatedAuthors" should "return authors ordered by average rating" in {
		val user = User(8, "Alice", "a@example.com")
		val author1 = Author("Author A", List.empty)
		val author2 = Author("Author B", List.empty)
		val reviews = List(
			Review(Book(30, "Book A1", author1), user.id, Some(4), "good"),
			Review(Book(31, "Book A2", author1), user.id, Some(5), "great"),
			Review(Book(32, "Book B1", author2), user.id, Some(3), "ok")
		)

		val top = user.bestRatedAuthors(reviews, 1)

		top.head._1 shouldBe "Author A"
	}

	"User.apply" should "initialize a new user with system bookshelves" in {
		val user = User(99, "New User", "new@example.com")

		user.bookshelves.map(_.name).toSet shouldBe Bookshelf.systemBookshelves.map(_.name).toSet
		user.bookshelves.size shouldBe Bookshelf.systemBookshelves.size
	}
