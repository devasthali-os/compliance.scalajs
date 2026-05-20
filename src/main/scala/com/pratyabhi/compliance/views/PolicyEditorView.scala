package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.{MockData, PolicyStatus}
import org.scalajs.dom.Node
import org.scalajs.dom.html.{Div, Element, Heading, Input, Label, Span, TextArea}

object PolicyEditorView:

  def render(id: String): (String, Node, Node) =
    val policy = MockData.policy(id).getOrElse(MockData.policies.head)
    val statusBadge = policy.status match
      case PolicyStatus.Active  => Ui.badgePolicyStatus(PolicyStatus.Active)
      case PolicyStatus.Draft   => Ui.badgePolicyStatus(PolicyStatus.Draft)
      case PolicyStatus.Retired => Ui.badgePolicyStatus(PolicyStatus.Retired)
    statusBadge.textContent = s"${statusBadge.textContent} · ${policy.version}"
    val actions = Shell.topbarActions(
      statusBadge,
      Ui.btn("Save draft"),
      Ui.btn("Publish", primary = true)
    )
    val content = el[Div]("div") { root =>
      style(root, "display" -> "grid", "grid-template-columns" -> "1fr 340px", "gap" -> "1rem")
      val rulesPanel = el[Div]("section") { section =>
        cls(section, "panel")
        val form = el[Div]("div") { formEl =>
          cls(formEl, "form-grid")
          append(formEl,
            formRow("Policy name", el[Input]("input") { i =>
              i.`type` = "text"
              i.value = policy.name
            }),
            formRow("Description", el[TextArea]("textarea") { t =>
              t.rows = 2
              t.value = "Block Social Security numbers in user prompts and model outputs."
            })
          )
        }
        val toggles = el[Div]("div") { togglesEl =>
          style(togglesEl, "padding" -> "0 1rem 1rem")
          List(
          ("PII detection", "Scan for government IDs, SSN patterns", true),
          ("Blocked topics", "Medical, legal, financial advice", false),
          ("Retention limit", "Auto-expire sessions with PII", true),
          ("Geo restriction", "EU-only inference region", false),
          ("Custom regex", """\d{3}-\d{2}-\d{4}""", true)
          ).foreach { case (title, sub, checked) =>
            append(togglesEl, toggleRow(title, sub, checked))
          }
        }
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h, el[Heading]("h3")(t => t.textContent = "Rules"))
          },
          form,
          toggles
        )
      }
      val simPanel = el[Div]("section") { section =>
        cls(section, "panel")
        val body = el[Div]("div") { bodyEl =>
          style(bodyEl, "padding" -> "1rem")
          val ta = el[TextArea]("textarea") { taEl =>
            taEl.rows = 5
            taEl.value = "Customer SSN is 123-45-6789 for verification."
            style(taEl, "width" -> "100%", "margin" -> "0.5rem 0", "padding" -> "0.5rem",
              "border" -> "1px solid var(--border)", "border-radius" -> "var(--radius)", "font-family" -> "inherit")
          }
          val runBtn = Ui.btn("Run simulation", primary = true)
          style(runBtn, "width" -> "100%")
          val result = el[Div]("div") { res =>
            cls(res, "preview-result fail")
            res.textContent = "FAIL — Rule Custom regex matched at position 16"
          }
          val preview = el[Div]("div") { prev =>
            cls(prev, "preview-box")
            style(prev, "margin-top" -> "1rem")
            prev.innerHTML =
              """Customer SSN is <span style="background:#fbbf24;color:#000;">123-45-6789</span> for verification."""
          }
          append(bodyEl,
            el[Element]("label") { l =>
              style(l, "font-weight" -> "600", "font-size" -> "0.8125rem")
              l.textContent = "Sample input"
            },
            ta,
            runBtn,
            result,
            preview
          )
        }
        append(section,
          el[Div]("div") { h =>
            cls(h, "panel-header")
            append(h, el[Heading]("h3")(t => t.textContent = "Simulate"))
          },
          body
        )
      }
      append(root, rulesPanel, simPanel)
    }
    (policy.name, actions, content)

  private def formRow(label: String, field: Node): Div =
    el[Div]("div") { row =>
      cls(row, "form-row")
      append(row, el[Label]("label")(l => l.textContent = label), field)
    }

  private def toggleRow(title: String, subtitle: String, checked: Boolean): Div =
    el[Div]("div") { row =>
      cls(row, "toggle-row")
      val info = el[Span]("span") { sp =>
        sp.innerHTML = s"<strong>$title</strong><br><small style=\"color:var(--muted)\">$subtitle</small>"
      }
      val cb = el[Input]("input") { input =>
        input.`type` = "checkbox"
        input.checked = checked
      }
      append(row, info, cb)
    }
