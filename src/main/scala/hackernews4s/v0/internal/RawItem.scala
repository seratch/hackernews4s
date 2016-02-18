package hackernews4s.v0.internal

import java.util.Date

import hackernews4s.v0.{ Item, ItemId, ItemType, UserId }
import org.joda.time.DateTime

private[hackernews4s] case class RawItem(
    id: Long,
    deleted: Option[Boolean],
    `type`: String,
    by: Option[String],
    time: Long,
    text: Option[String],
    dead: Option[Boolean],
    parent: Option[Long],
    kids: Seq[Long],
    url: Option[String],
    score: Option[Int],
    title: Option[String],
    parts: Seq[String]
) {

  def toItem: Item = new Item(
    id = ItemId(id),
    deleted = deleted.getOrElse(false),
    itemType = ItemType.from(`type`),
    by = by.map(UserId),
    createdAt = new DateTime(time * 1000),
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
