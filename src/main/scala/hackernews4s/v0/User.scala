package hackernews4s.v0

import org.joda.time.DateTime

case class User(
  id: UserId,
  about: Option[String],
  delay: Long,
  createdAt: DateTime,
  karma: Long,
  submitted: Seq[ItemId])
