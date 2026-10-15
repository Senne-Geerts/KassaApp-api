CREATE TABLE event (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    starts_at DATE,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE category (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sort_order INTEGER NOT NULL,
    event_id UUID NOT NULL REFERENCES event (id)
);

CREATE TABLE "order" (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES event (id)
);
