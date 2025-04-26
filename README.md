# 🏥 Patient Management System – Project Reflection

[![wakatime](https://wakatime.com/badge/user/7e817c98-c383-4c9a-abdf-0bc9e74be88d/project/99e5de32-faeb-4f1c-be2d-8ed272213580.svg?style=flat-square)](https://wakatime.com/badge/user/7e817c98-c383-4c9a-abdf-0bc9e74be88d/project/99e5de32-faeb-4f1c-be2d-8ed272213580)
[![wakatime](https://wakatime.com/badge/user/7e817c98-c383-4c9a-abdf-0bc9e74be88d/project/99e5de32-faeb-4f1c-be2d-8ed272213580.svg?style=for-the-badge)](https://wakatime.com/badge/user/7e817c98-c383-4c9a-abdf-0bc9e74be88d/project/99e5de32-faeb-4f1c-be2d-8ed272213580)

## 📘 About This Project

This project is a simple version of a **Patient Management System**, built as I followed along with the YouTube video. It's based on microservices using **Java Spring Boot**, and I also learned how to use tools like **Docker**, **Kafka**, **gRPC**, and **AWS (via LocalStack)**.

I'm still a student, and this is my first time building something at this scale. I definitely ran into a lot of bugs and moments where I felt lost, but it was really rewarding to see each part work step by step.

---

## 🛠 Technologies I Used

- Java 17 + Spring Boot
- gRPC
- Kafka
- PostgreSQL
- Docker & Docker Compose
- LocalStack (to simulate AWS)
- Spring Security + JWT
- API Gateway

---

## 🧱 What I Built

| Microservice       | Description |
|--------------------|-------------|
| `patient-service`  | Handles patient CRUD operations |
| `billing-service`  | Talks to `patient-service` with gRPC |
| `analytics-service`| Listens to Kafka messages for analytics |
| `auth-service`     | Handles user login and JWT tokens |
| `api-gateway`      | Central entrypoint, handles routing and authentication |

---

## ✍️ My Learning Notes

- **gRPC** was completely new to me. It was confusing at first, but once I understood how `proto` files work, it started to make sense.
- **Kafka** was very cool! Setting up producers and consumers helped me understand real-world event-driven systems.
- **Spring Security + JWT** was tricky but satisfying. I now understand more about how tokens work for protected routes.
- **Docker + LocalStack** made it possible to simulate AWS locally. It felt like magic when everything connected together.

---

## 💭 Reflections

As a student, this project felt like climbing a small mountain. At first, I didn’t even know where to start. I had never used so many technologies together. But by going through the tutorial slowly and trying to build along the way, I learned so much about backend architecture, security, and cloud deployment.

Some parts I had to rewatch multiple times, especially when dealing with gRPC and Kafka configs. But looking back, I’m really proud I didn’t give up. It’s still not perfect and I know there's a lot more to learn, but I feel more confident now about microservices and backend systems.

---

## 🙌 Thanks

Big thanks to those who post their experience and make such clear and structured tutorials online. It really helped me get over the fear of "big systems." If you're a beginner like me, I definitely recommend following along with.

---

## 🚧 Future Plans

- Add unit and integration tests (I skipped some during the build)
- Make the UI prettier and more user-friendly
- Try deploying it to real AWS (not just LocalStack)

---

