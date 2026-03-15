/**
  * Trait representing different types of formats for library entries.
  * 
  * @author Eva Ray
  */
trait Format

object Format:

  /**
    * Parses a format type from CSV data.
    * 
    * @param raw the raw string from CSV representing the format
    * @return The correct Format if recognized, None otherwise
    */
  def fromCsv(raw: String): Option[Format] =
    raw.trim.toLowerCase match
      case "hardcover" => PhysicalType.fromCsv(raw)
      case "paperback" => PhysicalType.fromCsv(raw)
      case "mass market paperback" => PhysicalType.fromCsv(raw)
      case "kindle edition" => EbookType.fromCsv(raw)
      case "ebook" => EbookType.fromCsv(raw)
      case "audiobook" => AudioType.fromCsv(raw)
      case "audio cd" => AudioType.fromCsv(raw)
      case "audible audio" => AudioType.fromCsv(raw)
      case "web comic" => EbookType.fromCsv(raw)
      case _ => None
