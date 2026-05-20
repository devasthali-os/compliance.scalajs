package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.app.Route
import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.MockData
import org.scalajs.dom.{Element, Node}
import org.scalajs.dom.html.{Div, Heading, Paragraph}

object DashboardView:

  def render(): (String, Node, Node) =
    val actions = Shell.topbarActions(
      Ui.btn("Last 24 hours ▾"),
      Ui.btn("Export summary", primary = true)
    )
    val content = el[Div]("div") { root =>
      val kpis = el[Div]("div") { g =>
        cls(g, "kpi-grid")
        MockData.kpis.foreach(k => append(g, Ui.kpiCard(k)))
      }
      val violations = Ui.panel(
        "Recent violations",
        Ui.table("Time", "Session", "App / User", "Model", "Severity", "Policy", "Status")(
          MockData.recentViolations.map { v =>
            Ui.tr(
              Ui.td(v.time),
              Ui.td(Ui.routeLink(Route.SessionDetail(v.sessionId), v.sessionLabel)),
              Ui.td(v.appUser),
              Ui.td(v.model),
              Ui.td(Ui.badgeSeverity(v.severity)),
              Ui.td(v.policy),
              Ui.td(Ui.badgeSessionStatus(v.status))
            )
          }*
        ),
        Some(Ui.panelHeaderLink(Route.Sessions, "View all sessions →"))
      )
      val policies = el[Element]("section") { section =>
        cls(section, "panel")
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h,
              el[Heading]("h3")(t => t.textContent = "Policies active"),
              Ui.panelHeaderLink(Route.Policies, "Manage policies →")
            )
          },
          el[Div]("div") { body =>
            style(body, "padding" -> "1rem 1.15rem")
            val p = el[Paragraph]("p") { par =>
              style(par, "margin" -> "0", "color" -> "var(--muted)", "font-size" -> "0.8125rem")
              par.innerHTML =
                "12 active · 2 draft · Last publish <strong>PII — No SSN</strong> v3 by arivera · May 18, 2026"
            }
            append(body, p)
          }
        )
      }
      append(root, kpis, violations, policies)
    }
    ("Dashboard", actions, content)
