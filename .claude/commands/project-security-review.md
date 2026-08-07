---
description: Review code for security vulnerabilities
argument-hint: "[path to review]"
---

Perform a security review of the following path:

$ARGUMENTS

If no path was supplied, ask the user to provide a file or directory path.
Do not assume a default path.

Review the code for:

1. Authentication and authorization weaknesses
2. Missing input validation
3. SQL, command, template, and code injection risks
4. Hard-coded credentials or secrets
5. Sensitive information in logs or error messages
6. Insecure cryptography
7. Path traversal
8. Server-side request forgery
9. Unsafe deserialization
10. Dependency or configuration risks

For every finding, report:

- Severity: Critical, High, Medium, Low, or Informational
- File and line location
- Vulnerable behavior
- Possible impact
- Recommended correction

Do not modify files. Return a review report only.