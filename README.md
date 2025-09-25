## Builder Portfolio Management System

A Java-based mini-project for managing projects, users, and documents. The system supports **Admin, Builder, Project Manager, and Client** roles, with role-specific menus and workflows. All actions are logged using **SLF4J/Logback**, and services are tested using **JUnit** and **Mockito**.

---
## Preview
Watch the full demo video on Google Drive:  
[Click here to view the video](https://drive.google.com/file/d/1KIl21E72Ybs-PK5rdbA-aKwkeDzzueEl/view?usp=drivesdk)


## Features

### User Roles & Menu Options

**Admin Menu**
- View All Projects
- View Audit Trail
- Delete Project Manager, Client, or Builder
- Logout

**Builder Menu**
- Add New Project
- Update Project
- Delete Project
- Update Project Manager
- Upload Project Documents
- View Portfolio
- View Gantt Chart
- Logout

**Project Manager Menu**
- View Assigned Projects
- Update Project Status
- Update Actual Project Spend
- Upload Project Documents
- Logout

**Client Menu**
- View Owned Projects
- View Budget Status
- View Documents
- View Timeline
- Logout

---

## Tech Stack

- **Language:** Java 17+
- **Database:** PostgreSQL
- **Logging:** SLF4J + Logback
- **Testing:** JUnit 5 + Mockito
- **Build Tool:** Maven

---

## Database Setup

**PostgreSQL Configuration:**

```properties
db_class_name=org.postgresql.Driver
db_database_url=jdbc:postgresql://localhost:5432
db_username=deep.p
db_password=newpassword
db_database_name=builderportfolio
```

## Required Tables:
```
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(50) NOT NULL
);
```
```
CREATE TABLE projects (
    project_id SERIAL PRIMARY KEY,
    project_name VARCHAR(255) NOT NULL,
    builder_id INT REFERENCES users(user_id),
    manager_id INT REFERENCES users(user_id),
    client_id INT REFERENCES users(user_id),
    planned_budget NUMERIC,
    actual_spend NUMERIC,
    status VARCHAR(50)
);
```
```
CREATE TABLE documents (
    document_id SERIAL PRIMARY KEY,
    project_id INT REFERENCES projects(project_id),
    document_name VARCHAR(255),
    uploaded_by INT REFERENCES users(user_id)
);
```
```
CREATE TABLE audit_trail (
    audit_id SERIAL PRIMARY KEY,
    action VARCHAR(255),
    performed_by INT REFERENCES users(user_id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
## Maven Dependencies

```
<dependencies>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.7</version>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.38</version>
        <scope>provided</scope>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.4.11</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <version>2.21.0</version>
    </dependency>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```
## Project Structure

```
src/
├─ main/
│  ├─ java/
│  │  ├─ controller/   # Controllers for handling UI input
│  │  ├─ service/      # Business logic
│  │  ├─ repository/   # Database interaction
│  │  ├─ model/        # Entities: User, Project, Document, AuditTrail
│  │  └─ util/         # Utilities (DB connection, session management)
│  └─ resources/
│     └─ logback.xml   # Logging configuration
└─ test/
   └─ java/
      └─ ...           # Unit and Mockito test classes
```

## Running the Project

# Clone the Repository
```
git clone https://github.com/DeepParekh03/MiniProject_BuilderPortfolio
cd builder-portfolio
```
# Set up PostgreSQL Database
```
Create database builderportfolio
```
Run required SQL scripts to create tables

Update application.properties with your DB credentials

Build the Project
```
mvn clean install
```

# Run the Application
```
mvn exec:java -Dexec.mainClass="com.yourpackage.Main"
```

# Run Tests
```
mvn test
```
# Logging

All operations are logged using SLF4J + Logback

# Example log messages:

INFO: User 'builder1' added project 'Project Alpha'
INFO: Admin deleted user 'client2'

## Example Workflow

Builder logs in → adds a project → assigned Project Manager → Project Manager updates spend → Client views budget.

Admin can monitor all projects and audit trails.

All CRUD actions are logged and stored in the database.

# Author

Deep
