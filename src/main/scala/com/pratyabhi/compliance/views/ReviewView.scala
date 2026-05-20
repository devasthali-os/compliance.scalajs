package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.app.Route
import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.MockData
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Div, Heading, Span}

object ReviewView:

  def render(): (String, Node, Node) =
    val actions = Shell.topbarActions(
      el[Span]("span") { s =>
        style(s, "color" -> "var(--muted)", "font-size" -> "0.8125rem")
        s.textContent = "14 pending · 6 SLA < 4h"
      }
    )
    val content = el[Div]("div") { root =>
      val queue = Ui.panel(
        "Review queue",
        Ui.table("Queued", "Session", "Severity", "Policy", "Assignee", "SLA", "")(
          MockData.reviewQueue.map { r =>
            val slaCell = el[Span]("span") { cell =>
              cell.textContent = r.sla
              if r.slaUrgent then style(cell, "color" -> "var(--danger)", "font-weight" -> "600")
            }
            val triage = Ui.routeLink(
              Route.SessionDetail(r.sessionId),
              if r.assignee == "Unassigned" then "Triage" else "Continue"
            )
            cls(triage, if r.assignee == "Unassigned" then "btn btn-primary" else "btn")
            style(triage, "padding" -> "0.3rem 0.6rem", "text-decoration" -> "none")
            Ui.tr(
              Ui.td(r.queued),
              Ui.td(Ui.routeLink(Route.SessionDetail(r.sessionId), r.sessionId)),
              Ui.td(Ui.badgeSeverity(r.severity)),
              Ui.td(r.policy),
              Ui.td(r.assignee),
              Ui.td(slaCell),
              Ui.td(triage)
            )
          }*
        )
      )
      val recent = el[Div]("section") { section =>
        cls(section, "panel")
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h, el[Heading]("h3")(t => t.textContent = "Recently completed"))
          },
          el[Div]("div") { b =>
            cls(b, "panel-body")
            style(b, "padding" -> "1rem", "color" -> "var(--muted)", "font-size" -> "0.8125rem")
            b.textContent =
              "sess_4c21a88 — Approved by arivera · \"False positive; test data\" · May 19, 09:10"
          }
        )
      }
      append(root, queue, recent)
    }
    ("Review queue", actions, content)
