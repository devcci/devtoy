INSERT INTO member (member_id, name, password, email, created_at, modified_at)
VALUES ('tester1', '테스트1', '$2a$10$rL9Izkn6XB8P19uYwY6Kn.tzv36ClKbNwP3QNamgtiKefFJphLh72', 'tester1@gmail.com',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('tester2', '테스트2', '$2a$10$gBArOlKYC0nV7FNEc5z3ce7oXLihod5PMQzogHWP3BpsszUZr9DGq', 'tester2@gmail.com',
        CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO member_role (id, role)
values (1, 'admin'),
       (2, 'member');