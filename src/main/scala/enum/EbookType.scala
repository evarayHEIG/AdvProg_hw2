/**
  * Enumeration representing different types of ebook formats for library entries.
  * 
  * @author Eva Ray
  */
enum EbookType extends Format:
  case Kindle, WebComic, Other

  /**
    * Returns a formatted string representation of the ebook type.
    * 
    * @return a string describing the ebook format
    */
  override def toString(): String =
    this match
      case Kindle => "Kindle Edition"
      case WebComic => "Web Comic"
      case Other => "Ebook"

object EbookType:

  /**
    * Parses an ebook type from CSV data.
    * 
    * @param raw the raw string from CSV representing the ebook format
    * @return The correct EbookType if recognized, None otherwise
    */
  def fromCsv(raw: String): Option[EbookType] =
    raw.trim.toLowerCase match
      case "kindle edition" => Some(Kindle)
      case "web comic" => Some(WebComic)
      case "ebook" => Some(Other)
      case _ => None
