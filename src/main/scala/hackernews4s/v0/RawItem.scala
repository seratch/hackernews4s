package hackernews4s.v0

private[hackernews4s] case class RawItem(
    id: Long,
    deleted: Option[Boolean],
    `type`: String,
    by: String,
    time: Long,
    text: Option[String],
    dead: Option[Boolean],
    parent: Option[Long],
    kids: Seq[Long],
    url: Option[String],
    score: Option[Int],
    title: Option[String],
    parts: Seq[String]) {

  def toItem: Item = new Item(
    id = ItemId(id),
    deleted = deleted.getOrElse(false),
    itemType = ItemType.from(`type`),
    by = UserId(by),
    time = time,
    text = text.getOrElse(""),
    dead = dead.getOrElse(false),
    parent = parent.map(ItemId),
    commentIds = kids.map(ItemId),
    url = url,
    score = score.getOrElse(0),
    title = title,
    parts = parts
  )
}
