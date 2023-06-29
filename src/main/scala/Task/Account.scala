package Task

import java.nio.file.attribute.UserPrincipalNotFoundException
import scala.util.{Success, Try, Failure}

class UserNotUsingSocialNetworkException(msg: String) extends Exception(msg)

case class Account(id: String, socialNetworks: Seq[SocialNetwork]) {
    def post(socialNetwork: SocialNetwork): Try[_] = {
        if (socialNetworks.contains(socialNetwork)) {
            println(s"пост в ${socialNetwork.name}")
            Success()
        } else {
            Failure(new UserPrincipalNotFoundException(s"user ${id} not using ${socialNetwork.name}"))
        }
    }
}