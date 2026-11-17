---
name: gad-code-review
description: "Skill for code review of changes to GAD API tests framework project. Use when asked to review a diff, branch, merge request or named files in this repository (Java 25, Maven, TestNG, Rest Assured, Lombok, Gson, Allure), or to check changes before merge. Checks clean-code patterns, Java pitfalls, security and this project's test conventions. Review only; not for writing or refactoring code."
---

# Code review: GAD API tests framework

API tests (Java 25, Maven, TestNG, Rest Assured, Gson, Lombok, Log4j2, Datafaker, OWNER, Allure, Spotless) that run against a locally started GAD server (`baseUrl`, default `http://localhost:3000`).

## Version 1.0

## How to review

1. **Scope.** If the user names files, commits or a range, review exactly that, and list uncommitted changes outside it as not reviewed. Otherwise review the current branch against `origin/main` (`git diff origin/main...HEAD`; local `main` can be stale) plus any uncommitted changes in the working tree (`git status`), and say in the summary that they were included. Read changed files in full, plus the code they call, not only the hunks. Before judging consistency, compare each changed file with its nearest existing counterpart (`ArticlesAdapter` with `UsersAdapter`, `ArticlesTest` with `UsersTest`).
2. **Read-only.** Do not edit files. Do not run `mvn` unless asked: Spotless rewrites source files in the `validate` phase, and the tests create and delete data on the live GAD server. If asked, add `-Dspotless.apply.skip=true`. Do not read `src/test/resources/config.properties`; the `.example` is enough.
3. **Trace, do not infer.** Every finding needs `file:line` evidence you have read in this review. For something missing (a test class, a README entry, a reader for a field), cite the line that needs it and the search that came up empty.
   - A claim about behaviour (a status code, an exception, a null, whether tests run, in which order or with shared state, "the only X in the repo") needs every step traced to code, configuration, or library source or bytecode. Names, conventions and "how the framework usually works" are guesses until checked.
   - Read the TestNG and Surefire configuration before claiming anything about order, retries or shared state. Search exhaustively before saying "only" or "never".
   - Facts in this skill and earlier reviews, including your own, are starting points, not evidence.
   - Check GAD behaviour in its source if a local checkout is available (ask the user for its path) or with read-only GET requests; requests that write data need the user's OK.
   - A step you cannot verify turns the finding into a question for the author, and it cannot raise the severity.
4. **Challenge each candidate.**
   - Argue the opposite first: what is the strongest case that the code is fine? Report only if that case fails.
   - Deliberate handling: if the code already handles the condition on purpose (a guard clause, a `log.warn`, a best-effort catch), raise it at most as a Minor question about the chosen behaviour, quoting that handling with `file:line`. Security problems and wrong behaviour are the exception (see "Follow the project").
   - If another part of the same change already deals with it, drop it.
   - No speculative defensive coding: "could fail", "add a try/catch" or "validate more" needs a concrete failure path you can point at.
   - `@Test` methods fail fast on purpose: a missing null check there is not a finding, because the crash is the signal. Shared helpers, set-up and cleanup are different: a crash there skips cleanup or hides the real failure.
   - Report what you are at least about 80% sure of; otherwise drop it or ask it as a question.
5. **Skip what tooling or taste decides.** Spotless formatting (indentation, wrapping, import order, braces), a missing newline at end of file, use of `var`.
6. **Follow the project.** Conventions listed in "Not defects here" are not mentioned at all. If another established convention conflicts with a pattern below, follow the project and say so in one line. New code that copies an established pattern is reported once, as a Minor with "(existing pattern)" in its title, not as a new defect. Security problems and wrong behaviour are never downgraded because they were copied from existing code or deliberately handled. Ask for changes only on lines the change adds or touches; mention a pre-existing problem at most once: as a finding if it is in a reviewed file, otherwise as a note under "Verified / not verified".
7. **Priority and volume.** Wrong behaviour and data safety, then security, then test reliability, then maintainability, then nits. Group repeats ("same in 4 places"). At most 10 findings, of which at most 3 are nits; drop the lowest-priority ones beyond that. One precise finding beats several speculative ones.
8. **Suggested fixes change only what the finding is about.** Everything else (exceptions, ordering, null handling, side effects including logging, cleanup, mutability) stays as it was, and the fix stays within Java 25 and the libraries already in `pom.xml`. If you cannot offer such a fix, describe the problem without a rewrite. Never claim a fix is faster without a measurement.
9. **Limited tools.** If you cannot run commands (git, grep, the JDK tools) or read files beyond what you were given, say so at the top of the summary, list the steps you skipped (scope, tracing, line numbers, comparison with counterparts), and mark every finding that depends on them "unverified". Never present a skipped step as done. If you cannot ask the user (for example for the GAD path), record the open question under "Verified / not verified" instead.

