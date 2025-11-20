🚀 ShopMe — E-Commerce Backend (Spring Boot)

A complete backend system for an online store, featuring admin dashboard APIs, JWT authentication, product management, order tracking, Razorpay ID generation, and fully automated HTML email notifications.

⚠ Note: Razorpay is used only to generate payment/order IDs.
No real money transactions are processed.

📌 Features
👤 Authentication & User Management

User registration with email

Login with JWT authentication

Email-based account verification

Secure password reset via OTP

Role-based access (USER / ADMIN)

Admin can register separately

🛒 Product Management (Admin Only)

Add new products

Edit existing products

Delist/remove products

Search products

By name

By price range

Fetch all products

📦 Order Management

Users can place orders

Users can cancel their orders

Admin updates:

Delivery status (Packed, Shipped, Delivered, Cancelled, etc.)

Payment status

View all orders

💳 Razorpay (Mock Integration)

Razorpay SDK used to generate order IDs

No real payments

The Razorpay ID is stored with order details

📧 Automated Email Notifications

Beautiful HTML email templates sent to the user:

🎉 Order confirmation

📦 Order packed

🚚 Order shipped

❤️ Order delivered

❌ Order cancelled

Emails include:

Product details

Price

Address

Order number

Status update

📇 Personal Details API

Users can:

Add personal details

Update details

Get details

Delete details

Admin has a similar personal details API for admin profile.

📑 API Documentation

Swagger is included and auto-generated.

URL:

http://localhost:8080/swagger-ui/index.html

🛠 Tech Stack
Backend

Java 21

Spring Boot

Spring MVC

Spring Security (JWT)

Spring Data JPA

MySQL

Maven

Email

Spring Mail

HTML templates (Thymeleaf-like custom templates)

Other Tools

Postman (API testing)

Swagger / OpenAPI

Razorpay SDK (for generating IDs only)

🚀 How to Run the Project
1️⃣ Clone the repository
git clone https://github.com/<your-username>/shopme-backend.git
cd shopme-backend

2️⃣ Configure application.properties

Set your own values:

spring.datasource.url=jdbc:mysql://localhost:3306/shopme
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASS

spring.mail.username=YOUR_EMAIL
spring.mail.password=YOUR_APP_PASSWORD

razorpay.key=YOUR_KEY
razorpay.secret=YOUR_SECRET

3️⃣ Build the project
mvn clean install -DskipTests

4️⃣ Run the project
mvn spring-boot:run


OR from IDE → Run Application.java

📁 Project Structure
shopme-backend
│── src/main/java/shop/me/back/end/
│   ├── Controller
│   ├── Service
│   ├── Repository
│   ├── Entity
│   ├── Dto
│   ├── Mapping
│   ├── Config
│   └── Jwt
│── src/main/resources/
│── postman/ShopMe-API.postman_collection.json
│── screenshots/
│── pom.xml
└── README.md


🧪 Postman Collection

A full collection for testing all APIs is included in the repo:

ShopMe-backend-api-test.postman_collection.json

🖼 Screenshots (Optional but Recommended)

Create a folder named:

/screenshots


Add:

Swagger UI

Email templates

Postman testing

Database tables

Recruiters love visual proof.

❤️ Author

Yaseen Patel

If you like the project, ⭐ star the repository!
