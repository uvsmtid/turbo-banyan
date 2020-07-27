
create table if not exists turbo_banyan_student (
    student_id bigint auto_increment primary key,
    first_name varchar(250) not null,
    last_name varchar(250) not null
);
