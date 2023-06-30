package Task

import scala.collection.mutable

abstract class AccountCache(val cacheEntryTTL: Long) {
    case class CacheEntry(account: Account, lastUpdateTs: Long)

    val repository: AccountRepository

    def getById(id: String): Option[Account]

    def expiredById(id: String): Boolean

    def postInstagram(): Unit

    protected def entryOverdue(entry: CacheEntry): Boolean

    protected def updateEntry(entry: CacheEntry): Option[CacheEntry]

    protected def put(account: Account): Option[CacheEntry]
}

class AccountCacheImpl(val repository: AccountRepository, override val cacheEntryTTL: Long) extends AccountCache(cacheEntryTTL) {
    val hashMap = new mutable.HashMap[String, CacheEntry]()

    override def getById(id: String): Option[Account] = {
        hashMap.get(id) match {
            case Some(entry) => if (entryOverdue(entry)) updateEntry(entry).map(_.account) else Some(entry.account)
            case None => repository.getById(id) match {
                case Some(account) => put(account).map(_.account)
            }
        }
    }

    override def expiredById(id: String): Boolean = hashMap.get(id) match {
        case Some(entry) => entryOverdue(entry)
        case None => false
    }

    def getCurrentTs() = System.currentTimeMillis()

    // Return Some if account with same already exist(new account not added)
    // None if account successfuly added
    override def put(account: Account) = {
        hashMap.get(account.id) match {
            case Some(entry) => Some(entry)
            case None => hashMap.put(account.id, CacheEntry(account, getCurrentTs()))
        }
    }

    override def entryOverdue(entry: CacheEntry): Boolean = getCurrentTs() - entry.lastUpdateTs > cacheEntryTTL

    override def postInstagram(): Unit = {
        hashMap.values
          .filter(entry => entry.account.socialNetworks.contains(SocialNetwork.Instagram))
          .map(entry => if (entryOverdue(entry)) updateEntry(entry) else Some(entry))
          .filter(_.nonEmpty)
          .map(_.get)
          .foreach(entry => entry.account.post(SocialNetwork.Instagram))
    }

    override def updateEntry(cacheEntry: CacheEntry): Option[CacheEntry] = repository.getById(cacheEntry.account.id) match {
        case Some(account) => {
            val entry = CacheEntry(account, getCurrentTs())
            hashMap.put(account.id, entry)
            Some(entry)
        }
        case None => None
    }
}