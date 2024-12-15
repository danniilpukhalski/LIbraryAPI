insert into library_schema.trackers (isbn, title, genre, description, author, deleted)
values ('978-3-16-148410-0', 'A Clockwork Orange', 'Fantasy', 'A thought-provoking novel about free will and morality.',
        'Anthony Burgess', 'false'),
       ('978-0-7432-7356-5', 'The Great Gatsby', 'Classic', 'A novel about the American dream and the jazz age.',
        'F. Scott Fitzgerald', 'true'),
       ('978-0-452-28423-4', '1984', 'Dystopian', 'A chilling dystopian novel about totalitarianism and surveillance.',
        'George Orwell', 'false'),
       ('978-0-618-00222-8', 'The Hobbit', 'Fantasy', 'A classic tale of adventure in Middle-earth.', 'J.R.R. Tolkien',
        'false'),
       ('978-1-5011-3104-5', 'Where the Crawdads Sing', 'Mystery',
        'A mystery novel set in the marshes of North Carolina.', 'Delia Owens', 'false'),
       ('978-0-670-03383-3', 'To Kill a Mockingbird', 'Classic',
        'A timeless story of race, justice, and morality in the American South.', 'Harper Lee', 'false'),
       ('978-0-14-044913-6', 'The Odyssey', 'Epic', 'An epic poem about the adventures of Odysseus.', 'Homer', 'false'),
       ('978-0-316-76948-8', 'The Catcher in the Rye', 'Classic', 'A novel about teenage rebellion and alienation.',
        'J.D. Salinger', 'false'),
       ('978-0-7434-8773-2', 'Angels & Demons', 'Thriller',
        'A fast-paced thriller exploring the battle between science and religion.', 'Dan Brown', 'false'),
       ('978-0-671-02735-9', 'Dune', 'Science Fiction', 'A science fiction epic set on the desert planet Arrakis.',
        'Frank Herbert', 'false');

insert into library_schema.users (username, password, deleted)
values ('user', '$2a$12$s6vfljJ.Szmr.8foVMTr1uEjWfmxkY1dtouzG31Qp7xVKOF0w2W/a', false), --user
       ('admin', '$2a$12$rKeNIuaIlmm5XVJXZZR9sO94zZ3UpcAjmhnVS2Zb1.R6u6ePFwOKy', false); --admin

insert into library_schema.users_roles(user_id, roles)
values (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (2, 'ROLE_ADMIN');

insert into library_schema.booktrack (bookId, status, taken, returned, deleted)
values (1, 'free', null, null, false),
       (2, 'taken', '2024-11-01', null, true),
       (3, 'free', '2024-10-10', '2024-11-01', false),
       (4, 'free', null, null, false),
       (5, 'taken', '2024-11-20', null, false),
       (6, 'free', '2024-09-15', '2024-09-30', false),
       (7, 'free', null, null, false),
       (8, 'taken', '2024-11-25', null, false),
       (9, 'free', null, null, false),
       (10, 'free', '2024-10-01', '2024-10-15', false);

