Live App: https://eclectic-gaufre-6ce38f.netlify.app/



Frontend github repo:https://github.com/vansh180198/notesmakingapp-frontend



# Notes Collaboration App - Backend

## Overview
The **Notes Collaboration App - Backend** is the server-side implementation for the real-time collaborative note-taking application. It provides RESTful APIs for user authentication, notes management, and real-time collaboration, ensuring secure and efficient operations. The backend is designed using **Java Spring Boot** and integrates seamlessly with the frontend to support features like token-based authentication, session management, email services, and real-time updates.

---

## Features

### Core Features
- **User Authentication**:
  - Secure login and signup functionalities.
  - Token-based authentication using JWT (JSON Web Tokens).
  - Password hashing for secure storage.

- **Notes Management**:
  - Create, read, update, and delete notes.
  - Support for categorizing notes.
  - Real-time notifications for note creation, updates, and deletions.

- **Real-Time Collaboration**:
  - Enable multiple users to edit a single note simultaneously.
  - Real-time updates using Server-Sent Events (SSE).
  - Track online users for collaborative features.

- **Email Services**:
  - Send invitation emails for collaboration.

- **Session Management**:
  - Automatic session expiry handling.
  - Middleware to validate tokens for protected routes.

---

## Tech Stack

### Backend
- **Framework**: Java Spring Boot for building RESTful APIs.
- **Database**: PostgreSQL for persistent data storage.
- **Authentication**: JWT for secure user sessions.
- **Real-Time**: Server-Sent Events (SSE) for real-time collaboration.
- **Email Service**: JavaMailSender for email notifications.

### Deployment
- Hosted on **Heroku** for scalability and reliability.

---

## Getting Started

### Prerequisites
Ensure you have the following installed:
- Java (version 17 or above)
- PostgreSQL
- Maven

### Setup Instructions

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/vansh180198/notesmakingapp-backend.git
   cd notesmakingapp-backend
   ```

2. **Set Up Environment Variables**:
   Create an `application.properties` file in the `src/main/resources` directory and configure the following:
   ```properties
   server.port=5000
   spring.datasource.url=jdbc:postgresql://<your-database-url>:5432/<database-name>
   spring.datasource.username=<your-database-username>
   spring.datasource.password=<your-database-password>
   spring.jpa.hibernate.ddl-auto=update
   jwt.secret=<your-jwt-secret>
   email.username=<your-email-address>
   email.password=<your-email-password>
   ```

3. **Build and Run the Application**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The server will start at `http://localhost:5000`.

---

## API Endpoints

### **Authentication**
- `POST /users/register`: Register a new user.
- `POST /users/login`: Log in a user and return a JWT token.
- `GET /users/me`: Fetch user details using a valid token.
- `POST /users/logout`: Log out a user and remove them from the online list.

### **Notes**
- `POST /notes`: Create a new note.
- `GET /notes`: Fetch all notes for the authenticated user.
- `GET /notes/category/{category}`: Fetch notes by category.
- `PUT /notes/{id}`: Update a note by ID.
- `DELETE /notes/{id}`: Delete a note by ID.
- `GET /notes/stream`: Stream real-time updates for notes.

### **Collaboration**
- `POST /email/sendinvite`: Send an invitation email.
- `GET /users/online`: Get the list of online users.
- `GET /users/stream`: Stream updates for online users.

---

## Deployment

### Deploying to Heroku
1. **Login to Heroku CLI**:
   ```bash
   heroku login
   ```

2. **Add Heroku Remote**:
   ```bash
   heroku git:remote -a mysterious-brushlands-09229
   ```

3. **Push to Heroku**:
   ```bash
   git push heroku main
   ```

4. **Set Environment Variables**:
   Configure your `application.properties` variables in Heroku under **Settings > Config Vars**.

---

## Folder Structure
```
notesmakingapp-backend/
├── src/                # Source files
│   ├── main/
│   │   ├── java/notesmaking/
│   │   │   ├── controller/   # REST API endpoints
│   │   │   ├── emitter/      # SSE services
│   │   │   ├── model/        # Data models
│   │   │   ├── service/      # Business logic
│   │   │   └── util/         # Utility classes
│   │   └── resources/
│   │       ├── application.properties  # Configuration
│   │       └── static/                  # Static files
├── pom.xml             # Maven dependencies
├── README.md           # Project documentation
└── Heroku Procfile     # Heroku deployment configuration
```

---

## Contributing

Contributions are welcome! Follow these steps:
1. Fork the repository.
2. Create a new branch for your feature/bugfix:
   ```bash
   git checkout -b feature-name
   ```
3. Commit your changes and push them to your fork.
4. Create a pull request to the main repository.

---

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Contact
For any questions or feedback, feel free to contact:
- **Name**: Vansh Bhatia
- **Email**: vanshbhatia53@yahoo.com
- **GitHub**: [vansh180198](https://github.com/vansh180198)

---

Happy Collaborating!

