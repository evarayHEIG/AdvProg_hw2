package utils

/**
 * A trait for calculating similarity between two instances of type A.
 *
 * @tparam A the type for which similarity is calculated
 * @author Eva Ray
 */
trait Similarity[A]:
    def similarity(a1: A, b1: A): Double

object Similarity:
  
    /**
    * A helper function to create a Jaccard similarity function for any type A based on a set of features of type B.
    *
    * @param extract a function that extracts a set of features from an instance of type A
    * @tparam A the type for which similarity is calculated
    * @tparam B the type of features used for calculating similarity
    * @return a Similarity[A] instance that calculates Jaccard similarity based on the extracted features
    */
    def jaccard[A, B](extract: A => Set[B]): Similarity[A] =
    (a1: A, a2: A) =>
        val s1 = extract(a1)
        val s2 = extract(a2)
        if (s1.isEmpty && s2.isEmpty) 1.0
        else s1.intersect(s2).size.toDouble / s1.union(s2).size.toDouble
