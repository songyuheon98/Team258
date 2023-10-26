## book_category
LOAD DATA LOCAL INFILE 'F:/hanghae/FinalProjects/bookLibraryCsv/bookCategoryOriginal.csv'
    INTO TABLE team258.book_category
    FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@a1, @a2)
    SET
        book_category_isbn_code = @a1,
        book_category_name = @a2;


## book
LOAD DATA LOCAL INFILE 'F:/hanghae/FinalProjects/bookLibraryCsv/result.csv'
    INTO TABLE team258.book
    FIELDS TERMINATED BY ',' ENCLOSED BY '"'
    LINES TERMINATED BY '\n'
    IGNORE 1 ROWS
    (@a1, @a2, @a3, @a4)
    SET
        book_name = @a2,
        book_author = @a1,
        book_publish = @a3,
        book_status = 'POSSIBLE',
        book_category_id = CASE
                               WHEN @a4 = '' THEN NULL
                               WHEN POSITION('.' IN @a4) = 0 AND FLOOR(CAST(@a4 AS SIGNED INTEGER) / 10) >= 100 THEN NULL
                               WHEN POSITION('.' IN @a4) = 0 THEN FLOOR(CAST(@a4 AS SIGNED INTEGER) / 10) + 1
                               WHEN FLOOR(CAST(SUBSTRING_INDEX(@a4, '.', 1) AS SIGNED INTEGER) / 10) >= 100 THEN NULL
                               ELSE FLOOR(CAST(SUBSTRING_INDEX(@a4, '.', 1) AS SIGNED INTEGER) / 10) + 1
END;