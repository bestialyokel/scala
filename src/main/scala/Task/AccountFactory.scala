package Task

import java.util.UUID

abstract class AccountFactory {
    def make(): Account
}

class ZoomerAccountFactory extends AccountFactory {
    def make() = Account(UUID.randomUUID().toString, List(SocialNetwork.TikTok))
}

class BoomerAccountFactory extends AccountFactory {
    def make() = Account(UUID.randomUUID().toString, List(SocialNetwork.Instagram))
}

class OpportunistAccountFactory extends AccountFactory {
    def make() = Account(UUID.randomUUID().toString, List(SocialNetwork.TikTok, SocialNetwork.Instagram))
}

