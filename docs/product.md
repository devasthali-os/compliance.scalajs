# Compliance — Product Specification

## 1. Overview

**Compliance** is a browser-based UX for an AI compliance system, implemented in **Scala 3** and **Scala.js**. It gives compliance officers, security teams, and engineering leads a single place to monitor AI usage, enforce organizational policy, and review evidence when models or agents handle regulated or sensitive data.

This repository (`compliance.scalajs`) is the **front-end shell**: Scala.js compiles to JavaScript, served by a small Node/Express host. Business logic and policy engines will grow in Scala on the client (and eventually connect to backend services).

| Attribute | Value |
|-----------|-------|
| Product name | Compliance |
| Version | 0.1 (scaffold) |
| Primary entry | `com.duwamish.compliance.Compliance` |
| Stack | Scala 3.3.4, Scala.js 1.16, scalajs-dom, Express |
| Default URL | `http://localhost:8080/` |

---

## 2. Problem Statement

Organizations deploying LLMs and AI agents face gaps between **how models are used in practice** and **what policies require** (data residency, PII handling, retention, human review, auditability). Spreadsheets and ad-hoc logs do not scale.

Compliance addresses this by providing:

- **Visibility** — what was prompted, what was returned, which model and tenant
- **Policy alignment** — rules that can be evaluated against sessions in near real time
- **Accountability** — searchable audit trails and exportable reports for regulators or internal review

---

## 3. Goals & Non-Goals

### Goals

- Type-safe, maintainable UI in Scala.js shared with future JVM services where possible
- Fast local dev loop: `npm run sbtBuild` → `npm start`
- Clear separation: static assets + compiled `main.js` served from `src/main/resources`
- UX suitable for non-developers (compliance, legal, ops)

### Non-Goals (v0.1)

- Production-grade authn/authz (planned later)
- Backend API implementation in this repo (client-first; APIs TBD)
- Model hosting or inference
- Replacing enterprise GRC suites end-to-end

---

## 4. Users & Personas

| Persona | Needs |
|---------|--------|
| **Compliance officer** | Dashboards, policy violations, report export |
| **Security engineer** | Integration hooks, severity, incident drill-down |
| **ML / platform engineer** | SDK/config surfaces, CI policy checks |
| **Auditor (read-only)** | Immutable logs, time-bounded queries, PDF/CSV export |

---

## 5. Current State (v0.1 Scaffold)

Shipped today:

- Landing page (`index.html`) with `#headerText` host element
- `Compliance.main` runs on `DOMContentLoaded`, sets welcome copy and logs to console
- Express serves `index.html` at `/` and static files from `src/main/resources/` (including compiled `main.js`)
- Build pipeline: `buildJs` = `fastOptJS` + copy to `src/main/resources/main.js`
- Docker image builds JS and runs `server.js` on port **8080**

This is intentionally minimal: it validates the Scala.js → browser → Node delivery path before feature UI.

---

## 6. Functional Requirements

### 6.1 Phase 1 — Shell & Navigation (next)

| ID | Requirement | Priority |
|----|-------------|----------|
| F1.1 | App shell: header, sidebar, main content region | P0 |
| F1.2 | Client-side routing (e.g. hash or lightweight router) without full page reload | P0 |
| F1.3 | Responsive layout (desktop-first; tablet usable) | P1 |
| F1.4 | Global loading and error states | P1 |

### 6.2 Phase 2 — Compliance Dashboard

| ID | Requirement | Priority |
|----|-------------|----------|
| F2.1 | **Overview** — counts: sessions today, open violations, policies active | P0 |
| F2.2 | **Session list** — paginated table: time, user/app, model, risk score, status | P0 |
| F2.3 | **Session detail** — prompt/response timeline, matched rules, reviewer actions | P0 |
| F2.4 | Filters: date range, severity, policy, model, free-text search | P1 |
| F2.5 | Drill-down from violation badge to rule definition | P1 |

### 6.3 Phase 3 — Policy Management

| ID | Requirement | Priority |
|----|-------------|----------|
| F3.1 | List policies with name, version, status (draft / active / retired) | P0 |
| F3.2 | Policy editor: rule types (PII, blocked topics, retention, geo, custom regex) | P0 |
| F3.3 | Simulate policy against sample text before publish | P1 |
| F3.4 | Version history and rollback | P2 |

### 6.4 Phase 4 — Review & Workflow

| ID | Requirement | Priority |
|----|-------------|----------|
| F4.1 | Queue of sessions flagged for human review | P0 |
| F4.2 | Approve / reject / escalate with mandatory comment | P0 |
| F4.3 | Assignment and SLA indicators | P2 |

### 6.5 Phase 5 — Reporting & Integrations

| ID | Requirement | Priority |
|----|-------------|----------|
| F5.1 | Export audit log (CSV, JSON) for date range | P1 |
| F5.2 | Scheduled summary email (backend) | P2 |
| F5.3 | Webhook on critical violation | P1 |
| F5.4 | API client stubs generated from OpenAPI (future backend) | P2 |

---

## 7. UX Specification

### 7.1 Information Architecture

```
/                     → Dashboard (overview)
/sessions             → Session list
/sessions/:id         → Session detail
/policies             → Policy list
/policies/:id         → Policy editor
/review               → Human review queue
/reports              → Exports & schedules
/settings             → Org, API keys, integrations (admin)
```

### 7.2 Visual & Interaction Principles

