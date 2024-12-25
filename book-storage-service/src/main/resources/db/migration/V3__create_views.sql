create view library_schema.book_view as
select *
from library_schema.books;

create view library_schema.book_track_view as
select *
from library_schema.booktrack;

create view library_schema.users_view as
select *
from library_schema.users;

create view library_schema.free_books_view as
select t1.id, t1.isbn, t1.title, t1.genre, t1.description, t1.author
from library_schema.books t1
         join library_schema.booktrack t2 on t1.id = t2.bookId
where t2.status = 'free';