### Output

```
## Summary
Verdict (approve / approve with comments / changes requested) and the main risk, 2-3 sentences.

## Findings
### [Blocker|Major|Minor|Nit] Short title: `path/File.java:42` (pattern: cleanup, duplication, secrets in logs, ...)
What is wrong (evidence), why it matters here, the concrete fix (a short before/after when it helps).

## Done well (optional, at most 2 lines)

## Verified / not verified
What was read and checked; what was not (for example tests not run, GAD behaviour not re-checked).

## Paste-ready comments
One fenced block, one line per finding, same order as above:
Major src/test/java/tests/ArticlesTest.java:42 - self-contained comment (the comment text itself at most 200 characters)
```

- Line numbers refer to the version you reviewed: the branch head (check with `git show <branch>:<path> | grep -n <symbol>`), the named commit, or the working tree for uncommitted changes. Never use a position in the diff. If you cannot locate the line (for something missing, the line that needs it), drop the finding.
- Paste-ready lines use full repo-relative paths. If a finding is downgraded or reframed, rewrite its paste-ready line too.
- Paste-ready comments talk about the code: never mention this skill, its sections or "the checklist" in them.
- **Blocker.** Wrong behaviour, a test that always fails or can never fail, data loss or leak, a committed secret, a hard-coded non-local host or a default that is no longer localhost (anything that would reach a shared or real environment by accident), a broken build.
- **Major.** Flaky or unsafe cleanup, a security problem that matters even locally (see "Security checks"), a problem with real maintenance cost.
- **Minor.** A clear but low-impact issue.
- **Nit.** Taste.

## What this project expects

- **Layers.** `src/main/java/dto`: generated test data and factories. `models/`: request/response payloads. `src/test/java/api_adapters`: Rest Assured calls. `tests/`: test classes and `BaseTest` helpers. `src/test/resources`: schemas, config example, Allure/log4j config, Spotless files.
- **One set per API resource.** A new API resource comes with request/response models (`models/<resource>`), a `New<Resource>Data` class with its `New<Resource>DataFactory`, an adapter, a GET schema for one item and one for the list, a test class, and a README coverage entry; a missing counterpart is a finding. A new endpoint on an existing resource needs an adapter method, plus models for its request and response bodies, and a schema if it is a GET; its test falls under "Scope and docs". Supporting endpoints such as login are exempt.
- **Adapters vs tests.** An adapter sends the request, checks transport-level expectations (status via `ResponseSpecification`, content type, schema) and returns the result. A test checks business values (ids, fields, owner). No raw HTTP calls in tests, no business assertions in adapters. Request bodies are request models serialized by Gson, not hand-written JSON strings (use a text block if a literal is unavoidable).
- **Independent tests.** Each test creates its own data through a factory and does not rely on execution order, other tests, or data left on the server. `priority` is for readability only.
- **Cleanup.** Everything a test creates is removed in `@AfterMethod(alwaysRun = true)`:
  - dependants before owners (articles before their user);
  - best effort: one failed delete does not skip the rest, and it is logged;
  - entities the test already deleted are marked deleted, so they are not deleted twice;
  - `finally` blocks follow the Exceptions check in "Java checks";
  - it works when the test or `@BeforeMethod` failed midway, and never reuses stale state from the previous test.
