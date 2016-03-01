# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table aluno (
  id                        bigint not null,
  nome                      varchar(255),
  email                     varchar(255),
  cidade                    varchar(255),
  bairro                    varchar(255),
  senha                     varchar(255),
  matricula                 varchar(255),
  constraint pk_aluno primary key (id))
;

create table carona (
  id                        bigint not null,
  criador_id                bigint,
  vagas                     integer,
  tolerancia                integer,
  data                      varchar(255),
  hora                      varchar(255),
  sentido                   varchar(255),
  constraint pk_carona primary key (id))
;

create sequence aluno_seq;

create sequence carona_seq;

alter table carona add constraint fk_carona_criador_1 foreign key (criador_id) references aluno (id) on delete restrict on update restrict;
create index ix_carona_criador_1 on carona (criador_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists aluno;

drop table if exists carona;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists aluno_seq;

drop sequence if exists carona_seq;

