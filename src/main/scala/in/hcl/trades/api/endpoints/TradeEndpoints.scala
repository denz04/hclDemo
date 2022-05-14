package in.hcl.trades.api.endpoints

import io.circe.generic.codec.DerivedAsObjectCodec.deriveCodec
import sttp.tapir._
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import in.hcl.trades.api.model.TradeRequests._
import in.hcl.trades.api.model.TradeResponses._

object TradeEndpoints {

  def getAllRoutes = {
    List(
      login
    )
  }

  private val tradesBaseEndpoint = endpoint.in("v1").in("app")

  def login: Endpoint[Login, Seq[
    FailureResponse
  ], UserLoginResponse, Any] =
    tradesBaseEndpoint.post
      .in("login")
      .in(jsonBody[Login])
      .out(jsonBody[UserLoginResponse])
      .errorOut(jsonBody[Seq[FailureResponse]])
      .description("This API is used to verify user credentials")

}
