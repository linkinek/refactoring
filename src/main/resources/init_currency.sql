REATE TABLE CURRENCY(
    ID VARCHAR(255) NOT NULL,
    CURRENCY_TYPE INTEGER,
    FULL_NAME VARCHAR(255),
    NOMINAL INTEGER,
    VALUE DECIMAL(19,2),
    ---
    PRIMARY KEY (ID)
);