- **Clarity over density** — compliance users need scan-friendly tables and explicit severity labels
- **Severity encoding** — consistent color + icon: info, low, medium, high, critical (accessible contrast)
- **Immutable audit** — historical records are read-only; actions create new events, never overwrite
- **Confirm destructive actions** — policy publish, bulk export, retention changes
- **Empty states** — guided copy when no sessions or policies exist

### 7.3 Key Screens (wireframe-level)

**Dashboard**

- KPI cards: sessions (24h), violations, mean risk score, review backlog
- Recent violations table (top 10)
- Link to full session list

**Session detail**

- Left: metadata (id, timestamp, actor, model, app)
- Center: message thread (user / assistant turns)
- Right: matched policies, scores, review status + actions

**Policy editor**

- Form sections per rule type; enable/disable toggles
- Preview panel with pass/fail on sample input

### 7.4 Accessibility

- WCAG 2.1 AA target for text contrast and keyboard navigation
- Table headers associated with data cells; sortable columns announced to screen readers
- No reliance on color alone for severity

---

## 8. Technical Architecture

```
┌─────────────────────────────────────────────────────────┐
│  Browser                                                 │
│  index.html → main.js (Scala.js / Compliance)           │
│  scalajs-dom, future UI components                       │
└───────────────────────────┬─────────────────────────────┘
                            │ HTTP (static + future REST)
┌───────────────────────────▼─────────────────────────────┐
│  Node / Express (server.js)                              │
│  GET / → index.html                                      │
│  static → src/main/resources/                            │
└───────────────────────────┬─────────────────────────────┘
                            │ (future)
┌───────────────────────────▼─────────────────────────────┐
│  Compliance API (not in this repo)                        │
│  sessions, policies, audit, auth                            │
└─────────────────────────────────────────────────────────────┘
```

### 8.1 Module Layout (target)

```
src/main/scala/com/duwamish/compliance/
  Compliance.scala          # entry point
  app/                      # shell, routing
  views/                    # screens
  models/                   # DTOs aligned with API
  services/                 # HTTP client, mock adapters
  components/               # reusable UI widgets
```

### 8.2 Build & Deploy

| Step | Command |
|------|---------|
| Install deps | `npm install` |
| Bootstrap sbt | `npm run sbtInit` |
| Compile Scala.js | `npm run sbtBuild` |
| Run locally | `npm start` |
| Container | `docker build` → exposes 8080 |

### 8.3 Dependencies

- **Scala.js** — UI and client logic
- **scalajs-dom** — DOM API (v0.1); consider Scala.js React or Laminar for Phase 1+
- **sjs-nodejs** — bootstraps sbt from GitHub releases
- **Express** — static file server (dev/demo); production may sit behind nginx/CDN

---

## 9. Data Model (draft)

Types below are conceptual; JSON shape should match a future backend.

```scala
// Session — one logical interaction (may include many turns)
case class Session(
  id: String,
  startedAt: Instant,
  actorId: String,
  applicationId: String,
  modelId: String,
  riskScore: Double,
  status: SessionStatus,  // Clean | Flagged | UnderReview | Blocked
  violations: List[Violation]
)

case class Violation(
  policyId: String,
  ruleId: String,
  severity: Severity,
  message: String,
  evidence: String          // excerpt that triggered the rule
)

case class Policy(
  id: String,
  name: String,
  version: Int,
  status: PolicyStatus,
  rules: List[Rule]
)
```

---

## 10. Non-Functional Requirements

| Area | Requirement |
|------|-------------|
| **Performance** | Dashboard initial render &lt; 2s on typical laptop; session list virtualized for 10k+ rows |
| **Security** | HTTPS in production; CSP headers; no secrets in `main.js` |
| **Privacy** | PII redaction in UI by default; role-based field masking |
| **Reliability** | Graceful offline message if API unreachable |
| **Observability** | Client error logging hook (e.g. console + future telemetry) |
| **i18n** | English first; string externalization in Phase 2+ |

---

## 11. Success Metrics

- Time to detect a policy violation (ingest → visible in UI) &lt; 5 minutes
- Compliance reviewer can complete triage of one session in &lt; 3 minutes
- Zero silent failures on policy publish (all errors surfaced in UI)
- Developer build time `sbtBuild` &lt; 30s incremental after warm cache

---

## 12. Risks & Open Questions

| Risk | Mitigation |
|------|------------|
| Scala.js UI ecosystem smaller than React | Evaluate Laminar/Scala.js React early in Phase 1 |
| Policy engine complexity on client | Keep evaluation server-side; client displays results only |
| Large audit payloads | Pagination, streaming export, server-side aggregation |

**Open questions**

1. Which regulations are in scope first (GDPR, HIPAA, EU AI Act, internal only)?
2. Single-tenant vs multi-tenant SaaS?
3. Identity provider (OIDC/SAML) for enterprise customers?
4. Real-time updates (WebSocket/SSE) vs polling for violation feed?

---

## 13. Roadmap Summary

| Phase | Focus | Outcome |
|-------|--------|---------|
| **v0.1** (now) | Scaffold | Scala.js builds, Express serves, welcome screen |
| **v0.2** | Shell + routing | Navigable app structure |
| **v0.3** | Dashboard + sessions (mock data) | End-to-end UX prototype |
| **v0.4** | Policy UI + API integration | Live data, auth |
| **v1.0** | Review workflow + export | Production pilot |

---

## 14. References

- README: build and run instructions
- `com.duwamish.compliance.Compliance` — current entry point
- Sibling package: `../sjs-nodejs` — sbt bootstrap for npm workflows
