package com.duwamish.compliance

import org.scalajs.dom
import scala.scalajs.js

object Compliance {

  def main(args: Array[String]): Unit =
    dom.document.addEventListener(
      "DOMContentLoaded",
      (_: dom.Event) => {
        js.Dynamic.global.console.log("welcome to Compliance v1")
        Option(dom.document.getElementById("headerText")).foreach { el =>
          el.textContent = "welcome to compliance"
        }
      }
    )

}
