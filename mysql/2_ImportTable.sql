load data local
  infile '/docker-entrypoint-initdb.d/KEN_ALL_utf8_out.csv'
  into table POSTCODE_TABLE
  fields
    terminated by ','
    enclosed by '"'
;