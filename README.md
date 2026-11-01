# Travel and Tourism Agency – Backend

- **Name:** Ganza Kenny
- **Student ID:** 27550
- **Project:** Travel and Tourism Agency (Web Technology and Internet, AUCA – Mid-Term Project)

Spring Boot + Spring Data JPA + PostgreSQL backend for a Rwandan tour operator: tourists book guided tours, each tour guide drives one vehicle and speaks several languages, a tour booking goes BOOKED → PAID → CONDUCTED (or CANCELLED), and tour expenses are recorded once a tour is CONDUCTED.

## Entities

| Entity | Table | Relationships |
|---|---|---|
| Language | `language` | `@ManyToMany(mappedBy = "languages")` tourGuides (`@JsonIgnore`) |
| Vehicle | `vehicle` | none |
| TourGuide | `tour_guide` | `@ManyToOne` vehicle, `@ManyToMany` languages (`tour_guide_language`) |
| Tourist | `tourist` | none (`passportNumber` unique) |
| TourBooking | `tour_booking` | `@ManyToOne` tourist, `@ManyToOne` tourGuide, `TourStatus` enum |
| TourExpense | `tour_expense` | `@ManyToOne` tourBooking |

## Requirements

- Java 21
- PostgreSQL running on `localhost:5432`

## Steps to run

1. Create the database:
   ```sql
   CREATE DATABASE travel_tourism_agency;
   ```
2. Check the username and password in `src/main/resources/application.properties`.
3. Start the API:
   ```bash
   ./mvnw spring-boot:run
   ```
   The tables are created automatically (`spring.jpa.hibernate.ddl-auto=update`). The API runs on `http://localhost:8080`.
4. Import `traveland-tourism-agency-midterm.postman_collection.json` into Postman. One folder per part, each request named by its requirement ID (A2, B1, ..., F2, Bonus). The collection variables (`tourGuideId`, `bookedId`, `conductedId`, ...) must be replaced with the ids from your own database (`GET /api/.../all`).

## Endpoints

| ID | Method | Path |
|---|---|---|
| A2 | POST/GET/PUT/DELETE | `/api/{languages,vehicles,tour-guides,tourists,tour-bookings}/{save,all,{id},update/{id},delete/{id}}` |
| B1 | GET | `/api/tourists/by-nationality?nationality=kenyan` |
| B2 | GET | `/api/tour-bookings/by-status?status=BOOKED` |
| B3 | GET | `/api/tour-bookings/between?start=2026-10-01&end=2026-10-31` |
| C1 | GET | `/api/tour-guides/by-language?name=english` |
| C2 | GET | `/api/vehicles/free` |
| C3 | GET | `/api/languages/unused` |
| C4 | GET | `/api/tourists/of-tour-guide/{tourGuideId}` |
| D1 | POST | `/api/tour-bookings/save` |
| D2 | PATCH | `/api/tour-bookings/{id}/status?status=PAID` |
| D3 | PUT | `/api/tour-guides/{tourGuideId}/languages/{languageId}` |
| D4 | PATCH | `/api/tour-guides/{tourGuideId}/vehicle?fleetNumber=4` |
| E1 | POST | `/api/tour-expenses/save?tourBookingId=...` |
| E2 | GET | `/api/tour-expenses/tourist/{passportNumber}` |
| E3 | DELETE | `/api/tour-expenses/delete/{id}` and `/api/tour-bookings/delete/{id}` |
| F1 | GET | `/api/tour-bookings/stats/by-status` |
| F2 | PATCH | `/api/tour-bookings/cancel-day?tourGuideId=...&date=2026-10-20` |
| Bonus | GET | `/api/dashboard/summary` |
| Bonus | GET | `/api/tour-bookings/page?page=0&size=5&sort=tourDate,desc` |

## Request bodies

Tour guide (vehicle by fleetNumber, languages by id):
```json
{"firstName": "Jean", "lastName": "Mugisha", "phone": "0788111111",
 "vehicle": {"fleetNumber": 1}, "languages": [{"id": "<languageId>"}]}
```

Tour booking (tourist by passportNumber, tour guide by id):
```json
{"tourDate": "2026-12-10", "destination": "Volcanoes National Park", "travellers": 2,
 "tourist": {"passportNumber": "PC123456"}, "tourGuide": {"id": "<tourGuideId>"}}
```
