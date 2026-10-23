# Requirements Document

## Introduction

This feature adds two read-only endpoints to the kassa-app-api Spring Boot POS application.

The **menu endpoint** lets the frontend fetch a full event menu (categories and products) by event slug, so it can render the POS screen without needing a prior lookup of the event's UUID.

The **sales summary endpoint** lets organisers retrieve aggregated per-product and per-category sales figures (quantity sold, revenue) for a given event, derived from all confirmed orders.

## Glossary

- **Event**: A named, time-bounded occasion (e.g. a festival edition) that owns a set of categories and receives orders. Identified by a UUID and a human-readable slug.
- **Slug**: A URL-safe, lowercase, hyphen-separated identifier unique to an Event (regex: `^[a-z0-9]+(-[a-z0-9]+)*$`).
- **Category**: A named grouping of products within an Event, carrying a `sortOrder` integer.
- **Product**: A sellable item within a Category, carrying a name, a price, and a `sortOrder` integer.
- **Order**: A confirmed sale linked to an Event, consisting of one or more OrderLines and an immutable total.
- **OrderLine**: A single line within an Order recording the Product, quantity sold, `unitPrice` (snapshot at sale time), and `nameSnapshot`.
- **Menu_API**: The REST controller responsible for serving the menu endpoint.
- **Summary_API**: The REST controller responsible for serving the sales summary endpoint.
- **Event_Service**: The service layer component that handles event-scoped business logic.
- **Summary_Service**: The service layer component that computes sales aggregations.
- **sortOrder**: A non-negative integer used to determine the display order of categories and products.
- **Revenue**: The monetary total calculated as `unitPrice × quantity` summed across all qualifying OrderLines.

---

## Requirements

### Requirement 1: Menu Endpoint

**User Story:** As a frontend application, I want to retrieve the full product menu for an event by its slug, so that I can render the POS screen without first resolving the event's UUID.

#### Acceptance Criteria

1. WHEN a `GET /api/events/slug/{slug}/menu` request is received with a slug that matches an existing Event, THE Menu_API SHALL return HTTP 200 with a response body containing the Event's id, name, and slug, and an ordered list of Categories each carrying the Category's id, name, and sortOrder.
2. WHEN a `GET /api/events/slug/{slug}/menu` request is received, THE Menu_API SHALL include each Category in the response ordered by `sortOrder` ascending; IF two Categories share the same `sortOrder` value, THEN THE Menu_API SHALL order those Categories by their id ascending.
3. WHEN a `GET /api/events/slug/{slug}/menu` request is received, THE Menu_API SHALL include within each Category an ordered list of its Products, each carrying the Product's id, name, price, and `sortOrder`, ordered by `sortOrder` ascending; IF two Products share the same `sortOrder` value, THEN THE Menu_API SHALL order those Products by their id ascending.
4. IF the slug in the request path does not match any existing Event, THEN THE Menu_API SHALL return HTTP 404 with an error message indicating that no Event was found for the given slug.
5. THE Menu_API SHALL expose the menu endpoint at the path `GET /api/events/slug/{slug}/menu`.
6. WHEN a `GET /api/events/slug/{slug}/menu` request is received with a slug that matches an existing Event, THE Menu_API SHALL include Categories that have no associated Products in the response as Categories with an empty product list.

---

### Requirement 2: Sales Summary Endpoint

**User Story:** As an event organiser, I want to view aggregated sales figures for an event, so that I can see how many units of each product were sold and how much revenue each product and category generated.

#### Acceptance Criteria

1. WHEN a `GET /api/events/{id}/summary` request is received with an id that matches an existing Event, THE Summary_API SHALL return HTTP 200 with a response body containing per-product and per-category sales aggregations.
2. WHEN computing sales aggregations, THE Summary_Service SHALL derive product-level figures by summing `quantity` and `unitPrice × quantity` across all OrderLines whose Order belongs to the specified Event and whose Order status is `completed`.
3. WHEN computing category-level aggregations, THE Summary_Service SHALL group product-level figures by the Category to which each Product belongs, summing `quantitySold` and `revenue` across all products in that category.
4. WHEN the response is constructed, THE Summary_API SHALL include for each product entry: the `productId`, `nameSnapshot`, `quantitySold`, and `revenue` rounded to two decimal places.
5. WHEN the response is constructed, THE Summary_API SHALL include for each category entry: the `categoryId`, `categoryName`, `totalQuantitySold`, `totalRevenue` rounded to two decimal places, and the list of product entries for that category.
6. IF the event id in the request path does not match any existing Event, THEN THE Summary_API SHALL return HTTP 404 with a response body containing an error message indicating that the event was not found.
7. WHEN an Event has no Orders, THE Summary_Service SHALL return an empty list of category aggregations.
8. THE Summary_API SHALL expose the summary endpoint at the path `GET /api/events/{id}/summary`.
