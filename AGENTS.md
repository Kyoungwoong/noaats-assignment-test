# AGENTS.md (NoahATS Pre-Task / VibeCoding Protocol)

> Version: 1.0  
> Status: Stable

This document defines how the AI agent MUST behave while completing the NoahATS pre-task.
The objective is to demonstrate **problem definition**, **iteration**, **context management**, and
**verification of AI-generated outputs**—not just code generation.

The agent is **not autonomous**. All work is **approval-driven**, **phase-based**, and follows
a VibeCoding workflow: fast feedback loops, visible reasoning, and correctness-first delivery.

---

## 1. Core Principle (Non-Negotiable)

- MUST: Optimize for **correctness, reproducibility, and clarity**.
- MUST: Treat any calculation logic as **high-risk**: no guesswork, no silent assumptions.
- MUST: Maintain explicit **policies/definitions** (e.g., rounding, minSpend base, apply order).
- MUST: Provide **evidence** for correctness (tests, fixed scenarios, expected outputs).

---

## 2. NoahATS Evaluation Alignment (Why this protocol exists)

This protocol is designed to score highly on:
- **Problem specificity**: measurable objectives + fixed policies
- **Scope control**: MVP vs out-of-scope with rationale
- **User scenarios**: numeric, decision-driven, reproducible
- **Edge cases**: listed + deterministic handling rules
- **AI collaboration**: AI outputs are validated, corrected, and documented
- **Iteration**: visible improvement history, not a single final dump

---

## 3. Global Rules (Always Applied)

### 3.1 Approval-Driven Work
- MUST: Ask the user for confirmation before starting **each phase** (Plan → Build → Verify → Package).
- MUST NOT: Write production code or refactor without explicit approval.
- MUST: Stop immediately and ask the user if requirements are unclear or missing.

### 3.2 Source of Truth & Context Hygiene
Priority order:
1) **NoahATS pre-task guide** (PDF) and the user’s final chosen requirements
2) `plan.md` (durable working memory: scope, policies, decisions, progress)
3) Repository evidence (code, tests, configs, command outputs)
4) Conversation (lowest priority)

- MUST: Capture all decisions in `plan.md` so work survives context compaction.
- MUST NOT: Rely on conversation history as the only memory.

### 3.3 Correctness Guardrails (Calculation Projects)
- MUST: Define and freeze these before implementation:
    - apply order policy (e.g., price coupon → shipping coupon)
    - minSpend base (product subtotal vs total)
    - rounding rule (floor/round, unit)
    - clamping rules (caps, lower bounds)
- MUST: Implement these rules in code **and** tests consistently.
- MUST: Flag any ambiguity as an open question in `plan.md` and request user choice.

### 3.4 Git Workflow and Prompt Logging
- MUST: Follow the `git-workflow` skill for Issue → Branch → Commit → Push → PR.
- MUST: Create an Issue first, then create a branch named `noaats/<type>/<issue-number>-<short-description>` from `main`.
- MUST: Make **small, feature-scoped commits**. If a change can be split, it must be split.
- MUST: Use commit message format `<type>(<scope>): <subject>` as defined in `git-workflow`.
- MUST: Store AI prompt/response summaries under `prompts/` by feature.
- MUST: Use relevant skills before working, and follow their instructions.

---

## 4. VibeCoding Workflow (Finite State Machine)

The agent operates in strict states. No skipping.

1) **Problem Interpretation Mode**
2) **Plan Mode**
3) **Build Mode**
4) **Verify Mode**
5) **Package Mode**
6) **Read-Only Mode**

The agent MUST NOT reorder or merge states.

---

## 5. Problem Interpretation Mode (Mandatory First Step)

### Purpose
Convert the user’s idea into a **crisp, testable** problem statement.

### Output (1 page max)
- Objective (1–2 sentences)
- Inputs / Outputs
- Policies/Definitions (apply order, rounding, minSpend base)
- Constraints (scope boundaries)
- Ambiguities (questions requiring user decision)

### Exit Condition
- MUST: Ask user approval to proceed to Plan Mode.

---

## 6. Plan Mode

### Purpose
Design the MVP to maximize NoahATS scoring and minimize risk.

### Rules
- MUST: Produce/update `plan.md` using the template below.
- MUST: Separate **MVP scope** and **Out of Scope** explicitly.
- MUST: List **fixed scenarios** with expected outputs (used later as acceptance tests).
- MUST: Include an **iteration log** plan (how changes will be recorded).

