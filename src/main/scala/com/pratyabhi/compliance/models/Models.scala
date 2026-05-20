package com.pratyabhi.compliance.models

enum Severity:
  case Info, Low, Medium, High, Critical

enum SessionStatus:
  case Clean, Flagged, UnderReview, Blocked

enum PolicyStatus:
  case Active, Draft, Retired

final case class ViolationRow(
    time: String,
    sessionId: String,
    sessionLabel: String,
    appUser: String,
    model: String,
    severity: Severity,
    policy: String,
    status: SessionStatus
)

final case class SessionRow(
    id: String,
    started: String,
    userApp: String,
    model: String,
    risk: String,
    violationLabel: Option[(Severity, String)],
    status: SessionStatus
)

final case class Message(turn: String, role: String, body: String, highlight: Option[String] = None)

final case class MatchedRule(severity: Severity, name: String, detail: String)

final case class SessionDetail(
    id: String,
    started: String,
    actor: String,
    application: String,
    model: String,
    risk: String,
    status: SessionStatus,
    retention: String,
    severity: Severity,
    messages: List[Message],
    rules: List[MatchedRule]
)

final case class PolicyRow(
    id: String,
    name: String,
    version: String,
    status: PolicyStatus,
    rules: Int,
    updated: String,
    editable: Boolean
)

final case class ReviewRow(
    queued: String,
    sessionId: String,
    severity: Severity,
    policy: String,
    assignee: String,
    sla: String,
    slaUrgent: Boolean
)

final case class ExportRow(requested: String, range: String, format: String, status: String, ready: Boolean)

final case class Kpi(label: String, value: String, delta: String, deltaUp: Option[Boolean] = None)
