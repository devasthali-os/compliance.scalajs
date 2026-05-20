package com.pratyabhi.compliance.app

import com.pratyabhi.compliance.components.Shell
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.views.*
import org.scalajs.dom
import org.scalajs.dom.html.Div

object Router:

  private var mount: Div = dom.document.createElement("div").asInstanceOf[Div]

  def init(appRoot: Div): Unit =
    mount = appRoot
    dom.window.addEventListener("hashchange", (_: dom.Event) => render(current()))
    val route = current()
    if dom.window.location.hash.isEmpty then
      dom.window.location.hash = Route.toHash(Route.Dashboard).stripPrefix("#")
      render(Route.Dashboard)
    else render(route)

  def navigate(route: Route): Unit =
    dom.window.location.hash = Route.toHash(route).stripPrefix("#")
    render(route)

  def current(): Route =
    Route.parse(dom.window.location.hash)

  private def render(route: Route): Unit =
    clear(mount)
    val (title, actions, content) = route match
      case Route.Dashboard              => DashboardView.render()
      case Route.Sessions               => SessionsView.render()
      case Route.SessionDetail(id)      => SessionDetailView.render(id)
      case Route.Policies               => PoliciesView.render()
      case Route.PolicyEditor(id)       => PolicyEditorView.render(id)
      case Route.Review                 => ReviewView.render()
      case Route.Reports                => ReportsView.render()
    append(mount, Shell.render(route, title, actions, content))
    dom.document.title = s"$title — Compliance"
