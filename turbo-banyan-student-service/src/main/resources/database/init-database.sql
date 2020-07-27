-- Database creation used for self-managed MySQL instance.
create database turbo_banyan_database;
create user 'turbo_banyan_username'@'%' identified by 'turbo_banyan_password';
grant all on turbo_banyan_database.* to 'turbo_banyan_username'@'%';
