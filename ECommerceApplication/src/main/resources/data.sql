-- ======================
-- USERS
-- ======================

INSERT INTO users (user_id, email, first_name, last_name, mobile_number, password)
VALUES 
(1, 'admin@email.com', 'Admin', 'System', '0811111111', '$2a$12$V7nel6c9aTF7CWBJGz1kg.24a5AlYDmCVq/kWei4TzW4lGWRJz/H2'),
(2, 'user@email.com', 'John', 'Doe', '0822222222', '$2a$12$.wR9hm.IX/33ypPFurARx.52cCbH6w7k3YDvXnE..ph342TvQlrF6');

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
WHERE u.email = 'admin@webshop.com';

INSERT INTO user_role (user_id, role_id)
SELECT u.user_id, 102
FROM users u
WHERE u.email = 'user@webshop.com';


-- ======================
-- CATEGORY
-- ======================

INSERT INTO categories (category_id, category_name)
VALUES 
(1, 'Electronics'),
(2, 'Fashion');


-- ======================
-- PRODUCTS
-- ======================

INSERT INTO products 
(product_id, description, discount, image, price, product_name, quantity, special_price, category_id)
VALUES
(1, 'Gaming Laptop High Performance', 10, 'laptop.jpg', 15000000, 'Gaming Laptop', 10, 13500000, 1),
(2, 'Wireless Mouse RGB', 5, 'mouse.jpg', 300000, 'Wireless Mouse', 50, 285000, 1),
(3, 'Casual T-Shirt Cotton', 0, 'shirt.jpg', 150000, 'T-Shirt', 100, 150000, 2);


-- ======================
-- PAYMENTS
-- ======================

INSERT INTO payments (payment_id, payment_method)
VALUES
(1, 'COD'),
(2, 'BANK_TRANSFER'),
(3, 'CREDIT_CARD');


-- ======================
-- CART
-- ======================

INSERT INTO carts (cart_id, total_price, user_id)
VALUES
(1, 0, 2);


-- ======================
-- ADDRESSES
-- ======================

INSERT INTO addresses (address_id, building_name, city, country, pincode, state, street)
VALUES
(1, 'Green Residence', 'Jakarta', 'Indonesia', '12345', 'DKI Jakarta', 'Jl. Sudirman');

INSERT INTO user_address (user_id, address_id)
VALUES
(2, 1);
