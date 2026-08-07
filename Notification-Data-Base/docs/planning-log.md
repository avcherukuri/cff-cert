# Notification Preferences — Planning Log

Record of the design discussion that produced this feature, kept alongside the
code per the project's request to log the planning phase in the application
rather than leaving it only in chat history.

## Requirements as given

Authenticated-user notification preferences supporting email, SMS, and push
channels, with: Spring Boot REST APIs, React frontend, PostgreSQL persistence,
validation, a DB migration, unit/integration/frontend tests, and backward
compatibility with the rest of the system.

## Schema options evaluated

| Criterion | (a) Columns on `users` | (b) Separate table | (c) JSONB blob |
|---|---|---|---|
| Normalization | Violates 3NF, mixes concerns | Clean, single responsibility | Denormalized, no schema |
| Extensibility | New channel = migration on a shared table | New channel = additive column migration on an isolated table | New key = no migration, but no enforced structure |
| Query complexity | Low, but couples to a busy table | Low (`WHERE user_id = ?`, no join needed) | Higher — JSONB operators required |
| Performance | Lock-contention risk with unrelated `users` writes | Isolated, no contention with `users` | Read-modify-write races on partial updates |
| Migration effort/risk | High — touches a shared, critical table | Low — additive, new table only | Low schema effort, but "migration" becomes untested app logic |
| Maintainability | Poor — ownership boundary violated | Good — feature owns the table exclusively | Poor — no compile/DB-time guarantees |
| Validation | Column-level only, entangled with `User` | Full NOT NULL/CHECK/FK/UNIQUE + bean validation | DB cannot enforce internal JSON shape |
| Backward compatibility | Violates the "don't touch `users`" constraint | Zero impact on `users` or its consumers | Safe only if not added to `users` itself |

A second question was explored within option (b): a simple table with fixed
boolean columns per channel, vs. a channel/category reference (EAV-style)
model that stores preferences as rows against lookup tables. The EAV model is
more open-ended (new channels/categories are data, not schema) but adds joins,
lookup tables, and resolution logic that nothing in the current requirements
calls for.

## Decisions

- **Frontend stack**: the original feature request mentioned "Angular
  reactive forms," but the project's binding rules (`react-components.md`,
  `testing.md`, `CLAUDE.md`) mandate React 18 + Jest/RTL. Resolved in favor of
  **React** — the plan implements the React equivalent (controlled form state
  in a custom hook) of what reactive forms would have provided.
- **Schema shape**: **separate table**, `user_notification_preferences`, with
  fixed boolean columns (`email_enabled`, `sms_enabled`, `push_enabled`).
  Chosen over the EAV model because it matches today's actual 3-channel scope
  without speculative machinery; extensibility is still preserved since a new
  channel is one additive `ALTER TABLE ... ADD COLUMN ... DEFAULT ...`
  migration, non-breaking for existing rows and consumers.
- **`users` table**: assumed to already exist elsewhere in the same
  PostgreSQL database, owned by another part of the system. This feature
  never modifies or migrates `users` — only references `users.id` via FK.
  Assumed type `BIGINT`; flagged for confirmation against the real schema.
- **Auth mechanism**: session-based (Spring Security, server-side HTTP
  session, cookie-based). The authenticated user's id is always derived
  server-side from the session, never accepted from a request
  path/query/body parameter — this removes the IDOR attack surface by
  construction, since no endpoint accepts a target user id at all.

## Outcome

Full design (DB schema, JPA entities/DTOs, REST API contract, backend and
frontend component layering, validation rules, security model, error
handling, testing strategy, phased implementation plan, and rollback plan) is
recorded in the approved plan file used to drive implementation. This log
captures the *why* behind the choices; the plan file captures the *what*.
