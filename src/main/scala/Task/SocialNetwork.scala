package Task

sealed abstract class SocialNetwork(val name: String)
object SocialNetwork {
    final case object TikTok extends SocialNetwork(name = "TikTok")
    final case object Instagram extends SocialNetwork(name = "Instagram")
}

