package com.pratyabhi.compliance

import com.pratyabhi.compliance.app.App
import org.scalajs.dom

object Compliance:

  def main(args: Array[String]): Unit =
    dom.document.addEventListener("DOMContentLoaded", (_: dom.Event) => App.start())
