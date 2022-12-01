In order to reproduce the issue, run the following test: `mn.flux.response.bug.RevisionControllerTest.getRevisions_bug_JsonEOFException`.

Looking at the git history, there are 2 commits:
- [451eed12](https://github.com/stefanos-kalantzis/mn-flux-response-bug/commit/451eed12248039707bbd6f17bb9f25e15bcea095) _"working state"_ (test passes) ✅
- [c42f58a2](https://github.com/stefanos-kalantzis/mn-flux-response-bug/commit/c42f58a2cf98870f857d345e3736c68f24b8cf93) _"broken state"_ (test fails) ❌

The latter commit is the one that causes the issue.
It's obvious from the changes made, that enabling tracing-zipkin causes this problem.
