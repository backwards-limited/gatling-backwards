package com.backwards.gatling

import io.gatling.app.Gatling
import io.gatling.core.config.GatlingPropertiesBuilder

object GatlingRunner extends App {
  val simulationClass = classOf[Simulation2]

  val props = new GatlingPropertiesBuilder
  props simulationClass simulationClass.getName

  Gatling fromMap props.build
}