/**
  * Enumeration representing different types of audio formats for library entries.
  * 
  * @author Eva Ray 
  */
enum AudioType extends Format:
  case CD, Audible

  /**
   * Returns a formatted string representation of the audio type.
   * 
   * @return a string describing the audio format
   */
  override def toString(): String =
    this match
      case CD => "Audio CD"
      case Audible => "Audible Audio"

object AudioType:

  /**
    * Parses an audio type from CSV data.
    *
    * @param raw the raw string from CSV representing the audio format
    * @return The correct AudioType if recognized, None otherwise
    */
  def fromCsv(raw: String): Option[AudioType] =
    raw.trim.toLowerCase match
      case "audio cd" => Some(CD)
      case "audible audio" => Some(Audible)
      case _ => None