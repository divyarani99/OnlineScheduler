This is a simple online scheduler application built using Java, Spring Boot, and Maven. The application allows users to book, reschedule, and cancel appointments.

Prerequisites
Before you begin, ensure you have met the following requirements:
You have installed the latest version of Java Development Kit(JDK 17) and Maven(3)

Running the Online Scheduler Application
To run the Online Scheduler Application, follow these steps:
1. Clone the repository from GitHub:
   git clone https://github.com/divyarani99/OnlineScheduler.git

2. Navigate to the project directory:
   cd OnlineScheduler

3. Build the project using Maven:
   mvn clean install

4. Run the application:
   mvn spring-boot:run

Also, you can execute main method in OnlineSchedulerApplication class from your IDE. By default the application will run on port 8080

Please find below the samle curls:

1. GetAvailableSlots of an operator:

curl --location 'http://localhost:8080/operators/3/availableSlots?date=2023-03-15' \
--data ''

2. GetAllAppointmentTimeSlots of an operator

curl --location 'http://localhost:8080/operators/3/appointments?date=2023-03-15' \
--data ''

3. BookAppointment

curl --location 'http://localhost:8080/appointments/bookAppointment' \
--header 'Content-Type: application/json' \
--data '{
    "operatorId": "1",
    "customerName": "John Doe",
    "startTime": "05:00",
    "date": "2023-03-15"
}'

4. RescheduleAppointment

curl --location --request PUT 'http://localhost:8080/appointments/602/rescheduleAppointment' \
--header 'Content-Type: application/json' \
--data '{
    "operatorId": "1",
    "customerName": "John Doe",
    "startTime": "06:00",
    "date": "2023-03-15"
}'

5. CancelAppointment

curl --location --request DELETE 'http://localhost:8080/appointments/602/cancelAppointment' \
--data ''



