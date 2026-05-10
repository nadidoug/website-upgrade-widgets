# Folder Creator

A Java-based project setup utility that automates frontend project folder creation and opens the project directly in Visual Studio Code.

This tool was built to speed up repetitive development setup tasks for web projects and reusable widget systems.

---

## Features

- Creates a main project folder
- Creates custom subfolders based on user input
- Accepts dynamic folder names
- Opens the project automatically in VS Code
- Terminal-based workflow
- Lightweight and beginner-friendly

---

## Example Workflow

```text
Enter project name:
website-upgrade-widgets

How many folders do you want inside the project?
3

Enter folder name 1:
widgets

Enter folder name 2:
assets

Enter folder name 3:
demo
```

The program then creates:

```text
website-upgrade-widgets/
│
├── widgets
├── assets
├── demo
```

And automatically opens the project in Visual Studio Code.

---

## Technologies Used

- Java
- Scanner class
- File class
- ProcessBuilder
- Command line automation

---

## Skills Demonstrated

- User input handling
- Looping
- File system automation
- Process execution
- Java terminal applications
- Developer tooling concepts

---

## Future Improvements

- Automatic file generation
- Widget template generation
- GUI version
- Cross-platform support
- Git initialization
- README generation
- Package installation
- One-command project scaffolding

---

## Run Instructions

Compile:

```bash
javac FolderCreator.java
```

Run:

```bash
java FolderCreator
```

---

## Purpose

This project was created as part of a frontend tooling and automation learning path focused on improving development workflow efficiency.