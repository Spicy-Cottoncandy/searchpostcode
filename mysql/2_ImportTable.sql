load data local
  infile '/docker-entrypoint-initdb.d/import.csv'
  into table POSTCODE_TABLE
  fields
    terminated by ','
    enclosed by '"'
;