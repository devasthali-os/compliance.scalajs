package com.pratyabhi.compliance.app

import org.scalajs.dom
import org.scalajs.dom.html.Div

object App:

  def start(): Unit =
    Option(dom.document.getElementById("app")).foreach { n =>
      Router.init(n.asInstanceOf[Div])
    }
