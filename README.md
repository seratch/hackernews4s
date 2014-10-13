# HackerNews API Client in Scala

## HackerNews API?

- http://blog.ycombinator.com/hacker-news-api
- https://hacker-news.firebaseio.com/
- https://github.com/HackerNews/API

## How to use

```scala
libraryDependencies += "com.github.seratch" %% "hackernews4s" % "0.1"
```

## APIs

See the scaladocs:

[hackernews4s.v0.HackerNews.scala](https://oss.sonatype.org/service/local/repositories/releases/archive/com/github/seratch/hackernews4s_2.11/0.1/hackernews4s_2.11-0.1-javadoc.jar/!/index.html#hackernews4s.v0.HackerNews$)

## Examples

```scala
import hackernews4s.v0._
val topStories: Seq[Item] = HackerNews.getTopStories()
topStories.foreach(println)
```

## License

The MIT License

## Buid Status

[![Build Status](https://travis-ci.org/seratch/hackernews4s.svg?branch=master)](https://travis-ci.org/seratch/hackernews4s)
