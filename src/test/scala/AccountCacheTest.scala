import Task.{AccountCacheImpl, AccountRepositoryImpl, ZoomerAccountFactory}
import org.scalatest._
import flatspec._
import matchers._

import java.time.Duration


class AccountCacheTest extends AnyFlatSpec with should.Matchers {
    "Account cache" should "getById" in {
        val repository = new AccountRepositoryImpl()

        val account = new ZoomerAccountFactory().make()

        repository.put(account)

        val ttl = Duration.ofMillis(100).toMillis()

        val cache = new AccountCacheImpl(repository, ttl)
    }
}