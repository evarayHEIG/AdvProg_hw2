import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Test suite for CSV parsing functionalities. This suite covers methods for parsing CSV rows into structured data.
 * @author Eva Ray
 */
class CsvParserTest extends AnyFlatSpec with Matchers:

  /* Sample CSV row for testing */
  private val sampleRow: List[String] = List(
    "10",          // bookId
    "Test Book",   // title
    "Author Name", // author
    "",            // authorLf
    "",            // additionalAuthors
    "123",         // isbn
    "12313",       // isbn13
    "5",           // myRating
    "4.5",         // averageRating
    "Pub",         // publisher
    "hardcover",   // binding
    "150",         // numberOfPages
    "2000",        // yearPublished
    "2000",        // originalPublicationYear
    "",            // dateRead
    "2022-01-01",  // dateAdded
    "",            // bookshelves
    "",            // bookshelvesWithPositions
    "",            // exclusiveShelf
    "",            // myReview
    "",            // spoiler
    "",            // privateNotes
    "1",           // readCount
    "",            // recommendedFor
    "",            // recommendedBy
    "",            // ownedCopies
    "",            // originalPurchaseDate
    "",            // originalPurchaseLocation
    "",            // condition
    "",            // conditionDescription
    ""             // bcid
  )

  private def parsedRow: GoodreadsRow = GoodreadCSVParser.parseRow(sampleRow)

  "GoodreadCSVParser.parseRow" should "parse a minimal row into GoodreadsRow" in {
    val gr = parsedRow
    gr.bookId shouldBe "10"
    gr.title shouldBe "Test Book"
    gr.binding shouldBe "hardcover"
  }

  "Book.fromCsv" should "build a Book from GoodreadsRow" in {
    val book = Book.fromCsv(parsedRow)
    book.id shouldBe 10
    book.title shouldBe "Test Book"
    book.averageRating shouldBe Some(4.5)
  }

  "Edition.fromCsv" should "build an Edition with parsed format and pages" in {
    val edition = Edition.fromCsv(parsedRow)
    edition.book.title shouldBe "Test Book"
    edition.nbPages shouldBe Some(150)
    edition.format shouldBe Some(PhysicalType.Hardcover)
  }

  "LibraryEntry.fromCsv" should "build a LibraryEntry from GoodreadsRow" in {
    val entry = LibraryEntry.fromCsv(parsedRow)
    entry.edition.book.title shouldBe "Test Book"
    entry.readCount shouldBe 1
    entry.readingStatus shouldBe ReadingStatus.ToRead
  }

  "Format.fromCsv" should "parse known format values" in {
    Format.fromCsv("hardcover") shouldBe Some(PhysicalType.Hardcover)
    Format.fromCsv("kindle edition") shouldBe Some(EbookType.Kindle)
    Format.fromCsv("audio cd") shouldBe Some(AudioType.CD)
    Format.fromCsv("unknown") shouldBe None
  }

  "PhysicalType.fromCsv" should "parse physical format values" in {
    PhysicalType.fromCsv("hardcover") shouldBe Some(PhysicalType.Hardcover)
    PhysicalType.fromCsv("paperback") shouldBe Some(PhysicalType.Paperback)
    PhysicalType.fromCsv("mass market paperback") shouldBe Some(PhysicalType.MassMarket)
    PhysicalType.fromCsv("kindle edition") shouldBe None
  }

  "EbookType.fromCsv" should "parse ebook format values" in {
    EbookType.fromCsv("kindle edition") shouldBe Some(EbookType.Kindle)
    EbookType.fromCsv("web comic") shouldBe Some(EbookType.WebComic)
    EbookType.fromCsv("ebook") shouldBe Some(EbookType.Other)
    EbookType.fromCsv("hardcover") shouldBe None
  }

  "AudioType.fromCsv" should "parse audio format values" in {
    AudioType.fromCsv("audio cd") shouldBe Some(AudioType.CD)
    AudioType.fromCsv("audible audio") shouldBe Some(AudioType.Audible)
    AudioType.fromCsv("paperback") shouldBe None
  }

  "ReadingStatus.fromCsv" should "parse reading status values" in {
    ReadingStatus.fromCsv("to-read") shouldBe Some(ReadingStatus.ToRead)
    ReadingStatus.fromCsv("currently-reading") shouldBe Some(ReadingStatus.CurrentlyReading)
    ReadingStatus.fromCsv("read") shouldBe Some(ReadingStatus.Read)
    ReadingStatus.fromCsv("dnf") shouldBe Some(ReadingStatus.Dnf)
    ReadingStatus.fromCsv("unknown") shouldBe None
  }

  "Condition.fromCsv" should "parse condition values and default to Unspecified" in {
    Condition.fromCsv("new") shouldBe Condition.New
    Condition.fromCsv("very good") shouldBe Condition.VeryGood
    Condition.fromCsv("good") shouldBe Condition.Good
    Condition.fromCsv("acceptable") shouldBe Condition.Acceptable
    Condition.fromCsv("anything else") shouldBe Condition.Unspecified
  }
