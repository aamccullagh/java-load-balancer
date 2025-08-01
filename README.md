# Java Layer-4 Load Balancer

This project is a simple, software-based **Layer 4 TCP Load Balancer**, implemented in Java. It simulates how early-stage companies might have scaled their services using basic networking techniques — like round-robin load distribution and health checks — before the cloud era.

## 📦 Features

- Accepts concurrent connections from many clients
- Forwards raw TCP traffic to multiple backend services
- Round-robin backend selection
- Automatically removes offline backends via health checks
- JavaFX GUI to monitor backend status and request counts
- Logging using SLF4J + Logback
- Unit tests with JUnit 5 and Mockito

---

## 🛠 Requirements

- Java 17+
- Maven
- Internet (for fetching Maven dependencies)

---

## 🚀 How to Run

### 1. Start Backend Servers

In IntelliJ or from terminal:

```
Run BackendNode1.java  (port 9100)
Run BackendNode2.java  (port 9200)
```

### 2. Start Load Balancer (with GUI)

```
Run LoadBalancerApplication.java
```

You’ll see a GUI dashboard window showing the load balancer and backend status.

---

## 🧪 How to Test

### Option A: Use Telnet (built-in on Windows/macOS)

```bash
telnet localhost 9000
```

Then type a message, e.g., `hello`, and press Enter. You'll see a response like:

```
Backend1: hello
```

Close connection with `Ctrl + ]`, then type `quit`, hit Enter. Reconnect to test round-robin.

### Option B: Run Unit Tests

From IntelliJ or terminal:
```bash
mvn test
```

Includes:
- Round-robin backend selection test (mocked)
- Backend health & counter tests
- ClientConnection socket I/O test (mocked)
- JavaFX GUI launch smoke test

---

## 🧰 Technologies

- Java 17
- JavaFX
- SLF4J + Logback
- JUnit 5
- Mockito
- Maven

---

## ⚠️ Limitations

- Layer 4 only (TCP-based) — no HTTP parsing
- Uses round-robin only (no load weights or least-connections)
- GUI is basic, no admin controls

---

## 📁 Project Structure

```
src/
├── main/
│   └── java/payroc/challenge/loadbalancer/
│       ├── config/Settings.java
│       ├── core/
│       │   ├── ServiceNode.java
│       │   ├── ClientConnection.java
│       │   └── NetworkLoadBalancer.java
│       ├── servers/
│       │   ├── BackendNode1.java
│       │   └── BackendNode2.java
│       ├── ui/
│       │   └── LoadBalancerDashboard.java
│       └── LoadBalancerApplication.java
├── test/
│   └── java/payroc/challenge/loadbalancer/
│       ├── core/
│       │   ├── ServiceNodeTest.java
│       │   ├── ClientConnectionTest.java
│       │   └── NetworkLoadBalancerTest.java
│       └── ui/
│           └── LoadBalancerDashboardTest.java
```

---

## ✍️ Author Notes

This implementation was designed to match a coding challenge spec:
> Build a software-based Layer 4 load balancer with multiple client support, backend balancing, and offline detection.

Fully testable, GUI-equipped, and cleanly organized.

Enjoy!
