Beginner's Guide

-- Quick summary (commands)
- Clone (HTTPS):   git clone https://github.com/123EFD/Viva1.git
- Clone (SSH):     git clone git@github.com:123EFD/Viva1.git
- Create branch:   git checkout -b feature/brief-description
- Commit:          git commit -m "Short summary: more details"
- Push branch:     git push origin feature/brief-description
- Open PR:         Visit repository on GitHub → "Compare & pull request"

## 1. Should you fork or clone the repo?
- If you are a repository collaborator (have write access), clone directly:
  - git clone https://github.com/123EFD/Viva1.git
- If you don't have push rights, fork the repo (GitHub web), then clone your fork:
  - git clone https://github.com/<your-username>/Viva1.git
- If you cloned a fork, add the original repo as upstream:
  - git remote add upstream https://github.com/123EFD/Viva1.git

Check remotes:
- git remote -v

---

## 2. Create a working branch (Either you can create branch or directly contribute to the main branch since this is just a small project assignment :)
Create a short descriptive branch name for each change:
- git checkout -b feature/fix-off-by-one
- or: git checkout -b bugfix/handle-null-input
- or: git checkout -b docs/add-contributing

This helps keep the work isolated and easier to review.

---

## 3. Make changes & follow style
- Follow any coding/style rules in the repo (check README, CODE_OF_CONDUCT, or an existing style guide).
- Add or update tests for bug fixes or new features.
- Update documentation or README if behavior or usage changes. (You can create a readme.md file in your folder to explain how your code works etc.)

---

## 4. Commit guidance
- Stage your changes:
  - git add path/to/file.java
  - or git add -A (to add all)
- Use clear commit messages. A suggested format:
  - Short summary (imperative, ≤ 50 chars)
  - Blank line
  - Optional longer description (wrap at ~72 chars) with motivation and details.
- Example:
  - git commit -m "Fix off-by-one in fine calculation" -m "Days > 30 were counted twice. Adjusted tier calculation and added unit tests for boundaries."
- Avoid committing generated files or large binaries. Use .gitignore.

---

## 5. Push your branch
- Push to origin (your fork or repo):
  - git push origin feature/fix-off-by-one

If you've rebased or force-updated, use:
  - git push --force-with-lease origin feature/fix-off-by-one
(note: force pushing to shared branches can disrupt others—use with care.)

---

## 6. Open a Pull Request (PR)
- Go to https://github.com/123EFD/Viva1 (or your fork) and click the "Compare & pull request" button, or click "New pull request".
- Choose the correct base (e.g., `main` or `develop`) and compare branch (your feature branch).
- PR title: concise summary; PR body: describe what you changed, why, and any testing steps.
- Link any related issues (e.g., Fixes #12).
- Add reviewers and relevant labels if the project uses them.

---

## 7. Keep your branch up to date
Before creating or while your PR is under review, keep your branch synced with the main branch to avoid merge conflicts:

If you cloned the upstream repository (recommended):
- git fetch upstream
- git checkout main
- git pull upstream main
- git checkout feature/fix-off-by-one
- git merge main
  - or rebase: git rebase main (preferred by some teams; ask maintainers)

If conflicts occur, resolve them locally, run tests, then commit and push updates:
- git add resolved-files
- git commit
- git push origin feature/fix-off-by-one

---

## 8. Useful Git commands (cheat sheet)
- Clone: git clone <repo-url>
- List remotes: git remote -v
- Create branch: git checkout -b branch-name
- Switch branch: git checkout branch-name
- Stage: git add file
- Commit: git commit -m "message"
- Push: git push origin branch-name
- Pull remote changes: git pull
- Fetch upstream: git fetch upstream
- Merge: git merge main
- Rebase: git rebase main
- Show status: git status
- Show log: git log --oneline --graph --decorate
- Undo last commit but keep changes staged: git reset --soft HEAD~1

Here's the git reference book if you want to know more :) 
--> https://git-scm.com/book/en/v2
