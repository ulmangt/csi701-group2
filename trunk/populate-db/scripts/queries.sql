-------------------------------------------------------------------------------------------

-- QUESTION 1 Which digit yielded the best accuracy of detection by our tests?

-- select number of correctly classified samples per run and character type
(select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct;

-- select total number of samples per run and character type
(select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total;

-- select percentage of correctly classified samples per run and character type
select Total.ixRun, Total.sCharacter, Correct.count/Total.count from 
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total,
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct
  where Total.ixRun = Correct.ixRun and Total.sCharacter = Correct.sCharacter;

+-----------+------------+---------------------------+
| ixRun     | sCharacter | Correct.count/Total.count |
+-----------+------------+---------------------------+
|         1 | 0          |                    0.9745 |
|         1 | 1          |                    0.9730 |
|         1 | 2          |                    0.9737 |
|         1 | 3          |                    0.9734 |
|         1 | 4          |                    0.9700 |
|         1 | 5          |                    0.9721 |
|         1 | 6          |                    0.9729 |
|         1 | 7          |                    0.9745 |
|         1 | 8          |                    0.9717 |
|         1 | 9          |                    0.9717 |
| 201011133 | 0          |                    0.8983 |
| 201011133 | 1          |                    0.8245 |
| 201011133 | 2          |                    0.8226 |
| 201011133 | 3          |                    0.6667 |
| 201011133 | 4          |                    0.7845 |
| 201011133 | 5          |                    0.7513 |
| 201011133 | 6          |                    0.9272 |
| 201011133 | 7          |                    0.8518 |
| 201011133 | 8          |                    0.7874 |
| 201011133 | 9          |                    0.6983 |
| 201011134 | 0          |                    0.9228 |
| 201011134 | 1          |                    0.9041 |
| 201011134 | 2          |                    0.8232 |
| 201011134 | 3          |                    0.6504 |
| 201011134 | 4          |                    0.8001 |
| 201011134 | 5          |                    0.7658 |
| 201011134 | 6          |                    0.9060 |
| 201011134 | 7          |                    0.8585 |
| 201011134 | 8          |                    0.7287 |
| 201011134 | 9          |                    0.7130 |
| 201011141 | 0          |                    0.8856 |
| 201011141 | 1          |                    0.9438 |
| 201011141 | 2          |                    0.8216 |
| 201011141 | 3          |                    0.8156 |
| 201011141 | 4          |                    0.8022 |
| 201011141 | 5          |                    0.7152 |
| 201011141 | 6          |                    0.9002 |
| 201011141 | 7          |                    0.8500 |
| 201011141 | 8          |                    0.7593 |
| 201011141 | 9          |                    0.8455 |
| 201011161 | 0          |                    0.9177 |
| 201011161 | 1          |                    0.9930 |
| 201011161 | 2          |                    0.7416 |
| 201011161 | 3          |                    0.7601 |
| 201011161 | 4          |                    0.7387 |
| 201011161 | 5          |                    0.7244 |
| 201011161 | 6          |                    0.9363 |
| 201011161 | 7          |                    0.8648 |
| 201011161 | 8          |                    0.6481 |
| 201011161 | 9          |                    0.7955 |
| 201011162 | 0          |                    0.9334 |
| 201011162 | 1          |                    0.9214 |
| 201011162 | 2          |                    0.8431 |
| 201011162 | 3          |                    0.7860 |
| 201011162 | 4          |                    0.9237 |
| 201011162 | 5          |                    0.8327 |
| 201011162 | 6          |                    0.9356 |
| 201011162 | 7          |                    0.8374 |
| 201011162 | 8          |                    0.8130 |
| 201011162 | 9          |                    0.8156 |
| 201011231 | 0          |                    0.3213 |
| 201011231 | 1          |                    0.9803 |
| 201011231 | 2          |                    0.2590 |
| 201011231 | 3          |                    0.2754 |
| 201011231 | 4          |                    0.2426 |
| 201011231 | 5          |                    0.2951 |
| 201011231 | 6          |                    0.3475 |
| 201011231 | 7          |                    0.3770 |
| 201011231 | 8          |                    0.2164 |
| 201011231 | 9          |                    0.1902 |
| 201011232 | 0          |                    0.6984 |
| 201011232 | 1          |                    0.9115 |
| 201011232 | 2          |                    0.4557 |
| 201011232 | 3          |                    0.5246 |
| 201011232 | 4          |                    0.5311 |
| 201011232 | 5          |                    0.4525 |
| 201011232 | 6          |                    0.4459 |
| 201011232 | 7          |                    0.5148 |
| 201011232 | 8          |                    0.3049 |
| 201011232 | 9          |                    0.3803 |
+-----------+------------+---------------------------+

-- select character type with largest percentage of correctly classified samples per run
select * from (
select Total.ixRun, Total.sCharacter, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total,
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct
  where Total.ixRun = Correct.ixRun and Total.sCharacter = Correct.sCharacter order by ixRun, accuracy desc
) as Accuracies group by Accuracies.ixRun;

+-----------+------------+----------+
| ixRun     | sCharacter | accuracy |
+-----------+------------+----------+
|         1 | 0          |   0.9745 |
| 201011133 | 6          |   0.9272 |
| 201011134 | 0          |   0.9228 |
| 201011141 | 1          |   0.9438 |
| 201011161 | 1          |   0.9930 |
| 201011162 | 6          |   0.9356 |
| 201011231 | 1          |   0.9803 |
| 201011232 | 1          |   0.9115 |
+-----------+------------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 2 Which digit yielded the worst accuracy of detection by our tests?
-- (identical to QUESTION 1 query except order by is asc instead of desc)

select * from (
select Total.ixRun, Total.sCharacter, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total,
  (select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct
  where Total.ixRun = Correct.ixRun and Total.sCharacter = Correct.sCharacter order by ixRun, accuracy asc
) as Accuracies group by Accuracies.ixRun;

+-----------+------------+----------+
| ixRun     | sCharacter | accuracy |
+-----------+------------+----------+
|         1 | 4          |   0.9700 |
| 201011133 | 3          |   0.6667 |
| 201011134 | 3          |   0.6504 |
| 201011141 | 5          |   0.7152 |
| 201011161 | 8          |   0.6481 |
| 201011162 | 3          |   0.7860 |
| 201011231 | 9          |   0.1902 |
| 201011232 | 8          |   0.3049 |
+-----------+------------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 5 What is the accuracy of detection from collected data based on age group?

-- select ages for each sample with age metadata (only consider data from the scanned DataSet)
select Metadata.ixData, Metadata.sValue from Data, Metadata where Data.ixData = Metadata.ixData and Metadata.sKey = 'Age' and Data.ixDataSet = 2;

-- select the number of samples with particular age values
select count(*), convert(sValue,UNSIGNED INTEGER) as age from Data, Metadata where Data.ixData = Metadata.ixData and Metadata.sKey = 'Age' and Data.ixDataSet = 2 group by sValue order by age;

+----------+------+
| count(*) | age  |
+----------+------+
|       10 |    6 |
|      320 |    7 |
|      580 |    8 |
|      730 |    9 |
|      540 |   10 |
|       80 |   11 |
|       10 |   13 |
|      250 |   14 |
|       70 |   15 |
|       90 |   16 |
|      110 |   17 |
|       30 |   18 |
|       10 |   19 |
|       10 |   39 |
|       10 |   45 |
|      120 |   47 |
|       30 |   48 |
|       20 |   49 |
|       10 |   53 |
|       10 |   56 |
|       10 |   57 |
+----------+------+

-- select the number of samples with particular age values which were correctly classified
select ixRun, convert(sValue,UNSIGNED INTEGER) as age, count(*) from Data, Metadata, Result where Data.ixData = Metadata.ixData and Data.ixData = Result.ixData and Data.sCharacter = Result.sClassification and Metadata.sKey = 'Age' and Data.ixDataSet = 2 group by Result.ixRun, age;

select ixRun, convert(sValue,UNSIGNED INTEGER) as age from Data, Metadata, Result where Data.ixData = Metadata.ixData and Data.ixData = Result.ixData and Data.sCharacter = Result.sClassification and Metadata.sKey = 'Age' and Data.ixDataSet = 2;
select ixRun, sValue from Data, Metadata, Result where Data.ixData = Metadata.ixData and Data.ixData = Result.ixData and Data.sCharacter = Result.sClassification and Metadata.sKey = 'Age' and Data.ixDataSet = 2;

select Result.ixRun, count(*) from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun;

