create table
  POSTCODE_TABLE(
    id int unsigned not null,
    postcode char(7) not null,
    kana_prefecture varchar(200),
    kana_city varchar(200),
    kana_street varchar(200),
    kanji_prefecture varchar(200),
    kanji_city varchar(200),
    kanji_street varchar(200),
    primary key (id)
  )
  default character set utf8mb4 collate utf8mb4_bin;