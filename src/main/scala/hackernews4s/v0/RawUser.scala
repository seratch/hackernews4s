package hackernews4s.v0

case class RawUser(
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
    created = created,
    karma = karma,
    about = about,
    submitted = submitted.map(ItemId)
  )
}
