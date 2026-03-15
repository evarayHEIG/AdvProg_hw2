/**
  * Enumeration representing different types of physical formats for library entries.
  * 
  * @author Eva Ray
  */
enum PhysicalType extends Format:
  case Hardcover, Paperback, MassMarket

  /**
    * Returns a formatted string representation of the physical type.
    * 
    * @return a string describing the physical format
    */
  override def toString(): String =
    this match
      case Hardcover => "Hardcover"
      case Paperback => "Paperback"
      case MassMarket => "Mass Market Paperback"

object PhysicalType:

  /**
    * Parses a physical type from CSV data.
    * 
    * @param raw the raw string from CSV representing the physical format
    * @return The correct PhysicalType if recognized, None otherwise
    */
  def fromCsv(raw: String): Option[PhysicalType] =
    raw.trim.toLowerCase match
      case "hardcover" => Some(Hardcover)
      case "paperback" => Some(Paperback)
      case "mass market paperback" => Some(MassMarket)
      case _ => None