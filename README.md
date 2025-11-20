🚀 ShopMe — E-Commerce Backend (Spring Boot)

A complete backend system for an online store, featuring admin dashboard APIs, JWT authentication, product management, order tracking, Razorpay ID generation, and fully automated HTML email notifications.

⚠ Note: Razorpay is used only to generate payment/order IDs.
No real payment or money transaction happens.

📌 Features
👤 Authentication & User Management

User registration with email

Login using JWT authentication

Email-based account verification

Secure password reset via OTP

Role-based access (USER / ADMIN)

Separate admin registration

🛒 Product Management (Admin Only)

Add products

Update product info

Delist/remove products

Search products by:

Name

Price range

Fetch all products

📦 Order Management

Users can place orders

Users can cancel orders

Admin can update:

Delivery status (Packed, Shipped, Delivered, Cancelled, etc.)

Payment status

Admin can view all orders

💳 Razorpay (Mock Integration)

Razorpay SDK used to generate order IDs only

No payment processing

Razorpay ID stored with order details

📧 Automated Email Notifications

HTML emails sent for:

🎉 Order confirmation

📦 Order packed

🚚 Order shipped

❤️ Order delivered

❌ Order cancelled

Each email contains:

Product details

Price

Order number

Quantity

Address

Updated status

📇 Personal Details API

Users can:

Add personal details

Update details

Fetch their details

Delete details

Admin also has a personal details API.

📑 Documentation (Swagger)

Swagger UI automatically generated:

👉 http://localhost:8080/swagger-ui/index.html

🛠 Tech Stack
Backend

Java 21

Spring Boot

Spring MVC

Spring Security + JWT

Spring Data JPA

MySQL

Maven

Email

Spring Mail

HTML custom templates

Tools

Postman

Swagger UI

Razorpay Java SDK

🚀 How to Run the Project
1️⃣ Clone the repository
git clone https://github.com/<your-username>/shopme-backend.git
cd shopme-backend

2️⃣ Configure application.properties
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


OR run Application.java from your IDE.

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
  │
  │── src/main/resources/
  │
  │── postman/ShopMe-backend-api-test.postman_collection.json
  │── screenshots/
  │── pom.xml
  └── README.md

🧪 Postman Collection

A complete Postman collection is included:

ShopMe-backend-api-test.postman_collection.json



Swagger UI

Email templates

API testing screenshots

Database table views

Order flow screenshots

These help recruiters understand the project visually.

❤️ Author

Yaseen Patel

If you like this project, please ⭐ star the repository!
