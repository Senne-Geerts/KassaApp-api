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
    event_id UUID NOT NULL REFERENCES event (id),
    name VARCHAR(100) NOT NULL,
    sort_order INTEGER NOT NULL,
    CONSTRAINT uq_category_event_name UNIQUE (event_id, name)
);

CREATE TABLE product (
    id UUID PRIMARY KEY,
    category_id UUID NOT NULL REFERENCES category (id),
    name VARCHAR(100) NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    sort_order INTEGER NOT NULL
);

CREATE TABLE orders (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL REFERENCES event (id),
    client_id UUID NOT NULL UNIQUE,
    total NUMERIC(10, 2) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE TABLE order_line (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL REFERENCES orders (id),
    product_id UUID NOT NULL REFERENCES product (id),
    quantity INTEGER NOT NULL,
    unit_price NUMERIC(10, 2) NOT NULL,
    name_snapshot VARCHAR(100) NOT NULL,
    CONSTRAINT uq_order_line_order_product UNIQUE (order_id, product_id)
);

CREATE INDEX idx_product_category ON product (category_id);
CREATE INDEX idx_orders_event_created ON orders (event_id, created_at);
