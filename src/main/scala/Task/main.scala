package Task

import java.time.Duration
import scala.util.Random

object main {
    val ZOOMER_CNT = 20
    val BOOMER_CNT = 30
    val OPPORTUNIST_CNT = 50
    val CACHE_TTL = Duration.ofMinutes(10).toMillis()

    val zoomerFactory = new ZoomerAccountFactory()
    val boomerFactory = new BoomerAccountFactory()
    val opportunistFactory = new OpportunistAccountFactory()

    val repository = new AccountRepositoryImpl()
    val cache = new AccountCacheImpl(repository, CACHE_TTL)

    def makeAccounts(cnt: Int, factory: AccountFactory) = (1 to cnt).map(_ => factory.make())

    def makeAllAccounts() = {
        val accountsConfig = Seq(
            (ZOOMER_CNT, zoomerFactory),
            (BOOMER_CNT, boomerFactory),
            (OPPORTUNIST_CNT, opportunistFactory)
        )
        val accounts = accountsConfig.foldLeft(List[Account]())((list, pair) => list ++ makeAccounts(pair._1, pair._2))
        accounts
    }


    def main(args: Array[String]): Unit = {
        val accounts = makeAllAccounts()
        repository.bulkLoad( accounts )

        val shuffledAccounts = Random.shuffle(accounts)
        val thirdPartAccounts = shuffledAccounts take accounts.length / 3

        for (account <- thirdPartAccounts) {
            cache.getById(account.id)
        }

        cache.postInstagram()
    }
}