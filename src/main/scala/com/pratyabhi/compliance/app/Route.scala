package com.pratyabhi.compliance.app

enum Route:
  case Dashboard
  case Sessions
  case SessionDetail(id: String)
  case Policies
  case PolicyEditor(id: String)
  case Review
  case Reports

object Route:

  def parse(hash: String): Route =
    val path = hash.stripPrefix("#").stripPrefix("/").trim
    if path.isEmpty || path == "dashboard" then Dashboard
    else
      val parts = path.split("/").toList.filter(_.nonEmpty)
      parts match
        case "sessions" :: id :: Nil  => SessionDetail(id)
        case "sessions" :: Nil        => Sessions
        case "policies" :: id :: Nil  => PolicyEditor(id)
        case "policies" :: Nil        => Policies
        case "review" :: Nil          => Review
        case "reports" :: Nil         => Reports
        case _                        => Dashboard

  def toHash(route: Route): String = route match
    case Dashboard              => "#/dashboard"
    case Sessions               => "#/sessions"
    case SessionDetail(id)      => s"#/sessions/$id"
    case Policies               => "#/policies"
    case PolicyEditor(id)       => s"#/policies/$id"
    case Review                 => "#/review"
    case Reports                => "#/reports"

  def activeNav(route: Route): Route = route match
    case SessionDetail(_) => Sessions
    case PolicyEditor(_)  => Policies
    case other            => other
