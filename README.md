# HackerNews API Client in Scala

## HackerNews API?

- http://blog.ycombinator.com/hacker-news-api
- https://hacker-news.firebaseio.com/
- https://github.com/HackerNews/API

## How to use

```scala
libraryDependencies += "com.github.seratch" %% "hackernews4s" % "0.0"
```

## Examples

```scala
import hackernews4s.v0._
val topStories: Seq[Item] = HackerNews.getTopStories()
topStories.foreach(println)
```

## License

The MIT License
