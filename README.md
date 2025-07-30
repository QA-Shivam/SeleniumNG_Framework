🚀 Automation Testing with Docker and CI/CD
This project demonstrates end-to-end automation testing integrated with Docker and CI/CD practices, developed as part of the “Proficient Automation Tester by Leveraging Docker with CI/CD” course on Udemy.
It enables fast, scalable, and repeatable test executions using containerized infrastructure and continuous integration workflows—perfect for modern DevOps pipelines.

🧠 Key Concepts
Docker: Containerized test environments for consistent execution across machines.
CI/CD Pipelines: Seamless integration of automated tests into Continuous Integration and Delivery processes.
Automation Testing: Accelerate and standardize test execution with TestNG, Selenium Grid, and logging frameworks.
Slack Reporting (New): Real-time test execution summary and detailed HTML reports delivered directly to Slack channels via bot integration.

✨ Features
Feature	Description
✅ Cross-Platform Execution	Supports local Selenium Grid and cloud platforms like LambdaTest.
⚙️ Parallel Execution	Execute tests in parallel using TestNG for faster feedback.
📜 Config-Driven Execution	Easily manage environments and parameters via config.properties.
🧪 Logging & Reporting	Integrated Log4j2 for logging and Extent Reports for rich HTML reports.
🐳 Docker Integration	Run tests inside Docker containers for isolated and reproducible environments.
🔄 GitHub Actions CI	Built-in workflow to automatically run tests on pull requests and merges.
📣 Slack Integration (New)	Sends test summary (Pass/Fail/Skip counts) and Extent HTML Report link to a Slack channel using a Slack bot.

🧰 Tech Stack
Language: Java
Testing Framework: TestNG
Automation: Selenium 4
Build Tool: Maven
Reporting: ExtentReports, Log4j2
Version Control: Git + GitHub
CI/CD Tools: GitHub Actions & Jenkins
Containerization: Docker
Notifications: Slack Bot API (for summary + HTML report link)

🐳 Docker Support
Easily spin up and tear down Selenium Grid or your custom containerized testing environment using:
docker-compose up -d

🔄 GitHub Actions CI Integration
This project includes a ready-to-use GitHub Actions workflow file (.github/workflows/ci.yml) which:
Builds and tests the project
Generates HTML reports
Sends Slack notifications (if enabled)

📢 Slack Test Report Sharing (New Feature)
At the end of the test suite, the framework:
Calculates total Pass, Fail, and Skipped test cases
Uploads the HTML report to Slack
Sends a rich formatted message via bot in your specified channel
You can enable or disable this feature via a flag in config.properties:
properties
sendSlackReport=true
and add your slack bot token & channel id in secret text and fetch form jenkins to read in SlackIntegartion utility class at run time.

Sample Slack Message
<img width="1485" height="422" alt="Screenshot 2025-07-30 074605" src="https://github.com/user-attachments/assets/62484ea3-f074-45c1-a90c-e1cd9aed3902" />

🧪 Test Execution
Locally:
mvn clean test
With Docker:
docker-compose up --build
With GitHub Actions:
Just push your changes or create a PR to trigger the pipeline.

📄 Documentation
Please refer to selenium-docker.pdf in the repo for a deeper explanation of architecture, setup, and configurations.
