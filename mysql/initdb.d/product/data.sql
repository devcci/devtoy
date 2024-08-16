INSERT INTO brand (name, created_at, modified_at)
VALUES ('A', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('B', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('C', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('D', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('E', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('F', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('G', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('H', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('I', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO category (name, created_at, modified_at)
VALUES ('상의', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('아우터', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('바지', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('스니커즈', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('가방', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('모자', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('양말', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('액세서리', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO product (name, category_id, brand_id, price, description, created_at, modified_at)
values ('상품', 1, 1, 11200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 2, 10500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 3, 10000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 4, 10100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 5, 10700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 6, 11200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 7, 10500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 8, 10800, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 1, 9, 11400, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 1, 5500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 2, 5900, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 3, 6200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 4, 5100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 5, 5000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 6, 7200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 7, 5800, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 8, 6300, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 2, 9, 6700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 1, 4200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 2, 3800, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 3, 3300, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 4, 3000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 5, 3800, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 6, 4000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 7, 3900, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 8, 3100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 3, 9, 3200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 1, 9000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 2, 9100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 3, 9200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 4, 9500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 5, 9900, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 6, 9300, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 7, 9000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 8, 9700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 4, 9, 9500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 1, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 2, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 3, 2200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 4, 2500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 5, 2300, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 6, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 7, 2200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 8, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 5, 9, 2400, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 1, 1700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 2, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 3, 1900, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 4, 1500, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 5, 1800, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 6, 1600, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 7, 1700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 8, 1600, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 6, 9, 1700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 1, 1800, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 2, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 3, 2200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 4, 2400, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 5, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 6, 2300, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 7, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 8, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 7, 9, 1700, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 1, 2300, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 2, 2200, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 3, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 4, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 5, 2100, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 6, 1900, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 7, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 8, 2000, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('상품', 8, 9, 2400, '상품', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);