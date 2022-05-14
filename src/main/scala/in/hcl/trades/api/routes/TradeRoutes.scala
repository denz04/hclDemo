package in.hcl.trades.api.routes

import akka.http.scaladsl.server.Directives.concat
import akka.http.scaladsl.server.Route
import com.typesafe.scalalogging.LazyLogging
import in.hcl.trades.api.endpoints.TradeEndpoints
import in.hcl.trades.api.model.TradeRequests._
import in.hcl.trades.api.model.TradeResponses._
import in.hcl.trades.api.services.TradeServices.verifyLogin
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TradeRoutes extends LazyLogging {

  def login(
      login: Login
  ): Future[Either[Seq[FailureResponse], UserLoginResponse]] = Future {
    logger.info(
      s"verifying credentials for user:: ${login.username}"
    )
    verifyLogin(login)
  }

  val getAllRoutes: Route =
    concat(
      AkkaHttpServerInterpreter().toRoute(TradeEndpoints.login)(login)
    )

  def apiDoc: String = OpenAPIDocsInterpreter()
    .toOpenAPI(TradeEndpoints.getAllRoutes, "HCL Trade Api Services", "1.0")
    .toYaml

}
