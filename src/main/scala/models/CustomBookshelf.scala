/**
 * Represents a custom bookshelf with a name.
 * 
 * @param name the name of the custom bookshelf
 * @author Eva Ray
 */
case class CustomBookshelf(override val name: String) extends Bookshelf(name: String)

