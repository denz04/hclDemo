package in.hcl.trades.api.util

import java.util.Base64
import java.nio.charset.StandardCharsets
import in.hcl.trades.api.model.TradeRequests.Login
import in.hcl.trades.api.model.TradeResponses._

object PasswordValidator {

  def validatePassword(
      login: Login,
      password: Option[String]
  ): Either[Seq[FailureResponse], UserLoginResponse] = {
    password match {
      case Some(value) =>
        val decoded = Base64.getDecoder().decode(value)
        login.password.equals(new String(decoded, StandardCharsets.UTF_8)) match {
          case true =>
            Right(
              LoginSuccess(
                Base64.getEncoder().encodeToString(login.username.getBytes(StandardCharsets.UTF_8))
              )
            )
          case false => Right(LoginFailure(1, "Invalid username/password."))
        }
      case None => Left(List(Error(2, "Credential verification failed.")))
    }

  }
}
