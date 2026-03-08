/**
 * Represents the physical condition of a book in a library.
 * 
 * @author Eva Ray
 */
enum Condition:
  case New, VeryGood, Good, Acceptable, Unspecified

  /**
   * Returns the string representation of the condition in lowercase.
   * 
   * @return the condition as a human-readable string
   */
  override def toString(): String =
    this match
      case New => "new"
      case VeryGood => "very good"
      case Good => "good"
      case Acceptable => "acceptable"
      case Unspecified => "unspecified"

object Condition:

  /**
   * Parses a condition from a CSV string value.
   * 
   * @param raw the raw string from CSV data
   * @return the corresponding Condition enum value, or Unspecified if not recognized
   */
  def fromCsv(raw: String): Condition =
    raw.trim.toLowerCase match
      case "new" => New
      case "very good" => VeryGood
      case "good" => Good
      case "acceptable" => Acceptable
      case "unspecified" => Unspecified
      case _ => Unspecified
