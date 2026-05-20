package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.MockData
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Anchor, Div, Heading, Input, Label, Option as HtmlOption, Paragraph, Select, Span}

object ReportsView:

  def render(): (String, Node, Node) =
    val actions = Shell.topbarActions(Ui.btn("New export", primary = true))
    val content = el[Div]("div") { root =>
      val exportPanel = el[Div]("section") { section =>
        cls(section, "panel")
        def dateInput(value: String): Input =
          el[Input]("input") { i =>
            i.`type` = "date"
            i.value = value
          }
        val grid = el[Div]("div") { gridEl =>
          cls(gridEl, "form-grid")
          append(gridEl,
            formRow("Date range", el[Div]("div") { d =>
              style(d, "display" -> "flex", "gap" -> "0.5rem")
              append(d, dateInput("2026-05-01"), dateInput("2026-05-19"))
            }),
            formRow("Format", el[Select]("select") { s =>
              List("CSV", "JSON").foreach(f => append(s, el[HtmlOption]("option")(o => o.textContent = f)))
            }),
            formRow("Include", el[Div]("div") { d =>
              List(("Sessions", true), ("Violations", true), ("Redacted prompts", false)).foreach {
                case (label, on) =>
                  append(d, el[Label]("label") { lb =>
                    val cb = el[Input]("input") { input =>
                      input.`type` = "checkbox"
                      input.checked = on
                    }
                    append(lb, cb, text(s" $label"))
                  })
              }
            }),
            formRow("", Ui.btn("Generate export", primary = true))
          )
        }
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h, el[Heading]("h3")(t => t.textContent = "Export audit log"))
          },
          grid
        )
      }
      val history = Ui.panel(
        "Recent exports",
        Ui.table("Requested", "Range", "Format", "Status", "")(
          MockData.exports.map { e =>
            val status = el[Span]("span") { cell =>
              cls(cell, if e.ready then "badge badge-clean" else "badge badge-low")
              cell.textContent = e.status
            }
            Ui.tr(
              Ui.td(e.requested),
              Ui.td(e.range),
              Ui.td(e.format),
              Ui.td(status),
              Ui.td(
                if e.ready then
                  el[Anchor]("a") { a =>
                    a.textContent = "Download"
                    a.href = "#"
                  }
                else "—"
              )
            )
          }*
        )
      )
      val schedule = el[Div]("section") { section =>
        cls(section, "panel")
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h,
              el[Heading]("h3")(t => t.textContent = "Scheduled summaries"),
              el[Span]("span") { s =>
                style(s, "font-size" -> "0.75rem", "color" -> "var(--muted)")
                s.textContent = "Phase 5 — backend"
              }
            )
          },
          el[Div]("div") { empty =>
            cls(empty, "empty-state")
            append(empty,
              el[Paragraph]("p")(p => p.textContent = "No schedules configured."),
              Ui.btn("Add weekly summary")
            )
          }
        )
      }
      append(root, exportPanel, history, schedule)
    }
    ("Reports & exports", actions, content)

  private def formRow(label: String, field: Node): Div =
    el[Div]("div") { row =>
      cls(row, "form-row")
      append(row, el[Label]("label")(l => l.textContent = label), field)
    }
