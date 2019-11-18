package sttp.tapir.server

import sttp.model.StatusCode

/**
  * Create a decode failure handler, which:
  * - decides whether the given decode failure should lead to a response (and if so, with which status code),
  *   or return a [[DecodeFailureHandling.noMatch]], using `respondWithStatusCode`
  * - creates decode failure messages using `failureMessage`
  * - creates the response using `response`
  */
case class DefaultDecodeFailureHandler[R](
    respondWithStatusCode: DecodeFailureContext[R] => Option[StatusCode],
    response: (StatusCode, String) => DecodeFailureHandling,
    failureMessage: DecodeFailureContext[R] => String
) extends DecodeFailureHandler[R] {
  def apply(ctx: DecodeFailureContext[R]): DecodeFailureHandling = {
    respondWithStatusCode(ctx) match {
      case Some(c) =>
        val failureMsg = failureMessage(ctx)
        response(c, failureMsg)
      case None => DecodeFailureHandling.noMatch
    }
  }
}
