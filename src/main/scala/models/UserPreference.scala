/**
  * Enum that represents a user's preference for book popularity, which can be either Popular, Liked, or All.
  */
enum PopularityPreference:
    case Popular, Liked, All

/**
  * Enum that represents a user's preference for publication year, which can be Old (before 1950), Recent (1950-2009), 
  * New (2010 and later), or All.
  */
enum PublicationYearPreference:
    case Old, Recent, New, All

/**
  * Enum that represents a user's preference for book length, which can be Short (less than 200 pages), Medium (200-399 
  * pages), Long (400 or more pages), or All.
  */
enum LengthPreference:
    case Short, Medium, Long, All

/**
  * A case class representing a user's preferences for book selection, including their preferences for popularity, 
  * publication year, and length. 
  * 
  * This class is used in conjunction with the EditionMatcher trait to evaluate how well a given edition matches the 
  * user's preferences.
  * 
  * @param popularityPreference the user's preference for book popularity
  * @param publicationYearPreference the user's preference for publication year 
  * @param lengthPreference the user's preference for book length
  * @author Eva Ray
  */
case class UserPreference(
    popularityPreference: PopularityPreference,
    publicationYearPreference: PublicationYearPreference,
    lengthPreference: LengthPreference
)

object UserPreference:

    // A sample user preference for a casual reader who prefers popular, recent, medium-length books.
    val casualReader: UserPreference =
        UserPreference(
            popularityPreference = PopularityPreference.Popular,
            publicationYearPreference = PublicationYearPreference.Recent,
            lengthPreference = LengthPreference.Medium
        )