package in.hcl.trades.api.util

import cats.data._
import cats.implicits._
import in.hcl.trades.api.model.TradeRequests.Login
import in.hcl.trades.api.model.TradeResponses.InvalidField
import org.joda.time.format._
import org.joda.time.{DateTime, Days}

object TradeValidator {

  type ValidationResult[A] = ValidatedNec[InvalidField, A]

  def validateFloat(amount: Float, field: String, min: Float = 0): ValidationResult[Float] = {
    if (amount < min) InvalidField(s"Invalid value for $field, min value: $min").invalidNec
    else amount.validNec
  }

  def validateLong(value: Long, field: String, min: Long = 0): ValidationResult[Long] = {
    if (value < min) InvalidField(s"Invalid value for $field, min value: $min").invalidNec
    else value.validNec
  }

  def validateString(
      value: String,
      field: String,
      preDefinedList: List[String] = List.empty[String],
      minLength: Int = 0,
      length: Int = 0,
      isRange: Boolean = false
  ): ValidationResult[String] = {
    if (!preDefinedList.isEmpty) {
      if (!preDefinedList.contains(value.toLowerCase))
        InvalidField(s"Invalid value for $field").invalidNec
      else value.validNec
    } else if (isRange) {
      if ((minLength to length contains value.length) && !value.map(_.isDigit).contains(false))
        value.validNec
      else InvalidField(s"Invalid value for $field").invalidNec
    } else if (minLength > 0) {
      if (value.length >= minLength) value.validNec
      else InvalidField(s"Invalid value for $field").invalidNec
    } else if (length > 0) {
      if (value.length == length && !value.map(_.isDigit).contains(false)) value.validNec
      else InvalidField(s"Invalid value for $field").invalidNec
    } else {
      if (value.length > 0) value.validNec else InvalidField(s"Invalid value for $field").invalidNec
    }
  }

  def validateDate(
      date: String,
      field: String,
      delimiter: String,
      after: Int = 0
  ): ValidationResult[String] = {
    val dateArray: Array[String] = date.split(delimiter)
    val dateFormat               = "dd" + delimiter + "MM" + delimiter + "yyyy"
    dateArray.size match {
      case 3 =>
        try {
          val parsedDate = Some(DateTimeFormat forPattern dateFormat parseDateTime date)
          after match {
            case 0 => date.validNec
            case _ => {
              Days
                .daysBetween(new DateTime().toLocalDate, parsedDate.get.toLocalDate)
                .getDays() compare after match {
                case 0 | 1 => date.validNec
                case _ =>
                  InvalidField(
                    s"invalid date value, date should be $after days after current date"
                  ).invalidNec
              }
            }
          }
        } catch {
          case e: IllegalArgumentException =>
            InvalidField(s"invalid date format for $field use $dateFormat").invalidNec
        }
      case _ => InvalidField(s"invalid date input for $field").invalidNec
    }
  }

  def validateLoginPayload(
      login: Login
  ): ValidationResult[Login] = {
    (
      validateString(login.username, "username", List.empty, 4),
      validateString(login.password, "password", List.empty, 5)
    ).mapN(Login)
  }

}
