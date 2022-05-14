package in.hcl.trades.api.util

import com.typesafe.config.{Config, ConfigFactory}

object ConfigLoader {

  val config: Config = ConfigFactory.load("trades.conf")

  def getConfig(confName: String): Config = {
    config.getConfig(confName)
  }

}
