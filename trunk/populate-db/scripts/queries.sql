-------------------------------------------------------------------------------------------

-- QUESTION 1 Which digit yielded the best accuracy of detection by our tests?

-- select number of correctly classified samples (per run and character type)
(select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData and Result.sClassification = Data.sCharacter group by Result.ixRun, Data.sCharacter) as Correct;

-- select total number of samples (per run and character type)
(select Result.ixRun, Data.sCharacter, count(*) as count from Result, Data where Result.ixData = Data.ixData group by Result.ixRun, Data.sCharacter) as Total;

-- select percentage of correctly classified samples (per run and character type)
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

-- select character type with largest percentage of correctly classified samples (per run)
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

-- select the number of samples with particular age values (per run)
select Result.ixRun, AgeData.sValue, count(*) from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun, AgeData.sValue;

-- select the number of samples with particular age values which were correctly classified (per run)
select Result.ixRun, AgeData.sValue, count(*) from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData and AgeData.sCharacter = Result.sClassification group by Result.ixRun, AgeData.sValue;

-- select percentage of correctly classified samples (per run and age value)
select Total.ixRun, Total.sValue as age, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun, AgeData.sValue) as Total,
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData and AgeData.sCharacter = Result.sClassification group by Result.ixRun, AgeData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue order by Total.ixRun, accuracy desc;

+-----------+------+----------+
| ixRun     | age  | accuracy |
+-----------+------+----------+
| 201011231 | 6    |   0.8000 |
| 201011231 | 7    |   0.6813 |
| 201011231 | 9    |   0.4932 |
| 201011231 | 8    |   0.4379 |
| 201011231 | 13   |   0.3000 |
| 201011231 | 10   |   0.2278 |
| 201011231 | 16   |   0.1444 |
| 201011231 | 47   |   0.1333 |
| 201011231 | 18   |   0.1333 |
| 201011231 | 14   |   0.1200 |
| 201011231 | 15   |   0.1143 |
| 201011231 | 11   |   0.1125 |
| 201011231 | 17   |   0.1091 |
| 201011231 | 56   |   0.1000 |
| 201011231 | 49   |   0.1000 |
| 201011231 | 39   |   0.1000 |
| 201011231 | 57   |   0.1000 |
| 201011231 | 53   |   0.1000 |
| 201011231 | 48   |   0.1000 |
| 201011231 | 45   |   0.1000 |
| 201011231 | 19   |   0.1000 |
| 201011232 | 6    |   1.0000 |
| 201011232 | 56   |   1.0000 |
| 201011232 | 7    |   0.8969 |
| 201011232 | 9    |   0.6479 |
| 201011232 | 8    |   0.6103 |
| 201011232 | 13   |   0.6000 |
| 201011232 | 39   |   0.4000 |
| 201011232 | 45   |   0.4000 |
| 201011232 | 10   |   0.3926 |
| 201011232 | 47   |   0.3917 |
| 201011232 | 15   |   0.3429 |
| 201011232 | 11   |   0.3250 |
| 201011232 | 14   |   0.2800 |
| 201011232 | 48   |   0.2667 |
| 201011232 | 18   |   0.2333 |
| 201011232 | 17   |   0.2182 |
| 201011232 | 16   |   0.2111 |
| 201011232 | 57   |   0.2000 |
| 201011232 | 19   |   0.2000 |
| 201011232 | 49   |   0.1000 |
| 201011232 | 53   |   0.1000 |
+-----------+------+----------+

