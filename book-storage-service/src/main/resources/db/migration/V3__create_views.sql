create view library_schema.book_view as
select *
from library_schema.trackers
where deleted = false;

create view library_schema.book_track_view as
select *
from library_schema.booktrack
where deleted = false;

create view library_schema.users_view as
select *
from library_schema.users
where deleted = false;

create view library_schema.view_table as
select t1.id, t1.isbn, t1.title, t1.genre, t1.description, t1.author
from library_schema.trackers t1
         join library_schema.booktrack t2 on t1.id = t2.bookId
where t1.deleted = false
  and t2.status = 'free'
  and t2.deleted = false;