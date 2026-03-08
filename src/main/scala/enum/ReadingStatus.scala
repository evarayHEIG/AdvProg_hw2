/**
 * Represents the reading status of a book in a user's library.
 * 
 * @author Eva Ray
 */
enum ReadingStatus:
  case ToRead, CurrentlyReading, Read, Dnf

  /**
   * Returns the printable string representation of a status.
   * 
   * @return the status as a string
   */
  override def toString(): String =
    this match
      case ToRead => "to-read"
      case CurrentlyReading => "currently-reading"
      case Read => "read"
      case Dnf => "dnf"

object ReadingStatus:

  /**
   * Parses a reading status from CSV data.
   * 
   * @param raw the raw string from CSV
   * @return Some(ReadingStatus) if recognized, None otherwise
   */
  def fromCsv(raw: String): Option[ReadingStatus] =
    raw.trim.toLowerCase match
      case "to-read" => Some(ToRead)
      case "currently-reading" => Some(CurrentlyReading)
      case "read" => Some(Read)
      case "dnf" => Some(Dnf)
      case _ => None