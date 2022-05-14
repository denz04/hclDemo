package in.hcl.trades.api.database

import cats.effect.IO
import com.typesafe.scalalogging.LazyLogging
import doobie._
import doobie.implicits._
import doobie.postgres.circe.jsonb.implicits.{pgDecoderGet, pgEncoderPut}
import in.hcl.trades.api.util.ConfigLoader
import io.circe._

import java.sql.Timestamp
import java.time.LocalDateTime
import scala.concurrent.ExecutionContext

object TradeDAO extends LazyLogging {

  implicit val cs               = IO.contextShift(ExecutionContext.global)
  implicit val meta: Meta[Json] = new Meta(pgDecoderGet, pgEncoderPut)

  val jdbc = ConfigLoader.getConfig("jdbc")

  val xa = Transactor.fromDriverManager[IO](
    jdbc.getString("driver"),
    jdbc.getString("url"),
    jdbc.getString("username"),
    jdbc.getString("password")
  )
  def getUserPassword(
      username: String
  ): ConnectionIO[Option[String]] = {
    sql"SELECT password from user_info where username = $username"
      .query[String]
      .option
  }
}
