package hackernews4s.v0.internal

import hackernews4s.v0._
import org.joda.time.DateTime

private[hackernews4s] case class RawUser(
    id: String,
    delay: Int,
    created: Long,
    karma: Int,
    about: Option[String],
    submitted: Seq[Long]
) {

  def toUser: User = new User(
    id = UserId(id),
    delay = delay,
    createdAt = new DateTime(created * 1000),
    karma = karma,
    about = about,
    submitted = submitted.map(ItemId)
  )

}
