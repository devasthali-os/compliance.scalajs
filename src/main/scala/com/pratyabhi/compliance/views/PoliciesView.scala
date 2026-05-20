package com.pratyabhi.compliance.views

import com.pratyabhi.compliance.app.Route
import com.pratyabhi.compliance.components.{Shell, Ui}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.MockData
import org.scalajs.dom.Node
import org.scalajs.dom.html.Div

object PoliciesView:

  def render(): (String, Node, Node) =
    val actions = Shell.topbarActions(Ui.btn("New policy", primary = true))
    val content = el[Div]("div") { root =>
      append(root, Ui.panel(
        "",
        Ui.table("Name", "Version", "Status", "Rules", "Last updated", "")(
          MockData.policies.map { p =>
            Ui.tr(
              Ui.td(if p.editable then Ui.routeLink(Route.PolicyEditor(p.id), p.name) else p.name),
              Ui.td(p.version),
              Ui.td(Ui.badgePolicyStatus(p.status)),
              Ui.td(p.rules.toString),
              Ui.td(p.updated),
              Ui.td(if p.editable then Ui.routeLink(Route.PolicyEditor(p.id), "Edit") else "View history")
            )
          }*
        )
      ))
    }
    ("Policies", actions, content)
