# API Quick Reference

## Base URL
```
http://localhost:8080/api/v1
```

## Ad Space Endpoints

### 1. Get All Available Ad Spaces
```http
GET /ad-spaces
GET /ad-spaces?type=BILLBOARD
GET /ad-spaces?city=New York
GET /ad-spaces?type=BUS_STOP&city=New York
```

**Response 200:**
```json
[
  {
    "id": 1,
    "name": "Times Square Billboard",
    "type": "BILLBOARD",
    "city": "New York",
    "address": "1 Times Square",
    "pricePerDay": 500.00,
    "status": "AVAILABLE"
  }
]
```

### 2. Get Ad Space by ID
```http
GET /ad-spaces/1
```

**Response 200:**
```json
{
  "id": 1,
  "name": "Times Square Billboard",
  "type": "BILLBOARD",
  "city": "New York",
  "address": "1 Times Square",
  "pricePerDay": 500.00,
  "status": "AVAILABLE"
}
```

**Response 404:**
```json
{
  "message": "Ad space not found with id: 1",
  "timestamp": "2025-11-27T22:00:00",
  "path": "/api/v1/ad-spaces/1"
}
```

## Booking Request Endpoints

### 3. Create Booking Request
```http
POST /booking-requests
Content-Type: application/json
```

**Request Body:**
```json
{
  "adSpaceId": 1,
  "advertiserName": "John Doe",
  "advertiserEmail": "john.doe@example.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08"
}
```

**Response 201:**
```json
{
  "id": 1,
  "adSpaceId": 1,
  "adSpaceName": "Times Square Billboard",
  "advertiserName": "John Doe",
  "advertiserEmail": "john.doe@example.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08",
  "status": "PENDING",
  "totalCost": 4000.00,
  "createdAt": "2025-11-27T22:00:00"
}
```

**Response 400 (Validation Error):**
```json
{
  "message": "Validation failed",
  "timestamp": "2025-11-27T22:00:00",
  "path": "/api/v1/booking-requests",
  "errors": [
    "advertiserEmail: Invalid email format",
    "startDate: Start date is required"
  ]
}
```

**Response 400 (Business Rule Violation):**
```json
{
  "message": "Minimum booking duration is 7 days",
  "timestamp": "2025-11-27T22:00:00",
  "path": "/api/v1/booking-requests"
}
```

### 4. Get Booking by ID
```http
GET /booking-requests/1
```

**Response 200:**
```json
{
  "id": 1,
  "adSpaceId": 1,
  "adSpaceName": "Times Square Billboard",
  "advertiserName": "John Doe",
  "advertiserEmail": "john.doe@example.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08",
  "status": "PENDING",
  "totalCost": 4000.00,
  "createdAt": "2025-11-27T22:00:00"
}
```

### 5. Get All Bookings
```http
GET /booking-requests
GET /booking-requests?status=PENDING
GET /booking-requests?status=APPROVED
GET /booking-requests?status=REJECTED
```

**Response 200:**
```json
[
  {
    "id": 1,
    "adSpaceId": 1,
    "adSpaceName": "Times Square Billboard",
    "advertiserName": "John Doe",
    "advertiserEmail": "john.doe@example.com",
    "startDate": "2025-12-01",
    "endDate": "2025-12-08",
    "status": "PENDING",
    "totalCost": 4000.00,
    "createdAt": "2025-11-27T22:00:00"
  }
]
```

### 6. Approve Booking
```http
PATCH /booking-requests/1/approve
```

**Response 200:**
```json
{
  "id": 1,
  "adSpaceId": 1,
  "adSpaceName": "Times Square Billboard",
  "advertiserName": "John Doe",
  "advertiserEmail": "john.doe@example.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08",
  "status": "APPROVED",
  "totalCost": 4000.00,
  "createdAt": "2025-11-27T22:00:00"
}
```

**Response 400:**
```json
{
  "message": "Only PENDING bookings can be approved. Current status: APPROVED",
  "timestamp": "2025-11-27T22:00:00",
  "path": "/api/v1/booking-requests/1/approve"
}
```

### 7. Reject Booking
```http
PATCH /booking-requests/1/reject
```

**Response 200:**
```json
{
  "id": 1,
  "adSpaceId": 1,
  "adSpaceName": "Times Square Billboard",
  "advertiserName": "John Doe",
  "advertiserEmail": "john.doe@example.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08",
  "status": "REJECTED",
  "totalCost": 4000.00,
  "createdAt": "2025-11-27T22:00:00"
}
```

## cURL Examples

### Create a Booking
```bash
curl -X POST http://localhost:8080/api/v1/booking-requests \
  -H "Content-Type: application/json" \
  -d '{
    "adSpaceId": 1,
    "advertiserName": "John Doe",
    "advertiserEmail": "john.doe@example.com",
    "startDate": "2025-12-01",
    "endDate": "2025-12-08"
  }'
```

### Get Available Ad Spaces
```bash
curl http://localhost:8080/api/v1/ad-spaces

curl http://localhost:8080/api/v1/ad-spaces?type=BILLBOARD&city=New%20York
```

### Approve a Booking
```bash
curl -X PATCH http://localhost:8080/api/v1/booking-requests/1/approve
```

### Get Pending Bookings
```bash
curl http://localhost:8080/api/v1/booking-requests?status=PENDING
```

## Validation Rules

### CreateBookingRequestDto
- `adSpaceId`: Required, not null
- `advertiserName`: Required, not blank
- `advertiserEmail`: Required, not blank, must be valid email format
- `startDate`: Required, not null, must be in the future
- `endDate`: Required, not null

### Business Rules
- Start date must be in the future (relative to today)
- End date must be after start date
- Minimum booking duration: 7 days (inclusive)
- Ad space must have status "AVAILABLE"
- No overlapping APPROVED bookings for the same ad space
- Only PENDING bookings can be APPROVED or REJECTED

## Enum Values

### AdSpaceType
- `BILLBOARD`
- `BUS_STOP`
- `MALL_DISPLAY`
- `TRANSIT_AD`

### AdSpaceStatus
- `AVAILABLE`
- `BOOKED`
- `MAINTENANCE`

### BookingStatus
- `PENDING`
- `APPROVED`
- `REJECTED`

## HTTP Status Codes

- **200 OK**: Successful GET, PATCH
- **201 Created**: Successful POST
- **400 Bad Request**: Validation error or business rule violation
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Unexpected server error

## Cost Calculation

```
totalCost = (endDate - startDate + 1 days) × pricePerDay
```

Example:
- Start Date: 2025-12-01
- End Date: 2025-12-08
- Duration: 8 days
- Price Per Day: $500
- **Total Cost: $4,000**