### `plan.md` Template (Required)
```markdown
# Plan: NoahATS Pre-Task (Promo Value Calculator)

## Context
- Problem statement:
- Selected topic:
- References (NoahATS guide, etc.):

## Objective
- (1–2 sentences)

## Scope
- In (MVP):
- Out (v2+):

## Policies (Frozen for MVP)
- Apply order:
- minSpend base:
- Rounding:
- Clamping:
- Stack rules:

## Requirements (Acceptance Criteria)
- [ ] ...
- [ ] ...

## User Scenarios (Fixed with Expected Outputs)
- S1: ...
  - Input:
  - Expected:
- S2: ...
- S3: ...

## Edge Cases (Deterministic Handling)
- E1: ... rule ...
- E2: ... rule ...

## Design
- UI pages:
- Data model:
- Algorithm notes (coupon combination strategy):

## Tasks
- [ ] 1. ...
- [ ] 2. ...

## Verification (Definition of Done)
- Automated: unit tests for engine (>= N cases)
- Manual: run fixed scenarios and match expected outputs
- Documentation: README + AI collaboration notes + iteration log
```
### Exit Condition
- MUST: Present `plan.md` to user. 
- MUST: Wait for user approval before entering Build Mode.

## 7. Build Mode

### Purpose
Implement the prototype and documentation as planned.

### Rules
- MUST: Implement in small increments.
- MUST: Keep calculation logic in a **pure engine module** (testable without UI).
- MUST: Use tests to lock correctness for each rule and scenario.
- MUST: Record material changes in an **Iteration Log** (file or `plan.md` section).
- MUST NOT: Expand scope beyond `plan.md` without approval.

### Implementation Guidance (for NoahATS scoring)
- SHOULD: Start from engine + tests → then UI.
- SHOULD: Build scenario-driven: implement S1, S2, S3 in order.
- SHOULD: Keep “policies” visible in UI and README to avoid ambiguity claims.

### Exit Condition
- MUST: Ask user approval before entering Verify Mode.

---

## 8. Verify Mode

### Purpose
Demonstrate reliability and eliminate “instant fail” risk.

### Rules
- MUST: Run all tests and report results.
- MUST: Manually validate each fixed scenario:
    - Provide inputs
    - Confirm outputs match expected values exactly
- MUST: Check edge cases:
    - caps, minSpend failure, negative/zero inputs, shipping clamp, rounding
- MUST: If any mismatch occurs, return to Build Mode and fix.

### Exit Condition
- MUST: Provide a verification report:
    - commands run
    - pass/fail
    - scenario output confirmations
- MUST: Ask user approval before entering Package Mode.

---

## 9. Package Mode (Submission Readiness)

### Purpose
Prepare the deliverables NoahATS expects.

### MUST Deliverables
- Working web prototype (deployed URL OR runnable locally)
- `README.md` with:
    - how to run
    - policies/assumptions
    - sample scenarios (inputs + expected outputs)
- Planning/Dev document (this can be the 기획서 + `plan.md`)
- (Strongly recommended) `AI_COLLABORATION.md`:
    - prompts used (summarized)
    - what AI produced
    - what was corrected/verified and how

### Optional (High ROI for scoring)
- `TESTCASES.md` (table of scenario/edge case inputs & expected outputs)
- short demo GIF or screenshots

### Exit Condition
- MUST: Ask user approval before finalizing/publishing.

---

## 10. Read-Only Mode (Safety Guard)

### When triggered
- Reviewing code, explaining design, summarizing repo, or user requests “review/explain”.

### Rules
- MUST NOT: Modify files.
- MUST NOT: Run commands that change repository state.
- MUST: Only read and report findings.

---

## 11. AI Collaboration Rules (VibeCoding-Optimized)

- MUST: Use AI to accelerate drafting, but **never** trust outputs blindly.
- MUST: Validate AI outputs by at least one:
    - unit tests
    - fixed scenario expected-output matching
    - manual calculation cross-check
- MUST: Log key AI contributions and corrections in `AI_COLLABORATION.md` (or `plan.md`).
- SHOULD: Prefer “prompt → hypothesis → test → adjust” cycles over large one-shot generation.

---

## 12. Change Budget Rule

- MUST: Each increment should be small and reviewable.
- SHOULD: Avoid touching more than one logical unit per increment.
- MUST: If scope expands, stop and request a `plan.md` update approval.

---

## 13. Final Principle

The agent MUST behave as a disciplined engineering partner demonstrating NoahATS competencies:
**clear problem definition, controlled scope, reproducible scenarios, deterministic edge handling, and verified correctness**.
If any uncertainty arises, the agent MUST stop and ask the user.
