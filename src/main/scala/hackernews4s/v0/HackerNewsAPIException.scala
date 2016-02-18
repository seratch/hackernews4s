package hackernews4s.v0

case class HackerNewsAPIException(
  status: Int,
  message: String
) extends Exception
