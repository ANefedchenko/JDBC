To start use IDE and -> HybrisProject / src / sample / Main.java 
 
To create and work with the database: 
1) Fill in the file your data -> HybrisProject / config / local.properties 
2) Use the following script

CREATE DATABASE store;

USE store;

CREATE TABLE orders(
	id INT NOT NULL AUTO_INCREMENT,
	user_id INT,
   	status VARCHAR(20) DEFAULT 'Process',
	created_at VARCHAR(20),
	    CONSTRAINT order_pk PRIMARY KEY(id)
);
		
CREATE TABLE products(
	id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20),
    price INT,
    status VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT product_pk PRIMARY KEY(id)
);

CREATE TABLE order_items(
	order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    CONSTRAINT order_fk FOREIGN KEY(order_id) REFERENCES orders(id),
    CONSTRAINT product_fk FOREIGN KEY(product_id) REFERENCES products(id) ON DELETE CASCADE
);

INSERT INTO products (name, price, status)
VALUES
('TV', '700', 'in_stock'),
('PS4', '500', 'in_stock'),
('PS5', '1000', 'in_stock'),
('iPhone', '1000', 'in_stock'),
('iPad', '800', 'in_stock'),
('PC', '1200', 'in_stock');

INSERT INTO orders (user_id, created_at)
VALUES
('3220', '04.10.2021'),
('1506', '04.10.2021'),
('5654', '04.10.2021'),
('2346', '04.10.2021'),
('2667', '04.10.2021'),
('2356', '04.10.2021');

INSERT INTO order_items (order_id, product_id, quantity)
VALUES
('1', '2', '5'),
('2', '3', '7'),
('2', '1', '4'),
('3', '4', '12'),
('4', '5', '10'),
('5', '5', '8');


