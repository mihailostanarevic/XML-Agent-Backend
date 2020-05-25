insert into permission (name) values ('CRUD_AD'), ('CRUD_COMMENT'), ('CRUD_RATE'), ('CREATE_BASKET'), ('CREATE_REQUEST')
    , ('LOGIN'), ('RECEIVE_MESSAGE'), ('REGISTER'), ('RENT_A_CAR'), ('SEARCH'), ('SEND_MESSAGE'), ('UPLOAD_PHOTO');

insert into authority (name) values ('ROLE_ADMIN'), ('ROLE_AGENT'), ('ROLE_SIMPLE_USER'), ('ROLE_AD_USER'),
    ('ROLE_REVIEWER_USER'), ('ROLE_MESSAGE_USER'), ('ROLE_RENT_USER');

insert into authorities_permissions (authority_id, permission_id) values (3, 6), (3, 8), (3, 10), (4, 1), (5, 2), (5, 3), (6, 7), (6, 11),
    (7, 4), (7, 5), (7, 9);

insert into user_entity (id, username, password, user_role, deleted, has_signed_in, enabled, last_password_reset_date)
    values ('9bbbd6c1-34b4-4ea6-8889-be247cfebc34', 'admin@gmail.com', '$2a$04$SwzgBrIJZhfnzOw7KFcdzOTiY6EFVwIpG7fkF/D1w26G1.fWsi.aK', 2, false, false, true, '2019-10-01 21:58:58.508-07');

insert into user_authority (user_id, authority_id) values ('9bbbd6c1-34b4-4ea6-8889-be247cfebc34', 3), ('9bbbd6c1-34b4-4ea6-8889-be247cfebc34', 7);