- **Valid data.** Generated data must pass GAD's validation. An article `date` must be a valid ISO date and not in the future; a random string gives 422. Title is at most 128 characters, body at most 10000. Happy-path data stays clear of these limits: the server's "future" cut-off moves with its UTC offset, so use a date one day in the past, not "now". Limits are tested in dedicated tests, never with random data; such tests are suggestions here (see "Scope and docs"). Not-found checks use an id the test created and deleted, checked right after the delete: GAD can give a deleted user's id to the next new user (article ids are not reused, because articles are soft-deleted). Never use a magic id such as 9999.
- **Server facts.** Article `user_id` comes from the token, not the payload. Article DELETE is soft (GET then returns 404, the row stays); user DELETE is hard while GAD's `feature_soft_delete_users` flag is off (the default). PATCH merges fields. The list hides inactive rows. Assertions must match this.
- **Gson models.** Only `@Expose` fields are serialized and nulls are omitted: a new field without `@Expose` silently vanishes from the payload. A wrong `@SerializedName` sends the wrong key in a request, and leaves the field at its default (`null`, `0` or `false`) when reading a response. A mismatched type either throws `JsonSyntaxException` (the string `"admin"` into an `int`) or is silently converted (`"42"` into `42`, `5` into `"5"`). Unknown fields are ignored, so schema checks are what catch drift. `Map<String, Object>` turns numbers into `Double`. A new field needs both a writer (factory or builder) and a reader (assertion or schema); otherwise ask whether it is intended.
- **Schemas.** JSON schemas in `json_schemas/` describe what the server really returns (article `user_id` is an integer or a string), not something stricter.
- **Config.** The base URL comes only from `src/test/resources/config.properties`: `TestConfig` has the single OWNER source `classpath:config.properties`, so `-DbaseUrl=...` has no effect (OWNER reads system properties only from a `system:properties` source). No hard-coded hosts. `config.properties` stays local and git-ignored; only the example is committed.
- **Naming and reporting.** Test methods start with `verify` and name the request (`verifyPostArticleRequest`, `verifyGetArticlesResponseAgainstSchema`). `testName` reads "Verify <METHOD> /<path> <outcome>", and the class carries `@Test(testName = "<Resource> API Tests")`. `@Step` on adapter methods, `@Feature`/`@Severity` on tests, assertion messages that say what failed.
- **Unused members.** A new method or field with no caller is a question ("is this intended?"). These count as used: TestNG- and Allure-annotated methods, Lombok-generated members, fields Gson fills by reflection, OWNER config methods.
- **Retries and shared state.** A test that only passes on retry is a defect. Do not assume retries run because they are configured: without evidence (for example a report showing repeated attempts), mark it unverified and suggest the author checks with a deliberately failing test. TestNG uses one instance per class, so instance fields in `BaseTest` and test classes (`userId`, `requestBody`, `createdArticles`, `ownerToken`) are shared by all methods: fine sequentially, unsafe under `parallel`. Flag any change that enables parallelism without isolating that state.
- **Scope and docs.** Tests mirror `UsersTest` (GET list schema, POST, GET by id, PATCH, DELETE); extra scenarios (limits, negative, auth, PUT/HEAD) are suggestions, not defects. One purpose per change. README updated, and its commands work.

### Not defects here

These are established conventions. Do not report them:

- Public fields on response models (they are plain data).
- Static adapters, repeated inline Rest Assured chains, and adapters that return a raw Rest Assured `Response`. The `.log().all()` in those chains is the exception (see "Secrets in logs and reports").
- Adapter methods that return the HTTP status code as an `int` (`deleteUser`, `deleteArticle`, `getDeletedArticle`).
- Wildcard imports and fluent chains (Rest Assured, Lombok builders).
- Names like `*Data`, `*Rq`, `*Rs`.
- TestNG lifecycle methods that share state through fields (`@BeforeMethod` sets `ownerToken`, `userId`, `requestBody`), including the protected fields in `BaseTest`.
- `BaseTest` create helpers that return the created entity and record it for cleanup.
- Tests that assert status codes with literals (`200`, `404`), even though the adapter spec already checks them.
- List-schema checks done inside the test (`verifyGetArticlesResponseAgainstSchema`, `verifyGetUsersResponseAgainstSchema`) rather than in the adapter.
- Several asserts on one response.
- Backslash image paths in the factories: they are GAD data values, not local files.
- Lombok-generated code: accessors, builders and loggers exist even though they are not in the source, so never report them as missing, and do not review them line by line. The choice of annotations is reviewed (see "Lombok" and "Secrets in generated text").

## Code quality patterns

### Naming

- Names say what and why, at the right level of abstraction. A longer precise name beats a short vague one, and length matches scope.
- No misleading names (`userList` that is not a list), no noise words (`Data`, `Info`) used only to tell two things apart, no type or scope prefixes.
- One word per concept (not `get`/`fetch`/`retrieve` for the same thing). Standard terms where they exist. A name reveals side effects (`createAndSave`, not `create`).

