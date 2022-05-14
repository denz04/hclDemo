package in.hcl.trades.api.model

object TradeResponses {

  sealed trait FailureResponse
  case class Error(errorCode: Int, errorMessage: String) extends FailureResponse
  case class InvalidField(msg: String)                   extends FailureResponse

  sealed trait UserLoginResponse
  case class LoginFailure(error: Int, message: String) extends UserLoginResponse
  case class LoginSuccess(accessToken: String)         extends UserLoginResponse

  case class CashBalance(balance: Long)

  case class Order(
      id: Long,
      stockId: String,
      quantity: Long,
      orderType: String,
      createdAt: String,
      updatedAt: String
  )
  case class Trade(
      id: Long,
      orderId: Long,
      isSuccess: Boolean,
      createdAt: String,
      updatedAt: String
  )

}