-- select percentage of correctly classified samples (per run and age value)
-- restrict results to those age groups where we have larger than 100 data samples
select Total.ixRun, Total.sValue as age, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData group by Result.ixRun, AgeData.sValue) as Total,
  (select Result.ixRun, AgeData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Age') as AgeData where AgeData.ixData = Result.ixData and AgeData.sCharacter = Result.sClassification group by Result.ixRun, AgeData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue and Total.count > 100 order by Total.ixRun, accuracy desc;

+-----------+------+----------+
| ixRun     | age  | accuracy |
+-----------+------+----------+
| 201011231 | 7    |   0.6813 |
| 201011231 | 9    |   0.4932 |
| 201011231 | 8    |   0.4379 |
| 201011231 | 10   |   0.2278 |
| 201011231 | 47   |   0.1333 |
| 201011231 | 14   |   0.1200 |
| 201011231 | 17   |   0.1091 |
| 201011232 | 7    |   0.8969 |
| 201011232 | 9    |   0.6479 |
| 201011232 | 8    |   0.6103 |
| 201011232 | 10   |   0.3926 |
| 201011232 | 47   |   0.3917 |
| 201011232 | 14   |   0.2800 |
| 201011232 | 17   |   0.2182 |
+-----------+------+----------+


-------------------------------------------------------------------------------------------

-- QUESTION 6 What is the accuracy of detection from collected data based on handedness?

select Total.ixRun, Total.sValue as handedness, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, HandData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Handedness') as HandData where HandData.ixData = Result.ixData group by Result.ixRun, HandData.sValue) as Total,
  (select Result.ixRun, HandData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Handedness') as HandData where HandData.ixData = Result.ixData and HandData.sCharacter = Result.sClassification group by Result.ixRun, HandData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue order by Total.ixRun, accuracy desc;

+-----------+------------+----------+
| ixRun     | handedness | accuracy |
+-----------+------------+----------+
| 201011231 | L          |   0.3778 |
| 201011231 | R          |   0.3487 |
| 201011231 | B          |   0.1000 |
| 201011232 | L          |   0.5556 |
| 201011232 | R          |   0.5195 |
| 201011232 | B          |   0.3000 |
+-----------+------------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 7 Is there a difference in accuracy of the algorithms on data samples visually classified as "good" or "bad" by a team member?

select Total.ixRun, Total.sValue as quality, Correct.count/Total.count as accuracy from 
  (select Result.ixRun, QualityData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Quality') as QualityData where QualityData.ixData = Result.ixData group by Result.ixRun, QualityData.sValue) as Total,
  (select Result.ixRun, QualityData.sValue, count(*) as count from Result, (select Metadata.ixData, Metadata.sValue, Data.sCharacter from Metadata, Data where Metadata.ixData = Data.ixData and Metadata.sKey = 'Quality') as QualityData where QualityData.ixData = Result.ixData and QualityData.sCharacter = Result.sClassification group by Result.ixRun, QualityData.sValue) as Correct
where Total.ixRun = Correct.ixRun and Total.sValue = Correct.sValue order by Total.ixRun, accuracy desc;

+-----------+---------+----------+
| ixRun     | quality | accuracy |
+-----------+---------+----------+
| 201011231 | B       |   0.4932 |
| 201011231 | G       |   0.3264 |
| 201011232 | B       |   0.6159 |
| 201011232 | G       |   0.5061 |
+-----------+---------+----------+

-------------------------------------------------------------------------------------------

-- QUESTION 9 Are there any common miscategorizations for sets of digits (such as classifying 6's and 9's as 0's)?

-- for each run and combination of truth and classified character, list the total number of samples
--
-- for example:
-- +-----------+-------+-------+-------+
-- | ixRun     | truth | class | total |
-- +-----------+-------+-------+-------+
-- |         1 | 2     | 5     |    34 |
-- +-----------+-------+-------+-------+
-- indicates that in run 1 there were 34 2s classified as 5s

select Run.ixRun, Data.sCharacter as truth, Result.sClassification as class, count(*) as total from Data, Result, Run where Data.ixData = Result.ixData and Result.ixRun = Run.ixRun and Data.sCharacter != Result.sClassification group by Run.ixRun, Data.sCharacter, Result.sClassification order by Run.ixRun, total desc;

+-----------+-------+-------+-------+
| ixRun     | truth | class | total |
+-----------+-------+-------+-------+
|         1 | 2     | 5     |    34 |
|         1 | 8     | 9     |    33 |
|         1 | 4     | 2     |    33 |
|         1 | 3     | 8     |    33 |
|         1 | 9     | 3     |    32 |
|         1 | 7     | 5     |    29 |
|         1 | 1     | 3     |    27 |
|         1 | 5     | 3     |    26 |
|         1 | 8     | 2     |    26 |
|         1 | 6     | 9     |    26 |
|         1 | 1     | 7     |    26 |
|         1 | 1     | 9     |    25 |
|         1 | 6     | 8     |    25 |
|         1 | 8     | 4     |    25 |
|         1 | 1     | 4     |    25 |
|         1 | 9     | 8     |    25 |
|         1 | 1     | 0     |    25 |
|         1 | 3     | 4     |    25 |
|         1 | 5     | 6     |    24 |
|         1 | 4     | 3     |    24 |
|         1 | 0     | 6     |    24 |
|         1 | 6     | 3     |    24 |
|         1 | 1     | 6     |    24 |
|         1 | 6     | 4     |    24 |
|         1 | 4     | 9     |    24 |
|         1 | 9     | 5     |    24 |
|         1 | 7     | 9     |    24 |
|         1 | 4     | 8     |    24 |
|         1 | 2     | 7     |    23 |
|         1 | 8     | 1     |    23 |
|         1 | 1     | 8     |    23 |
|         1 | 0     | 7     |    23 |
|         1 | 2     | 8     |    23 |
|         1 | 5     | 9     |    23 |
|         1 | 2     | 0     |    23 |
|         1 | 7     | 2     |    22 |
|         1 | 3     | 1     |    22 |
|         1 | 7     | 0     |    22 |
|         1 | 6     | 1     |    22 |
|         1 | 4     | 5     |    22 |
|         1 | 4     | 6     |    22 |
|         1 | 3     | 9     |    22 |
|         1 | 0     | 1     |    21 |
|         1 | 9     | 6     |    21 |
|         1 | 9     | 4     |    21 |
|         1 | 8     | 7     |    21 |
|         1 | 3     | 2     |    21 |
|         1 | 9     | 2     |    21 |
|         1 | 5     | 7     |    21 |
|         1 | 4     | 1     |    21 |
|         1 | 7     | 3     |    20 |
|         1 | 2     | 9     |    20 |
|         1 | 1     | 2     |    20 |
|         1 | 2     | 6     |    20 |
|         1 | 0     | 4     |    20 |
|         1 | 3     | 5     |    20 |
|         1 | 6     | 7     |    19 |
|         1 | 0     | 2     |    19 |
|         1 | 9     | 1     |    19 |
|         1 | 5     | 4     |    19 |
|         1 | 8     | 5     |    19 |
|         1 | 5     | 0     |    19 |
|         1 | 9     | 7     |    19 |
|         1 | 0     | 5     |    19 |
|         1 | 1     | 5     |    18 |
|         1 | 7     | 8     |    18 |
|         1 | 7     | 1     |    18 |
|         1 | 2     | 1     |    18 |
|         1 | 3     | 6     |    18 |
|         1 | 8     | 0     |    18 |
|         1 | 4     | 0     |    18 |
|         1 | 7     | 6     |    17 |
|         1 | 4     | 7     |    17 |
|         1 | 0     | 3     |    17 |
|         1 | 5     | 2     |    17 |
|         1 | 6     | 2     |    17 |
|         1 | 0     | 8     |    17 |
|         1 | 3     | 0     |    16 |
|         1 | 8     | 3     |    16 |
|         1 | 0     | 9     |    16 |
|         1 | 7     | 4     |    16 |
|         1 | 5     | 8     |    15 |
|         1 | 6     | 5     |    15 |
|         1 | 9     | 0     |    15 |
|         1 | 2     | 3     |    14 |
|         1 | 6     | 0     |    14 |
|         1 | 3     | 7     |    13 |
|         1 | 8     | 6     |    12 |
|         1 | 5     | 1     |    12 |
|         1 | 2     | 4     |     9 |
| 201011133 | 1     | 2     |  1092 |
| 201011133 | 9     | 4     |   802 |
| 201011133 | 4     | 9     |   779 |
| 201011133 | 3     | 2     |   761 |
| 201011133 | 9     | 7     |   555 |
| 201011133 | 3     | 5     |   552 |
| 201011133 | 8     | 5     |   519 |
| 201011133 | 3     | 8     |   496 |
| 201011133 | 2     | 6     |   388 |
| 201011133 | 7     | 9     |   367 |
| 201011133 | 5     | 3     |   344 |
| 201011133 | 9     | 8     |   335 |
| 201011133 | 4     | 8     |   299 |
| 201011133 | 0     | 8     |   286 |
| 201011133 | 7     | 4     |   283 |
| 201011133 | 2     | 8     |   277 |
| 201011133 | 5     | 4     |   266 |
| 201011133 | 5     | 8     |   253 |
| 201011133 | 8     | 2     |   229 |
| 201011133 | 5     | 6     |   226 |
| 201011133 | 8     | 3     |   195 |
| 201011133 | 4     | 6     |   186 |
| 201011133 | 2     | 4     |   186 |
| 201011133 | 5     | 0     |   175 |
| 201011133 | 9     | 5     |   164 |
| 201011133 | 3     | 7     |   161 |
| 201011133 | 1     | 8     |   159 |
| 201011133 | 8     | 9     |   151 |
| 201011133 | 0     | 5     |   151 |
| 201011133 | 0     | 6     |   142 |
| 201011133 | 7     | 2     |   137 |
| 201011133 | 5     | 2     |   136 |
| 201011133 | 3     | 9     |   130 |
| 201011133 | 6     | 1     |   128 |
| 201011133 | 8     | 1     |   125 |
| 201011133 | 6     | 5     |   124 |
| 201011133 | 2     | 3     |   111 |
| 201011133 | 2     | 7     |   109 |
| 201011133 | 6     | 8     |   106 |
| 201011133 | 3     | 1     |    99 |
| 201011133 | 2     | 0     |    96 |
| 201011133 | 7     | 8     |    85 |
| 201011133 | 5     | 9     |    81 |
| 201011133 | 9     | 1     |    79 |
| 201011133 | 7     | 1     |    79 |
| 201011133 | 8     | 6     |    78 |
| 201011133 | 4     | 7     |    69 |
| 201011133 | 9     | 0     |    68 |
| 201011133 | 8     | 4     |    68 |
| 201011133 | 0     | 2     |    65 |
| 201011133 | 6     | 2     |    63 |
| 201011133 | 7     | 0     |    61 |
| 201011133 | 3     | 0     |    56 |
| 201011133 | 4     | 5     |    55 |
| 201011133 | 3     | 6     |    54 |
| 201011133 | 9     | 3     |    51 |
| 201011133 | 5     | 1     |    47 |
| 201011133 | 7     | 5     |    42 |
| 201011133 | 6     | 0     |    39 |
| 201011133 | 8     | 0     |    39 |
| 201011133 | 3     | 4     |    38 |
| 201011133 | 4     | 1     |    31 |
| 201011133 | 1     | 3     |    31 |
| 201011133 | 2     | 1     |    30 |
| 201011133 | 6     | 4     |    29 |
| 201011133 | 8     | 7     |    26 |
| 201011133 | 1     | 6     |    26 |
| 201011133 | 0     | 3     |    22 |
| 201011133 | 1     | 5     |    19 |
| 201011133 | 2     | 5     |    18 |
| 201011133 | 0     | 4     |    18 |
| 201011133 | 5     | 7     |    17 |
| 201011133 | 1     | 4     |    17 |
| 201011133 | 4     | 0     |    15 |
| 201011133 | 4     | 2     |    14 |
| 201011133 | 1     | 7     |    11 |
| 201011133 | 9     | 2     |    10 |
| 201011133 | 1     | 9     |    10 |
| 201011133 | 7     | 6     |     7 |
| 201011133 | 2     | 9     |     7 |
| 201011133 | 9     | 6     |     5 |
| 201011133 | 0     | 9     |     5 |
| 201011133 | 7     | 3     |     5 |
| 201011133 | 0     | 7     |     3 |
| 201011133 | 6     | 3     |     3 |
| 201011133 | 4     | 3     |     1 |
| 201011133 | 6     | 9     |     1 |
| 201011134 | 8     | 5     |   874 |
| 201011134 | 9     | 7     |   814 |
| 201011134 | 3     | 2     |   784 |
| 201011134 | 4     | 9     |   704 |
| 201011134 | 3     | 8     |   607 |
| 201011134 | 9     | 4     |   575 |
| 201011134 | 3     | 5     |   535 |
| 201011134 | 7     | 9     |   482 |
| 201011134 | 1     | 2     |   399 |
| 201011134 | 2     | 6     |   383 |
| 201011134 | 5     | 3     |   353 |
| 201011134 | 2     | 8     |   251 |
| 201011134 | 5     | 4     |   240 |
| 201011134 | 1     | 8     |   233 |
| 201011134 | 6     | 1     |   227 |
| 201011134 | 4     | 8     |   213 |
| 201011134 | 5     | 6     |   201 |
| 201011134 | 9     | 8     |   198 |
| 201011134 | 7     | 4     |   197 |
| 201011134 | 8     | 1     |   195 |
| 201011134 | 5     | 8     |   190 |
| 201011134 | 6     | 5     |   188 |
| 201011134 | 8     | 3     |   181 |
| 201011134 | 0     | 8     |   174 |
| 201011134 | 2     | 4     |   168 |
| 201011134 | 0     | 5     |   164 |
| 201011134 | 8     | 2     |   163 |
| 201011134 | 8     | 9     |   159 |
| 201011134 | 4     | 6     |   143 |
| 201011134 | 2     | 0     |   140 |
| 201011134 | 3     | 9     |   138 |
| 201011134 | 9     | 5     |   132 |
| 201011134 | 5     | 0     |   126 |
| 201011134 | 3     | 1     |   120 |
| 201011134 | 4     | 5     |   112 |
| 201011134 | 7     | 1     |   110 |
| 201011134 | 0     | 6     |   103 |
| 201011134 | 9     | 1     |    99 |
| 201011134 | 3     | 7     |    99 |
| 201011134 | 5     | 2     |    93 |
| 201011134 | 3     | 0     |    92 |
| 201011134 | 5     | 9     |    89 |
| 201011134 | 5     | 1     |    86 |
| 201011134 | 8     | 6     |    85 |
| 201011134 | 7     | 8     |    82 |
| 201011134 | 2     | 7     |    82 |
| 201011134 | 2     | 3     |    79 |
| 201011134 | 5     | 7     |    77 |
| 201011134 | 6     | 8     |    77 |
| 201011134 | 7     | 2     |    75 |
| 201011134 | 9     | 0     |    71 |
| 201011134 | 4     | 7     |    69 |
| 201011134 | 8     | 4     |    69 |
| 201011134 | 6     | 0     |    67 |
| 201011134 | 9     | 3     |    59 |
| 201011134 | 3     | 6     |    56 |
| 201011134 | 6     | 2     |    55 |
| 201011134 | 8     | 0     |    54 |
| 201011134 | 4     | 1     |    51 |
| 201011134 | 2     | 1     |    47 |
| 201011134 | 2     | 5     |    45 |
| 201011134 | 8     | 7     |    44 |
| 201011134 | 0     | 2     |    39 |
| 201011134 | 7     | 0     |    33 |
| 201011134 | 1     | 3     |    32 |
| 201011134 | 1     | 5     |    31 |
| 201011134 | 3     | 4     |    30 |
| 201011134 | 4     | 0     |    25 |
| 201011134 | 4     | 2     |    25 |
| 201011134 | 2     | 9     |    23 |
| 201011134 | 1     | 6     |    19 |
| 201011134 | 6     | 4     |    19 |
| 201011134 | 0     | 3     |    16 |
| 201011134 | 7     | 3     |    15 |
| 201011134 | 1     | 9     |    13 |
| 201011134 | 7     | 6     |    12 |
| 201011134 | 7     | 5     |    12 |
| 201011134 | 0     | 4     |    12 |
| 201011134 | 9     | 2     |    11 |
| 201011134 | 1     | 7     |    10 |
| 201011134 | 1     | 4     |     9 |
| 201011134 | 9     | 6     |     9 |
| 201011134 | 0     | 9     |     9 |
| 201011134 | 0     | 7     |     6 |
| 201011134 | 6     | 3     |     3 |
| 201011134 | 0     | 1     |     2 |
| 201011134 | 4     | 3     |     2 |
| 201011134 | 6     | 9     |     1 |
| 201011141 | 4     | 9     |   856 |
| 201011141 | 5     | 3     |   778 |
| 201011141 | 7     | 9     |   469 |
| 201011141 | 9     | 4     |   462 |
| 201011141 | 8     | 3     |   376 |
| 201011141 | 2     | 8     |   313 |
| 201011141 | 0     | 5     |   297 |
| 201011141 | 8     | 9     |   276 |
| 201011141 | 8     | 1     |   271 |
| 201011141 | 8     | 5     |   260 |
| 201011141 | 3     | 2     |   252 |
| 201011141 | 3     | 8     |   247 |
| 201011141 | 2     | 3     |   225 |
| 201011141 | 3     | 5     |   219 |
| 201011141 | 2     | 6     |   211 |
| 201011141 | 6     | 5     |   205 |
| 201011141 | 1     | 5     |   190 |
| 201011141 | 3     | 9     |   179 |
| 201011141 | 7     | 4     |   178 |
| 201011141 | 9     | 7     |   167 |
| 201011141 | 5     | 9     |   165 |
| 201011141 | 6     | 2     |   164 |
| 201011141 | 5     | 4     |   161 |
| 201011141 | 7     | 1     |   160 |
| 201011141 | 0     | 6     |   152 |
| 201011141 | 5     | 0     |   149 |
| 201011141 | 0     | 8     |   144 |
| 201011141 | 5     | 8     |   132 |
| 201011141 | 4     | 6     |   116 |
| 201011141 | 5     | 6     |   114 |
| 201011141 | 2     | 4     |   111 |
| 201011141 | 8     | 2     |   105 |
| 201011141 | 3     | 1     |   104 |
| 201011141 | 2     | 1     |   101 |
| 201011141 | 6     | 1     |    95 |
| 201011141 | 9     | 3     |    88 |
| 201011141 | 8     | 4     |    87 |
| 201011141 | 1     | 8     |    85 |
| 201011141 | 4     | 8     |    77 |
| 201011141 | 2     | 7     |    77 |
| 201011141 | 9     | 8     |    76 |
| 201011141 | 7     | 2     |    76 |
| 201011141 | 3     | 7     |    76 |
| 201011141 | 7     | 8     |    75 |
| 201011141 | 5     | 2     |    73 |
| 201011141 | 9     | 1     |    73 |
| 201011141 | 6     | 4     |    64 |
| 201011141 | 3     | 6     |    63 |
| 201011141 | 4     | 5     |    63 |
| 201011141 | 0     | 2     |    62 |
| 201011141 | 8     | 0     |    59 |
| 201011141 | 6     | 0     |    58 |
| 201011141 | 1     | 2     |    54 |
| 201011141 | 4     | 2     |    48 |
| 201011141 | 8     | 6     |    47 |
| 201011141 | 9     | 0     |    47 |
| 201011141 | 2     | 0     |    46 |
| 201011141 | 4     | 1     |    43 |
| 201011141 | 9     | 5     |    40 |
| 201011141 | 3     | 0     |    38 |
| 201011141 | 6     | 8     |    36 |
| 201011141 | 5     | 1     |    32 |
| 201011141 | 1     | 3     |    31 |
| 201011141 | 2     | 9     |    31 |
| 201011141 | 1     | 6     |    29 |
| 201011141 | 3     | 4     |    28 |
| 201011141 | 9     | 2     |    27 |
| 201011141 | 2     | 5     |    25 |
| 201011141 | 5     | 7     |    23 |
| 201011141 | 0     | 3     |    21 |
| 201011141 | 0     | 9     |    20 |
| 201011141 | 7     | 0     |    19 |
| 201011141 | 0     | 4     |    19 |
| 201011141 | 4     | 7     |    19 |
| 201011141 | 8     | 7     |    17 |
| 201011141 | 7     | 3     |    15 |
| 201011141 | 1     | 9     |    12 |
| 201011141 | 7     | 5     |    10 |
| 201011141 | 4     | 0     |     9 |
| 201011141 | 1     | 7     |     5 |
| 201011141 | 6     | 3     |     3 |
| 201011141 | 0     | 7     |     3 |
| 201011141 | 1     | 4     |     3 |
| 201011141 | 0     | 1     |     3 |
| 201011141 | 9     | 6     |     2 |
| 201011141 | 7     | 6     |     2 |
| 201011141 | 6     | 9     |     1 |
| 201011161 | 4     | 9     |  1141 |
| 201011161 | 8     | 1     |   725 |
| 201011161 | 9     | 7     |   667 |
| 201011161 | 5     | 1     |   647 |
| 201011161 | 2     | 1     |   522 |
| 201011161 | 8     | 5     |   509 |
| 201011161 | 7     | 9     |   465 |
| 201011161 | 8     | 9     |   440 |
| 201011161 | 2     | 7     |   435 |
| 201011161 | 3     | 5     |   388 |
| 201011161 | 4     | 1     |   382 |
| 201011161 | 3     | 1     |   374 |
| 201011161 | 5     | 3     |   327 |
| 201011161 | 9     | 4     |   322 |
| 201011161 | 5     | 6     |   322 |
| 201011161 | 7     | 1     |   310 |
| 201011161 | 3     | 8     |   302 |
| 201011161 | 8     | 3     |   285 |
| 201011161 | 6     | 1     |   241 |
| 201011161 | 0     | 6     |   223 |
| 201011161 | 2     | 4     |   216 |
| 201011161 | 9     | 1     |   212 |
| 201011161 | 3     | 7     |   202 |
| 201011161 | 2     | 8     |   184 |
| 201011161 | 3     | 9     |   182 |
| 201011161 | 5     | 9     |   180 |
| 201011161 | 3     | 2     |   167 |
| 201011161 | 0     | 5     |   167 |
| 201011161 | 2     | 0     |   147 |
| 201011161 | 4     | 7     |   146 |
| 201011161 | 8     | 7     |   132 |
| 201011161 | 2     | 3     |   124 |
| 201011161 | 5     | 0     |   122 |
| 201011161 | 2     | 9     |   104 |
| 201011161 | 7     | 4     |   101 |
| 201011161 | 8     | 4     |   101 |
| 201011161 | 6     | 0     |    87 |
| 201011161 | 8     | 0     |    86 |
| 201011161 | 8     | 6     |    82 |
| 201011161 | 0     | 8     |    70 |
| 201011161 | 5     | 4     |    69 |
| 201011161 | 4     | 6     |    63 |
| 201011161 | 9     | 0     |    61 |
| 201011161 | 6     | 4     |    60 |
| 201011161 | 9     | 3     |    59 |
| 201011161 | 2     | 6     |    59 |
| 201011161 | 7     | 5     |    58 |
| 201011161 | 5     | 8     |    49 |
| 201011161 | 3     | 0     |    43 |
| 201011161 | 8     | 2     |    42 |
| 201011161 | 9     | 5     |    38 |
| 201011161 | 3     | 6     |    37 |
| 201011161 | 6     | 5     |    30 |
| 201011161 | 9     | 8     |    28 |
| 201011161 | 0     | 1     |    27 |
| 201011161 | 9     | 2     |    26 |
| 201011161 | 0     | 4     |    24 |
| 201011161 | 7     | 2     |    21 |
| 201011161 | 4     | 5     |    19 |
| 201011161 | 0     | 2     |    19 |
| 201011161 | 0     | 9     |    19 |
| 201011161 | 3     | 4     |    18 |
| 201011161 | 5     | 7     |    17 |
| 201011161 | 7     | 0     |    16 |
| 201011161 | 1     | 7     |    16 |
| 201011161 | 2     | 5     |    15 |
| 201011161 | 1     | 2     |    14 |
| 201011161 | 4     | 0     |    14 |
| 201011161 | 0     | 7     |    13 |
| 201011161 | 4     | 8     |    12 |
| 201011161 | 6     | 8     |    11 |
| 201011161 | 9     | 6     |    10 |
| 201011161 | 7     | 8     |     8 |
| 201011161 | 1     | 3     |     7 |
| 201011161 | 5     | 2     |     7 |
| 201011161 | 1     | 8     |     7 |
| 201011161 | 0     | 3     |     6 |
| 201011161 | 4     | 2     |     5 |
| 201011161 | 6     | 2     |     5 |
| 201011161 | 1     | 4     |     4 |
| 201011161 | 1     | 6     |     4 |
| 201011161 | 7     | 3     |     4 |
| 201011161 | 7     | 6     |     3 |
| 201011161 | 6     | 7     |     3 |
| 201011161 | 1     | 5     |     1 |
| 201011161 | 4     | 3     |     1 |
| 201011161 | 1     | 0     |     1 |
| 201011161 | 1     | 9     |     1 |
| 201011161 | 6     | 9     |     1 |
| 201011162 | 9     | 4     |   511 |
| 201011162 | 7     | 9     |   492 |
| 201011162 | 1     | 2     |   454 |
| 201011162 | 3     | 5     |   415 |
| 201011162 | 3     | 8     |   385 |
| 201011162 | 3     | 2     |   336 |
| 201011162 | 4     | 9     |   316 |
| 201011162 | 2     | 4     |   308 |
| 201011162 | 8     | 5     |   306 |
| 201011162 | 9     | 7     |   270 |
| 201011162 | 5     | 3     |   239 |
| 201011162 | 8     | 1     |   228 |
| 201011162 | 5     | 6     |   219 |
| 201011162 | 8     | 9     |   192 |
| 201011162 | 8     | 2     |   186 |
| 201011162 | 7     | 4     |   169 |
| 201011162 | 0     | 6     |   168 |
| 201011162 | 7     | 2     |   164 |
| 201011162 | 3     | 9     |   159 |
| 201011162 | 8     | 3     |   154 |
| 201011162 | 2     | 8     |   152 |
| 201011162 | 7     | 5     |   144 |
| 201011162 | 2     | 7     |   143 |
| 201011162 | 5     | 4     |   139 |
| 201011162 | 6     | 4     |   136 |
| 201011162 | 9     | 2     |   134 |
| 201011162 | 5     | 1     |   126 |
| 201011162 | 2     | 6     |   124 |
| 201011162 | 2     | 3     |   120 |
| 201011162 | 7     | 0     |   115 |
| 201011162 | 0     | 5     |   109 |
| 201011162 | 5     | 8     |   103 |
| 201011162 | 3     | 7     |    99 |
| 201011162 | 5     | 0     |    97 |
| 201011162 | 9     | 0     |    96 |
| 201011162 | 9     | 8     |    92 |
| 201011162 | 8     | 4     |    83 |
| 201011162 | 6     | 5     |    81 |
| 201011162 | 2     | 1     |    80 |
| 201011162 | 2     | 0     |    77 |
| 201011162 | 0     | 8     |    77 |
| 201011162 | 9     | 3     |    74 |
| 201011162 | 8     | 6     |    71 |
| 201011162 | 7     | 1     |    69 |
| 201011162 | 6     | 2     |    68 |
| 201011162 | 9     | 5     |    65 |
| 201011162 | 5     | 9     |    63 |
| 201011162 | 5     | 2     |    60 |
| 201011162 | 2     | 9     |    59 |
| 201011162 | 1     | 8     |    59 |
| 201011162 | 6     | 0     |    55 |
| 201011162 | 3     | 1     |    54 |
| 201011162 | 6     | 1     |    52 |
| 201011162 | 4     | 6     |    49 |
| 201011162 | 0     | 4     |    49 |
| 201011162 | 6     | 8     |    46 |
| 201011162 | 1     | 5     |    42 |
| 201011162 | 0     | 2     |    42 |
| 201011162 | 4     | 1     |    41 |
| 201011162 | 9     | 1     |    39 |
| 201011162 | 3     | 0     |    35 |
| 201011162 | 2     | 5     |    34 |
| 201011162 | 8     | 7     |    32 |
| 201011162 | 3     | 6     |    30 |
| 201011162 | 4     | 7     |    27 |
| 201011162 | 4     | 0     |    25 |
| 201011162 | 4     | 2     |    24 |
| 201011162 | 8     | 0     |    24 |
| 201011162 | 4     | 8     |    23 |
| 201011162 | 1     | 3     |    21 |
| 201011162 | 7     | 3     |    19 |
| 201011162 | 1     | 7     |    16 |
| 201011162 | 4     | 5     |    16 |
| 201011162 | 3     | 4     |    15 |
| 201011162 | 1     | 9     |    12 |
| 201011162 | 1     | 4     |    11 |
| 201011162 | 5     | 7     |    10 |
| 201011162 | 7     | 6     |    10 |
| 201011162 | 0     | 3     |     7 |
| 201011162 | 6     | 3     |     5 |
| 201011162 | 0     | 9     |     4 |
| 201011162 | 7     | 8     |     4 |
| 201011162 | 0     | 7     |     3 |
| 201011162 | 1     | 6     |     3 |
| 201011162 | 9     | 6     |     2 |
| 201011162 | 1     | 0     |     1 |
| 201011162 | 0     | 1     |     1 |
| 201011231 | 9     | 1     |   221 |
| 201011231 | 2     | 1     |   212 |
| 201011231 | 4     | 1     |   206 |
| 201011231 | 3     | 1     |   203 |
| 201011231 | 6     | 1     |   193 |
| 201011231 | 7     | 1     |   185 |
| 201011231 | 8     | 1     |   176 |
| 201011231 | 0     | 1     |   173 |
| 201011231 | 5     | 1     |   169 |
| 201011231 | 0     | 6     |    22 |
| 201011231 | 8     | 3     |    21 |
| 201011231 | 4     | 9     |    20 |
| 201011231 | 9     | 4     |    14 |
| 201011231 | 5     | 6     |    13 |
| 201011231 | 8     | 5     |    12 |
| 201011231 | 5     | 3     |    11 |
| 201011231 | 8     | 7     |    11 |
| 201011231 | 3     | 7     |    10 |
| 201011231 | 5     | 9     |    10 |
| 201011231 | 8     | 6     |     8 |
| 201011231 | 8     | 9     |     7 |
| 201011231 | 2     | 7     |     6 |
| 201011231 | 9     | 7     |     6 |
| 201011231 | 3     | 5     |     5 |
| 201011231 | 2     | 3     |     5 |
| 201011231 | 5     | 4     |     5 |
| 201011231 | 7     | 8     |     3 |
| 201011231 | 5     | 8     |     3 |
| 201011231 | 3     | 9     |     3 |
| 201011231 | 0     | 5     |     3 |
| 201011231 | 5     | 0     |     3 |
| 201011231 | 8     | 4     |     3 |
| 201011231 | 6     | 3     |     3 |
| 201011231 | 4     | 6     |     3 |
| 201011231 | 1     | 2     |     2 |
| 201011231 | 9     | 3     |     2 |
| 201011231 | 9     | 8     |     2 |
| 201011231 | 2     | 8     |     2 |
| 201011231 | 0     | 2     |     2 |
| 201011231 | 0     | 3     |     2 |
| 201011231 | 0     | 7     |     2 |
| 201011231 | 0     | 8     |     2 |
| 201011231 | 9     | 6     |     2 |
| 201011231 | 4     | 7     |     2 |
| 201011231 | 1     | 4     |     1 |
| 201011231 | 7     | 3     |     1 |
| 201011231 | 1     | 8     |     1 |
| 201011231 | 7     | 2     |     1 |
| 201011231 | 6     | 4     |     1 |
| 201011231 | 5     | 7     |     1 |
| 201011231 | 6     | 5     |     1 |
| 201011231 | 2     | 9     |     1 |
| 201011231 | 6     | 0     |     1 |
| 201011231 | 1     | 6     |     1 |
| 201011231 | 8     | 0     |     1 |
| 201011231 | 1     | 9     |     1 |
| 201011231 | 0     | 9     |     1 |
| 201011232 | 6     | 1     |   129 |
| 201011232 | 9     | 1     |   129 |
| 201011232 | 3     | 1     |   127 |
| 201011232 | 7     | 1     |   123 |
| 201011232 | 2     | 1     |   116 |
| 201011232 | 8     | 1     |    97 |
| 201011232 | 4     | 1     |    96 |
| 201011232 | 5     | 1     |    66 |
| 201011232 | 0     | 1     |    48 |
| 201011232 | 5     | 3     |    45 |
| 201011232 | 8     | 3     |    29 |
| 201011232 | 2     | 3     |    26 |
| 201011232 | 8     | 5     |    26 |
| 201011232 | 9     | 4     |    23 |
| 201011232 | 0     | 3     |    19 |
| 201011232 | 5     | 0     |    19 |
| 201011232 | 4     | 9     |    17 |
| 201011232 | 8     | 2     |    16 |
| 201011232 | 8     | 9     |    15 |
| 201011232 | 5     | 6     |    15 |
| 201011232 | 4     | 6     |    14 |
| 201011232 | 6     | 0     |    13 |
| 201011232 | 2     | 6     |    12 |
| 201011232 | 9     | 3     |    12 |
| 201011232 | 8     | 6     |    11 |
| 201011232 | 5     | 9     |    10 |
| 201011232 | 1     | 5     |    10 |
| 201011232 | 0     | 5     |     9 |
| 201011232 | 9     | 7     |     9 |
| 201011232 | 7     | 0     |     8 |
| 201011232 | 7     | 3     |     8 |
| 201011232 | 5     | 4     |     8 |
| 201011232 | 3     | 2     |     7 |
| 201011232 | 0     | 9     |     7 |
| 201011232 | 8     | 7     |     7 |
| 201011232 | 6     | 5     |     7 |
| 201011232 | 9     | 0     |     7 |
| 201011232 | 0     | 6     |     6 |
| 201011232 | 6     | 2     |     6 |
| 201011232 | 6     | 3     |     6 |
| 201011232 | 1     | 3     |     6 |
| 201011232 | 4     | 3     |     6 |
| 201011232 | 8     | 4     |     6 |
| 201011232 | 8     | 0     |     5 |
| 201011232 | 2     | 5     |     5 |
| 201011232 | 6     | 4     |     5 |
| 201011232 | 4     | 2     |     4 |
| 201011232 | 1     | 6     |     4 |
| 201011232 | 3     | 7     |     4 |
| 201011232 | 7     | 2     |     4 |
| 201011232 | 2     | 8     |     3 |
| 201011232 | 7     | 8     |     3 |
| 201011232 | 4     | 0     |     3 |
| 201011232 | 3     | 9     |     3 |
| 201011232 | 9     | 2     |     3 |
| 201011232 | 9     | 6     |     3 |
| 201011232 | 1     | 0     |     2 |
| 201011232 | 9     | 8     |     2 |
| 201011232 | 1     | 2     |     2 |
| 201011232 | 7     | 9     |     2 |
| 201011232 | 4     | 7     |     2 |
| 201011232 | 1     | 4     |     2 |
| 201011232 | 3     | 6     |     2 |
| 201011232 | 2     | 0     |     2 |
| 201011232 | 2     | 9     |     2 |
| 201011232 | 6     | 8     |     2 |
| 201011232 | 5     | 2     |     2 |
| 201011232 | 6     | 9     |     1 |
| 201011232 | 5     | 8     |     1 |
| 201011232 | 4     | 5     |     1 |
| 201011232 | 5     | 7     |     1 |
| 201011232 | 9     | 5     |     1 |
| 201011232 | 0     | 8     |     1 |
| 201011232 | 3     | 8     |     1 |
| 201011232 | 0     | 7     |     1 |
| 201011232 | 3     | 5     |     1 |
| 201011232 | 0     | 2     |     1 |
| 201011232 | 1     | 9     |     1 |
+-----------+-------+-------+-------+

-- select all combinations (including correct classifications) and order by truth and class instead of by count
select Run.ixRun, Data.sCharacter as truth, Result.sClassification as class, count(*) as total from Data, Result, Run where Data.ixData = Result.ixData and Result.ixRun = Run.ixRun group by Run.ixRun, Data.sCharacter, Result.sClassification order by Run.ixRun, Data.sCharacter, Result.sClassification desc;

+-----------+-------+-------+-------+
| ixRun     | truth | class | total |
+-----------+-------+-------+-------+
|         1 | 0     | 9     |    16 |
|         1 | 0     | 8     |    17 |
|         1 | 0     | 7     |    23 |
|         1 | 0     | 6     |    24 |
|         1 | 0     | 5     |    19 |
|         1 | 0     | 4     |    20 |
|         1 | 0     | 3     |    17 |
|         1 | 0     | 2     |    19 |
|         1 | 0     | 1     |    21 |
|         1 | 0     | 0     |  6727 |
|         1 | 1     | 9     |    25 |
|         1 | 1     | 8     |    23 |
|         1 | 1     | 7     |    26 |
|         1 | 1     | 6     |    24 |
|         1 | 1     | 5     |    18 |
|         1 | 1     | 4     |    25 |
|         1 | 1     | 3     |    27 |
|         1 | 1     | 2     |    20 |
|         1 | 1     | 1     |  7664 |
|         1 | 1     | 0     |    25 |
|         1 | 2     | 9     |    20 |
|         1 | 2     | 8     |    23 |
|         1 | 2     | 7     |    23 |
|         1 | 2     | 6     |    20 |
|         1 | 2     | 5     |    34 |
|         1 | 2     | 4     |     9 |
|         1 | 2     | 3     |    14 |
|         1 | 2     | 2     |  6806 |
|         1 | 2     | 1     |    18 |
|         1 | 2     | 0     |    23 |
|         1 | 3     | 9     |    22 |
|         1 | 3     | 8     |    33 |
|         1 | 3     | 7     |    13 |
|         1 | 3     | 6     |    18 |
|         1 | 3     | 5     |    20 |
|         1 | 3     | 4     |    25 |
|         1 | 3     | 3     |  6951 |
|         1 | 3     | 2     |    21 |
|         1 | 3     | 1     |    22 |
|         1 | 3     | 0     |    16 |
|         1 | 4     | 9     |    24 |
|         1 | 4     | 8     |    24 |
|         1 | 4     | 7     |    17 |
|         1 | 4     | 6     |    22 |
|         1 | 4     | 5     |    22 |
|         1 | 4     | 4     |  6619 |
|         1 | 4     | 3     |    24 |
|         1 | 4     | 2     |    33 |
|         1 | 4     | 1     |    21 |
|         1 | 4     | 0     |    18 |
|         1 | 5     | 9     |    23 |
|         1 | 5     | 8     |    15 |
|         1 | 5     | 7     |    21 |
|         1 | 5     | 6     |    24 |
|         1 | 5     | 5     |  6137 |
|         1 | 5     | 4     |    19 |
|         1 | 5     | 3     |    26 |
|         1 | 5     | 2     |    17 |
|         1 | 5     | 1     |    12 |
|         1 | 5     | 0     |    19 |
|         1 | 6     | 9     |    26 |
|         1 | 6     | 8     |    25 |
|         1 | 6     | 7     |    19 |
|         1 | 6     | 6     |  6690 |
|         1 | 6     | 5     |    15 |
|         1 | 6     | 4     |    24 |
|         1 | 6     | 3     |    24 |
|         1 | 6     | 2     |    17 |
|         1 | 6     | 1     |    22 |
|         1 | 6     | 0     |    14 |
|         1 | 7     | 9     |    24 |
|         1 | 7     | 8     |    18 |
|         1 | 7     | 7     |  7107 |
|         1 | 7     | 6     |    17 |
|         1 | 7     | 5     |    29 |
|         1 | 7     | 4     |    16 |
|         1 | 7     | 3     |    20 |
|         1 | 7     | 2     |    22 |
|         1 | 7     | 1     |    18 |
|         1 | 7     | 0     |    22 |
|         1 | 8     | 9     |    33 |
|         1 | 8     | 8     |  6632 |
|         1 | 8     | 7     |    21 |
|         1 | 8     | 6     |    12 |
|         1 | 8     | 5     |    19 |
|         1 | 8     | 4     |    25 |
|         1 | 8     | 3     |    16 |
|         1 | 8     | 2     |    26 |
|         1 | 8     | 1     |    23 |
|         1 | 8     | 0     |    18 |
|         1 | 9     | 9     |  6761 |
|         1 | 9     | 8     |    25 |
|         1 | 9     | 7     |    19 |
|         1 | 9     | 6     |    21 |
|         1 | 9     | 5     |    24 |
|         1 | 9     | 4     |    21 |
|         1 | 9     | 3     |    32 |
|         1 | 9     | 2     |    21 |
|         1 | 9     | 1     |    19 |
|         1 | 9     | 0     |    15 |
| 201011133 | 0     | 9     |     5 |
| 201011133 | 0     | 8     |   286 |
| 201011133 | 0     | 7     |     3 |
| 201011133 | 0     | 6     |   142 |
| 201011133 | 0     | 5     |   151 |
| 201011133 | 0     | 4     |    18 |
| 201011133 | 0     | 3     |    22 |
| 201011133 | 0     | 2     |    65 |
| 201011133 | 0     | 0     |  6111 |
| 201011133 | 1     | 9     |    10 |
| 201011133 | 1     | 8     |   159 |
| 201011133 | 1     | 7     |    11 |
| 201011133 | 1     | 6     |    26 |
| 201011133 | 1     | 5     |    19 |
| 201011133 | 1     | 4     |    17 |
| 201011133 | 1     | 3     |    31 |
| 201011133 | 1     | 2     |  1092 |
| 201011133 | 1     | 1     |  6412 |
| 201011133 | 2     | 9     |     7 |
| 201011133 | 2     | 8     |   277 |
| 201011133 | 2     | 7     |   109 |
| 201011133 | 2     | 6     |   388 |
| 201011133 | 2     | 5     |    18 |
| 201011133 | 2     | 4     |   186 |
| 201011133 | 2     | 3     |   111 |
| 201011133 | 2     | 2     |  5668 |
| 201011133 | 2     | 1     |    30 |
| 201011133 | 2     | 0     |    96 |
| 201011133 | 3     | 9     |   130 |
| 201011133 | 3     | 8     |   496 |
| 201011133 | 3     | 7     |   161 |
| 201011133 | 3     | 6     |    54 |
| 201011133 | 3     | 5     |   552 |
| 201011133 | 3     | 4     |    38 |
| 201011133 | 3     | 3     |  4694 |
| 201011133 | 3     | 2     |   761 |
| 201011133 | 3     | 1     |    99 |
| 201011133 | 3     | 0     |    56 |
| 201011133 | 4     | 9     |   779 |
| 201011133 | 4     | 8     |   299 |
| 201011133 | 4     | 7     |    69 |
| 201011133 | 4     | 6     |   186 |
| 201011133 | 4     | 5     |    55 |
| 201011133 | 4     | 4     |  5275 |
| 201011133 | 4     | 3     |     1 |
| 201011133 | 4     | 2     |    14 |
| 201011133 | 4     | 1     |    31 |
| 201011133 | 4     | 0     |    15 |
| 201011133 | 5     | 9     |    81 |
| 201011133 | 5     | 8     |   253 |
| 201011133 | 5     | 7     |    17 |
| 201011133 | 5     | 6     |   226 |
| 201011133 | 5     | 5     |  4668 |
| 201011133 | 5     | 4     |   266 |
| 201011133 | 5     | 3     |   344 |
| 201011133 | 5     | 2     |   136 |
| 201011133 | 5     | 1     |    47 |
| 201011133 | 5     | 0     |   175 |
| 201011133 | 6     | 9     |     1 |
| 201011133 | 6     | 8     |   106 |
| 201011133 | 6     | 6     |  6283 |
| 201011133 | 6     | 5     |   124 |
| 201011133 | 6     | 4     |    29 |
| 201011133 | 6     | 3     |     3 |
| 201011133 | 6     | 2     |    63 |
| 201011133 | 6     | 1     |   128 |
| 201011133 | 6     | 0     |    39 |
| 201011133 | 7     | 9     |   367 |
| 201011133 | 7     | 8     |    85 |
| 201011133 | 7     | 7     |  6127 |
| 201011133 | 7     | 6     |     7 |
| 201011133 | 7     | 5     |    42 |
| 201011133 | 7     | 4     |   283 |
| 201011133 | 7     | 3     |     5 |
| 201011133 | 7     | 2     |   137 |
| 201011133 | 7     | 1     |    79 |
| 201011133 | 7     | 0     |    61 |
| 201011133 | 8     | 9     |   151 |
| 201011133 | 8     | 8     |  5295 |
| 201011133 | 8     | 7     |    26 |
| 201011133 | 8     | 6     |    78 |
| 201011133 | 8     | 5     |   519 |
| 201011133 | 8     | 4     |    68 |
| 201011133 | 8     | 3     |   195 |
| 201011133 | 8     | 2     |   229 |
| 201011133 | 8     | 1     |   125 |
| 201011133 | 8     | 0     |    39 |
| 201011133 | 9     | 9     |  4789 |
| 201011133 | 9     | 8     |   335 |
| 201011133 | 9     | 7     |   555 |
| 201011133 | 9     | 6     |     5 |
| 201011133 | 9     | 5     |   164 |
| 201011133 | 9     | 4     |   802 |
| 201011133 | 9     | 3     |    51 |
| 201011133 | 9     | 2     |    10 |
| 201011133 | 9     | 1     |    79 |
| 201011133 | 9     | 0     |    68 |
| 201011134 | 0     | 9     |     9 |
| 201011134 | 0     | 8     |   174 |
| 201011134 | 0     | 7     |     6 |
| 201011134 | 0     | 6     |   103 |
| 201011134 | 0     | 5     |   164 |
| 201011134 | 0     | 4     |    12 |
| 201011134 | 0     | 3     |    16 |
| 201011134 | 0     | 2     |    39 |
| 201011134 | 0     | 1     |     2 |
| 201011134 | 0     | 0     |  6277 |
| 201011134 | 1     | 9     |    13 |
| 201011134 | 1     | 8     |   233 |
| 201011134 | 1     | 7     |    10 |
| 201011134 | 1     | 6     |    19 |
| 201011134 | 1     | 5     |    31 |
| 201011134 | 1     | 4     |     9 |
| 201011134 | 1     | 3     |    32 |
| 201011134 | 1     | 2     |   399 |
| 201011134 | 1     | 1     |  7030 |
| 201011134 | 2     | 9     |    23 |
| 201011134 | 2     | 8     |   251 |
| 201011134 | 2     | 7     |    82 |
| 201011134 | 2     | 6     |   383 |
| 201011134 | 2     | 5     |    45 |
| 201011134 | 2     | 4     |   168 |
| 201011134 | 2     | 3     |    79 |
| 201011134 | 2     | 2     |  5671 |
| 201011134 | 2     | 1     |    47 |
| 201011134 | 2     | 0     |   140 |
| 201011134 | 3     | 9     |   138 |
| 201011134 | 3     | 8     |   607 |
| 201011134 | 3     | 7     |    99 |
| 201011134 | 3     | 6     |    56 |
| 201011134 | 3     | 5     |   535 |
| 201011134 | 3     | 4     |    30 |
| 201011134 | 3     | 3     |  4579 |
| 201011134 | 3     | 2     |   784 |
| 201011134 | 3     | 1     |   120 |
| 201011134 | 3     | 0     |    92 |
| 201011134 | 4     | 9     |   704 |
| 201011134 | 4     | 8     |   213 |
| 201011134 | 4     | 7     |    69 |
| 201011134 | 4     | 6     |   143 |
| 201011134 | 4     | 5     |   112 |
| 201011134 | 4     | 4     |  5379 |
| 201011134 | 4     | 3     |     2 |
| 201011134 | 4     | 2     |    25 |
| 201011134 | 4     | 1     |    51 |
| 201011134 | 4     | 0     |    25 |
| 201011134 | 5     | 9     |    89 |
| 201011134 | 5     | 8     |   190 |
| 201011134 | 5     | 7     |    77 |
| 201011134 | 5     | 6     |   201 |
| 201011134 | 5     | 5     |  4757 |
| 201011134 | 5     | 4     |   240 |
| 201011134 | 5     | 3     |   353 |
| 201011134 | 5     | 2     |    93 |
| 201011134 | 5     | 1     |    86 |
| 201011134 | 5     | 0     |   126 |
| 201011134 | 6     | 9     |     1 |
| 201011134 | 6     | 8     |    77 |
| 201011134 | 6     | 6     |  6138 |
| 201011134 | 6     | 5     |   188 |
| 201011134 | 6     | 4     |    19 |
| 201011134 | 6     | 3     |     3 |
| 201011134 | 6     | 2     |    55 |
| 201011134 | 6     | 1     |   227 |
| 201011134 | 6     | 0     |    67 |
| 201011134 | 7     | 9     |   482 |
| 201011134 | 7     | 8     |    82 |
| 201011134 | 7     | 7     |  6174 |
| 201011134 | 7     | 6     |    12 |
| 201011134 | 7     | 5     |    12 |
| 201011134 | 7     | 4     |   197 |
| 201011134 | 7     | 3     |    15 |
| 201011134 | 7     | 2     |    75 |
| 201011134 | 7     | 1     |   110 |
| 201011134 | 7     | 0     |    33 |
| 201011134 | 8     | 9     |   159 |
| 201011134 | 8     | 8     |  4900 |
| 201011134 | 8     | 7     |    44 |
| 201011134 | 8     | 6     |    85 |
| 201011134 | 8     | 5     |   874 |
| 201011134 | 8     | 4     |    69 |
| 201011134 | 8     | 3     |   181 |
| 201011134 | 8     | 2     |   163 |
| 201011134 | 8     | 1     |   195 |
| 201011134 | 8     | 0     |    54 |
| 201011134 | 9     | 9     |  4889 |
| 201011134 | 9     | 8     |   198 |
| 201011134 | 9     | 7     |   814 |
| 201011134 | 9     | 6     |     9 |
| 201011134 | 9     | 5     |   132 |
| 201011134 | 9     | 4     |   575 |
| 201011134 | 9     | 3     |    59 |
| 201011134 | 9     | 2     |    11 |
| 201011134 | 9     | 1     |    99 |
| 201011134 | 9     | 0     |    71 |
| 201011141 | 0     | 9     |    20 |
| 201011141 | 0     | 8     |   144 |
| 201011141 | 0     | 7     |     3 |
| 201011141 | 0     | 6     |   152 |
| 201011141 | 0     | 5     |   297 |
| 201011141 | 0     | 4     |    19 |
| 201011141 | 0     | 3     |    21 |
| 201011141 | 0     | 2     |    62 |
| 201011141 | 0     | 1     |     3 |
| 201011141 | 0     | 0     |  5581 |
| 201011141 | 1     | 9     |    12 |
| 201011141 | 1     | 8     |    85 |
| 201011141 | 1     | 7     |     5 |
| 201011141 | 1     | 6     |    29 |
| 201011141 | 1     | 5     |   190 |
| 201011141 | 1     | 4     |     3 |
| 201011141 | 1     | 3     |    31 |
| 201011141 | 1     | 2     |    54 |
| 201011141 | 1     | 1     |  6867 |
| 201011141 | 2     | 9     |    31 |
| 201011141 | 2     | 8     |   313 |
| 201011141 | 2     | 7     |    77 |
| 201011141 | 2     | 6     |   211 |
| 201011141 | 2     | 5     |    25 |
| 201011141 | 2     | 4     |   111 |
| 201011141 | 2     | 3     |   225 |
| 201011141 | 2     | 2     |  5249 |
| 201011141 | 2     | 1     |   101 |
| 201011141 | 2     | 0     |    46 |
| 201011141 | 3     | 9     |   179 |
| 201011141 | 3     | 8     |   247 |
| 201011141 | 3     | 7     |    76 |
| 201011141 | 3     | 6     |    63 |
| 201011141 | 3     | 5     |   219 |
| 201011141 | 3     | 4     |    28 |
| 201011141 | 3     | 3     |  5334 |
| 201011141 | 3     | 2     |   252 |
| 201011141 | 3     | 1     |   104 |
| 201011141 | 3     | 0     |    38 |
| 201011141 | 4     | 9     |   856 |
| 201011141 | 4     | 8     |    77 |
| 201011141 | 4     | 7     |    19 |
| 201011141 | 4     | 6     |   116 |
| 201011141 | 4     | 5     |    63 |
| 201011141 | 4     | 4     |  4992 |
| 201011141 | 4     | 2     |    48 |
| 201011141 | 4     | 1     |    43 |
| 201011141 | 4     | 0     |     9 |
| 201011141 | 5     | 9     |   165 |
| 201011141 | 5     | 8     |   132 |
| 201011141 | 5     | 7     |    23 |
| 201011141 | 5     | 6     |   114 |
| 201011141 | 5     | 5     |  4085 |
| 201011141 | 5     | 4     |   161 |
| 201011141 | 5     | 3     |   778 |
| 201011141 | 5     | 2     |    73 |
| 201011141 | 5     | 1     |    32 |
| 201011141 | 5     | 0     |   149 |
| 201011141 | 6     | 9     |     1 |
| 201011141 | 6     | 8     |    36 |
| 201011141 | 6     | 6     |  5649 |
| 201011141 | 6     | 5     |   205 |
| 201011141 | 6     | 4     |    64 |
| 201011141 | 6     | 3     |     3 |
| 201011141 | 6     | 2     |   164 |
| 201011141 | 6     | 1     |    95 |
| 201011141 | 6     | 0     |    58 |
| 201011141 | 7     | 9     |   469 |
| 201011141 | 7     | 8     |    75 |
| 201011141 | 7     | 7     |  5688 |
| 201011141 | 7     | 6     |     2 |
| 201011141 | 7     | 5     |    10 |
| 201011141 | 7     | 4     |   178 |
| 201011141 | 7     | 3     |    15 |
| 201011141 | 7     | 2     |    76 |
| 201011141 | 7     | 1     |   160 |
| 201011141 | 7     | 0     |    19 |
| 201011141 | 8     | 9     |   276 |
| 201011141 | 8     | 8     |  4726 |
| 201011141 | 8     | 7     |    17 |
| 201011141 | 8     | 6     |    47 |
| 201011141 | 8     | 5     |   260 |
| 201011141 | 8     | 4     |    87 |
| 201011141 | 8     | 3     |   376 |
| 201011141 | 8     | 2     |   105 |
| 201011141 | 8     | 1     |   271 |
| 201011141 | 8     | 0     |    59 |
| 201011141 | 9     | 9     |  5375 |
| 201011141 | 9     | 8     |    76 |
| 201011141 | 9     | 7     |   167 |
| 201011141 | 9     | 6     |     2 |
| 201011141 | 9     | 5     |    40 |
| 201011141 | 9     | 4     |   462 |
| 201011141 | 9     | 3     |    88 |
| 201011141 | 9     | 2     |    27 |
| 201011141 | 9     | 1     |    73 |
| 201011141 | 9     | 0     |    47 |
| 201011161 | 0     | 9     |    19 |
| 201011161 | 0     | 8     |    70 |
| 201011161 | 0     | 7     |    13 |
| 201011161 | 0     | 6     |   223 |
| 201011161 | 0     | 5     |   167 |
| 201011161 | 0     | 4     |    24 |
| 201011161 | 0     | 3     |     6 |
| 201011161 | 0     | 2     |    19 |
| 201011161 | 0     | 1     |    27 |
| 201011161 | 0     | 0     |  6335 |
| 201011161 | 1     | 9     |     1 |
| 201011161 | 1     | 8     |     7 |
| 201011161 | 1     | 7     |    16 |
| 201011161 | 1     | 6     |     4 |
| 201011161 | 1     | 5     |     1 |
| 201011161 | 1     | 4     |     4 |
| 201011161 | 1     | 3     |     7 |
| 201011161 | 1     | 2     |    14 |
| 201011161 | 1     | 1     |  7822 |
| 201011161 | 1     | 0     |     1 |
| 201011161 | 2     | 9     |   104 |
| 201011161 | 2     | 8     |   184 |
| 201011161 | 2     | 7     |   435 |
| 201011161 | 2     | 6     |    59 |
| 201011161 | 2     | 5     |    15 |
| 201011161 | 2     | 4     |   216 |
| 201011161 | 2     | 3     |   124 |
| 201011161 | 2     | 2     |  5184 |
| 201011161 | 2     | 1     |   522 |
| 201011161 | 2     | 0     |   147 |
| 201011161 | 3     | 9     |   182 |
| 201011161 | 3     | 8     |   302 |
| 201011161 | 3     | 7     |   202 |
| 201011161 | 3     | 6     |    37 |
| 201011161 | 3     | 5     |   388 |
| 201011161 | 3     | 4     |    18 |
| 201011161 | 3     | 3     |  5428 |
| 201011161 | 3     | 2     |   167 |
| 201011161 | 3     | 1     |   374 |
| 201011161 | 3     | 0     |    43 |
| 201011161 | 4     | 9     |  1141 |
| 201011161 | 4     | 8     |    12 |
| 201011161 | 4     | 7     |   146 |
| 201011161 | 4     | 6     |    63 |
| 201011161 | 4     | 5     |    19 |
| 201011161 | 4     | 4     |  5041 |
| 201011161 | 4     | 3     |     1 |
| 201011161 | 4     | 2     |     5 |
| 201011161 | 4     | 1     |   382 |
| 201011161 | 4     | 0     |    14 |
| 201011161 | 5     | 9     |   180 |
| 201011161 | 5     | 8     |    49 |
| 201011161 | 5     | 7     |    17 |
| 201011161 | 5     | 6     |   322 |
| 201011161 | 5     | 5     |  4573 |
| 201011161 | 5     | 4     |    69 |
| 201011161 | 5     | 3     |   327 |
| 201011161 | 5     | 2     |     7 |
| 201011161 | 5     | 1     |   647 |
| 201011161 | 5     | 0     |   122 |
| 201011161 | 6     | 9     |     1 |
| 201011161 | 6     | 8     |    11 |
| 201011161 | 6     | 7     |     3 |
| 201011161 | 6     | 6     |  6438 |
| 201011161 | 6     | 5     |    30 |
| 201011161 | 6     | 4     |    60 |
| 201011161 | 6     | 2     |     5 |
| 201011161 | 6     | 1     |   241 |
| 201011161 | 6     | 0     |    87 |
| 201011161 | 7     | 9     |   465 |
| 201011161 | 7     | 8     |     8 |
| 201011161 | 7     | 7     |  6307 |
| 201011161 | 7     | 6     |     3 |
| 201011161 | 7     | 5     |    58 |
| 201011161 | 7     | 4     |   101 |
| 201011161 | 7     | 3     |     4 |
| 201011161 | 7     | 2     |    21 |
| 201011161 | 7     | 1     |   310 |
| 201011161 | 7     | 0     |    16 |
| 201011161 | 8     | 9     |   440 |
| 201011161 | 8     | 8     |  4423 |
| 201011161 | 8     | 7     |   132 |
| 201011161 | 8     | 6     |    82 |
| 201011161 | 8     | 5     |   509 |
| 201011161 | 8     | 4     |   101 |
| 201011161 | 8     | 3     |   285 |
| 201011161 | 8     | 2     |    42 |
| 201011161 | 8     | 1     |   725 |
| 201011161 | 8     | 0     |    86 |
| 201011161 | 9     | 9     |  5535 |
| 201011161 | 9     | 8     |    28 |
| 201011161 | 9     | 7     |   667 |
| 201011161 | 9     | 6     |    10 |
| 201011161 | 9     | 5     |    38 |
| 201011161 | 9     | 4     |   322 |
| 201011161 | 9     | 3     |    59 |
| 201011161 | 9     | 2     |    26 |
| 201011161 | 9     | 1     |   212 |
| 201011161 | 9     | 0     |    61 |
| 201011162 | 0     | 9     |     4 |
| 201011162 | 0     | 8     |    77 |
| 201011162 | 0     | 7     |     3 |
| 201011162 | 0     | 6     |   168 |
| 201011162 | 0     | 5     |   109 |
| 201011162 | 0     | 4     |    49 |
| 201011162 | 0     | 3     |     7 |
| 201011162 | 0     | 2     |    42 |
| 201011162 | 0     | 1     |     1 |
| 201011162 | 0     | 0     |  6443 |
| 201011162 | 1     | 9     |    12 |
| 201011162 | 1     | 8     |    59 |
| 201011162 | 1     | 7     |    16 |
| 201011162 | 1     | 6     |     3 |
| 201011162 | 1     | 5     |    42 |
| 201011162 | 1     | 4     |    11 |
| 201011162 | 1     | 3     |    21 |
| 201011162 | 1     | 2     |   454 |
| 201011162 | 1     | 1     |  7258 |
| 201011162 | 1     | 0     |     1 |
| 201011162 | 2     | 9     |    59 |
| 201011162 | 2     | 8     |   152 |
| 201011162 | 2     | 7     |   143 |
| 201011162 | 2     | 6     |   124 |
| 201011162 | 2     | 5     |    34 |
| 201011162 | 2     | 4     |   308 |
| 201011162 | 2     | 3     |   120 |
| 201011162 | 2     | 2     |  5893 |
| 201011162 | 2     | 1     |    80 |
| 201011162 | 2     | 0     |    77 |
| 201011162 | 3     | 9     |   159 |
| 201011162 | 3     | 8     |   385 |
| 201011162 | 3     | 7     |    99 |
| 201011162 | 3     | 6     |    30 |
| 201011162 | 3     | 5     |   415 |
| 201011162 | 3     | 4     |    15 |
| 201011162 | 3     | 3     |  5613 |
| 201011162 | 3     | 2     |   336 |
| 201011162 | 3     | 1     |    54 |
| 201011162 | 3     | 0     |    35 |
| 201011162 | 4     | 9     |   316 |
| 201011162 | 4     | 8     |    23 |
| 201011162 | 4     | 7     |    27 |
| 201011162 | 4     | 6     |    49 |
| 201011162 | 4     | 5     |    16 |
| 201011162 | 4     | 4     |  6303 |
| 201011162 | 4     | 2     |    24 |
| 201011162 | 4     | 1     |    41 |
| 201011162 | 4     | 0     |    25 |
| 201011162 | 5     | 9     |    63 |
| 201011162 | 5     | 8     |   103 |
| 201011162 | 5     | 7     |    10 |
| 201011162 | 5     | 6     |   219 |
| 201011162 | 5     | 5     |  5257 |
| 201011162 | 5     | 4     |   139 |
| 201011162 | 5     | 3     |   239 |
| 201011162 | 5     | 2     |    60 |
| 201011162 | 5     | 1     |   126 |
| 201011162 | 5     | 0     |    97 |
| 201011162 | 6     | 8     |    46 |
| 201011162 | 6     | 6     |  6433 |
| 201011162 | 6     | 5     |    81 |
| 201011162 | 6     | 4     |   136 |
| 201011162 | 6     | 3     |     5 |
| 201011162 | 6     | 2     |    68 |
| 201011162 | 6     | 1     |    52 |
| 201011162 | 6     | 0     |    55 |
| 201011162 | 7     | 9     |   492 |
| 201011162 | 7     | 8     |     4 |
| 201011162 | 7     | 7     |  6107 |
| 201011162 | 7     | 6     |    10 |
| 201011162 | 7     | 5     |   144 |
| 201011162 | 7     | 4     |   169 |
| 201011162 | 7     | 3     |    19 |
| 201011162 | 7     | 2     |   164 |
| 201011162 | 7     | 1     |    69 |
| 201011162 | 7     | 0     |   115 |
| 201011162 | 8     | 9     |   192 |
| 201011162 | 8     | 8     |  5549 |
| 201011162 | 8     | 7     |    32 |
| 201011162 | 8     | 6     |    71 |
| 201011162 | 8     | 5     |   306 |
| 201011162 | 8     | 4     |    83 |
| 201011162 | 8     | 3     |   154 |
| 201011162 | 8     | 2     |   186 |
| 201011162 | 8     | 1     |   228 |
| 201011162 | 8     | 0     |    24 |
| 201011162 | 9     | 9     |  5675 |
| 201011162 | 9     | 8     |    92 |
| 201011162 | 9     | 7     |   270 |
| 201011162 | 9     | 6     |     2 |
| 201011162 | 9     | 5     |    65 |
| 201011162 | 9     | 4     |   511 |
| 201011162 | 9     | 3     |    74 |
| 201011162 | 9     | 2     |   134 |
| 201011162 | 9     | 1     |    39 |
| 201011162 | 9     | 0     |    96 |
| 201011231 | 0     | 9     |     1 |
| 201011231 | 0     | 8     |     2 |
| 201011231 | 0     | 7     |     2 |
| 201011231 | 0     | 6     |    22 |
| 201011231 | 0     | 5     |     3 |
| 201011231 | 0     | 3     |     2 |
| 201011231 | 0     | 2     |     2 |
| 201011231 | 0     | 1     |   173 |
| 201011231 | 0     | 0     |    98 |
| 201011231 | 1     | 9     |     1 |
| 201011231 | 1     | 8     |     1 |
| 201011231 | 1     | 6     |     1 |
| 201011231 | 1     | 4     |     1 |
| 201011231 | 1     | 2     |     2 |
| 201011231 | 1     | 1     |   299 |
| 201011231 | 2     | 9     |     1 |
| 201011231 | 2     | 8     |     2 |
| 201011231 | 2     | 7     |     6 |
| 201011231 | 2     | 3     |     5 |
| 201011231 | 2     | 2     |    79 |
| 201011231 | 2     | 1     |   212 |
| 201011231 | 3     | 9     |     3 |
| 201011231 | 3     | 7     |    10 |
| 201011231 | 3     | 5     |     5 |
| 201011231 | 3     | 3     |    84 |
| 201011231 | 3     | 1     |   203 |
| 201011231 | 4     | 9     |    20 |
| 201011231 | 4     | 7     |     2 |
| 201011231 | 4     | 6     |     3 |
| 201011231 | 4     | 4     |    74 |
| 201011231 | 4     | 1     |   206 |
| 201011231 | 5     | 9     |    10 |
| 201011231 | 5     | 8     |     3 |
| 201011231 | 5     | 7     |     1 |
| 201011231 | 5     | 6     |    13 |
| 201011231 | 5     | 5     |    90 |
| 201011231 | 5     | 4     |     5 |
| 201011231 | 5     | 3     |    11 |
| 201011231 | 5     | 1     |   169 |
| 201011231 | 5     | 0     |     3 |
| 201011231 | 6     | 6     |   106 |
| 201011231 | 6     | 5     |     1 |
| 201011231 | 6     | 4     |     1 |
| 201011231 | 6     | 3     |     3 |
| 201011231 | 6     | 1     |   193 |
| 201011231 | 6     | 0     |     1 |
| 201011231 | 7     | 8     |     3 |
| 201011231 | 7     | 7     |   115 |
| 201011231 | 7     | 3     |     1 |
| 201011231 | 7     | 2     |     1 |
| 201011231 | 7     | 1     |   185 |
| 201011231 | 8     | 9     |     7 |
| 201011231 | 8     | 8     |    66 |
| 201011231 | 8     | 7     |    11 |
| 201011231 | 8     | 6     |     8 |
| 201011231 | 8     | 5     |    12 |
| 201011231 | 8     | 4     |     3 |
| 201011231 | 8     | 3     |    21 |
| 201011231 | 8     | 1     |   176 |
| 201011231 | 8     | 0     |     1 |
| 201011231 | 9     | 9     |    58 |
| 201011231 | 9     | 8     |     2 |
| 201011231 | 9     | 7     |     6 |
| 201011231 | 9     | 6     |     2 |
| 201011231 | 9     | 4     |    14 |
| 201011231 | 9     | 3     |     2 |
| 201011231 | 9     | 1     |   221 |
| 201011232 | 0     | 9     |     7 |
| 201011232 | 0     | 8     |     1 |
| 201011232 | 0     | 7     |     1 |
| 201011232 | 0     | 6     |     6 |
| 201011232 | 0     | 5     |     9 |
| 201011232 | 0     | 3     |    19 |
| 201011232 | 0     | 2     |     1 |
| 201011232 | 0     | 1     |    48 |
| 201011232 | 0     | 0     |   213 |
| 201011232 | 1     | 9     |     1 |
| 201011232 | 1     | 6     |     4 |
| 201011232 | 1     | 5     |    10 |
| 201011232 | 1     | 4     |     2 |
| 201011232 | 1     | 3     |     6 |
| 201011232 | 1     | 2     |     2 |
| 201011232 | 1     | 1     |   278 |
| 201011232 | 1     | 0     |     2 |
| 201011232 | 2     | 9     |     2 |
| 201011232 | 2     | 8     |     3 |
| 201011232 | 2     | 6     |    12 |
| 201011232 | 2     | 5     |     5 |
| 201011232 | 2     | 3     |    26 |
| 201011232 | 2     | 2     |   139 |
| 201011232 | 2     | 1     |   116 |
| 201011232 | 2     | 0     |     2 |
| 201011232 | 3     | 9     |     3 |
| 201011232 | 3     | 8     |     1 |
| 201011232 | 3     | 7     |     4 |
| 201011232 | 3     | 6     |     2 |
| 201011232 | 3     | 5     |     1 |
| 201011232 | 3     | 3     |   160 |
| 201011232 | 3     | 2     |     7 |
| 201011232 | 3     | 1     |   127 |
| 201011232 | 4     | 9     |    17 |
| 201011232 | 4     | 7     |     2 |
| 201011232 | 4     | 6     |    14 |
| 201011232 | 4     | 5     |     1 |
| 201011232 | 4     | 4     |   162 |
| 201011232 | 4     | 3     |     6 |
| 201011232 | 4     | 2     |     4 |
| 201011232 | 4     | 1     |    96 |
| 201011232 | 4     | 0     |     3 |
| 201011232 | 5     | 9     |    10 |
| 201011232 | 5     | 8     |     1 |
| 201011232 | 5     | 7     |     1 |
| 201011232 | 5     | 6     |    15 |
| 201011232 | 5     | 5     |   138 |
| 201011232 | 5     | 4     |     8 |
| 201011232 | 5     | 3     |    45 |
| 201011232 | 5     | 2     |     2 |
| 201011232 | 5     | 1     |    66 |
| 201011232 | 5     | 0     |    19 |
| 201011232 | 6     | 9     |     1 |
| 201011232 | 6     | 8     |     2 |
| 201011232 | 6     | 6     |   136 |
| 201011232 | 6     | 5     |     7 |
| 201011232 | 6     | 4     |     5 |
| 201011232 | 6     | 3     |     6 |
| 201011232 | 6     | 2     |     6 |
| 201011232 | 6     | 1     |   129 |
| 201011232 | 6     | 0     |    13 |
| 201011232 | 7     | 9     |     2 |
| 201011232 | 7     | 8     |     3 |
| 201011232 | 7     | 7     |   157 |
| 201011232 | 7     | 3     |     8 |
| 201011232 | 7     | 2     |     4 |
| 201011232 | 7     | 1     |   123 |
| 201011232 | 7     | 0     |     8 |
| 201011232 | 8     | 9     |    15 |
| 201011232 | 8     | 8     |    93 |
| 201011232 | 8     | 7     |     7 |
| 201011232 | 8     | 6     |    11 |
| 201011232 | 8     | 5     |    26 |
| 201011232 | 8     | 4     |     6 |
| 201011232 | 8     | 3     |    29 |
| 201011232 | 8     | 2     |    16 |
| 201011232 | 8     | 1     |    97 |
| 201011232 | 8     | 0     |     5 |
| 201011232 | 9     | 9     |   116 |
| 201011232 | 9     | 8     |     2 |
| 201011232 | 9     | 7     |     9 |
| 201011232 | 9     | 6     |     3 |
| 201011232 | 9     | 5     |     1 |
| 201011232 | 9     | 4     |    23 |
| 201011232 | 9     | 3     |    12 |
| 201011232 | 9     | 2     |     3 |
| 201011232 | 9     | 1     |   129 |
| 201011232 | 9     | 0     |     7 |
+-----------+-------+-------+-------+


