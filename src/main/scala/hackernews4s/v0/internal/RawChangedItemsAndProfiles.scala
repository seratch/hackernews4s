package hackernews4s.v0.internal

import hackernews4s.v0._

private[hackernews4s] case class RawChangedItemsAndProfiles(
    items: Seq[Long],
    profiles: Seq[String]) {

  def toChangedItemsAndProfiles: ChangedItemsAndProfiles = new ChangedItemsAndProfiles(
    itemIds = items.map(ItemId),
    userIds = profiles.map(UserId)
  )

}
