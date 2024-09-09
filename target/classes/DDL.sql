create table role
(
    role_id varchar(255) not null
        primary key,
    name    varchar(255) not null
);


create table users
(
    user_id  varchar(255) not null
        primary key,
    email    varchar(255) not null
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    password varchar(255) not null
);


create table user_roles
(
    user_id varchar(255) not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references users,
    role_id varchar(255) not null
        constraint fkrhfovtciq1l558cw6udg0h0d3
            references role,
    primary key (user_id, role_id)
);


create table quiz
(
    quiz_id         varchar(255) not null
        primary key,
    created_on      timestamp(6),
    difficulty      varchar(255),
    grade           integer      not null,
    max_score       real         not null,
    subject         varchar(255),
    total_questions real         not null
);


create table question
(
    question_id     varchar(255) not null
        primary key,
    correct_answer  varchar(255),
    hint            varchar(255),
    question_text   varchar(255),
    quizzes_quiz_id varchar(255)
        constraint fkgt8c3ro6p65h4s5lxb9hdg2ca
            references quiz
);


create table option
(
    unique_option_id      varchar(255) not null
        primary key,
    option_id             varchar(255),
    option_text           varchar(255),
    questions_question_id varchar(255)
        constraint fkhe5f3pnd3wdhfu0qdwb1h13ri
            references question
);


create table user_response
(
    question_response_id varchar(255) not null
        primary key,
    is_correct           boolean      not null,
    question_id          varchar(255),
    response             varchar(255),
    submision_id         varchar(255)
);


create table user_submision
(
    submision_id varchar(255) not null
        primary key,
    grade        real         not null,
    marks        real         not null,
    quiz_id      varchar(255),
    submited_on  timestamp(6),
    user_id      varchar(255)
);