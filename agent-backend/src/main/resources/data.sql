insert into permission (name) values
    ('CREATE_AD'), ('VIEW_AD'), ('UPDATE_AD'), ('DELETE_AD'), ('POST_COMMENT'),
    ('READ_COMMENT'),  ('DELETE_COMMENT'), ('UPDATE_COMMENT'), ('POST_RATE'), ('READ_RATE'),
    ('UPDATE_RATE'), ('CREATE_REQUEST'), ('LOGIN'), ('RECEIVE_MESSAGE'), ('REGISTER'),
    ('RENT_A_CAR'), ('SEARCH'), ('SEND_MESSAGE'), ('UPLOAD_PHOTO'), ('DELETE_RATE');

insert into authority (name) values ('ROLE_ADMIN'), ('ROLE_AGENT'), ('ROLE_SIMPLE_USER'),
    ('ROLE_REVIEWER_USER'), ('ROLE_MESSAGE_USER'), ('ROLE_RENT_USER'), ('ROLE_COMMENT_USER');

insert into authorities_permissions (authority_id, permission_id) values
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18), (1, 19),
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 19),
    (3, 13), (3, 15), (3, 17), (3, 2), (3, 6), (3, 10),
    (4, 9), (4, 10), (4, 11), (4, 20),
    (5, 14), (5, 18),
    (6, 16), (6, 12),
    (7, 5), (7, 6), (7, 7), (7, 8);

insert into user_entity (id, username, password, deleted, has_signed_in, last_password_reset_date)
    values ('9bbbd6c1-34b4-4ea6-8889-be247cfebc34', 'admin@gmail.com', '$2a$04$SwzgBrIJZhfnzOw7KFcdzOTiY6EFVwIpG7fkF/D1w26G1.fWsi.aK', 'false', 'false', '2019-10-01 21:58:58.508-07');

insert into user_authority (user_id, authority_id) values ('9bbbd6c1-34b4-4ea6-8889-be247cfebc34', 1), ('9bbbd6c1-34b4-4ea6-8889-be247cfebc34', 7);

insert into admin (id, first_name, last_name, user_id) values
('51d5e58d-ac22-4233-a1dc-e4251a18e815', 'Ms', 'Misoni', '9bbbd6c1-34b4-4ea6-8889-be247cfebc34');