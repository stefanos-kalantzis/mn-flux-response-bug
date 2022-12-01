In order to reproduce the issue, run the following test: `mn.flux.response.bug.RevisionControllerTest.getRevisions_bug_JsonEOFException`.

Looking at the git history, there are 2 commits:
- 451eed12 _"working state"_ (test passes) ✅
- c42f58a2 _"broken state"_ (test fails) ❌

The latter commit is the one that causes the issue.
It's obvious from the changes made, that enabling tracing-zipkin causes this problem.
