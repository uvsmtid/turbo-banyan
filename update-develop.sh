#!/usr/bin/env bash

# This script updates develop by squashing the history.

# Intentionally set excessive debug output and error detection:
# *   Fail (stop) script on non-zero exit code from any command.
set -e
# *   Use error code of the first failed command within a pipe.
set -o pipefail
# *   Fail on detection of any undefined variable.
set -u
# *   Print trace.
set -x
# *   Print each command before execution.
set -v

# Determine current branch:
current_branch="$( set -eu ; git rev-parse --abbrev-ref HEAD )"
# Make sure it is `dirty-develop`:
if [ "${current_branch}" != "dirty-develop" ]
then
    exit 1
fi

# Forgotten commit: fail if there are local changes:
git diff-index --quiet HEAD --
# Fetch remote as there could be concurrent modifications:
git fetch origin "${current_branch}"
# Forgotten push: fail if remote and local branches are different:
git diff --quiet "${current_branch}" "origin/${current_branch}"

git checkout develop

git reset --hard dirty-develop

git reset $(git commit-tree HEAD^{tree} -m "Baseline sources with squashed noisy history")

