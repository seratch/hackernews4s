package hackernews4s.v0

import org.joda.time.DateTime

case class Item(
  id: ItemId,
  deleted: Boolean,
  itemType: ItemType,
  by: Option[UserId],
  createdAt: DateTime,
  text: String,
  dead: Boolean,
  parent: Option[ItemId],
  commentIds: Seq[ItemId],
  url: Option[String],
  score: Int,
  title: Option[String],
  parts: Seq[String]
)