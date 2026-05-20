package com.pratyabhi.compliance.models

object MockData:

  val kpis: List[Kpi] = List(
    Kpi("Sessions (24h)", "1,284", "↑ 12% vs yesterday", Some(true)),
    Kpi("Open violations", "37", "↑ 4 since morning", Some(true)),
    Kpi("Mean risk score", "0.42", "↓ 0.08 vs 7-day avg", Some(false)),
    Kpi("Review backlog", "14", "6 due within SLA", None)
  )

  val recentViolations: List[ViolationRow] = List(
    ViolationRow("10:42 AM", "sess_8f2ac91", "sess_8f2a…c91", "Support Bot / jchen", "gpt-4o",
      Severity.Critical, "PII — No SSN in prompts", SessionStatus.UnderReview),
    ViolationRow("10:38 AM", "sess_3b1d7e2", "sess_3b1d…7e2", "Internal Copilot / mross", "claude-3-5",
      Severity.High, "Blocked topics — Medical advice", SessionStatus.Flagged),
    ViolationRow("10:21 AM", "sess_9c44a03", "sess_9c44…a03", "Docs Assistant / api-svc", "gpt-4o-mini",
      Severity.Medium, "Retention — 90 day limit", SessionStatus.Flagged),
    ViolationRow("09:55 AM", "sess_1a77f88", "sess_1a77…f88", "HR Portal / lkim", "gpt-4o",
      Severity.High, "Geo — EU data residency", SessionStatus.UnderReview),
    ViolationRow("09:41 AM", "sess_5e90b2bd", "sess_5e90…2bd", "Code Review Agent / dev-ci", "gpt-4o",
      Severity.Low, "Custom — API key pattern", SessionStatus.Clean)
  )

  val sessions: List[SessionRow] = List(
    SessionRow("sess_8f2ac91", "May 19, 10:42", "jchen · Support Bot", "gpt-4o", "0.91",
      Some((Severity.Critical, "1 critical")), SessionStatus.UnderReview),
    SessionRow("sess_3b1d7e2", "May 19, 10:38", "mross · Internal Copilot", "claude-3-5", "0.78",
      Some((Severity.High, "1 high")), SessionStatus.Flagged),
    SessionRow("sess_9c44a03", "May 19, 10:21", "api-svc · Docs Assistant", "gpt-4o-mini", "0.55",
      Some((Severity.Medium, "1 medium")), SessionStatus.Flagged),
    SessionRow("sess_2d88f01", "May 19, 09:12", "pkumar · Sales Enablement", "gpt-4o", "0.12", None,
      SessionStatus.Clean),
    SessionRow("sess_7aa3b19", "May 19, 08:47", "dev-ci · Code Review Agent", "gpt-4o", "0.24",
      Some((Severity.Low, "1 low")), SessionStatus.Clean)
  )

  val sessionDetails: Map[String, SessionDetail] = Map(
    "sess_8f2ac91" -> SessionDetail(
      id = "sess_8f2ac91",
      started = "May 19, 2026 · 10:42:18 UTC",
      actor = "jchen (employee)",
      application = "Support Bot",
      model = "gpt-4o · us-east-1",
      risk = "0.91",
      status = SessionStatus.UnderReview,
      retention = "Expires Aug 17, 2026",
      severity = Severity.Critical,
      messages = List(
        Message("user", "User",
          "Customer asked for refund. Their SSN is 123-45-6789 — can you draft a response?",
          Some("123-45-6789")),
        Message("assistant", "Assistant",
          "I can help draft a refund response. I should not store or repeat full SSNs. Please confirm last four digits only for verification."),
        Message("user", "User", "Just use the full number in the ticket notes.")
      ),
      rules = List(
        MatchedRule(Severity.Critical, "PII — No SSN in prompts",
          "Evidence: pattern ###-##-#### in user message"),
        MatchedRule(Severity.Medium, "Retention — 90 day limit",
          "Session contains PII; shortened retention applied")
      )
    )
  )

  def sessionDetail(id: String): SessionDetail =
    sessionDetails.getOrElse(
      id,
      sessionDetails("sess_8f2ac91").copy(id = id)
    )

  val policies: List[PolicyRow] = List(
    PolicyRow("pii-ssn", "PII — No SSN in prompts", "v3", PolicyStatus.Active, 4,
      "May 18, 2026 · arivera", true),
    PolicyRow("blocked-medical", "Blocked topics — Medical advice", "v2", PolicyStatus.Active, 6,
      "May 10, 2026 · sdev", true),
    PolicyRow("geo-eu", "Geo — EU data residency", "v1", PolicyStatus.Active, 3,
      "Apr 22, 2026 · mross", true),
    PolicyRow("api-key", "Custom — API key pattern", "v4 (draft)", PolicyStatus.Draft, 2,
      "May 19, 2026 · pkumar", true),
    PolicyRow("retention-90", "Retention — 90 day limit", "v1", PolicyStatus.Retired, 1,
      "Jan 8, 2025 · arivera", false)
  )

  def policy(id: String): Option[PolicyRow] =
    policies.find(_.id == id)

  val reviewQueue: List[ReviewRow] = List(
    ReviewRow("32m ago", "sess_8f2ac91", Severity.Critical, "PII — No SSN", "Unassigned",
      "3h 28m left", slaUrgent = true),
    ReviewRow("1h 12m ago", "sess_1a77f88", Severity.High, "Geo — EU residency", "arivera",
      "6h 48m left", slaUrgent = false),
    ReviewRow("2h 05m ago", "sess_3b1d7e2", Severity.High, "Blocked topics", "sdev",
      "On track", slaUrgent = false)
  )

  val exports: List[ExportRow] = List(
    ExportRow("May 19, 08:00", "May 1 – May 18", "CSV", "Ready", ready = true),
    ExportRow("May 12, 14:30", "Apr 1 – Apr 30", "JSON", "Ready", ready = true),
    ExportRow("May 5, 09:15", "Q1 2026", "CSV", "Expired", ready = false)
  )
