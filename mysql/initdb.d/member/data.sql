INSERT INTO member (member_id, name, password, email, created_at, modified_at, created_by, modified_by)
VALUES ('testUser1', '데브토일', 'test1234', 'testUser@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin',
        'admin'),
       ('testUser2', '데브토이이', 'test1234', 'testUser@gmail.com', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'admin',
        'admin');

INSERT INTO member_role (id, role)
values (1, 'admin'),
       (2, 'member');