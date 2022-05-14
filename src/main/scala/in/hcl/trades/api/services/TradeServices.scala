package in.hcl.trades.api.services

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import cats.implicits.toFoldableOps
import com.typesafe.scalalogging.LazyLogging
import in.hcl.trades.api.database.TradeDAO._
import doobie.implicits._

import scala.language.postfixOps
import in.hcl.trades.api.model.TradeRequests._
import in.hcl.trades.api.model.TradeResponses._
import in.hcl.trades.api.util.PasswordValidator._
import in.hcl.trades.api.database.TradeDAO.{xa, _}
import in.hcl.trades.api.util.TradeValidator._

object TradeServices extends LazyLogging {
  implicit val system           = ActorSystem(Behaviors.empty, "HCLTrades")
  implicit val executionContext = system.executionContext

  def verifyLogin(login: Login): Either[Seq[FailureResponse], UserLoginResponse] = {
    val validationResult = validateLoginPayload(login)
    if (validationResult.isInvalid)
      return Left(validationResult.swap.map(_.toList).getOrElse(Seq[FailureResponse]()))
    val password = getUserPassword(login.username).transact(xa).unsafeRunSync()
    validatePassword(login, password)
  }

}
