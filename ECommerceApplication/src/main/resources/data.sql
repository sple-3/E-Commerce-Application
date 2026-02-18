-- ======================
-- USERS
-- ======================

INSERT INTO users (email, first_name, last_name, mobile_number, password)
VALUES 
('admin@gmail.com', 'Admin', 'System', '0811111111', '$2a$12$V7nel6c9aTF7CWBJGz1kg.24a5AlYDmCVq/kWei4TzW4lGWRJz/H2'),
('user@gmail.com', 'John', 'Doe', '0822222222', '$2a$12$ive/2ON.wAJJ13x7hrYca.rAvHSKJx2gR4Brz1d35V.neydZTgbaC'); -- 123456

-- ======================
-- USER
-- ======================

INSERT INTO roles (role_id, role_name)
VALUES
(101, 'ADMIN'),
(102, 'USER');


-- ======================
-- USER ROLE
-- ======================

INSERT INTO user_role (user_id, role_id)
SELECT u.user_id, 101
FROM users u
WHERE u.email = 'admin@gmail.com';

INSERT INTO user_role (user_id, role_id)
SELECT u.user_id, 102
FROM users u
WHERE u.email = 'user@gmail.com';


-- ======================
-- CATEGORY
-- ======================

INSERT INTO categories (category_name)
VALUES 
('Electronics'),
('Fashion');


-- ======================
-- PRODUCTS
-- ======================

INSERT INTO products 
(description, discount, image, price, product_name, quantity, special_price, category_id)
VALUES
('Gaming Laptop High Performance', 10, 'laptop.jpg', 15000000, 'Gaming Laptop', 10, 13500000, 1),
('Wireless Mouse RGB', 5, 'mouse.jpg', 300000, 'Wireless Mouse', 50, 285000, 1),
('Casual T-Shirt Cotton', 0, 'shirt.jpg', 150000, 'T-Shirt', 100, 150000, 2);


-- ======================
-- PAYMENTS
-- ======================

INSERT INTO payments (payment_method)
VALUES
('CASH_ON_DELIVERY'),
('BANK_TRANSFER'),
('CREDIT_CARD');

-- ======================
-- MEMBERSHIPS
-- ======================

INSERT INTO memberships (code, discount_percent, active)
VALUES
('MEMBER10', 10, true),
('MEMBER15', 15, true),
('MEMBER20', 20, true),
('OLDMEMBER', 5, false);


-- ======================
-- CART
-- ======================

INSERT INTO carts (total_price, user_id)
VALUES
(27000000, 2);

-- ======================
-- CART ITEMS
-- ======================

INSERT INTO cart_items (discount, product_price, quantity, cart_id, product_id)
VALUES
(10, 15000000, 2, 1, 1);

-- ======================
-- orders
-- ======================

INSERT INTO orders (email, order_date, order_status, total_amount, payment_id)
VALUES
('user@gmail.com', '2026-02-17', 'Order Accepted !', 27000000, 2);

-- ======================
-- ORDER ITEMS
-- ======================

INSERT INTO order_items (discount, ordered_product_price, quantity, order_id, product_id)
VALUES
(10, 15000000, 2, 1, 1);

-- ======================
-- ADDRESSES
-- ======================

INSERT INTO addresses (building_name, city, country, pincode, state, street)
VALUES
('Green Residence', 'Jakarta', 'Indonesia', '12345', 'DKI Jakarta', 'Jl. Sudirman');

INSERT INTO user_address (user_id, address_id)
VALUES
(2, 1);

-- ============================================
-- RESET ALL SEQUENCES
-- ============================================
-- ALTER SEQUENCE products_seq RESTART WITH 4;