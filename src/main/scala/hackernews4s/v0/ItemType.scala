package hackernews4s.v0

object ItemType {
  def from(value: String): ItemType = {
    value match {
      case "story" => Story
      case "comment" => Comment
      case "job" => Job
      case "poll" => Poll
      case "pollout" => Pollopt
      case unknown => Unknown
    }
  }
}

sealed trait ItemType

case object Story extends ItemType
case object Job extends ItemType
case object Comment extends ItemType
case object Poll extends ItemType
case object Pollopt extends ItemType

case object Unknown extends ItemType