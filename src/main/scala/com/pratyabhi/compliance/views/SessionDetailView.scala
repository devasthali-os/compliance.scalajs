package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.app.Route
import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.MockData
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Div, Element, Heading, Paragraph, Span, TextArea, UList}

object SessionDetailView:

  def render(id: String): (String, Node, Node) =
    val s = MockData.sessionDetail(id)
    val actions = Shell.topbarActions(
      Ui.badgeSeverity(s.severity),
      Ui.btn("Assign reviewer"),
      Ui.btn("Open in review", primary = true)
    )
    val content = el[Div]("div") { root =>
      cls(root, "split-3")
      val meta = el[Element]("section") { section =>
        cls(section, "panel")
        val list = el[UList]("ul") { listEl =>
          cls(listEl, "meta-list")
          style(listEl, "padding" -> "0 1rem")
          append(listEl,
            Ui.metaItem("Started", text(s.started)),
            Ui.metaItem("Actor", text(s.actor)),
            Ui.metaItem("Application", text(s.application)),
            Ui.metaItem("Model", text(s.model)),
            Ui.metaItem("Risk score", text(s.risk)),
            Ui.metaItem("Status", Ui.badgeSessionStatus(s.status)),
            Ui.metaItem("Retention", text(s.retention))
          )
        }
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h, el[Heading]("h3")(t => t.textContent = "Metadata"))
          },
          list
        )
      }
      val thread = el[Element]("section") { section =>
        cls(section, "panel")
        val box = el[Div]("div") { boxEl =>
          cls(boxEl, "thread")
          s.messages.foreach { m =>
            append(boxEl, el[Element]("article") { art =>
              cls(art, s"message ${m.turn}")
              val p = el[Paragraph]("p") { pEl =>
                m.highlight match
                  case Some(h) =>
                    val parts = m.body.split(h, 2)
                    if parts.length == 2 then
                      append(pEl, text(parts(0)),
                        el[Span]("span") { sp =>
                          cls(sp, "highlight")
                          sp.textContent = h
                        },
                        text(parts(1)))
                    else pEl.textContent = m.body
                  case None => pEl.textContent = m.body
              }
              append(art, el[Div]("div") { r =>
                cls(r, "role")
                r.textContent = m.role
              }, p)
            })
          }
        }
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h, el[Heading]("h3")(t => t.textContent = "Conversation"))
          },
          box
        )
      }
      val rules = el[Element]("section") { section =>
        cls(section, "panel")
        append(section, el[Div]("div") { h =>
          cls(h, "panel-header")
          append(h, el[Heading]("h3")(t => t.textContent = "Matched policies"))
        })
        s.rules.foreach { r =>
          append(section, el[Div]("div") { card =>
            cls(card, "rule-card")
            val strong = el[Element]("strong") { st =>
              style(st, "display" -> "block", "margin" -> "0.35rem 0")
              st.textContent = r.name
            }
            val detail = el[Paragraph]("p") { p =>
              style(p, "margin" -> "0", "color" -> "var(--muted)")
              p.textContent = r.detail
            }
            append(card, Ui.badgeSeverity(r.severity), strong, detail,
              el[Paragraph]("p") { p =>
                style(p, "margin" -> "0.5rem 0 0")
                append(p, Ui.routeLink(Route.PolicyEditor("pii-ssn"), "View rule →"))
              }
            )
          })
        }
        append(section, el[Div]("div") { footer =>
          style(footer, "padding" -> "1rem", "border-top" -> "1px solid var(--border)")
          val ta = el[TextArea]("textarea") { taEl =>
            taEl.rows = 3
            taEl.placeholder = "Document decision…"
            style(taEl, "width" -> "100%", "padding" -> "0.5rem", "border" -> "1px solid var(--border)",
              "border-radius" -> "var(--radius)", "font-family" -> "inherit")
          }
          append(footer,
            el[Element]("label") { lb =>
              style(lb, "display" -> "block", "font-weight" -> "600", "margin-bottom" -> "0.35rem")
              lb.textContent = "Reviewer comment (required)"
            },
            ta,
            el[Div]("div") { btns =>
              style(btns, "display" -> "flex", "gap" -> "0.5rem", "margin-top" -> "0.75rem")
              append(btns, Ui.btn("Approve", primary = true), Ui.btn("Reject", danger = true), Ui.btn("Escalate"))
            }
          )
        })
      }
      append(root, meta, thread, rules)
    }
    (s.id, actions, content)
