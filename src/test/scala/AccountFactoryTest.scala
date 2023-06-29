import Task.{BoomerAccountFactory, OpportunistAccountFactory, ZoomerAccountFactory}

import org.scalatest._
import flatspec._
import matchers._

class AccountFactoryTest extends AnyFlatSpec with should.Matchers {
    "Zoomer factory" should "create account using TikTok" in {
        val factory = new ZoomerAccountFactory()
        val account = factory.make()
        account.socialNetworks should contain only (Task.SocialNetwork.TikTok)
    }

    "Boomer factory" should "create account using TikTok" in {
        val factory = new BoomerAccountFactory()
        val account = factory.make()
        account.socialNetworks should contain only (Task.SocialNetwork.Instagram)
    }

    "Opportunist factory" should "create account using TikTok" in {
        val factory = new OpportunistAccountFactory()
        val account = factory.make()
        account.socialNetworks should contain only (Task.SocialNetwork.TikTok, Task.SocialNetwork.Instagram)
    }
}