### Functions

- Small, one job, one level of abstraction, readable top-down.
- Few parameters (zero to two; three needs a reason; more means a parameter object). No boolean or mode flags that select behaviour (split the function). No output parameters (return the result). No hidden side effects. Either answers a question or changes state, not both.
- The name says what it does. Failures are exceptions, not error codes.

### Comments

- The code explains itself. A comment is for intent, a warning, or a public API. A TODO or workaround is tracked somewhere, with a reference.
- Remove commented-out code, obsolete or redundant comments, change history and author notes, rambling or noisy comments.

### Design and structure

- Single responsibility and cohesion: a class's methods use its fields, otherwise split it. Code lives where a reader expects it. No method more interested in another class's data than its own. Unrelated things are not coupled (a general constant parked in a specific class).
- No duplication (copy-paste, repeated if/switch chains, repeated assertion blocks; prefer polymorphism, a lookup table or a helper). No dead code (unreachable branches, unused imports, parameters or variables; unused methods and fields: see "Unused members"), no clutter.
- Consistency: the same thing is done the same way, following existing conventions and Java norms.
- Clear intent: explanatory variables for intermediate results, named constants instead of magic numbers and strings, conditions in a named method (`shouldRetry(attempt)`) and stated positively, boundary arithmetic (`level + 1`) done once in a named variable.
- Small public surface, no protected fields. A base class never knows its subclasses. Do not reach through objects (`a.getB().getC().doIt()`). Static only for stateless helpers.
- If order matters, make it explicit by passing data from one step to the next, not through fields set by an earlier call. If a module assumes something about another, ask it instead of hard-coding the assumption.
- Configurable values (URLs, defaults) live high up and are passed down. Keep other languages (JSON, SQL, regex) out of Java code where practical (`json_schemas/`).
- Understand why it works: "tests pass" is not an explanation. Be precise about null, rounding, money and ordering instead of leaving ambiguity.
- Simplicity order: it works and is tested, no duplication, clear intent, no more classes or methods than needed.
- Safeties stay on: no silenced warnings without a reason, no committed `-DskipTests`, no disabled test (`@Test(enabled = false)`) without a stated reason.

### Errors and boundaries

- Exceptions carry context (operation and what failed). Do not return null (return an empty collection or a special-case object) and do not pass null. Catching rules: see "Exceptions" in "Java checks".
- Third-party libraries (Rest Assured, Datafaker, Gson) are wrapped in adapters and factories; keep those interfaces small.
- Edge cases are handled deliberately: null and empty, minimum and maximum, off-by-one, inclusive versus exclusive ranges, time zones.

### Tests

- Fast. Independent (see "Independent tests"). Repeatable: the outcome does not change with when or where the suite runs (time zone, time of day, operating system). Self-validating: assertions, not eyeballing. Written together with the change.
- Readable set-up, action, check. Spell out only the data the test is about; the rest comes from factories. One concept per test.
- Each test asserts its business outcome; an adapter's status check alone does not prove it. Use the most specific assert (`assertEquals` rather than `assertTrue(a == b)`) so a failure shows both values. Do not assume the order of lists the API returns unless it is guaranteed; find items by id.
- Limits belong in dedicated tests (see "Valid data"). Areas near past bugs deserve extra tests, because bugs cluster. Trivial tests count. One command builds and runs everything.

## Java checks

