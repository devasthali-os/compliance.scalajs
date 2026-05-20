package com.pratyabhi.compliance.components

import com.pratyabhi.compliance.app.{Route, Router}
import com.pratyabhi.compliance.dom.DomBuilder.*
import com.pratyabhi.compliance.models.*
import org.scalajs.dom.{Element, Node}
import org.scalajs.dom.html.{Anchor, Button, Div, Heading, Label, LI, Span, Table, TableCell, TableRow}

object Ui:

  def badgeSeverity(s: Severity): Span =
    val (label, className) = s match
      case Severity.Info     => ("Info", "badge badge-info")
      case Severity.Low      => ("Low", "badge badge-low")
      case Severity.Medium   => ("Medium", "badge badge-medium")
      case Severity.High     => ("High", "badge badge-high")
      case Severity.Critical => ("Critical", "badge badge-critical")
    el[Span]("span") { b =>
      cls(b, className)
      b.textContent = label
    }

  def badgeSessionStatus(s: SessionStatus): Span =
    val (label, className) = s match
      case SessionStatus.Clean       => ("Clean", "badge badge-clean")
      case SessionStatus.Flagged     => ("Flagged", "badge badge-flagged")
      case SessionStatus.UnderReview => ("Under review", "badge badge-review")
      case SessionStatus.Blocked     => ("Blocked", "badge badge-critical")
    el[Span]("span") { b =>
      cls(b, className)
      b.textContent = label
    }

  def badgePolicyStatus(s: PolicyStatus): Span =
    val (label, className) = s match
      case PolicyStatus.Active  => ("Active", "badge badge-clean")
      case PolicyStatus.Draft   => ("Draft", "badge badge-flagged")
      case PolicyStatus.Retired => ("Retired", "badge badge-low")
    el[Span]("span") { b =>
      cls(b, className)
      b.textContent = label
    }

  def btn(label: String, primary: Boolean = false, danger: Boolean = false): Button =
    el[Button]("button") { b =>
      cls(b, (primary, danger) match
        case (true, _)  => "btn btn-primary"
        case (_, true)  => "btn btn-danger"
        case _          => "btn")
      b.`type` = "button"
      b.textContent = label
    }

  def routeLink(route: Route, label: String, extraClass: String = ""): Anchor =
    el[Anchor]("a") { a =>
      a.href = Route.toHash(route)
      a.textContent = label
      if extraClass.nonEmpty then cls(a, extraClass)
      onClick(a, () => Router.navigate(route))
    }

  def panel(title: String, body: Node, headerRight: scala.Option[Node] = None): Element =
    el[Element]("section") { section =>
      cls(section, "panel")
      val panelBody = el[Div]("div") { b =>
        cls(b, "panel-body")
        append(b, body)
      }
      if title.nonEmpty || headerRight.isDefined then
        val header = el[Div]("div") { h =>
          cls(h, "panel-header")
          if title.nonEmpty then append(h, el[Heading]("h3")(t => t.textContent = title))
          headerRight.foreach(n => append(h, n))
        }
        append(section, header, panelBody)
      else append(section, panelBody)
    }

  def panelHeaderLink(route: Route, label: String): Anchor =
    routeLink(route, label)

  def table(headers: String*)(rows: TableRow*): Table =
    el[Table]("table") { t =>
      val thead = el[Element]("thead") { headEl =>
        val hr = el[TableRow]("tr") { row =>
          headers.foreach(h => append(row, el[Element]("th") { c => c.textContent = h }))
        }
        append(headEl, hr)
      }
      val tbody = el[Element]("tbody") { bodyEl =>
        rows.foreach(r => append(bodyEl, r))
      }
      append(t, thead, tbody)
    }

  def tr(cells: Node*): TableRow =
    el[TableRow]("tr")(row => append(row, cells*))

  def td(content: Node | String): TableCell =
    el[TableCell]("td") { cell =>
      content match
        case s: String => cell.textContent = s
        case n: Node   => append(cell, n)
    }

  def kpiCard(k: Kpi): Div =
    el[Div]("div") { card =>
      cls(card, "kpi")
      append(card,
        el[Label]("label")(l => l.textContent = k.label),
        el[Div]("div") { v =>
          cls(v, "value")
          v.textContent = k.value
        },
        el[Span]("span") { s =>
          k.deltaUp match
            case Some(true)  => cls(s, "delta up")
            case Some(false) => cls(s, "delta down")
            case None        => cls(s, "delta")
          s.textContent = k.delta
        }
      )
    }

  def metaItem(label: String, value: Node): LI =
    el[LI]("li") { li =>
      append(li,
        el[Element]("strong")(s => s.textContent = label),
        value
      )
    }
