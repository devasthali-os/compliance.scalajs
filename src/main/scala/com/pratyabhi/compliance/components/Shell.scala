package com.pratyabhi.compliance.components

import com.pratyabhi.compliance.app.Route
import com.pratyabhi.compliance.dom.DomBuilder.*
import org.scalajs.dom.{Element, HTMLElement, Node}
import org.scalajs.dom.html.{Div, Heading, LI, Paragraph, UList}

object Shell:

  def render(active: Route, title: String, actions: Node, content: Node): Element =
    el[Div]("div") { root =>
      cls(root, "layout")
      append(root, sidebar(active), mainArea(title, actions, content))
    }

  private def sidebar(active: Route): HTMLElement =
    val navActive = Route.activeNav(active)
    el[HTMLElement]("aside") { aside =>
      cls(aside, "sidebar")
      val brand = el[Div]("div") { b =>
        cls(b, "brand")
        append(b,
          el[Heading]("h1")(h => h.textContent = "Compliance"),
          el[Paragraph]("p")(p => p.textContent = "Pratyabhi Corp · Production")
        )
      }
      val nav = el[UList]("ul") { ul =>
        cls(ul, "nav")
        def item(section: Option[String], route: Route, label: String): Unit =
          section.foreach { s =>
            append(ul, el[LI]("li") { li =>
              cls(li, "nav-section")
              li.textContent = s
            })
          }
          append(ul, el[LI]("li") { li =>
            val a = Ui.routeLink(route, label)
            if route == navActive then cls(a, "active")
            append(li, a)
          })
        item(Some("Monitor"), Route.Dashboard, "Dashboard")
        item(None, Route.Sessions, "Sessions")
        item(None, Route.Review, "Review queue")
        item(Some("Govern"), Route.Policies, "Policies")
        item(None, Route.Reports, "Reports")
      }
      append(aside, brand, nav)
    }

  private def mainArea(title: String, actions: Node, content: Node): Div =
    el[Div]("div") { main =>
      cls(main, "main")
      val topbar = el[HTMLElement]("header") { h =>
        cls(h, "topbar")
        append(h, el[Heading]("h2")(t => t.textContent = title), actions)
      }
      val mainContent = el[HTMLElement]("main") { m =>
        cls(m, "content")
        append(m, content)
      }
      append(main, topbar, mainContent)
    }

  def topbarActions(nodes: Node*): Div =
    el[Div]("div") { d =>
      cls(d, "topbar-actions")
      append(d, nodes*)
    }
