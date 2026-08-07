---
name: codebase-analysis
description: Analyze a specified source file and report its structure, dependencies, risks, and improvement opportunities
argument-hint: "Path to analyse"
context: fork
disable-model-invocation: true
allowed-tools:
  - Read
disallowed-tools:
  - Bash
  - Write
  - Edit
  - NotebookEdit
---

Analyze this path:

$ARGUMENTS

If the argument is missing or blank:

1. Do not inspect an assumed path.
2. Ask the user to invoke the skill again with a specific file path.
3. Show this example:

   /codebase-analysis src/auth.ts

Do not modify any files.

Analyze:

1. Purpose and responsibilities
2. Important functions, classes, and modules
3. Data flow
4. Dependencies
5. Error-handling behavior
6. Security risks
7. Maintainability concerns
8. Testing gaps
9. Recommended improvements

Return:

## Summary

/context: Provide a concise overview of the file's purpose and responsibilities.

## Main Components

## Data Flow

## Dependencies

## Risks

## Testing Gaps

## Recommendations