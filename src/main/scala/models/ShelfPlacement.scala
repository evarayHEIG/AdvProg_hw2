/**
 * Represents the placement of a bookshelf within a library entry, including the bookshelf itself and its position on the shelf.
 * 
 * @param bookshelf the bookshelf being placed
 * @param position the optional position of the bookshelf on the shelf (e.g., #187)
 * @author Eva Ray
 */
case class ShelfPlacement(
    bookshelf: Bookshelf,
    position: Option[Int]
):
    /**
     * Returns a formatted string representation of the shelf placement.
     * 
     * @return string with bookshelf name and position
     */
    override def toString(): String =
        s"${bookshelf.name} (${if (position.isDefined) s"#${position.get}" else "no position"})" 

object ShelfPlacement:

    // Regex to catch pattern like "(#187)" and extract the number
    private val regex = """\(#(\d+)\)""".r

    /**
    * Parses a bookshelf name and position from a raw string that loojs like "BookshelfName (#Number)".
    *
    * @param raw the raw string containing bookshelf name and optional position
    * @return a tuple of (bookshelf name, optional position)
    */
    def parseBookshelfWithPosition(raw: String): (String, Option[Int]) =
        val parts = raw.trim.split(" ")
        val bookshelf = parts(0)
        val position = parts(1) match {
            case regex(pos) => Some(pos.toInt)
            case _ => None
        }

        (bookshelf, position)
