package in.hcl.trades.api.model

object TradeRequests {

  case class Login(username: String, password: String)
  case class AddBalance(amount: Long)
  case class Order(stockId: String, quantity: Long, orderType: String)

}
