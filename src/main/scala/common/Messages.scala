package common

case class FindRequest(name: String)
case class OrderRequest(name: String)
case class ReadRequest(name: String)

case class FindResponse(name: String, price: Option[Integer])
case class OrderResponse(result: String)