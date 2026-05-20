package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.app.Route
import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.MockData
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Button, Div, Input, Select, Span}

object SessionsView:

  def render(): (String, Node, Node) =
    val actions = Shell.topbarActions(Ui.btn("Export CSV", primary = true))
    val content = el[Div]("div") { root =>
      val panel = el[Div]("div") { panelEl =>
        cls(panelEl, "panel")
        val filters = el[Div]("div") { f =>
          cls(f, "filters")
          def inp(tpe: String, placeholder: String = ""): Input =
            el[Input]("input") { i =>
              setAttr(i, "type", tpe)
              if placeholder.nonEmpty then setAttr(i, "placeholder", placeholder)
            }
          def sel(opts: String*): Select =
            el[Select]("select") { s =>
              opts.foreach(o => append(s, el[org.scalajs.dom.html.Option]("option")(op => op.textContent = o)))
            }
          append(f,
            inp("search", "Search prompts, users, session ID…"),
            inp("date"),
            inp("date"),
            sel("All severities", "Critical", "High"),
            sel("All policies", "PII — No SSN", "Geo — EU"),
            sel("All models", "gpt-4o", "claude-3-5"),
            sel("All statuses", "Flagged", "Under review"),
            Ui.btn("Apply")
          )
          style(f.firstChild.asInstanceOf[Input], "min-width" -> "220px")
        }
        val tbody = MockData.sessions.map { s =>
          Ui.tr(
            Ui.td(s.started),
            Ui.td(Ui.routeLink(Route.SessionDetail(s.id), s.id)),
            Ui.td(s.userApp),
            Ui.td(s.model),
            Ui.td(s.risk),
            Ui.td(s.violationLabel match
              case Some((sev, lbl)) =>
                val b = Ui.badgeSeverity(sev)
                b.textContent = lbl
                b
              case None => "—"
            ),
            Ui.td(Ui.badgeSessionStatus(s.status))
          )
        }
        val tableWrap = el[Div]("div") { b =>
          cls(b, "panel-body")
          append(b, Ui.table("Started", "Session ID", "User / App", "Model", "Risk", "Violations", "Status")(tbody*))
          append(b, el[Div]("div") { p =>
            cls(p, "pagination")
            append(p,
              el[Span]("span")(s => s.textContent = "Showing 1–25 of 1,284 sessions"),
              el[Span]("span") { s =>
                val prev = Ui.btn("Previous")
                prev.asInstanceOf[Button].disabled = true
                append(s, prev, Ui.btn("Next"))
              }
            )
          })
        }
        append(panelEl, filters, tableWrap)
      }
      append(root, panel)
    }
    ("Sessions", actions, content)
