/**
 * Represents the format of a book edition.
 * 
 * @author Eva Ray
 */
enum Format:
  case Hardcover, Paperback, MassMarket, Kindle, Ebook, Audiobook, WebComic

  /**
   * Returns a string representation of the format.
   *
   * @return a string describing the format
   */
  override def toString(): String =
    this match
      case Hardcover => "Hardcover"
      case Paperback => "Paperback"
      case MassMarket => "Mass Market Paperback"
      case Kindle => "Kindle Edition"
      case Ebook => "Ebook"
      case Audiobook => "Audiobook"
      case WebComic => "Web Comic"

object Format:

  /**
   * Parses a format from CSV data.
   * 
   * @param raw the raw string from CSV
   * @return Some(Format) if recognized, None otherwise
   */
  def fromCsv(raw: String): Option[Format] =
    raw.trim.toLowerCase match
      case "hardcover" => Some(Hardcover)
      case "paperback" => Some(Paperback)
      case "mass market paperback" => Some(MassMarket)
      case "kindle edition" => Some(Kindle)
      case "ebook" => Some(Ebook)
      case "audiobook" => Some(Audiobook)
      case "audio cd" => Some(Audiobook)
      case "audible audio" => Some(Audiobook)
      case "web comic" => Some(WebComic)
      case _ => None