- **Null safety.** In shared helpers, set-up and cleanup: unboxing a possibly-null `Integer`/`Long` throws `NullPointerException` (such as `userId` reset to `null` in `BaseTest`); `Optional.get()` (the JDK docs prefer `orElseThrow()`); `Optional` as a field or parameter (it is meant as a return type), or an `Optional` variable that is itself `null`; `null` instead of an empty collection.
- **Equality and asserts.** `==` between two boxed values or two strings compares references, not values; for `Integer` it only seems to work for small numbers, because values from -128 to 127 are cached. With one primitive side, the boxed value is unboxed and compared by value (a `null` then throws `NullPointerException`). TestNG's order is `assertEquals(actual, expected, message)`. `SoftAssert` needs `assertAll()`.
- **equals and hashCode.** Overridden together, no mutable map keys. Lombok `@Data` and `@EqualsAndHashCode` use all non-static, non-transient fields and leave out the superclass (Lombok warns when a subclass does not set `callSuper`).
- **Resources.** Try-with-resources for objects that hold a real resource: files, sockets, and streams from `Files.lines`, `Files.list` or `Files.walk`. Not every `AutoCloseable` needs it: most streams and in-memory streams such as `ByteArrayOutputStream` have nothing to release.
- **Exceptions.** Never swallow silently: no empty `catch`. No broad `catch` of `Exception`, `Throwable` or `Error` without a reason; a deliberate catch (such as best-effort cleanup, which may catch `AssertionError`, an `Error`) is narrow and logged. Keep the cause. Never leave a `finally` block early with `return`, `break`, `continue`, `yield` or `throw`: it discards the in-flight exception. Catching `InterruptedException` clears the interrupt flag, so restore it with `Thread.currentThread().interrupt()`. No `printStackTrace`, no exceptions as control flow, no log-and-rethrow.
- **Immutability.** `final` where possible, defensive copies of mutable inputs and outputs, no public mutable statics.
- **Collections.** No raw types. Do not modify a collection while iterating it (use the iterator's `remove`, `removeIf`, or a copy). Interface types in declarations. `LinkedHashMap` or `TreeMap` when order matters (`HashMap` guarantees no order). `List.of` and `Stream.toList()` are unmodifiable (the elements themselves may still change), and `List.of` rejects `null` elements. `Arrays.asList` is fixed-size and writes through to its array.
- **Streams.**
  - Pick the terminal that states the intent: `anyMatch`/`noneMatch` for existence (not `count() > 0` or collect-then-`isEmpty()`); `findFirst` when order decides the winner (`findAny` is explicitly nondeterministic, so only when every match is equally good); `min`/`max` rather than sorting to get one element; `Collectors.joining` rather than collect-then-`String.join`.
  - Let the stream build the result instead of a `forEach` that fills an outside collection or counter. A terminal `forEach` is fine when the side effect is the point. HTTP calls and other writes are their own step, never inside `map` or `filter`.
  - Lambdas are one-line glue or method references; extract a named helper for logic that branches or spans lines.
  - `toMap` throws `IllegalStateException` on duplicate keys unless it has a merge function. `groupingBy` and natural-order sorting throw `NullPointerException` on null keys.
  - No `parallelStream()` without a measured CPU-bound need, and never around HTTP calls. A loop stays a loop when it is clearer (state, early exit, index use).
- **Strings and environment.** No concatenation in loops (strings are immutable; use `StringBuilder` or `Collectors.joining`). `toLowerCase(Locale.ROOT)`/`toUpperCase(Locale.ROOT)` for identifiers and keys, and an explicit `Locale` when `String.format` formats numbers or dates for comparison. `Path.of` instead of hand-built file paths. Compile a regex once into a `static final Pattern` (patterns are immutable and thread-safe). Constant-first `equals`. The default charset is UTF-8 on Java 18 and later (unless `file.encoding=COMPAT` is set), so a missing charset is not a finding.
- **Time.** `java.time` only (`SimpleDateFormat` is not thread-safe; `DateTimeFormatter` is). Explicit zone: `LocalDateTime.now()` uses the default time zone. `Instant` for UTC.
- **Numbers and randomness.** Narrowing casts are silent: `(int)` of a `double` rounds toward zero, clamps to `Integer.MAX_VALUE`/`MIN_VALUE`, and turns NaN into 0. Floating-point values are compared with a delta, never `==`. Money uses `BigDecimal` built from a `String` or `BigDecimal.valueOf(double)`, never `new BigDecimal(double)`. Values that must be unique come from `UUID.randomUUID()` or a counter, not `Math.random()` (the factories use it only for test file names, which is fine). Security-relevant values: see "Crypto and randomness".
- **Concurrency.** Shared mutable statics, unsafe lazy initialisation, wide `synchronized`, thread handling mixed into other code, `Thread.sleep` in tests (wait on a condition instead). Instance state and `parallel`: see "Retries and shared state".
- **Lombok.** `@Builder` leaves a field it never sets at `0`, `null` or `false`, ignoring the field's initialiser, unless the field has `@Builder.Default`. `@Log4j2` generates a `private static final` `log`, so a subclass needs its own annotation. Keep Lombok current (it warns about `sun.misc.Unsafe` on new JDKs). Secrets in generated `toString`: see "Secrets in generated text".
- **Logging.** Parameterised messages, right level, no `System.out`, no secrets (request logging by Rest Assured: see "Secrets in logs and reports").
- **Modern Java (25).** Pattern matching, switch expressions, records and text blocks where they read better; follow the project's Lombok DTO style. Flag deprecated or removed APIs (for example `Thread.stop` always throws, and `finalize` is deprecated for removal).

## Security checks

This is a test framework talking to a local demo API with throwaway users. Flag these patterns where the change adds or touches them, because they get copied. Severity for security findings:

- Blocker: a committed real secret, or reaching a shared or real environment by accident (see "Blocker").
- Major: a problem that matters even locally, such as disabled TLS checks, injection or unsafe deserialization.
- Minor: tokens or passwords of throwaway local users in logs or reports. This becomes a Blocker once the suite runs against a shared environment or its logs or reports are published.

Pointing `baseUrl` at another host on purpose (by editing `config.properties`, see "Config") is by design, and nothing guards against a non-local host: mention that missing guard once, as a pre-existing suggestion, not on every test.

- **Secrets in the repo.** Passwords, tokens, keys, connection strings or internal hosts in code, resources, README or CI files. Check history if a secret is suspected. Real credentials never; Datafaker passwords for throwaway users and placeholders such as `${VAR}` are not secrets.
- **Secrets in logs and reports.** `.log().all()` in the adapters and the `AllureRestAssured` filter on `BaseAdapter.spec` write request headers and bodies (bearer tokens, passwords) to the console and to Allure attachments in `target/allure-results`. `@Step` methods record an `accessToken` argument as a step parameter. Severity: see the list at the top of "Security checks". For new code, prefer `.log().ifValidationFails()` and do not add `.log().all()` to authenticated or login requests. Mitigations: blacklist `Authorization` in Rest Assured's `LogConfig` (the Allure filter reads the same blacklist, so one setting covers both console and reports), and mark sensitive `@Step` parameters with Allure's `@Param(mode = Parameter.Mode.MASKED)` or `HIDDEN`.
- **Target safety.** The suite creates and deletes real records. It must not hit production or a shared environment by accident: the default stays localhost, and no real hostnames or IPs appear in code. A guard against non-local URLs would be an improvement, not a requirement for each new test.
- **Transport.** `relaxedHTTPSValidation()`, trust-all `TrustManager`, `HostnameVerifier` that returns `true`, `http` for a non-local host.
- **Injection.** SQL or commands built from strings; file paths with `..`; log injection (CR/LF in logged data); XML parsers with DTDs enabled; fetching URLs taken from data; scripting or JNDI lookups; URLs built from strings that need encoding (`PATH + "/" + id` is safe only for numeric ids).
- **Deserialization.** Java native serialization of untrusted data, polymorphic type handling in Jackson or Gson, unsafe YAML loading.
- **Crypto and randomness.** `SecureRandom` for anything security-relevant (`java.util.Random` and `Math.random()` are not cryptographically secure); no MD5, SHA-1, DES, ECB or hard-coded keys.
- **Auth in tests.** If a change touches an authenticated endpoint, is the unauthenticated or wrong-user path covered or consciously out of scope? This is a suggestion, not a defect. Never hard-code a real person's credentials.
- **Dependencies.** Dependency versions pinned in `<properties>` or through a BOM (the Allure BOM manages `allure-testng`); plugin versions pinned as literals or properties. From Maven Central, no ranges, `LATEST` or `-SNAPSHOT`, no `http://` repositories. New dependencies are justified and correctly scoped (`test` unless `src/main` needs them). When `pom.xml` changes a version, check the release notes between the old and new version for breaking changes and CVEs, and whether this repo uses the affected APIs; call the change safe, risky or breaking. Log4j must stay patched. No `curl ... | sh` in the README or build files.
- **Secrets in generated text.** Lombok `@Data`/`@ToString` and assertion messages on objects holding passwords or tokens (`LoginRq` and `PostCreateUserRq` use `@Data`) leak them wherever the object is logged: `@ToString` prints all non-static fields. Use `@ToString.Exclude` or keep the secret out of the message.
- **Test artifacts.** `target/`, `.allure/`, results and surefire reports contain request and response bodies: keep them git-ignored, never publish them unscrubbed, never copy real user data into fixtures, schemas or README examples.
- **Reflection and file system.** `setAccessible`, `sun.misc.Unsafe`, `System.setProperty` for trust stores, writing or recursively deleting outside the project directory.