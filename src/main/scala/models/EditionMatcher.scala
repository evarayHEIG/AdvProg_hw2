/**
  * A trait for matching book editions against user preferences to determine if an edition is a good fit for a user. 
  * 
  * This trait is contravariant in the edition type parameter [E] to allow a single matcher to be used for different 
  * specific edition types (e.g., physical or digital).
  * 
  * @author Eva Ray
  * @note This trait is contravariant in the edition type parameter [E] because it is a "consumer" of editions 
  * (it takes an edition as input to evaluate it against preferences). Contravariance allows us to use a more general
  * matcher (e.g., `EditionMatcher[Edition[Format]]`) to match against more specific edition types (e.g., 
  * `PhysicalEdition` or `DigitalEdition`) without losing type safety.
  */
trait EditionMatcher[-E <: Edition[Format]]:

    /**
      * Determines if a given edition matches the user's preferences.
      *
      * @param pref the user's preferences to match against
      * @param edition the edition to evaluate
      * @return true if the edition is a good fit for the user, false otherwise
      */
    def isGoodFor(pref: UserPreference, edition: E): Boolean

object EditionMatcher:

    // A general matcher that uses the user's popularity, publication year, and length preferences to evaluate if an 
    // edition is a good fit.
    val generalMatcher: EditionMatcher[Edition[Format]] =
    (perf, edition) =>
        val popularityMatches =
            perf.popularityPreference match
            case PopularityPreference.Popular => edition.book.averageRating.exists(_ > 4)
            case PopularityPreference.Liked => edition.book.averageRating.exists(_ > 3.5)
            case PopularityPreference.All => true
        val yearMatches =
            perf.publicationYearPreference match
            case PublicationYearPreference.Old => edition.publicationYear.exists(_ < 1950)
            case PublicationYearPreference.Recent => edition.publicationYear.exists(y => y >= 1950 && y < 2010)
            case PublicationYearPreference.New => edition.publicationYear.exists(_ >= 2010)
            case PublicationYearPreference.All => true
        val lengthMatches =
            perf.lengthPreference match
            case LengthPreference.Short => edition.nbPages.exists(_ < 200)
            case LengthPreference.Medium => edition.nbPages.exists(p => p >= 200 && p < 400)
            case LengthPreference.Long => edition.nbPages.exists(_ >= 400)
            case LengthPreference.All => true
        popularityMatches && yearMatches && lengthMatches

