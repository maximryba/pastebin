create table if not exists users(
    id serial primary key ,
    username varchar(50) not null check ( length(trim(username)) >= 4 ),
    password varchar(30) not null check (length(trim(password)) >= 4)
);

create table if not exists pastes(
  id serial primary key ,
  text varchar(5000) not null,
  link varchar(100) not null,
  user_id int not null,
  foreign key (user_id) references users(id) on delete cascade
);