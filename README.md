# AdvProg - Exercise 2 - Goodreads Library Modeling in Scala

## Prérequis

- Scala >= 3
- Librairie [scala-csv](https://index.scala-lang.org/tototoshi/scala-csv) pour parse le CSV d'export de Goodreads.

## Lancer le programme

Au niveau du fichier `build.sbt`, lancer la commande suivante dans le terminal:

```bash
sbt run
```

Le programme load les données du CSV dans des collections Scala et affiche dans le terminal: 100 livres de la bibliothèque de l'utilisateur, ses 3 livres préférés et tous les auteurs du système.

## Modèle de données

Le schéma ci-dessous représente les classes et les relations qui modélisent les données du CSV d'export GoodReads. C'est une vue très haut niveau qui ne contient ni les attributs ni les méthodes des classes. 

![Class diagram](figures/class_diagram.png)

## Choix de conception et d'implémentation

- Les champs suivants du CSV n'ont pas été retenus, soit car ils sont tout le temps vides, soit car ils ne sont pas très pertinents pour la modélisation: `Author l-f`, `BCID`, `Condition Description`.
- Certains champs qui sont tout le temps vides dans le CSV ont été modélisés dans le but d'avoir une modélisation complète.
- Bien que le CSV ne contienne l'export des données d'un seul utilisateur, les utilisateurs ont été modélisés dans la classe `User` pour avoir une modélisation plus réaliste et pouvoir faire des recommandations entre utilisateurs. On admet que les champs `RecommendedFor` et `RecommendedBy` (qui sont tout le temps vides dans le CSV) contiennent des user id.
- Le champ `Exclusive Shelf` n'étant jamais vide, il a été décidé de rajouter le livre dans l'étagère qui en découle dans les cas où `Bookshelf` est vide. 
- Les auteurs et livres ne sont que ceux présents dans le CSV, ils ne sont donc pas exhaustif de tout ce qui peut être trouvé sur Goodreads. 
- Les `match` ont été préféré aux `if` si possible.
- La méthode `getOrElse` (sucre syntaxique) a été utilisée à la place de la construction avec match pour avoir un code moins verbeux.
```
foo match
    case Some(value) => value
    case None => defaultValue
```