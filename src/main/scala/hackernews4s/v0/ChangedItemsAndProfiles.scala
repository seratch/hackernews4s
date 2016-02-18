package hackernews4s.v0

case class ChangedItemsAndProfiles(
  itemIds: Seq[ItemId],
  userIds: Seq[UserId]
)
