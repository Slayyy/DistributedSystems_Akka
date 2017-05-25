package common

case class SearchRequest(name: String)
case class OrderRequest(name: String)
case class ReadRequest(name: String)

case class SearchResponse(book: Array[String])
case class OrderResponse(book: Boolean)