📘 CCRM – Course & Student Management System
📌 Project Overview

This is a Java-based CLI Project for managing students, courses, enrollments, grades, reports, and backups.
It demonstrates Java fundamentals, OOPs, packages, file handling, and CLI menus in a structured way.

The project supports:

* Student Management (Add, List, Search)

* Course Management (Add, List, Search)

* Enrollments & Grade Assignments

* Import/Export of Data

* Backup & Restore options

* Transcript Reports

🚀 How to Run
✅ Requirements

JDK Version: OpenJDK 21 (Temurin-21.0.8+9 LTS)

IDE (Optional): Eclipse IDE for Java Developers

OS Tested On: Windows 10/11

🔹 Compile
cd C:\Users\Hp\IdeaProjects\JAVA_PROJECT
javac -d out src/edu/ccrm/cli/MainMenu.java

🔹 Run
java -cp out edu.ccrm.cli.MainMenu

🏛 Evolution of Java (Quick Bullets)

1995 – Java released by Sun Microsystems.

1998 – Java 2 (introduced J2SE, J2EE, J2ME).

2004 – Java 5 (Generics, Annotations, Enhanced for loop).

2011 – Oracle acquired Sun Microsystems.

2014 – Java 8 (Streams, Lambda, Optional, Date-Time API).

2017 – Java 9 (Modules, JShell).

2018 onwards – 6-month release cycle (Java 10, 11 LTS, …).

2023+ – Latest long-term releases: Java 17, 21.

🖥 Java Editions Comparison
Feature	Java ME (Micro Edition)	Java SE (Standard Edition)	Java EE (Enterprise Edition)
Target	Embedded devices, Mobile	Desktop, Standalone Apps, Core API	Large scale Web & Enterprise apps
APIs	Limited	Full Core APIs (Collections, Streams)	Adds Servlets, JSP, EJB, JPA
Use Case	IoT, Old feature phones	General-purpose apps	Web servers, Enterprise business apps
🔧 JDK, JRE, JVM Explanation

JVM (Java Virtual Machine): Runtime engine that executes Java bytecode.

JRE (Java Runtime Environment): JVM + Core Libraries (needed to run Java apps).

JDK (Java Development Kit): JRE + Compiler + Debugger + Tools (needed to develop apps).

🪟 Windows Installation Steps

Download OpenJDK from Adoptium Temurin
.

Install and set JAVA_HOME in Environment Variables.

Verify installation:

java -version
javac -version


Install Eclipse IDE → Select Java Developers package.

Configure Eclipse → Add Installed JDK → Create New Java Project → Add src folder.

📷 (Insert your installation + Eclipse screenshots here as required by assignment)

📑 Mapping Table – Syllabus Topics → Code Files
Syllabus Topic	File/Class/Method Demonstrated
Packages	edu.ccrm.cli.MainMenu
OOP (Classes, Objects)	Student.java, Course.java
Inheritance / Interfaces	ExportService.java
Collections	StudentService.java
Exception Handling	MainMenu.java (menu inputs)
File Handling (Import/Export)	ExportService.java
CLI Interaction	MainMenu.java
Assertions	StudentServiceTest.java
✅ Enabling Assertions

Assertions are disabled by default in Java.
To enable, use the -ea flag.

Example:
java -ea -cp out edu.ccrm.cli.MainMenu

Sample Assertion in Code:
assert student != null : "Student should not be null after adding!";

📂 Project Structure
JAVA_PROJECT/
│── src/
│   └── edu/ccrm/cli/MainMenu.java
│   └── edu/ccrm/model/Student.java
│   └── edu/ccrm/model/Course.java
│   └── edu/ccrm/service/StudentService.java
│   └── edu/ccrm/service/ExportService.java
│── out/ (compiled .class files)
│── README.md

📌 Notes

This project is CLI-based, but can be extended to GUI (Swing/JavaFX) or Web (Spring Boot).

Use Eclipse for easier compilation and debugging.

For data persistence, CSV/JSON files are used (can be upgraded to MySQL).

Author: Vidishaa Deo