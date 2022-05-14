package in.hcl.trades.swagger

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server.Directives.{
  complete,
  concat,
  getFromResourceDirectory,
  path,
  pathEndOrSingleSlash,
  pathPrefix,
  redirect
}
import akka.http.scaladsl.server.{PathMatcher, Route}
import sttp.tapir.swagger.akkahttp.SwaggerAkka

import java.util.Properties

class Swagger(yaml: String, contextPath: String = "docs", yamlName: String = "docs.yaml")
    extends SwaggerAkka(yaml: String) {
  require(!contextPath.startsWith("/") && !contextPath.endsWith("/"))

  val redirectToIndex: Route =
    redirect(s"/$contextPath/index.html?url=/$contextPath/$yamlName", StatusCodes.PermanentRedirect)

  // needed only if you use oauth2 authorization
  def redirectToOath2(query: String): Route =
    redirect(s"/$contextPath/oauth2-redirect.html$query", StatusCodes.PermanentRedirect)

  val swaggerVersion = {
    val p = new Properties()
    val pomProperties =
      getClass.getResourceAsStream("/META-INF/maven/org.webjars/swagger-ui/pom.properties")
    try p.load(pomProperties)
    finally pomProperties.close()
    p.getProperty("version")
  }

  val contextPathMatcher = {
    def toPathMatcher(segment: String) = PathMatcher(segment :: Path.Empty, ())
    contextPath.split('/').map(toPathMatcher).reduceLeft(_ / _)
  }

  override val routes: Route =
    concat(
      pathPrefix(contextPathMatcher) {
        concat(
          pathEndOrSingleSlash { redirectToIndex },
          path(yamlName) { complete(yaml) },
          getFromResourceDirectory("swagger-ui")
        )
      },
      // needed only if you use oauth2 authorization
      path("oauth2-redirect.html") { request =>
        redirectToOath2(request.request.uri.rawQueryString.map(s => "?" + s).getOrElse(""))(request)
      }
    )
}
