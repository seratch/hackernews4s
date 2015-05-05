# HackerNews API Client in Scala

This library is made for Scala hackers.

## HackerNews API?

- http://blog.ycombinator.com/hacker-news-api
- https://hacker-news.firebaseio.com/
- https://github.com/HackerNews/API

## How to use

```scala
libraryDependencies += "com.github.seratch" %% "hackernews4s" % "0.5.0"
```

## APIs

See the scaladocs: [hackernews4s.v0.HackerNews](https://oss.sonatype.org/service/local/repositories/releases/archive/com/github/seratch/hackernews4s_2.11/0.5.0/hackernews4s_2.11-0.5.0-javadoc.jar/!/index.html#hackernews4s.v0.HackerNews)

## Examples

```scala
import hackernews4s.v0._
val topStories: Seq[Item] = HackerNews.getTopStories()
topStories.foreach(println)
```

See [HackerNews.scala](https://github.com/seratch/hackernews4s/blob/master/src/main/scala/hackernews4s/v0/HackerNews.scala) for details:

You can try the code examples in comments. We confirm they're valid code by using [sbt-doctest](https://github.com/tkawachi/sbt-doctest).

## License

The MIT License

## Buid Status

[![Build Status](https://travis-ci.org/seratch/hackernews4s.svg?branch=master)](https://travis-ci.org/seratch/hackernews4s)

