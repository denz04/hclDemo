package in.hcl.trades

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import in.hcl.trades.api.routes.TradeRoutes
import in.hcl.trades.swagger.Swagger

import scala.util.{Failure, Success}

object TradesMainActor {

  case class Server(host: String, port: Int, routes: Route)

  def startServer(server: Server, swaggerDoc: String)(implicit system: ActorSystem[_]) = {
    import akka.http.scaladsl.server.Directives._
    import system.executionContext
    val routes        = concat(server.routes, new Swagger(swaggerDoc).routes)
    val serverStartup = Http().newServerAt(server.host, server.port).bindFlow(routes)
    serverStartup.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {

    val rootBehavior = Behaviors.setup[Nothing] { context =>
      val tradeRoutes = new TradeRoutes()

      val tradeServer = Server("0.0.0.0", 6066, tradeRoutes.getAllRoutes)

      startServer(tradeServer, tradeRoutes.apiDoc)(context.system)

      Behaviors.same
    }
    ActorSystem[Nothing](rootBehavior, "Mamun_EDD_Apis")
  }

}
